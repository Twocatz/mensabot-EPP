package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.errorhandling.OptionalWithMessageMsg;
import de.uniwue.jpp.mensabot.dataclasses.MensaMenu;
import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.time.LocalDate;

public class FetchData implements Fetcher{
    String content;

    public FetchData(String content) {
        this.content = content;

    }

    @Override
    public OptionalWithMessage<String> fetchCurrentData() {
        return OptionalWithMessage.of("2021-03-22;Schneller Teller: Maultaschen mit geschmelzten Zwiebeln und Salat_290;" +
                "Schneller Teller: Vegane Gemüsemaultaschen mit geschmelzten Zwiebeln und Salat_290;" +
                "Ceasar-Salat mit Hähnchenbrust und Croutons_320;" + "Gemüsesticks auf Couscous-Salat_280;" + "Kleine Schale Pommes_100");
    }
    /*"2021-03-22;Schneller Teller: Maultaschen mit geschmelzten Zwiebeln und Salat_290;" +
            "Schneller Teller: Vegane Gemüsemaultaschen mit geschmelzten Zwiebeln und Salat_290;" +
            "Ceasar-Salat mit Hähnchenbrust und Croutons_320;" + "Gemüsesticks auf Couscous-Salat_280;" + "Kleine Schale Pommes_100"*/
}
