package com.german.deck.service;

import com.german.deck.entities.Card;
import com.german.deck.repository.CardRepository;
import com.german.deck.resources.entity.CardDto;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    public boolean insertCard(@NonNull String germanWord, @NonNull String englishWord, @NonNull String rootGermanForm) {
        germanWord = cleanString(germanWord);
        englishWord = cleanString(englishWord);
        rootGermanForm = cleanString(rootGermanForm);

        final List<Card> existingCards = cardRepository.findCardByGermanWord(rootGermanForm.toLowerCase());
        if (existingCards != null && !existingCards.isEmpty()) {
            logger.info("Skipped inserting [{}], word already does exist", rootGermanForm);
            return false;
        }

        final Card card = new Card();
        card.setGermanWord(germanWord);
        card.setEnglishWord(englishWord);
        card.setRootGermanForm(rootGermanForm);

        cardRepository.save(card);

        logger.info("Inserted [{}]", card);
        return true;
    }

    public boolean deleteCard(final int id) {
        final Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) {
            return false;
        }
        cardRepository.delete(card.get());
        return true;
    }

    private String cleanString(String word) {
        return Jsoup.parse(word).text();// remove html tags
    }


    public List<CardDto> searchCards(@NonNull final String searchTerm) {
        final List<Card> cards = cardRepository.searchCardByGermanWord(searchTerm);
        return cards.stream().map(card -> new CardDto(card.getId(), card.getGermanWord(), card.getEnglishWord(), card.getRootGermanForm())).collect(Collectors.toList());
    }
}
