package com.german.deck.repository;

import com.german.deck.entities.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {


    @Query(value = "select * from germandeck.card where LOWER(rootGermanForm) = :rootGermanForm", nativeQuery = true)
    List<Card> findCardByGermanWord(@Param("rootGermanForm") String rootGermanForm);

    @Query(value = "select * from germandeck.card where germanWord LIKE %:germanWord%", nativeQuery = true)
    List<Card> searchCardByGermanWord(@Param("germanWord") String germanWord);

    @Query(value = "SELECT * FROM germandeck.card ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Card getRandomCard();
}
