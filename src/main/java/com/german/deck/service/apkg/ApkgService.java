package com.german.deck.service.apkg;

import com.german.deck.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sormula.SormulaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.german.deck.service.apkg.ApkgReader.UNZIPPED_FILE_DIRECTORY;


@Service
public class ApkgService {

    private static final Logger logger = LoggerFactory.getLogger(ApkgService.class);

    @Autowired
    private CardService cardService;

    @Autowired
    private ApkgReader apkgReader;

    @Autowired
    private CollectionReader collectionReader;


    /***
     * 
     * @param apkgFileName
     * @return number of inserted rows
     * @throws IOException
     * @throws SormulaException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int importApgFile(final String apkgFileName) throws IOException, SormulaException, SQLException, ClassNotFoundException {
        logger.info("Import APKG file [{}]", apkgFileName);

        final File apkgFile = new File("apkgFiles/" + apkgFileName);

        apkgReader.read(apkgFile);

        final List<APkgCard> aPkgCards = collectionReader.readCollection();

        int numberOfInsertedRows = 0;

        for (final APkgCard aPkgCard : aPkgCards) {
            final String[] split = aPkgCard.getFlds().split("\\x1f");
            String rootGermanForm = split.length > 2 ? split[2] : split[0];
            final boolean isInserted = cardService.insertCard(split[0], split[1], rootGermanForm);
            numberOfInsertedRows += (isInserted) ? 1 : 0;
        }

        final boolean isDeleted = deleteDirectory(new File(UNZIPPED_FILE_DIRECTORY));
        if (!isDeleted) {
            logger.warn("Could not delete unzipped directory");
        }

        logger.info("Import complete");

        return numberOfInsertedRows;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        final File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            Arrays.stream(allContents).forEachOrdered(this::deleteDirectory);
        }
        return directoryToBeDeleted.delete();
    }
}
