package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;

import java.util.List;
import java.util.function.Supplier;


public class FormatterFromAnalyzer1 implements Formatter {
    String headline;
    Analyzer<?> analyzer;

    public FormatterFromAnalyzer1(String headline, Analyzer<?> analyzer) {
        this.headline = headline;
        this.analyzer = analyzer;
    }

    @Override
    public OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus) {
        StringBuilder sb = new StringBuilder();
        sb.append(headline).append(":");

        String error = "Analyzing is not possible";
        String all = "";
        List<Menu> menus = List.of();

        OptionalWithMessage<List<Menu>> menu = allMenus.get();
        if (menu.isEmpty()) {
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

        /*if (allMenus == null)
            return OptionalWithMessage.of(headline + ":" + "Analyzing is not possible");
        if (allMenus.get().isEmpty())
            return OptionalWithMessage.of(headline + ":" + "Analyzing is not possible");
        if (analyzer.analyze(allMenus.get().get()).isEmpty())
            return OptionalWithMessage.of(headline + ":" + "Analyzing is not possible");
        String sb = headline +
                ":" +
                analyzer.analyze(allMenus.get().get()).orElse(null) +
                System.getProperty("line.separator");
        return OptionalWithMessage.of(sb.toString());*/
    }

    public String toString() {
        return "FormatterFromAnalyzer";
    }

}

