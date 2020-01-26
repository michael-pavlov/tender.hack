package ru.gitpush.tender.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gitpush.tender.dto.CteIdDto;
import ru.gitpush.tender.dto.SuggestedCte;
import ru.gitpush.tender.service.CalculationService;
import ru.gitpush.tender.service.CteService;

import javax.xml.bind.ValidationException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/tender")
@Slf4j
@RequiredArgsConstructor
public class TenderController {
    private final CteService cteService;
    private final CalculationService calculationService;

    @PostMapping("/get-suggestion")
    public ResponseEntity getSuggestion(
            @RequestBody CteIdDto cteId) throws ValidationException {
        log.info("get-suggestion");
        if ((cteId == null) || (cteId.getId() == null) || (cteService.findById(cteId.getId()) == null)) {
            throw new ValidationException("Incorrect input cteId");
        }
        List<SuggestedCte> res = calculationService.calcSuggested(cteId.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
