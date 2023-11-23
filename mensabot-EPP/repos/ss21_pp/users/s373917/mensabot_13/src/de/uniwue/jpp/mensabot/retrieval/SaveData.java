package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class SaveData implements Saver {
    String csvData;

    public SaveData(String csvData) {
        this.csvData = csvData;
    }

    @Override
    public Optional<String> log(Path path, Menu newMenu) {

        try {
            if (Files.exists(path)) {
                StringBuilder sb = new StringBuilder();
                sb.append(newMenu.toCsvLine());
                sb.append("\n");

                String menuInFile = null;
                File file = path.toFile();
                Scanner reader = new Scanner(file);

                while (reader.hasNext()) {
                    sb.append(reader.nextLine());
                    sb.append("\n");
                    //sb0.append(menuInFile).deleteCharAt(menuInFile.length()-1);

                }
           //     sb.append(newMenu.toCsvLine()).append(menuInFile);
           //    sb.append("\n");
               PrintWriter writer = new PrintWriter(new File(path.toString()));
               writer.write(String.valueOf(sb));
                writer.close();


            } else if (Files.notExists(path)) {
                /*File file = new File(String.valueOf(path));
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(newMenu.toCsvLine().getBytes(StandardCharsets.UTF_8));
                outputStream.write(Integer.parseInt(System.getProperty("line.separator")));
                outputStream.close();*/
                File file = path.toFile();
                PrintWriter writer = new PrintWriter(new File(path.toString()));
                writer.write(newMenu.toString());
                writer.close();
            }
        } catch (Exception e) {
            return Optional.of("error during saving");
        }
        return Optional.empty();
    }
}
