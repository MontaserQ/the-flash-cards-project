package com.german.deck.resources;


import com.german.deck.entities.Card;
import com.german.deck.repository.CardRepository;
import com.german.deck.resources.entity.CardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flashcards/v1")
@CrossOrigin(origins = "*")
public class FlashCardsResource {

    private static final Logger logger = LoggerFactory.getLogger(FlashCardsResource.class);

    @Autowired
    private CardRepository cardRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<CardDto> getFlashCardRandomly() {
        logger.info("Received GetRandomFlashCard Request");
        
        final Card randomCard = cardRepository.getRandomCard();

        final CardDto cardDto = new CardDto();
        cardDto.setEnglishWord(randomCard.getEnglishWord());
        cardDto.setGermanWord(randomCard.getGermanWord());
        cardDto.setRootGermanForm(randomCard.getRootGermanForm());
        
        logger.info("Delivering [{}]", cardDto);

        return ResponseEntity.ok(cardDto);
    }
}
