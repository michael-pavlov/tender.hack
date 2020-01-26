package ru.gitpush.tender.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gitpush.tender.dto.SuggestedCte;
import ru.gitpush.tender.dto.SuggestedReason;
import ru.gitpush.tender.model.Cte;
import ru.gitpush.tender.model.Offer;
import ru.gitpush.tender.model.Order;
import ru.gitpush.tender.model.OrderStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculationService {
    private final OrderService orderService;
    private final OfferService offerService;
    private final CteService cteService;

    public List<SuggestedCte> calcSuggested(Long baseCteId) {
        List<SuggestedCte> res = new ArrayList<>();
        Cte cte = cteService.findById(baseCteId);
        Offer cteBestOffer = offerService.findByCteId(cte.getId()).stream().min(Comparator.comparing(Offer::getPrice)).get();

        List<Order> order = orderService.findByCteId(baseCteId);
        if ((order != null) && (order.stream().anyMatch(it -> it.getCooperativeId() != null))) {
            //A
            res.add(createSuggestedCte(cteBestOffer, SuggestedReason.COOPERATIVE_CHEAPER));
        }

        //B
        List<Order> cooperativeList = orderService.findIfCooperativeIsNotNullByType(cte.cteType, OrderStatus.ACTIVE, cte.getId());
        cooperativeList = removeIfIncorrectSpec(cooperativeList, cte);
        List<RetPriceData> cooperativeListRet = checkPrice(cooperativeList, cteBestOffer, (bestOffer, cteBbo) -> bestOffer.compareTo(cteBbo) <= 0);
        for (RetPriceData ret : cooperativeListRet) {
            res.add(createSuggestedCte(ret.getOffer(), SuggestedReason.ALREADY_COOPERATIVE));
        }

        //C
        List<Order> completeList = orderService.findByCte_CteTypeAndStatusAnfMoreGeneration(cte.cteType, OrderStatus.COMPLETE, cte.getGeneration());
        completeList = removeIfIncorrectSpec(completeList, cte);
        BigDecimal delta = new BigDecimal(0.005);
        List<RetPriceData> completeListRet = checkPrice(completeList, cteBestOffer, (bestOffer, cteBbo) ->
                bestOffer.divide(cteBbo, 10, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).compareTo(delta) <= 0);

        if (completeListRet.size() > 3) {
            HashMap<Long, Integer> totalCounts = getTotalCounts(completeListRet);
            completeListRet.sort((a, b) -> getCount(a.getOrder(), totalCounts).compareTo(getCount(b.getOrder(), totalCounts)));
        }
        for (int i = 0; i < completeListRet.size() && i < 3; i++) {
            RetPriceData ret = completeListRet.get(i);
            res.add(createSuggestedCte(ret.getOffer(), SuggestedReason.MORE_POPULAR));
        }

        //D
        List<Offer> discountList = offerService.findByCte_CteTypeAnfLessGeneration(cte.cteType, cte.getGeneration());
        discountList = removeIfIncorrectSpecOffer(discountList, cte);
        discountList = checkMinPrice(discountList, cteBestOffer);
        for (int i = 0; i < discountList.size() && i < 3; i++) {
            Offer off = discountList.get(i);
            res.add(createSuggestedCte(off, SuggestedReason.SALE));
        }
        res = res.stream().distinct().collect(Collectors.toList());
        return res;
    }

    private SuggestedCte createSuggestedCte(Offer off, SuggestedReason reason) {
        SuggestedCte cur = new SuggestedCte();
        cur.setName(off.getCte().getName());
        cur.setCte(off.getCte().getId());
        cur.setReason(reason.getMsg());
        cur.setPrice(off.getPrice());
        cur.setImg(getImage(off.getCte().getId()));
        return cur;
    }

    private String getImage(Long cte) {
        return "http://localhost:9080/img/" + cte.toString() + ".jpg";
    }

    private interface ComporatorBD {
        boolean compare(BigDecimal a, BigDecimal b);
    }

    private HashMap<Long, Integer> getTotalCounts(List<RetPriceData> completeListRet) {
        HashMap<Long, Integer> totalCounts = new HashMap<>();
        for (RetPriceData ret : completeListRet) {
            Long cooperId = ret.getOrder().getCooperativeId();
            if (cooperId != null) {
                totalCounts.merge(cooperId, ret.getOrder().getCountGoods(), (oldV, newValue) -> oldV + newValue);
            }
        }
        return totalCounts;
    }

    private HashMap<Long, Integer> getTotalCountsOrders(List<Order> completeListRet) {
        HashMap<Long, Integer> totalCounts = new HashMap<>();
        for (Order ret : completeListRet) {
            Long cooperId = ret.getCooperativeId();
            if (cooperId != null) {
                totalCounts.merge(cooperId, ret.getCountGoods(), (oldV, newValue) -> oldV + newValue);
            }
        }
        return totalCounts;
    }

    private Integer getCount(Order order, HashMap<Long, Integer> totalCounts) {
        Integer count = totalCounts.get(order.getCooperativeId());
        if (count == null) {
            count = order.getCountGoods();
        }
        return count;
    }

    private List<Offer> checkMinPrice(List<Offer> orders, Offer cteBestOffer) {
        orders.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
        return orders.stream().filter(it -> it.getPrice().compareTo(cteBestOffer.getPrice()) < 0).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class RetPriceData {
        Order order;
        Offer offer;
    }

    private List<RetPriceData> checkPrice(List<Order> orders, Offer cteBestOffer, ComporatorBD cdb) {
        List<RetPriceData> res = new ArrayList<>(orders.size());

        HashMap<Long, Integer> totalCounts = getTotalCountsOrders(orders);
        HashSet<Long> usedCooperative = new HashSet<>();
        for (Order order : orders) {
            if (order.getCooperativeId() != null) {
                if (usedCooperative.contains(order.getCooperativeId())) {
                    continue;
                }
                usedCooperative.add(order.getCooperativeId());
            }
            List<Offer> offers = offerService.findByCteId(order.getCte().getId());
            Integer count = getCount(order, totalCounts);
            Offer bestOffer = getBest(offers, count);
            if (cdb.compare(bestOffer.getPrice(), cteBestOffer.getPrice())) {
                res.add(new RetPriceData(order, bestOffer));
            }
        }
        return res;
    }

    private Offer getBest(List<Offer> offers, Integer totalCount) {
        Offer priceMin = offers.get(0);
        for (Offer offer : offers) {
            BigDecimal price = offer.getPrice();
            if (offer.getDicountGoods() <= totalCount) {
                offer.setPrice(price.multiply(BigDecimal.ONE.subtract(offer.getDiscount())));
            }
            if (offer.getPrice().compareTo(priceMin.getPrice()) < 0) {
                priceMin = offer;
            }
        }
        return priceMin;
    }

    private List<Order> removeIfIncorrectSpec(List<Order> cooperativeList, Cte cte) {
        List<Order> res = new ArrayList<>(cooperativeList.size());
        for (Order order : cooperativeList) {
            if (checkSpec(order.getCte(), cte)) {
                res.add(order);
            }
        }
        return res;
    }

    private List<Offer> removeIfIncorrectSpecOffer(List<Offer> cooperativeList, Cte cte) {
        List<Offer> res = new ArrayList<>(cooperativeList.size());
        for (Offer offer : cooperativeList) {
            if (checkSpec(offer.getCte(), cte)) {
                res.add(offer);
            }
        }
        return res;
    }

    private boolean checkSpec(Cte orderCte, Cte cte) {
        return ((orderCte.getHdd() >= cte.getHdd()) && (orderCte.getRam() >= cte.getRam()) &&
                (checkResolution(orderCte.getResolution(), cte.getResolution())));
    }

    private boolean checkResolution(String orderResolution, String cteResolution) {
        String[] orderResArr = orderResolution.split("[xх]");
        String[] cteResArr = cteResolution.split("[xх]");
        return Integer.valueOf(orderResArr[0]) * Integer.valueOf(orderResArr[1]) >=
                Integer.valueOf(cteResArr[0]) * Integer.valueOf(cteResArr[1]);
    }
}
