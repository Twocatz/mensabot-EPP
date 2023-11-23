package de.uniwue.jpp.mensabot.dataclasses;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

public class MensaMenu implements Menu{
    final Set<Meal> meals;
    LocalDate date;

    public MensaMenu(LocalDate date, Set<Meal>meals) {
        this.date = date;
        this.meals = meals;

    }
    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Set<Meal> getMeals() {
        return Collections.unmodifiableSet(meals);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        MensaMenu mensaMenu = (MensaMenu) o;
        return mensaMenu.getDate().equals(this.getDate());
    }

    public int hashCode() {
        final int prime = 31;
        int result = 2;
        return 0;
    }

    @Override
    public String toCsvLine() {
        StringBuilder allMeals = new StringBuilder();
        for (Meal meal : meals) {
            allMeals.append(meal.getName()).append("_").append(meal.getPrice()).append(";");
        }
        allMeals.deleteCharAt(allMeals.length()-1);
        return date + ";" + allMeals;
    }

    public String toString() {
        return toCsvLine();
    }
}
