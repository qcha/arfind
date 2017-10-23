package qcha.arfind.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import qcha.arfind.Sources;
import qcha.arfind.excel.TextCrawler;
import qcha.arfind.model.SearchResult;
import qcha.arfind.model.Source;

import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class SearchView extends BorderPane {
    private HBox searchPanel;
    private HBox newSearchPanel;
    private TextField textSearchLine;
    private SearchViewModel viewModel;
    //left view for companies
    private ListView<Source> lstCompanies;
    private WebView resultView;
    private SplitPane body;

    SearchView(SearchViewModel viewModel) {
        this.viewModel = viewModel;
        resultView = new WebView();

        //list view with companies names
        initCompaniesListView();

        //search panel
        initSearchPanel();

        //init new search panel
        initNewSearchPanel();

        //init body
        body = new SplitPane();
        body.getItems().addAll(
                lstCompanies,
                resultView
        );

        //init pane
        setTop(createMenuBar());
        setCenter(body);
        setBottom(searchPanel);
    }

    private void initCompaniesListView() {
        //list of companies for search
        lstCompanies = new ListView<>(viewModel.getCompanies());
        lstCompanies.setCellFactory(l -> new ListCell<Source>() {
            @Override
            protected void updateItem(Source item, boolean empty) {
                if (Objects.nonNull(item)) {
                    setText(item.getName());
                }
            }
        });

        lstCompanies.setCellFactory(CheckBoxListCell.forListView(source -> new SimpleBooleanProperty() {
            {
                addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                viewModel.getSourcesForSearch().add(source);
                            }

                            if (wasSelected) {
                                viewModel.getSourcesForSearch().remove(source);
                            }
                        }
                );
            }
        }, new StringConverter<Source>() {
            @Override
            public String toString(Source details) {
                return details.getName();
            }

            @Override
            public Source fromString(String name) {
                return Sources.getOrCreate().get(name);
            }
        }));
    }

    private void initSearchPanel() {
        searchPanel = new HBox();

        textSearchLine = new TextField() {
            {
                setPromptText("Введите текст для поиска");
                setFont(Font.font(18));
                setAlignment(Pos.CENTER);
                setFocusTraversable(false);
                setPrefHeight(75);
            }
        };

        HBox.setHgrow(textSearchLine, Priority.ALWAYS);

        Button btnSearch = new Button("Поиск") {
            {
                disableProperty().bind(textSearchLine.textProperty().isEqualTo("").or(
                        Bindings.size(viewModel.getSourcesForSearch()).isEqualTo(0)));
                setFocusTraversable(false);
                setDefaultButton(true);
                setMinHeight(75);
                setMinWidth(200);
                setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
            }
        };

        btnSearch.setOnAction(e -> {
            List<SearchResult> anyMatches = TextCrawler.findAnyMatches(textSearchLine.getText(), viewModel.getSourcesForSearch());
            WebEngine engine = resultView.getEngine();

            VelocityEngine ve = new VelocityEngine();
            ve.init();

            Template t = ve.getTemplate("arfind-ui/src/main/resources/search-results-table.vt");
            VelocityContext context = new VelocityContext();

            context.put("rows",
                    anyMatches
                            .stream()
                            .filter(result -> result.getResult().size() > 1)
                            .collect(Collectors.toList())
            );

            StringWriter writer = new StringWriter();
            t.merge(context, writer);

            engine.loadContent(writer.toString());

            this.setBottom(newSearchPanel);
            body.getItems().remove(lstCompanies);
        });

        searchPanel.getChildren().addAll(
                textSearchLine,
                btnSearch
        );
    }

    private void initNewSearchPanel() {
        Button prepareToSearchBtn = new Button("Новый поиск") {
            {
                setMaxWidth(Double.MAX_VALUE);
                setMinHeight(75);
                setFocusTraversable(false);
                setDefaultButton(true);
                setStyle("-fx-font: 18 arial; -fx-base: #b6e7c9;");
            }
        };

        prepareToSearchBtn.setOnAction(e -> {
            viewModel.getResults().clear();
            textSearchLine.clear();
            this.setBottom(searchPanel);
            body.getItems().add(0, lstCompanies);
        });

        newSearchPanel = new HBox() {
            {
                getChildren().add(prepareToSearchBtn);
            }
        };

        HBox.setHgrow(prepareToSearchBtn, Priority.ALWAYS);
    }

    /**
     * Create menu bar.
     *
     * @return MenuBar with menus: File, Options, About.
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        //init option menu
        Menu file = new Menu("Файл") {
            {
                MenuItem exit = new MenuItem("Выход") {
                    {
                        setOnAction(event -> {
                            Platform.exit();
                            System.exit(0);
                        });
                    }
                };

                getItems().add(exit);
            }
        };

        Menu options = new Menu("Настройки") {
            //init configuration menu
            {
                MenuItem configuration = new MenuItem("Конфигурация");
                configuration.setOnAction(event -> new ApplicationConfigurationWindow(viewModel.getStage()) {
                    {
                        showAndWait();
                    }
                });

                getItems().add(configuration);
            }
        };


        Menu help = new Menu("Помощь") {
            //init help menu
            {
                getItems().add(
                        new MenuItem("О программе") {
                            {
                                setOnAction(event -> new Alert(Alert.AlertType.INFORMATION) {
                                            {
                                                setTitle("Информация о программе");
                                                setContentText("Данная программа производит поиск строки в заданных excel-файлах.");
                                                showAndWait();
                                            }
                                        }
                                );
                            }
                        }
                );
            }
        };

        menuBar.getMenus().addAll(file, options, help);

        return menuBar;
    }
}
