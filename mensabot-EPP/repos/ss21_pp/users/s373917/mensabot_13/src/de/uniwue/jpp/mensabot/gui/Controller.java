package de.uniwue.jpp.mensabot.gui;

import de.uniwue.jpp.errorhandling.OptionalWithMessage;
import de.uniwue.jpp.mensabot.dataclasses.Meal;
import de.uniwue.jpp.mensabot.dataclasses.Menu;
import de.uniwue.jpp.mensabot.retrieval.Fetcher;
import de.uniwue.jpp.mensabot.retrieval.Parser;
import de.uniwue.jpp.mensabot.sending.Importer;
import de.uniwue.jpp.mensabot.sending.Sender;
import de.uniwue.jpp.mensabot.sending.formatting.Formatter;
import de.uniwue.jpp.mensabot.sending.formatting.analyse.Analyzer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller implements Initializable {
    //Controls Tab 1: Pipelining
    @FXML
    public Tab tabPipelining;
    @FXML
    public ListView<Formatter> listOfFormattersInPipeliningTab;
    @FXML
    public Button buttonFetchNSafeInPipelining;
    @FXML
    public Button buttonFormatInPipelining;
    @FXML
    public Button buttonSendInPipelining;
    @FXML
    public Label display;
    @FXML
    public Label labelFetchSaveFormatSendInPipelining;
    @FXML
    public Label labelFormattersinPipelining;
    @FXML
    public Label statisticsLabel;
    @FXML
    public Label analyzerResults;

    //Controls Tab 2: Formatter Creation
    @FXML
    public Tab tabFormatterCreation;
    @FXML
    public ListView<Formatter> listViewInFormatterTab;
    @FXML
    public ListView<Analyzer<?>> listViewOfAnalyzers;
    @FXML
    public ListView<Analyzer<?>> listOfSelectedAnalyzers;
    @FXML
    public ListView<Analyzer<?>> listViewOfAnalyzersOnTheRight;
    @FXML
    public Button selectAnalyzerButton;
    @FXML
    public Button deselectAnalyzer;
    @FXML
    public Button clearButton;
    @FXML
    public Button createNewFormatter;
    @FXML
    public Button createNewFormatterWithFormat;
    @FXML
    public TextField enterName;
    @FXML
    public TextArea formatWithDollarSigns;
    @FXML
    public TextField headline;
    @FXML
    public TextField enterNameRight;

    //Controls Tab 3: Diagrams
    @FXML
    public LineChart<String, Integer> baseStats;
    @FXML
    public PieChart prizeRangePieChart;
    @FXML
    public BarChart<String, Number> amountsOfDishesPerWeekday;
    @FXML
    public CategoryAxis xAxisBarChart;
    @FXML
    public NumberAxis yAxisBarChart;
    @FXML
    public TableView<Stats> tableViewInStats;
    @FXML
    public TableColumn<Stats, String> nameOfAnalyzer;
    @FXML
    public TableColumn<Stats, String> resultSingleMenu;
    @FXML
    public TableColumn<Stats, String> resultAllMenus;
    @FXML
    public TableView<Menu> logView;
    @FXML
    public TableColumn<Date, String> dateColumn;
    @FXML
    public TextField searchField;
    @FXML
    public Button searchButton;

    private final ObservableList<Menu> dataList = FXCollections.observableArrayList();


    @FXML
    XYChart.Series<String, Integer> seriesAvgPrize;
    XYChart.Series<String, Integer> seriesTotalPrize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateCharts();
        populateStats();
        populateLogView();

        listOfFormattersInPipeliningTab.getItems().addAll(Formatter.createSimpleFormatter(), Formatter.createSimpleMealFormatter(), Formatter.createRandomMealFormatter(), Formatter.createPricelessFormatter(), Formatter.createPriceOnlyFormatter(), Formatter.createLongestNameFormatter());
        listViewInFormatterTab.getItems().addAll(Formatter.createSimpleFormatter(), Formatter.createSimpleMealFormatter(), Formatter.createRandomMealFormatter(), Formatter.createPricelessFormatter(), Formatter.createPriceOnlyFormatter(), Formatter.createLongestNameFormatter());
        listViewInFormatterTab.setMouseTransparent(true);
        listViewInFormatterTab.setFocusTraversable(false);
        listViewOfAnalyzers.getItems().addAll(Analyzer.createTotalPrizeAnalyzer(), Analyzer.createMaxPrizeMealAnalyzer(), Analyzer.createMinPrizeMealAnalyzer(), Analyzer.createAveragePrizeAnalyzer(), Analyzer.createMedianPrizeAnalyzer());
        listViewOfAnalyzersOnTheRight.getItems().addAll(Analyzer.createTotalPrizeAnalyzer(), Analyzer.createMaxPrizeMealAnalyzer(), Analyzer.createMinPrizeMealAnalyzer(), Analyzer.createAveragePrizeAnalyzer(), Analyzer.createMedianPrizeAnalyzer());

        dataList.addAll(logView.getItems());
        FilteredList<Menu> filteredList = new FilteredList<>(dataList, b -> true);
        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(menu -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (menu.getMeals().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (logView.getColumns().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return false;
            });
        }));
        SortedList<Menu> menuSortedList = new SortedList<>(filteredList);
        menuSortedList.comparatorProperty().bind(logView.comparatorProperty());
        logView.setItems(menuSortedList);
    }

    //Tab 1: Pipelining
    @FXML
    public void sendingData(ActionEvent actionEvent) {
        Parser parser = Parser.createCsvParser();
        Fetcher fetcher = Fetcher.createDummyCsvFetcher();
        Sender sender = Sender.createDummySender();

        Formatter formatter = listOfFormattersInPipeliningTab.getSelectionModel().getSelectedItem();
        if (formatter == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Formatter has been chosen!");
            alert.setContentText("Please choose a Formatter. Nothing to send!");
            alert.showAndWait();
        }
        sender.send(formatter.format(parser.parse(fetcher.fetchCurrentData().get()).get(), null).get());
    }

    @FXML
    public void formatting(ActionEvent actionEvent) {
        this.display.setPadding(new Insets(15));
        statisticsLabel.setVisible(false);
        Parser parser = Parser.createCsvParser();
        Fetcher fetcher = Fetcher.createDummyCsvFetcher();
        Importer importer = Importer.createCsvImporter();


        if (fetcher.fetchCurrentData() == null || fetcher.fetchCurrentData().isEmpty()) {
            this.display.setVisible(true);
            this.display.setText("No meals for today!");
            return;
        }
        List<Menu> menu = List.of(parser.parse(fetcher.fetchCurrentData().get()).get());

        Formatter formatter = listOfFormattersInPipeliningTab.getSelectionModel().getSelectedItem();
        if (formatter == null) {
            this.display.setText("You need to choose a formatter!");
            this.display.setVisible(true);
            return;
        }
        Analyzer<Integer> analyzerTotalPrize = Analyzer.createTotalPrizeAnalyzer();
        Analyzer<Integer> analyzerAvgPrize = Analyzer.createAveragePrizeAnalyzer();
        Analyzer<Meal> analyzerMaxPrize = Analyzer.createMaxPrizeMealAnalyzer();
        Analyzer<Meal> analyzerMinPrize = Analyzer.createMinPrizeMealAnalyzer();
        Analyzer<Integer> analyzerMedianPrize = Analyzer.createMedianPrizeAnalyzer();

        statisticsLabel.setText("Statistiken am " + menu.get(0).getDate().toString());
        statisticsLabel.setVisible(true);
        String sb = "Total Prize" + ":" + "\t\t\t" + analyzerTotalPrize.analyze(menu).get() + System.getProperty("line.separator") +
                "Most Expensive Meal" + ":" + "\t" + analyzerMaxPrize.analyze(menu).get() + System.getProperty("line.separator") +
                "Cheapest Meal" + ":" + "\t\t" + analyzerMinPrize.analyze(menu).get() + System.getProperty("line.separator") +
                "Average Prize" + ":" + "\t\t\t" + analyzerAvgPrize.analyze(menu).get() + System.getProperty("line.separator") +
                "Median" + ":" + "\t\t\t\t" + analyzerMedianPrize.analyze(menu).get() + System.getProperty("line.separator");
        analyzerResults.setText(sb);

        OptionalWithMessage<String> result = formatter.format(parser.parse(fetcher.fetchCurrentData().get()).get(), () -> {
            try {
                return importer.getAll(new BufferedReader(new FileReader("C:\\Users\\Ana\\IdeaProjects\\EPP\\mensabot13neu\\mensabot_13\\src\\de\\uniwue\\jpp\\mensabot\\gui\\mensalogMenus.csv", StandardCharsets.UTF_8)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        String display = result.get();
        this.display.setText(display);
        this.display.setVisible(true);
    }

    @FXML
    public void fetchingNsaving(ActionEvent actionEvent) {

        this.display.setPadding(new Insets(15));
        Formatter formatter = Formatter.createSimpleFormatter();
        Parser parser = Parser.createCsvParser();
        Fetcher fetcher = Fetcher.createDummyCsvFetcher();

        if (fetcher.fetchCurrentData() == null || fetcher.fetchCurrentData().isEmpty()) {
            this.display.setVisible(true);
            this.display.setText("No meals for today!");
            return;
        }
        String display = formatter.format(parser.parse(fetcher.fetchCurrentData().get()).get(), null).get();
        this.display.setText(display);
        this.display.setVisible(true);
    }

    //Tab 2: Formatter Creation
    @FXML
    public void selectAnalyzer(ActionEvent actionEvent) {
        listOfSelectedAnalyzers.getItems().add(listViewOfAnalyzers.getSelectionModel().getSelectedItem());

    }

    public void deselectAnalyzer(ActionEvent actionEvent) {
        listOfSelectedAnalyzers.getItems().remove(listOfSelectedAnalyzers.getSelectionModel().getSelectedIndex());
    }

    public void clearList(ActionEvent actionEvent) {
        listOfSelectedAnalyzers.getItems().clear();
    }

    public void createNewFormatterFromFormatWithName(ActionEvent actionEvent) {
        String textFromFormat = formatWithDollarSigns.getText();
        String name = enterName.getText();
        List<Analyzer<?>> analyzerList = new ArrayList<>(List.of());
        analyzerList = new ArrayList<>(listOfSelectedAnalyzers.getItems());

        if (!(textFromFormat.isEmpty()) && !(name.isEmpty()) && !(analyzerList.isEmpty()) && textFromFormat.contains("$")) {
            Formatter formatter = Formatter.createFormatterFromFormat(textFromFormat, analyzerList, name);
            listViewInFormatterTab.getItems().addAll(formatter);
            listOfFormattersInPipeliningTab.getItems().addAll(formatter);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Field is empty");
            alert.setContentText("Please make sure, that the InputFields aren't empty and at least one Analyzer has been chosen!");
            alert.showAndWait();
        }
    }

    public void createNewFormatterFromAnalyzer(ActionEvent actionEvent) {
        String headlineFormatter = headline.getText();
        String name = enterNameRight.getText();
        Formatter formatter = null;

        if (!(headlineFormatter.isEmpty()) && !(name.isEmpty())) {
            formatter = Formatter.createFormatterFromAnalyzer(headlineFormatter, listViewOfAnalyzersOnTheRight.getSelectionModel().getSelectedItem(), name);
            listViewInFormatterTab.getItems().addAll(formatter);
            listOfFormattersInPipeliningTab.getItems().addAll(formatter);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Field is empty");
            alert.setContentText("Please check, that the InputFields have valid values and an Analyzer has been chosen!");
            alert.showAndWait();
        }


    }

    //Tab 3: Diagrams
    public void populateCharts() {
        baseStats.getData().clear();
        prizeRangePieChart.getData().clear();
        amountsOfDishesPerWeekday.getData().clear();
        List<Menu> menus = new ArrayList<>(List.of());
        List<Menu> menus1 = new ArrayList<>(List.of());


        Analyzer<Integer> avgAnalyzer = Analyzer.createAveragePrizeAnalyzer();
        Analyzer<Integer> totalPrizeAnalyzer = Analyzer.createTotalPrizeAnalyzer();
        Analyzer<Meal> maxPrizeAnalyzer = Analyzer.createMaxPrizeMealAnalyzer();
        String line = null;
        String datum;
        Parser parser = Parser.createCsvParser();
        XYChart.Series<String, Integer> seriesAvgPrize = new XYChart.Series<>();
        XYChart.Series<String, Integer> seriesTotalPrize = new XYChart.Series<>();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Prize in €");
        XYChart.Series<String, Number> seriesAmountOfDishes = new XYChart.Series<>();
        seriesAmountOfDishes.setName("Days");
        seriesAvgPrize.setName("Average Prize");
        seriesTotalPrize.setName("Total Prize");



        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ana\\IdeaProjects\\EPP\\mensabot13neu\\mensabot_13\\src\\de\\uniwue\\jpp\\mensabot\\gui\\mensalogMenus.csv", StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                menus1.add(parser.parse(line).get());
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der csv");
        }
        for (int z = 0; z < menus1.size(); z++) {
            int x = menus1.size() - z - 1;
            menus.add(menus1.get(x));
        }
        for (Menu menu : menus) {
            Set<Meal> mealHashSet = new HashSet<>();
            mealHashSet = menu.getMeals();
            List<Menu> menuList = new ArrayList<>();
            menuList.add(menu);
            datum = convertDate(menu.getDate().toString());
            int averagePrice = (eurosFromCents(avgAnalyzer.analyze(menuList).get()));
            int totalPrice = (eurosFromCents(totalPrizeAnalyzer.analyze(menuList).get()));
            seriesAvgPrize.getData().add(new XYChart.Data<String, Integer>(datum, averagePrice));
            seriesTotalPrize.getData().add(new XYChart.Data<String, Integer>(datum, totalPrice));
            seriesAmountOfDishes.getData().add(new XYChart.Data<String, Number>(datum, mealHashSet.size()));
            menuList.clear();
        }
        baseStats.getData().addAll(seriesAvgPrize, seriesTotalPrize);

        Map<String, Integer> results = Analyzer.createPrizeRangeAnalyzer().analyze(menus1).get();
        int numberOfSlices = results.size();

        for (String s : results.keySet()) {
            PieChart.Data slice = new PieChart.Data(s, results.get(s));
            prizeRangePieChart.getData().add(slice);
        }


        /*PieChart.Data slice1 = new PieChart.Data("0-1", 5);
        PieChart.Data slice2 = new PieChart.Data("1-2", 15);
        PieChart.Data slice3 = new PieChart.Data("2-3", 22);
        prizeRangePieChart.getData().addAll(slice1, slice2, slice3);*/
        amountsOfDishesPerWeekday.getData().add(seriesAmountOfDishes);

    }

    public void updateDiagrams(ActionEvent actionEvent) {
        populateCharts();
    }

    //Tab 4: Stats
    public void populateStats() {
        tableViewInStats.getItems().clear();
        String line = null;
        Parser parser = Parser.createCsvParser();
        List<Menu> menus = new ArrayList<>(List.of());
        HashSet<Meal> meals = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ana\\IdeaProjects\\EPP\\mensabot13neu\\mensabot_13\\src\\de\\uniwue\\jpp\\mensabot\\gui\\mensalogMenus.csv", StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                menus.add(parser.parse(line).get());
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der csv");
        }

        Analyzer<Integer> avgAnalyzer = Analyzer.createAveragePrizeAnalyzer();
        Analyzer<Integer> totalPrizeAnalyzer = Analyzer.createTotalPrizeAnalyzer();
        Analyzer<Meal> maxPrizeAnalyzer = Analyzer.createMaxPrizeMealAnalyzer();
        Analyzer<Meal> minPrizeAnalyzer = Analyzer.createMinPrizeMealAnalyzer();
        Analyzer<Integer> medianPrizeAnalyzer = Analyzer.createMedianPrizeAnalyzer();
        Analyzer<String> averagePrizePerDayAnalyzer = Analyzer.createAveragePrizePerDayAnalyzer();
        Analyzer<String> totalPrizePerDayAnalyzer = Analyzer.createTotalPrizePerDayAnalyzer();
        Analyzer<Map<String, Integer>> prizeRangeAnalyzer = Analyzer.createPrizeRangeAnalyzer();

        String singleMenuAvg = avgAnalyzer.analyse(List.of(menus.stream().findFirst().get()), this::euroFormat).get();
        String allMenusAvg = avgAnalyzer.analyse(menus, (t) -> euroFormat(t)).get();

        String singleMenuTotalPrice = totalPrizeAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> euroFormat(t)).get();
        String allMenusTotalPrice = totalPrizeAnalyzer.analyse(menus, (t) -> euroFormat(t)).get();

        String singleMenuMinPrice = minPrizeAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> String.valueOf(t)).get();
        String allMenusMinPrice = minPrizeAnalyzer.analyse(menus, (t) -> String.valueOf(t)).get();

        String singleMenuMaxPrice = maxPrizeAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> String.valueOf(t)).get();
        String allMenusMaxPrice = maxPrizeAnalyzer.analyse(menus, (t) -> String.valueOf(t)).get();

        String singleMenuMedian = medianPrizeAnalyzer.analyse(List.of(menus.stream().findFirst().get()), this::euroFormat).get();
        String allMenusMedian = medianPrizeAnalyzer.analyse(menus, (t) -> euroFormat(t)).get();

        String singleMenuAveragePricePerDay = averagePrizePerDayAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> String.valueOf(t)).get();
        String allMenusAvergagePricePerDay = averagePrizePerDayAnalyzer.analyse(menus, (t) -> String.valueOf(t)).get();

        String singleMenuTotalPricePerDay = totalPrizePerDayAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> String.valueOf(t)).get();
        String allMenusTotalPricePerDay = totalPrizePerDayAnalyzer.analyse(menus, (t) -> String.valueOf(t)).get();

        String singleMenuPrizeRange = prizeRangeAnalyzer.analyse(List.of(menus.stream().findFirst().get()), (t) -> prettyMap(t)).get();
        String allMenusPrizeRangeAnalyzer = prizeRangeAnalyzer.analyse(menus, (t) -> prettyMap(t)).get();



        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ana\\IdeaProjects\\EPP\\mensabot13neu\\mensabot_13\\src\\de\\uniwue\\jpp\\mensabot\\gui\\mensalogMenus.csv", StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                menus.add(parser.parse(line).get());
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der csv");
        }
        tableViewInStats.setEditable(false);
        tableViewInStats.setItems(FXCollections.observableArrayList(new Stats("Average Price", singleMenuAvg, allMenusAvg), new Stats("Total Price", singleMenuTotalPrice, allMenusTotalPrice), (new Stats("Most expensive Meal", singleMenuMaxPrice, allMenusMaxPrice)), new Stats("Cheapest Meal", singleMenuMinPrice, allMenusMinPrice), (new Stats("Median Price", singleMenuMedian, allMenusMedian)), (new Stats("Average Prize Per Day", singleMenuAveragePricePerDay, allMenusAvergagePricePerDay)), (new Stats("Total Price Per Day", singleMenuTotalPricePerDay, allMenusTotalPricePerDay)), (new Stats("Prize Range", singleMenuPrizeRange, allMenusPrizeRangeAnalyzer))));
        nameOfAnalyzer.setCellValueFactory(new PropertyValueFactory<>("name"));
        resultSingleMenu.setCellValueFactory(new PropertyValueFactory<>("singleMenu"));
        resultAllMenus.setCellValueFactory(new PropertyValueFactory<>("allMenus"));
    }

    public String prettyMap(Map<String,Integer> map) {
        StringBuilder sb = new StringBuilder();
        for(String s: map.keySet()){
            sb.append(s+ "\u20ac" + ": ");
            sb.append(map.get(s) + "\n");
        }

        return sb.toString();
    }

    private String euroFormat(int price) {
        int euro = 0;
        char cents1 = 0;
        char cents2 = 0;

        String lastDigits = "";

        euro = price / 100;
        String str = price + "";
        if (str.length() >= 2) {
            cents2 = str.charAt(str.length() - 1);
            cents1 = str.charAt(str.length() - 2);
            lastDigits = "" + cents1 + "" + cents2;
        }
        return euro + "," + lastDigits + "\u20ac";
    }

    private int eurosFromCents(int price) {
        return price / 100;
    }

    public void updateStats(ActionEvent actionEvent) {
        populateStats();
    }


    public static class Stats {
        SimpleStringProperty name;
        SimpleStringProperty singleMenu;
        SimpleStringProperty allMenus;

        public Stats(String name, String singleMenu, String allMenus) {
            this.name = new SimpleStringProperty(name);
            this.singleMenu = new SimpleStringProperty(singleMenu);
            this.allMenus = new SimpleStringProperty(allMenus);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getSingleMenu() {
            return singleMenu.get();
        }

        public void setSingleMenu(String singleMenu) {
            this.singleMenu.set(singleMenu);
        }

        public String getAllMenus() {
            return allMenus.get();
        }

        public void setAllMenus(String allMenus) {
            this.allMenus.get();
        }
    }

    //Tab 5: LogView
    public void populateLogView() {
        logView.getItems().clear();


        List<Menu> menus = new ArrayList<>(List.of());
        Set<Meal> meals = new HashSet<>();
        String line;
        int max = 0;
        int tableColumnsCount = 0;
        Parser parser = Parser.createCsvParser();
        List<String> columns = new ArrayList<>();
        ObservableList<ObservableList> csvData = FXCollections.observableArrayList();
        TableColumn<Menu, String>[] tableColumns = null;
        TableRow<String> tableRows[] = null;
        ArrayList<Meal> mealArrayList = new ArrayList<>();

        try {
            int columnIndex = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ana\\IdeaProjects\\EPP\\mensabot13neu\\mensabot_13\\src\\de\\uniwue\\jpp\\mensabot\\gui\\mensalogMenus.csv", StandardCharsets.UTF_8))) {
                while ((line = br.readLine()) != null) {
                    menus.add(parser.parse(line).get());
                    //logView.getItems().add(menus.get(menus.size()-1).toString());
                    logView.getItems().add(menus.get(menus.size() - 1));
                }
                for (Menu menu : menus) {
                    meals = menu.getMeals();
                    if (max < meals.size()) {
                        max = meals.size();
                    }
                }
                tableColumns = new TableColumn[(max * 2) + 1];
                tableColumns[0] = new TableColumn<>("Datum");
                tableColumns[0].setCellValueFactory(param -> new ReadOnlyObjectWrapper<String>(convertDate(param.getValue().getDate().toString())));
                tableColumnsCount = tableColumns.length;
                for (int j = 1, i = 1; i < tableColumnsCount; i += 2, j++) {
                    tableColumns[i] = new TableColumn<>("Gericht " + j);
                    int finalJ = j - 1;
                    tableColumns[i].setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(getName(param, finalJ)));
                    tableColumns[i + 1] = new TableColumn<>("Preis " + j);
                    tableColumns[i + 1].setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(getPrice(param, finalJ)));
                }
                logView.getColumns().addAll(tableColumns);
                tableRows = new TableRow[menus.size()];
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der csv");
        }

        for (Menu menu : menus) {
            menu.getDate();
        }
        //logView.getColumns().add(tableColumns);
    }

    public String getName(TableColumn.CellDataFeatures<Menu, String> param, int index) {
        if (param.getValue().getMeals().size() <= index)
            return "";
        else return new ArrayList<>(param.getValue().getMeals()).get(index).getName();
    }

    public String getPrice(TableColumn.CellDataFeatures<Menu, String> param, int index) {
        if (param.getValue().getMeals().size() <= index)
            return "";
        else return euroFormat(new ArrayList<>(param.getValue().getMeals()).get(index).getPrice());
    }


    public void updateLogView(ActionEvent actionEvent) {
        populateLogView();
    }

    public String convertDate(String date) {
        try {
            DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            return dateFormatter.format(dateFormatter1.parse(date));
        } catch (Exception e) {
            return null;
        }

    }
}
