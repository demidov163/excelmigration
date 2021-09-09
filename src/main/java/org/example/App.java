package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Hello World!");
        ExcelCatalogParser parser = new ExcelCatalogParser();
        final Map<String, CatalogRow> catalogRows = parser.parseCatalog("catalog.xls");
        final File[] oldfiles =  new File(App.class.getClassLoader().getResource("oldfiles").toURI()).listFiles();
        for (File oldfile : oldfiles) {
            final String name = oldfile.getName();
            final String[] split = name.split("\\.");
            if (split.length != 2) {
                moveOldFileToErrorFolder(oldfile);
                //move errorfiles
            } else {
                final CatalogRow catalogRow = catalogRows.get(split[0]);
                if (catalogRow != null) {
                    final Path newFilepath = oldfile.toPath()
                            .getParent()
                            .getParent()
                            .resolve("newfiles/" + catalogRow.newId() + "." + split[1]);
                    moveFileToNewDir(oldfile, newFilepath);
                } else {
                    moveOldFileToErrorFolder(oldfile);
                }

            }
        }


        //System.out.println(parser.parseCatalog("catalog.xls"));
    }

    private static void moveOldFileToErrorFolder(File oldfile) throws IOException {
        final Path newFilepath = oldfile.toPath()
                .getParent()
                .getParent()
                .resolve("errorfiles/" + oldfile.getName());
        moveFileToNewDir(oldfile, newFilepath);
    }

    private static void moveFileToNewDir(File oldfile, Path newFilepath) throws IOException {
        newFilepath.toFile().mkdirs();
        newFilepath.toFile().createNewFile();
        Files.copy(oldfile.toPath(), newFilepath,
                StandardCopyOption.REPLACE_EXISTING);
    }
}
