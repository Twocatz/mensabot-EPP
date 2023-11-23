package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.TotalPrizeAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FormatterFromFormat implements Formatter {
    String format;
    List<Analyzer<?>> analyzers;
    String name;

    public FormatterFromFormat(String format, List<Analyzer<?>> analyzers, String name) {
        this.format = format;
        this.analyzers = analyzers;
        this.name = name;
    }

    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        String formatBeforeFormatter = format;
        int numberOfAnalyzers = analyzers.size();
        String error = "Analyzing is not possible";
        String resultAfterAnalysis = "";
        OptionalWithMessage<List<Menu>> menu = allMenus.get();
        List<Menu> menus;

        for (int i = 0; i < analyzers.size(); i++) {
            Analyzer<?> analyzer = analyzers.get(i);
            if (allMenus.get().isEmpty()) {
                resultAfterAnalysis = replace(error);
            } else {
                menus = new ArrayList<>(menu.get());
                if (analyzers.get(i).analyze(menus).isPresent()) {
                    resultAfterAnalysis = replace(analyzer.analyze(menus).get().toString());
                } else {
                    resultAfterAnalysis = replace(error);
                }
            }
        }
        format = formatBeforeFormatter;
        return OptionalWithMessage.of(resultAfterAnalysis);
        /*String formatBeforeFormatter = format;
        int numberOfAnalyzers = analyzers.size();
        String error = "Analyzing is not possible";
        String resultAfterAnalysis = "";
        OptionalWithMessage<List<Menu>> menu = OptionalWithMessage.of(List.of(latestMenu));
        List<Menu> menus;

        for (int i = 0; i < analyzers.size(); i++) {
            Analyzer<?> analyzer = analyzers.get(i);
            menus = new ArrayList<>(menu.get());
            if (analyzers.get(i).analyze(menus).isPresent()) {
                resultAfterAnalysis = replace(analyzer.analyze(menus).get().toString());
            } else {
                resultAfterAnalysis = replace(error);
            }
        }
        format = formatBeforeFormatter;
        return OptionalWithMessage.of(resultAfterAnalysis);*/

        /*if (menu.isEmpty()) {
            resultAfterAnalysis = error;
        } else {
            menus = menu.get();
            if (menus.isEmpty()) {
                resultAfterAnalysis = error;
            }
        }
        menus = allMenus.get().get();
            for (int i = 0; i < numberOfAnalyzers; i++) {
                Analyzer<?> analyzer = analyzers.get(i);
                //String replacement = analyzer.analyze(menus).get().toString();
                if (analyzer.analyze(menus).get().toString().isEmpty() | allMenus.get().isEmpty()) {
                    resultAfterAnalysis = replace(error);
                } else {
                    resultAfterAnalysis = replace(analyzer.analyze(menus).get().toString());
                }
            }
        format = formatBeforeFormatter;

        return OptionalWithMessage.of(resultAfterAnalysis);*/
    }

    private String replace(String msg) {
        String before = format.substring(0, format.indexOf("$") + 1);
        String after = format.substring(format.indexOf("$") + 1, format.length());
        before = before.replace("$", msg);
        format = before + after;
        return format;
    }

    public String toString() {
        return name;
    }
}

