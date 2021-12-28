package com.german.deck.resources.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardDto {

    private int id;
    private String germanWord;
    private String englishWord;
    private String rootGermanForm;
}
