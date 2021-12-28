package com.german.deck.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "card")
@ToString
public class Card {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "germanWord")
    private String germanWord;

    @Column(name = "englishWord")
    private String englishWord;

    @Column(name = "rootGermanForm")
    private String rootGermanForm;
    
}
