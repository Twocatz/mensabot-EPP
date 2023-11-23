package de.uniwue.jpp.mensabot.dataclasses;

public interface Meal {
    String getName();
    int getPrice(); // in cents

    static Meal createMeal(String name, int price) {
        if (name == null)
            throw new IllegalArgumentException("The Meal must have a name!");
        if (name.equals(""))
            throw new IllegalArgumentException("Meal's name must have at least 2 letters!");
        if (price < 0)
            throw new IllegalArgumentException("The Meal's price can't be less than 0!");
        else return new MensaMeals(name, price);
    }
}
