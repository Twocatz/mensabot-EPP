package de.uniwue.jpp.mensabot.dataclasses;

import java.time.LocalDate;
import java.util.Set;

public interface Menu {

    LocalDate getDate();

    Set<Meal> getMeals();

    String toCsvLine();

    static Menu createMenu(LocalDate date, Set<Meal> meals) {
        if (date == null)
            throw new IllegalArgumentException("You must enter a date!");

        if (meals == null)
            throw new IllegalArgumentException("No meals inside the Set!");

        for (Meal meal : meals) {
            if (meal == null)
                throw new IllegalArgumentException("At least one of the meals was null!");
        }


        if (meals.isEmpty())
            throw new IllegalArgumentException("No meals inside the Set!");
        return new MensaMenu(date, meals);
    }
}
