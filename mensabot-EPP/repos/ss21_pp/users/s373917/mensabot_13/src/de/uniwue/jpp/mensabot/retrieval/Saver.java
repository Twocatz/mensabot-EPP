package de.uniwue.jpp.mensabot.retrieval;

import de.uniwue.jpp.mensabot.dataclasses.Menu;

import java.nio.file.Path;
import java.util.Optional;

public interface Saver {

    Optional<String> log(Path path, Menu newMenu);

    static Saver createCsvSaver() {
        return new SaveData("");
    }
}
