package com.german.deck.resources.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CardSearchResponse implements Serializable {
    private List<CardDto> cards;
    private String message;
}
