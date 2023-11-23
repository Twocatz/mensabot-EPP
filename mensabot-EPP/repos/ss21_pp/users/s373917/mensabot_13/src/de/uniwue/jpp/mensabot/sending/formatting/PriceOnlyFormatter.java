package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PriceOnlyFormatter implements Formatter{
    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        StringBuilder sb = new StringBuilder();
        String date = latestMenu.getDate().toString();
        ArrayList<Meal> meals = new ArrayList<>(latestMenu.getMeals());
        for (Meal meal : meals) {
            sb.append(meal.getPrice()).append(System.getProperty("line.separator"));
        }
        return OptionalWithMessage.of("Preise in Cent am " + date + System.getProperty("line.separator") + sb);
    }

    public String toString() {
        return "PriceOnlyFormatter";
    }
}
