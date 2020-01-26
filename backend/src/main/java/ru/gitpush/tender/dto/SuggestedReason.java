package ru.gitpush.tender.dto;

import lombok.Getter;

public enum SuggestedReason {
    COOPERATIVE_CHEAPER("Купить тот же товар совместно дешевле"),
    ALREADY_COOPERATIVE("Купить совместно лучший товар по той же цене"),
    MORE_POPULAR("Купить новую модель по той же цене"),
    SALE("Купить старую модель с хорошей скидкой");

    @Getter
    private final String msg;

    SuggestedReason(String msg) {
        this.msg = msg;
    };
}
