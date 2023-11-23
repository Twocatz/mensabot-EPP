package de.uniwue.jpp.mensabot;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.errorhandling.OptionalWithMessageVal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.FetchData;
import de.uniwue.jpp.mensabot.retrieval.Fetcher;
import de.uniwue.jpp.mensabot.retrieval.Parser;
import de.uniwue.jpp.mensabot.retrieval.Saver;
import de.uniwue.jpp.mensabot.sending.Importer;
import de.uniwue.jpp.mensabot.sending.Sender;
import de.uniwue.jpp.mensabot.sending.formatting.Formatter;
import de.uniwue.jpp.mensabot.sending.formatting.SimpleFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;

public class ControllerDummy implements Controller {
    Fetcher f;
    Parser p;
    Saver sav;
    Path logfile;
    Importer i;
    Sender s;

    public ControllerDummy(Fetcher f, Parser p, Saver sav, Path logfile, Importer i, Sender s) {
        this.f = f;
        this.p = p;
        this.sav = sav;
        this.logfile = logfile;
        this.i = i;
        this.s = s;
    }

    @Override
    public Optional<String> retrieveData() {
        if (f.fetchCurrentData().isEmpty()) {
            return Optional.of("no data fetchable!");
        } else if (p.parse(f.fetchCurrentData().get()).isEmpty()) {
            return Optional.of("error during parsing");
        } else if (sav.log(logfile, p.parse(f.fetchCurrentData().get()).get()).isPresent()) {
            return Optional.of("error during saving");
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> send(Formatter formatter) {
        try {
            OptionalWithMessage<Menu> getLatestMenu = i.getLatest(new BufferedReader(new FileReader(logfile.toFile())));
            if (getLatestMenu == null) {
                return Optional.of("error during import");
            }
            if (getLatestMenu.isEmpty()) {
                return Optional.of("error during import");
            }
            if (formatter == null) {
                return Optional.of("error during formatting");
            }

            OptionalWithMessage<String> result = formatter.format(getLatestMenu.get(), () -> {
                try {
                    i.getAll(new BufferedReader(new FileReader(logfile.toFile())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            });

            if (result.isEmpty()) {
                return Optional.of("error during formatting");
            }
            if (s.send(result.get()).isPresent()) {
                return Optional.of("error occurred during sending");
            }
        } catch (FileNotFoundException | NoSuchElementException e) {
            return Optional.of("error during import");
        } catch (IllegalArgumentException e) {
            return Optional.of("error during formatting");
        }
        return Optional.empty();
    }
}
/* try {
            OptionalWithMessage<List<Menu>> owm1 = i.getAll(new BufferedReader(new FileReader(logfile.toFile())));
            OptionalWithMessage<Menu> owm2 = i.getLatest(new BufferedReader(new FileReader(logfile.toFile())));


            if (owm2.isEmpty()) {
                return Optional.of("error during import");
            }
            OptionalWithMessage<String> result = formatter.format(owm2.get(), () -> owm1);
            if (result.isEmpty())
                return Optional.of("error during formatting");
            Optional<String> sendResult = s.send(result.get());
            if (sendResult.isPresent()) { //present oder empty was denn nun?
                return Optional.of("error occurred during sending");
            }

        } catch (IllegalArgumentException e) {
            return Optional.of("error during formatting");
        } catch (FileNotFoundException e) {
            return Optional.of("error during import");
        }*/

