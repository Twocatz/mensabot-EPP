package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrizeRangeAnalyzer<T> implements Analyzer<T> {
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        Analyzer<Meal> maxPriceAnalyzer = Analyzer.createMaxPrizeMealAnalyzer();
        var maxPriceMeal = maxPriceAnalyzer.analyze(data).get();
        int maxPrice = maxPriceMeal.getPrice();
        int priceCeiling = (int) Math.ceil(maxPrice / 100.0);
        Map<String, Integer> map = new HashMap<>();

        for (int i = 1; i <= priceCeiling; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i - 1);
            sb.append("-");
            sb.append(i);

            int mealsInThisBracket = 0;
            for (Menu menu : data) {
                for (Meal meal : menu.getMeals()) {
                    if (meal.getPrice() < i * 100) {
                        mealsInThisBracket++;
                    }
                }
            }
            map.put(sb.toString(), mealsInThisBracket);
        }


        return (OptionalWithMessage<T>) OptionalWithMessage.of(map);
    }

    public String toString() {
        return "PrizeRangeAnalyzer";
    }
}
