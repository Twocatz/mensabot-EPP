package de.uniwue.jpp.mensabot.sending.formatting;

import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;

import java.util.List;
import java.util.function.Supplier;

public interface Formatter {

    OptionalWithMessage<String> format(Menu latestMenu, Supplier<OptionalWithMessage<List<Menu>>> allMenus);

    static Formatter createSimpleFormatter() {
        return new SimpleFormatter();
    }

    static Formatter createSimpleMealFormatter() {
        return new SimpleMealFormatter();
    }

    static Formatter createFormatterFromAnalyzer(String headline, Analyzer<?> analyzer) {
        if (headline == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (headline.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if (analyzer == null)
            throw new IllegalArgumentException("Illegal argument!");
        return new FormatterFromAnalyzer1(headline, analyzer);
    }

    static Formatter createFormatterFromAnalyzer(String headline, Analyzer<?> analyzer, String name) {
        if (headline == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (headline.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if (analyzer == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (name == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (name.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        return new FormatterFromAnalyzer2(headline, analyzer, name);
    }

    static Formatter createComplexFormatter(List<String> headlines, List<Analyzer<?>> analyzers) {
        if (headlines == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (headlines.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if (analyzers == null)
            throw new IllegalArgumentException("Illegal argument!");
        if (analyzers.isEmpty())
            throw new IllegalArgumentException("Illegal argument!");
        if (headlines.size() != analyzers.size())
            throw new IllegalArgumentException("There must be a headline for each analyzer!");
        return new ComplexFormatter(headlines,analyzers);
    }

    static Formatter createFormatterFromFormat(String format, List<Analyzer<?>> analyzers, String name) {
        if (format == null)
            throw new IllegalArgumentException("Illegal format argument!");
        if (format.isEmpty())
            throw new IllegalArgumentException("Illegal format argument!");
        if (!(format.contains("$")))
            throw new IllegalArgumentException("Format must contain $ signs!");
        int countSigns = format.length() - format.replace("$","").length();
        if (name == null)
            throw new IllegalArgumentException("Illegal name argument!");
        if (name.isEmpty())
            throw new IllegalArgumentException("Illegal name argument!");
        if (analyzers == null)
            throw new IllegalArgumentException("Illegal analyzer argument!");
        if (analyzers.isEmpty())
            throw new IllegalArgumentException("Illegal analyzer argument!");
        if (analyzers.size() != countSigns)
            throw new IllegalArgumentException("There must be a $ for each analyzer");
        return new FormatterFromFormat(format,analyzers,name);
    }


    /* Die folgenden Formatter werden nicht getestet und muessen auch nicht implementiert werden.
     * Es sind lediglich Vorschlaege für Aufgabenteil 3. */
    static Formatter createHiddenFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createFirstWordFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createShortFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createPricelessFormatter() { return new PricelessFormatter();
    }

    static Formatter createSimpleTotalFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createAlphabeticalOrderFormatter() {
        throw new UnsupportedOperationException();
    }

    static Formatter createRandomMealFormatter() {
        return new RandomMealFormatter();
    }

    static Formatter createPriceOnlyFormatter() {
        return new PriceOnlyFormatter();
    }

    static Formatter createLongestNameFormatter() {
        return new LongestNameFormatter();
    }

}
