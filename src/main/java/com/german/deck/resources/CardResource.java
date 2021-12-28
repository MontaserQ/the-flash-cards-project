package com.german.deck.resources;


import com.german.deck.resources.entity.CardDto;
import com.german.deck.resources.entity.CardSearchResponse;
import com.german.deck.service.CardService;
import com.german.deck.service.apkg.ApkgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/card")
@CrossOrigin(origins = "*")
public class CardResource {

    private static final Logger logger = LoggerFactory.getLogger(CardResource.class);

    @Autowired
    private CardService cardService;

    @Autowired
    private ApkgService apkgService;

    private static final String PASSWORD = "passworder";

    @RequestMapping(value = "/insertCard", method = RequestMethod.POST)
    public ResponseEntity<String> insertCard(@RequestParam("germanWord") String germanWord, @RequestParam("englishWord") String englishWord, @RequestParam("rootGermanForm") String rootGermanForm) {
        logger.info("Received an insert card request with german word [{}], english word [{}] and root German Form [{}]", germanWord, englishWord, rootGermanForm);
        try {
            cardService.insertCard(germanWord.strip(), englishWord, rootGermanForm);
        } catch (final Exception ex) {
            logger.error("Something wrong happened while inserting a card", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something wrong happened while inserting the card  " + germanWord);
        }
        return ResponseEntity.ok("Card inserted");
    }

    @RequestMapping(value = "/search/{searchTerm}", produces = "application/json")
    public ResponseEntity<CardSearchResponse> search(@PathVariable("searchTerm") final String searchTerm) {
        logger.info("Received a search card request with german word [{}]", searchTerm);
        final List<CardDto> result;
        try {
            result = cardService.searchCards(searchTerm);
        } catch (final Exception ex) {
            logger.error("Something wrong happened while searching for a card", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CardSearchResponse(null, "Something wrong happened while searching for a card"));
        }
        return ResponseEntity.ok(new CardSearchResponse(result, "Successfully retrieved " + result.size() + " results"));
    }


    @RequestMapping(value = "/uploadApkg", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
    public ResponseEntity<String> uploadReportFiles(@RequestParam("files") final MultipartFile[] inputFiles) throws IOException {

        int numberOfInsertedWords = 0;

        for (MultipartFile file : inputFiles) {
            logger.info(file.getOriginalFilename());

            final File apkgFilesDirectory = new File("apkgFiles/" + file.getOriginalFilename());

            file.transferTo(apkgFilesDirectory);
            try {
                numberOfInsertedWords += apkgService.importApgFile(file.getOriginalFilename());
            } catch (final Exception ex) {
                logger.error("Something wrong happened while importing apkg file [{}]", file.getOriginalFilename(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
            }
        }

        return ResponseEntity.ok("Imported " + numberOfInsertedWords + " words");
    }


    @RequestMapping(value = "/delete/{id}/{password}", method = RequestMethod.GET)
    public ResponseEntity<String> deleteCard(@PathVariable("id") final String cardId, @PathVariable("password") final String password) {
        logger.info("Received delete card id request with id = [{}], password = [{}]", cardId, password);
        if (!PASSWORD.equals(password)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request failed");
        }

        int cardIdInt;
        try {
            cardIdInt = Integer.parseInt(cardId);
        } catch (Exception ex) {
            logger.error("Invalid id format", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid cardId");
        }

        final boolean deleted = cardService.deleteCard(cardIdInt);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Card deleted");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete card");
        }
    }
}
