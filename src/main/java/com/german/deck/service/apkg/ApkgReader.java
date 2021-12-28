package com.german.deck.service.apkg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ApkgReader {

    private static final Logger logger = LoggerFactory.getLogger(ApkgReader.class);

    public static final String UNZIPPED_FILE_DIRECTORY = "apkgFiles/unzipped";

    void read(final File apkgFile) throws IOException {
        try {
            createUnzippedDirectoryIfNotExist();
            unzipApkg(apkgFile);
        } catch (IOException ex) {
            logger.error("Something wrong happened while unzipping apg file [{}]", apkgFile.getName(), ex);
            throw ex;
        }
    }

    private void createUnzippedDirectoryIfNotExist() {
        final File unzipped = new File(UNZIPPED_FILE_DIRECTORY);
        if (!unzipped.exists()) {
            final boolean createdUnzippedFile = unzipped.mkdir();
            if (createdUnzippedFile) {
                logger.info("Created directory apkgFiles/unzipped");
            }
        }
    }

    private void unzipApkg(final File fileZip) throws IOException {
        final byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                final String fileName = zipEntry.getName();
                final File newFile = new File(UNZIPPED_FILE_DIRECTORY + "/" + fileName);
                final FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                int length = zis.read(buffer);

                while (length > 0) {
                    fileOutputStream.write(buffer, 0, length);
                    length = zis.read(buffer);
                }

                fileOutputStream.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }
}
