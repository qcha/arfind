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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qcha.arfind.excel.TextCrawler;
import qcha.arfind.model.SearchResult;

import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

final class SearchView extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SearchView.class);

    private HBox searchPanel;
    private HBox newSearchPanel;
    private TextField textSearchLine;
    private SearchViewModel viewModel;
    private SplitPane body;

    // left view for companies
    private ListView<SearchViewModel.SearchSource> lstCompanies;

    // right view for results
    private WebView resultView;


    SearchView(SearchViewModel viewModel) {
        this.viewModel = viewModel;
        resultView = new WebView();

        // list view with companies names
        initCompaniesListView();

        // search panel
        initSearchPanel();

        // init new search panel
        initNewSearchPanel();

        // init body
        body = new SplitPane();
        body.getItems().addAll(
                lstCompanies,
                resultView
        );

        // init pane
        setTop(createMenuBar());
        setCenter(body);
        setBottom(searchPanel);
    }

    private void initCompaniesListView() {
        // list of companies for search
        lstCompanies = new ListView<>(viewModel.getSourcesForSearch());
        lstCompanies.setCellFactory(l -> new ListCell<SearchViewModel.SearchSource>() {
            @Override
            protected void updateItem(SearchViewModel.SearchSource item, boolean empty) {
                if (Objects.nonNull(item)) {
                    setText(item.getName());
                }
            }
        });

        lstCompanies.setCellFactory(CheckBoxListCell.forListView(source -> new SimpleBooleanProperty() {
            {
                addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                logger.debug("Select source: {} for search.", source);
                                source.setForSearch(true);
                            }

                            if (wasSelected) {
                                logger.debug("Remove source: {} from search.", source);
                                source.setForSearch(false);
                            }
                        }
                );
            }
        }, new StringConverter<SearchViewModel.SearchSource>() {
            @Override
            public String toString(SearchViewModel.SearchSource details) {
                return details.getName();
            }

            @Override
            public SearchViewModel.SearchSource fromString(String name) {

                Optional<SearchViewModel.SearchSource> searchSource = viewModel.getSourcesForSearch().stream()
                        .filter(s -> s.getName().equals(name))
                        .findFirst();

                //  it should be always present value
                if (!searchSource.isPresent()) {
                    logger.error("Can't find search source by name: {}.", name);
                    return null;
                }

                return searchSource.get();
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
            final List<SearchResult> anyMatches = TextCrawler.findAnyMatches(
                    textSearchLine.getText(),
                    viewModel.getSourcesForSearch()
                            .stream()
                            .filter(SearchViewModel.SearchSource::isForSearch)
                            .collect(Collectors.toList())
            );

            final WebEngine engine = resultView.getEngine();
            final VelocityEngine ve = new VelocityEngine();
            ve.init();

            final Template t = ve.getTemplate("arfind-ui/src/main/resources/search-results-table.vt");
            final VelocityContext context = new VelocityContext();

            context.put("rows", anyMatches
                    .stream()
                    .filter(result -> result.getResult().size() > 1)
                    .collect(Collectors.toList())
            );

            final StringWriter writer = new StringWriter();
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

        // Main menu
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

        // Configuration menu
        Menu options = new Menu("Настройки") {
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


        // Help menu
        Menu help = new Menu("Помощь") {
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
