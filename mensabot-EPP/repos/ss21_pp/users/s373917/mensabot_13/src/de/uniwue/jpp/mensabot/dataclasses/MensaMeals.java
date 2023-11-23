package de.uniwue.jpp.mensabot.dataclasses;

public class MensaMeals implements Meal {
    String name;
    int price;

    public MensaMeals(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        MensaMeals mensaMeals = (MensaMeals) o;
        return getPrice() == mensaMeals.getPrice() & getName().equals(mensaMeals.getName());

    }

    public int hashCode() {
        int prime = 31;
        int result = 2;
        result = prime * result * price;
        return result;
    }

    public String toString() {
        int euro = 0;
        char cents1 = 0;
        char cents2 = 0;

        String lastDigits = "";

        euro = price / 100;
        String str = price + "";
        if (str.length() >= 2) {
            cents2 = str.charAt(str.length() - 1);
            cents1 = str.charAt(str.length() - 2);
            lastDigits = "" + cents1 + "" + cents2;
        }
        return name + " " + "(" + euro + "," + lastDigits + "\u20ac" + ")";
    }


}
