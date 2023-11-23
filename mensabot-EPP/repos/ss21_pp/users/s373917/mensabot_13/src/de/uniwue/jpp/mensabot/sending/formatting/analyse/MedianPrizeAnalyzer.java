package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MedianPrizeAnalyzer<T> implements Analyzer<T> {
    List<Menu> data;
    public MedianPrizeAnalyzer(List<Menu> data) {
        this.data = data;
    }
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        HashSet<Meal> meals = new HashSet<>();
        ArrayList<Integer> prices = new ArrayList<>();
        int middle;
        for (Menu menu : data) {
            meals.addAll(menu.getMeals());
        }
        for (Meal meal: meals) {
            prices.add(meal.getPrice());
        }
        Collections.sort(prices);
        if (prices.size() == 1) {
            return (OptionalWithMessage<T>) OptionalWithMessage.of(prices.get(0));
        }
        if (prices.size() % 2 != 0) {
            middle = (prices.size()/2);
            return (OptionalWithMessage<T>) OptionalWithMessage.of(prices.get(middle));
        }
        middle = (prices.size()/2) - 1;
        return (OptionalWithMessage<T>) OptionalWithMessage.of(prices.get(middle));
    }

    public String toString() {
        return "MedianPrizeAnalyzer";
    }
}
