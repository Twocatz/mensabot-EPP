package de.uniwue.jpp.mensabot.sending;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.MensaMeals;
import de.uniwue.jpp.mensabot.dataclasses.MensaMenu;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.ParseData;
import de.uniwue.jpp.mensabot.retrieval.Parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class ImportData implements Importer{
    @Override
    public OptionalWithMessage<Menu> getLatest(BufferedReader fileReader) {
        Menu menu = null;
        String line;
        try {
            if ((line = fileReader.readLine()) != null) {
                Parser parser = new ParseData(line);
                menu = parser.parse(line).get();
            }
            return OptionalWithMessage.of(menu);
        } catch (Exception e) {
            return OptionalWithMessage.ofMsg("Fehler");
        }
        /*try {
            String line = fileReader.readLine();
            String dateString = line.split(";")[0];
            int a = line.split(";").length;
            LocalDate date = LocalDate.parse(dateString,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            HashSet<Meal> meals = new HashSet<>();
                for (int i = 1; i < a; i++) {
                    String name = line.split(";")[i].split("_")[0];
                    int price = Integer.parseInt(line.split(";")[i].split("_")[i]);
                    meals.add(new MensaMeals(name, price));
                }

                return OptionalWithMessage.of(new MensaMenu(date, meals));

        } catch (Exception e){
            return OptionalWithMessage.ofMsg("Fehler");
        }*/
    }

    @Override
    public OptionalWithMessage<List<Menu>> getAll(BufferedReader fileReader) {
        List<Menu> menus = new ArrayList<>();
        String line;
        try {
            while((line = fileReader.readLine()) != null) {
                Parser parser = new ParseData(line);
                menus.add(parser.parse(line).get());
            }
            return OptionalWithMessage.of(menus);
        } catch (Exception e) {
            return OptionalWithMessage.ofMsg("Fehler");
        }
        /*List<Menu> menus = new ArrayList<>();
        String line;

        try {
                while ((line = fileReader.readLine()) != null) {
                    String dateString = line.split(";")[0];
                    LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    HashSet<Meal> meals = new HashSet<>();
                    if (line.split(";").length > 2) {
                        for (int i = 1; i < line.split(";").length; i++) {
                            String name = line.split(";")[i].split("_")[0];
                            int price = Integer.parseInt(line.split(";")[i].split("_")[i]);
                            meals.add(new MensaMeals(name, price));
                        }
                    }
                    menus.add(new MensaMenu(date, meals));
                }

            return OptionalWithMessage.of(menus);

        } catch (IOException e) {
            return OptionalWithMessage.ofMsg("Fehler");
        }*/

    }
}
