package de.uniwue.jpp.mensabot.gui;


import com.sun.javafx.scene.SceneHelper;
import de.uniwue.jpp.mensabot.retrieval.FetchData;
import de.uniwue.jpp.mensabot.retrieval.Fetcher;
import de.uniwue.jpp.mensabot.sending.formatting.Formatter;
import de.uniwue.jpp.mensabot.sending.formatting.FormatterFromAnalyzer1;
import de.uniwue.jpp.mensabot.sending.formatting.SimpleFormatter;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;


public class MensabotGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mensabot.fxml"));
        primaryStage.setTitle("Mensabot");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }
}
