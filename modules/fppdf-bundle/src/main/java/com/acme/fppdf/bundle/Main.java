package com.acme.fppdf.bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.fppdf.bundle.content.ContentProviderManager;
import com.acme.fppdf.bundle.content.ExternalContentProvider;
import com.acme.fppdf.bundle.content.ExternalFileContentProvider;
import com.acme.fppdf.bundle.content.URLContentProvider;

/**
 * Bundle generator for Freeplane mindmap files
 *
 */
public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * Entry point for standalone invocation
     *
     * @param args
     *            Names of mindmap files to be converted. Output file names will have <i>".zipˇ</i> extension appended.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            ContentProviderManager manager = ProviderManagerAssembler.assemble();

            for (String inputFileName : args) {
                String outputFileName = inputFileName + ".zip";
                LOGGER.info(String.format("Input file: '%s', Output file: '%s'", inputFileName, outputFileName));

                try (
                        InputStream src = new FileInputStream(inputFileName);
                        ZipOutputStream dst = new ZipOutputStream(new FileOutputStream(outputFileName))) {

                    String baseDirName = new File(inputFileName).getParentFile().getAbsolutePath();
                    LOGGER.debug("Base directory: '" + baseDirName + "'");

                    new ConverterMmToZip(src, dst, inputFileName, manager).convert(baseDirName);
                    LOGGER.info(String.format("Input file '%s' processed", inputFileName));
                    LOGGER.info("--------------------------------------------------------------------------------");
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    LOGGER.debug(e.getMessage(), e);
                }
            }
            LOGGER.info("All input files processed");
        } else {
            LOGGER.error("Usage: " + Main.class.getName() + " inputFileName...");
        }
    }

    private static final class ProviderManagerAssembler {
        private ProviderManagerAssembler() {
        }

        static ContentProviderManager assemble() {
            ContentProviderManager manager = new ContentProviderManager();

            ExternalContentProvider fileContentProvider = new ExternalFileContentProvider();
            manager.registerDefault(fileContentProvider);
            manager.register(ProtocolType.FILE.getPrefix(), fileContentProvider);

            URLContentProvider urlContentProvider = new URLContentProvider();
            manager.register(ProtocolType.HTTP.getPrefix(), urlContentProvider);
            manager.register(ProtocolType.HTTPS.getPrefix(), urlContentProvider);
            return manager;
        }
    }
}
