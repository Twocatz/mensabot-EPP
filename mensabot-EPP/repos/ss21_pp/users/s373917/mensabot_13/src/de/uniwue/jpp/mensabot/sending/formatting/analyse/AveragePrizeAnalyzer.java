package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AveragePrizeAnalyzer<T> implements Analyzer<T> {


    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        int sum = 0;
        int size = 0;
        HashSet<Meal> meals = new HashSet<>();

        for (Menu menu : data) {
            meals.addAll(menu.getMeals());
        }
        for (Meal meal : meals) {
            sum += meal.getPrice();
            size = meals.size();
        }
        return (OptionalWithMessage<T>) OptionalWithMessage.of(sum / size);
    }

    public String toString() {
        return "AveragePrizeAnalyzer";
    }
}
