package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MaxPrizeMealAnalyzer<T> implements Analyzer<T>{
    List<Menu> data;
    public MaxPrizeMealAnalyzer(List<Menu> data) {
        this.data = data;
    }
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        HashSet<Meal> meals = new HashSet<>();
        ArrayList<Integer> prices = new ArrayList<>();
        Meal mostExpensiveMeal = null;
        for (Menu menu : data) {
            meals.addAll(menu.getMeals());
        }
        for (Meal meal: meals) {
            prices.add(meal.getPrice());
        }
        int minPrice = Collections.max(prices);

        for (Meal meal: meals) {
            if (meal.getPrice() == minPrice)
                mostExpensiveMeal = meal;
        }
        return (OptionalWithMessage<T>) OptionalWithMessage.of(mostExpensiveMeal);
    }
    public String toString() {
        return "MaxPrizeMealAnalyzer";
    }
}
