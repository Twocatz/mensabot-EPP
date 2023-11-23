package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.MensaMeals;
import de.uniwue.jpp.mensabot.dataclasses.MensaMenu;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public interface Fetcher {

    OptionalWithMessage<String> fetchCurrentData();

    static Fetcher createDummyCsvFetcher() {
        return new FetchData("");
    }

    /* Falls Sie Aufgabenteil 2 nicht bearbeiten, kann diese Methode ignoriert werden */
    static Fetcher createHttpFetcher(HttpClient client, String url) {
        throw new UnsupportedOperationException();
    }
}
