package de.uniwue.jpp.mensabot.sending.formatting.analyse;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

public class TotalPricePerDayAnalyzer<T> implements Analyzer<T>{
    @Override
    public OptionalWithMessage<T> analyze_unsafe(List<Menu> data) {
        int sum = 0;
        int size = 0;
        StringBuilder sb = new StringBuilder();
        for (Menu menu: data) {
            sb.append(convertDate(menu.getDate().toString())).append(": ");
            for (Meal meal: menu.getMeals()) {
                sum += meal.getPrice();

            }
            sb.append(euroFormat(sum)).append(System.getProperty("line.separator"));
            sum = 0;
        }
        return (OptionalWithMessage<T>) OptionalWithMessage.of(sb.toString());
    }
    public String toString() {
        return "TotalPricePerDay";
    }
    private String euroFormat(int price) {
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
        return euro + "," + lastDigits + "\u20ac";
    }
    public String convertDate(String date) {
        try {
            DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            return dateFormatter.format(dateFormatter1.parse(date));
        } catch (Exception e) {
            return null;
        }

    }
}
