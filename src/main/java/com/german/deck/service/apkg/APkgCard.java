package com.german.deck.service.apkg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sormula.annotation.Column;
import org.sormula.annotation.Row;

@Getter
@Setter
@ToString
@Row(tableName = "notes")
public class APkgCard {

    @Column(primaryKey = true)
    private Long id;

    @Column(name = "tags")
    private String tags;

    @Column(name = "flds")
    private String flds;
    
}
