package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;

import java.util.List;
import java.util.function.Supplier;

public class FormatterFromAnalyzer2 implements Formatter {
    String headline;
    Analyzer<?> analyzer;
    String name;

    public FormatterFromAnalyzer2(String headline, Analyzer<?> analyzer, String name) {
        this.headline = headline;
        this.analyzer = analyzer;
        this.name = name;
    }

    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        StringBuilder sb = new StringBuilder();
        sb.append(headline).append(":");

        String error = "Analyzing is not possible";
        String all = "";
        OptionalWithMessage<List<Menu>> menu = allMenus.get(); //hier NullPointerException, weil wir in der GUI null eingetragen haben für allMenus
        List<Menu> menus = List.of();
        if (menu.isEmpty()){
            sb.append(error);
        } else {
            menus = menu.get();
            if (menus.isEmpty()) {
                sb.append(error);
            } else {
                if (analyzer.analyze(menus).isEmpty())
                    sb.append(error);
            }
        }

        OptionalWithMessage<?> a = analyzer.analyze(menus);
        if (a.isPresent()) {
            sb.append(a.get().toString());
        }

        sb.append(System.getProperty("line.separator"));
        return OptionalWithMessage.of(sb.toString());

    }

    public String toString() {
        return name;
    }

}
