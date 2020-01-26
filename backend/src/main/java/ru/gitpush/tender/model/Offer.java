package ru.gitpush.tender.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name="offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cte_id")
    public Cte cte;

    @Column(name = "price", nullable = false)
    public BigDecimal price;

    @Column(name = "discount", nullable = false)
    public BigDecimal discount;

    @Column(name = "vendor_id")
    public Long vendorId;

    @Column(name = "dicount_goods", nullable = false)
    public Integer dicountGoods;
}
