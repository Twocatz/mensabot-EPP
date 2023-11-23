package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleMealFormatter implements Formatter{
    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        ArrayList<Meal> meal = new ArrayList<>(latestMenu.getMeals());
        return OptionalWithMessage.of(meal.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }
    public String toString() {
        return "SimpleMealFormatter";
    }
}
