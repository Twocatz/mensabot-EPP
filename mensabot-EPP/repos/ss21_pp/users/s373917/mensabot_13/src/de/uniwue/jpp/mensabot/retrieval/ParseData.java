package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.MensaMeals;
import de.uniwue.jpp.mensabot.dataclasses.MensaMenu;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class ParseData implements Parser {
    String parser;

    public ParseData(String parser) {
        this.parser = parser;
    }

    @Override
    public OptionalWithMessage<Menu> parse(String fetched) {
        HashSet<Meal> menu = new HashSet<Meal>();
        LocalDate localDate;

        String[] partsOfMenu = fetched.split(";|_", fetched.length());
        if (!(canBeParsed(fetched))) {
            return OptionalWithMessage.ofMsg("Input does not match! Input was: '" + fetched + "'");
        } else if (partsOfMenu.length < 3) {
            return OptionalWithMessage.ofMsg("Input does not match! Input was: '" + fetched + "'");
        } else if (fetched.equals("")) {
            return OptionalWithMessage.ofMsg("Input does not match! Input was: '" + fetched + "'");
        } else if (containsLetters(partsOfMenu[0])) {
            return OptionalWithMessage.ofMsg("Input does not match! Input was: '" + fetched + "'");
        }
        try {
            String date = partsOfMenu[0];
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return OptionalWithMessage.ofMsg("Invalid date");
        }

        for (int i = 1; i < partsOfMenu.length; i++) {
            try {
                Integer.parseInt(partsOfMenu[i + 1]);
            } catch (Exception e) {
                return OptionalWithMessage.ofMsg("Input does not match! Input was: '" + fetched + "'");
            }
            menu.add(Meal.createMeal(partsOfMenu[i], Integer.parseInt(partsOfMenu[i + 1])));
            i++;

        }
        return OptionalWithMessage.of(Menu.createMenu(localDate, menu));
    }

    public boolean canBeParsed(String fetched) {
        if (fetched.isEmpty())
            return false;
        return fetched.contains(";") & fetched.contains("_");
    }

    public boolean containsLetters(String fetched) {
        return fetched.matches("[a-zA-Z]+");
    }
}
