package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LongestNameFormatter implements Formatter{
    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        String date = latestMenu.getDate().toString();
        StringBuilder sb = new StringBuilder();
        String longestNameMeal = null;
        ArrayList<Meal> meals = new ArrayList<>(latestMenu.getMeals());
        ArrayList<Integer> amountOfChars = new ArrayList<>();
        for (Meal meal : meals) {
            amountOfChars.add(meal.getName().length());
        }
        int i = Collections.max(amountOfChars);
        for (Meal meal : meals) {
            if (meal.getName().length() == i) {
                longestNameMeal = meal.getName().toString();
            }
        }
        return OptionalWithMessage.of("Essen mit der längsten Beschreibung am " + date + System.getProperty("line.separator") + longestNameMeal);
    }

    public String toString() {
        return "LongestNameFormatter";
    }

}
