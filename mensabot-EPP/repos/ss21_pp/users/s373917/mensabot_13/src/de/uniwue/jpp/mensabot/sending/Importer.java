package de.uniwue.jpp.mensabot.sending;

import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.errorhandling.OptionalWithMessage;

import java.io.BufferedReader;
import java.util.List;

public interface Importer {

    OptionalWithMessage<Menu> getLatest(BufferedReader fileReader);
    OptionalWithMessage<List<Menu>> getAll(BufferedReader fileReader);

    static Importer createCsvImporter() {
        return new ImportData();
    }
}
