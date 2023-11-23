package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RandomMealFormatter implements Formatter{
    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        String date = latestMenu.getDate().toString();
        ArrayList<Meal> meal = new ArrayList<>(latestMenu.getMeals());
        int random = (int) (Math.random() * meal.size());
        return OptionalWithMessage.of("Zufälliges Gericht am " + date + System.getProperty("line.separator") + meal.get(random).toString());
    }

    public String toString() {
        return "RandomMealFormatter";
    }
}
