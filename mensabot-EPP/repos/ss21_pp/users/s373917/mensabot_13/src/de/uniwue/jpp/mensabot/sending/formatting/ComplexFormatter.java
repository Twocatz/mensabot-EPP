package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ComplexFormatter implements Formatter {
    List<String> headlines;
    List<Analyzer<?>> analyzers;

    public ComplexFormatter(List<String> headlines, List<Analyzer<?>> analyzers) {
        this.headlines = headlines;
        this.analyzers = analyzers;
    }

    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < analyzers.size(); i++) {
            sb.append(headlines.get(i)).append(":");
            if (allMenus.get().isEmpty()) {
                sb.append("Analyzing is not possible");
            } else {
                List<Menu> menus = new ArrayList<>(allMenus.get().get());
                if (analyzers.get(i).analyze(menus).isPresent()) {
                    sb.append(analyzers.get(i).analyze(menus).get().toString());
                } else {
                    sb.append("Analyzing is not possible");
                }
            }
            sb.append(System.getProperty("line.separator"));
        }

        /*for (Menu menu : menus) {
            if (menu.getMeals().isEmpty()) {
                sb.append("Analyzing is not possible");
            }
            sb.append(System.getProperty("line.separator"));
        }*/
        return OptionalWithMessage.of(sb.toString());
        /*List<Menu> menus = new ArrayList<>(List.of());
        StringBuilder sb = new StringBuilder();
        for (Menu menu : menus) {
            for (String headline : headlines) {
                sb.append(headline).append(":");
                for (Analyzer<?> analyzer : analyzers) {
                    if (analyzer.analyze(menus).isPresent()) {
                        sb.append(analyzer.analyze(menus).get().toString());
                    } else if (!(analyzer.analyze(menus).isPresent())) {
                        sb.append("Analyzing is not possible");
                    }
                    sb.append(System.getProperty("line.separator"));
                }
            }
        }
        return OptionalWithMessage.of(sb.toString());*/
    }

    public String toString() {
        return "ComplexFormatter";
    }
}
