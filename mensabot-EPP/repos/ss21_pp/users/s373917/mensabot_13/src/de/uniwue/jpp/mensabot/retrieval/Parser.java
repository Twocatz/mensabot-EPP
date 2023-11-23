package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.time.LocalDate;

public interface Parser {

    OptionalWithMessage<Menu> parse(String fetched);

    static Parser createCsvParser() {
        return new ParseData("");
    }

    /* Falls Sie Aufgabenteil 2 nicht bearbeiten, kann diese Methode ignoriert werden */
    static Parser createMensaHtmlParser(LocalDate date) {
        throw new UnsupportedOperationException();
    }
}
