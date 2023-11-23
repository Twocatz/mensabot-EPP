package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.MensaMeals;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.*;

public class MinPrizeMealAnalyzer<T> implements Analyzer<T>{
    List<Menu> data;
    public MinPrizeMealAnalyzer(List<Menu> data) {
        this.data = data;
    }
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        HashSet<Meal> meals = new HashSet<>();
        ArrayList<Integer> prices = new ArrayList<>();
        Meal cheapestMeal = null;
        for (Menu menu : data) {
            meals.addAll(menu.getMeals());
        }
        for (Meal meal: meals) {
            prices.add(meal.getPrice());
        }
        int minPrice = Collections.min(prices);

        for (Meal meal: meals) {
            if (meal.getPrice() == minPrice)
                cheapestMeal = meal;
        }
        return (OptionalWithMessage<T>) OptionalWithMessage.of(cheapestMeal);


    }
    public String toString() {
        return "MinPrizeMealAnalyzer";
    }
}
