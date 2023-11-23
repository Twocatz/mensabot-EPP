package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Analyzer<T> {

    OptionalWithMessage<T> analyze_unsafe(List<Menu> data);

    default OptionalWithMessage<T> analyze(List<Menu> data) {
        if (data == null) {
            return OptionalWithMessage.ofMsg("Invalid data argument!");
        }
        if (data.isEmpty()) {
            return OptionalWithMessage.ofMsg("Invalid data argument!");
        }
        return analyze_unsafe(data);
    }

    default OptionalWithMessage<String> analyse(List<Menu> data, Function<T, String> convert) {
        if (convert == null) {
            return OptionalWithMessage.ofMsg("No convert-function given!");
        }
        T t = analyze(data).orElse(null);
        if (t == null) {
            return OptionalWithMessage.ofMsg("Invalid data argument!");
        }
        String result = convert.apply(t);
        return OptionalWithMessage.of(result);
    }


    static Analyzer<Integer> createAveragePrizeAnalyzer() {
        return new AveragePrizeAnalyzer<>();
    }

    static Analyzer<Integer> createMedianPrizeAnalyzer() {
        return new MedianPrizeAnalyzer<>(null);
    }

    static Analyzer<Meal> createMinPrizeMealAnalyzer() {
        return new MinPrizeMealAnalyzer<>(null);
    }

    static Analyzer<Meal> createMaxPrizeMealAnalyzer() {
        return new MaxPrizeMealAnalyzer<>(null);
    }

    static Analyzer<Integer> createTotalPrizeAnalyzer() {
        return new TotalPrizeAnalyzer<>();
    }

    /* Die folgenden Analyzer werden nicht getestet und muessen nicht implementiert werden.
     * Es sind lediglich Vorschlaege fuer weitere Analyzer fuer Aufgabenteil 3. */
    static Analyzer<String> createAveragePrizePerDayAnalyzer() {
        return new AveragePricePerDayAnalyzer<>();
    }

    static Analyzer<String> createTotalPrizePerDayAnalyzer() {
        return new TotalPricePerDayAnalyzer<>();
    }

    static Analyzer<Meal> createPopularityAnalyzer(int rank) {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Meal> createPopularMealOnWeekdayAnalyzer(DayOfWeek dayOfWeek) {
        throw new UnsupportedOperationException();
    }
    static Analyzer<Meal> createMostOfferedMealOfTheWeek() {
        return new MostOfferedMealOfWeek<>();
    }

    static Analyzer<Map<String, Integer>> createPrizeRangeAnalyzer() {
        return new PrizeRangeAnalyzer<>();
    }

    static Analyzer<Map<LocalDate, Integer>> createAmountOfDishesPerDayAnalyzer() {
        throw new UnsupportedOperationException();
    }

    static Analyzer<Map<Meal, Long>> createRepetitionAnalyzer() {
        throw new UnsupportedOperationException();
    }

}
