package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostOfferedMealOfWeek<T> implements Analyzer<T> {
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        Map<String, Long> countsOfMeals = data.stream().collect(Collectors.groupingBy(Object::toString, Collectors.counting()));
        Map.Entry<String, Long> maxEntry = null;

        for (Map.Entry<String,Long> entry : countsOfMeals.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return (OptionalWithMessage<T>) OptionalWithMessage.of(maxEntry);
    }
}
