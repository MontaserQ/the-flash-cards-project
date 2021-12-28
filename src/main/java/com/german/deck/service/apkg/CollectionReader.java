package com.german.deck.service.apkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.german.deck.service.apkg.ApkgReader.UNZIPPED_FILE_DIRECTORY;


@Component
public class CollectionReader {

    private static final Logger logger = LoggerFactory.getLogger(CollectionReader.class.getSimpleName());

    List<APkgCard> readCollection() throws SormulaException, SQLException, ClassNotFoundException {
        final File dbFile = new File(UNZIPPED_FILE_DIRECTORY + "/collection.anki2");
        final Database database = initSqliteConnection(dbFile);
        return readCards(database);
    }

    private Database initSqliteConnection(final File dbFile) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            final SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(true);

            final String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            final Connection connection = DriverManager.getConnection(url, config.toProperties());
            return new Database(connection);
        } catch (final Exception ex) {
            logger.error("Something wrong happened while establishing a connection to the SQLIte database file", ex);
            throw ex;
        }
    }

    private List<APkgCard> readCards(final Database database) throws SormulaException {
        try {
            final Table<APkgCard> table = database.getTable(APkgCard.class);
            return table.selectAll();
        } catch (final SormulaException ex) {
            logger.error("Something wrong happened while quering SQLite Card table", ex);
            throw ex;
        }
    }
}
