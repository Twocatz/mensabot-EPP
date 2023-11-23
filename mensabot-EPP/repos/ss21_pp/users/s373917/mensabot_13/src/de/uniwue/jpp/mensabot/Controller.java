package de.uniwue.jpp.mensabot;

import de.uniwue.jpp.mensabot.retrieval.Fetcher;
import de.uniwue.jpp.mensabot.retrieval.Parser;
import de.uniwue.jpp.mensabot.retrieval.Saver;
import de.uniwue.jpp.mensabot.sending.Importer;
import de.uniwue.jpp.mensabot.sending.Sender;
import de.uniwue.jpp.mensabot.sending.formatting.Formatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


public interface Controller {

    Optional<String> retrieveData();
    Optional<String> send(Formatter formatter);

    static Controller create(Fetcher f, Parser p, Saver sav, Path logfile, Importer i, Sender s) {
        if (f == null)
            throw new NullPointerException("Fetcher can't be null!");
        if (p == null)
            throw new NullPointerException("Parser can't be null!");
        if (sav == null)
            throw new NullPointerException("Saver can't be null!");
        if (logfile == null)
            throw new NullPointerException("Importer can't be null!");
        if (s == null)
            throw new NullPointerException("Sender can't be null!");
        return new ControllerDummy(f,p,sav,logfile,i,s);
    }

    static void executeDummyPipeline() {
        String directory = "dummylog.csv";
        Path logfile = Paths.get(directory);
        create(Fetcher.createDummyCsvFetcher(), Parser.createCsvParser(), Saver.createCsvSaver(), logfile, Importer.createCsvImporter(), Sender.createDummySender());
    }
}
