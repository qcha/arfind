package qcha.arfind.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_HEIGHT;
import static qcha.arfind.utils.Constants.UserResolutionConstants.DEFAULT_USER_RESOLUTION_WIDTH;

final class SearchView extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SearchView.class);

    private HBox searchPanel;
    private HBox newSearchPanel;
    private TextField textSearchLine;
    private SearchViewModel viewModel;
    private VBox companiesVBox;
    private CheckBox selectAll;
    private SplitPane body;
    private Button initConfBtn;

    // left view for companies
    private ListView<SearchViewModel.SearchSource> lstCompanies;

    // right view for results
    private WebView resultView;

    SearchView(SearchViewModel viewModel) {
        this.viewModel = viewModel;
        resultView = new WebView();
        companiesVBox = new VBox();

        // list view with companies names
        initCompaniesListView();

        // search panel
        initSearchPanel();

        // init new search panel
        initNewSearchPanel();

        // init load config button
        initLoadConfigButton();

        // init body
        body = new SplitPane();

        companiesVBox.getChildren().addAll(selectAll, lstCompanies);
        VBox.setVgrow(lstCompanies, Priority.ALWAYS);

        body.getItems().addAll(
                initConfBtn,
                resultView
        );

        if (!viewModel.getSourcesForSearch().isEmpty()) {
            body.getItems().set(0, companiesVBox);
        }

        // init pane
        setTop(createMenuBar());
        setCenter(body);
        setBottom(searchPanel);
    }

    private void initLoadConfigButton() {
        initConfBtn = new Button() {
            {
                setText("Загрузить конфигурацию");
                setTextAlignment(TextAlignment.CENTER);
                setFont(new Font(24));
                setPrefHeight(DEFAULT_USER_RESOLUTION_HEIGHT);
                setPrefWidth(DEFAULT_USER_RESOLUTION_WIDTH);
            }
        };

        initConfBtn.setOnMouseClicked(e -> {
            new ApplicationConfigurationWindow(viewModel.getStage()) {
                {
                    showAndWait();
                }
            };
            if (!viewModel.getSourcesForSearch().isEmpty()) {
                body.getItems().set(0, companiesVBox);
            }
        });

    }

    private void initCompaniesListView() {
        // list of companies for search
        lstCompanies = new ListView<>(viewModel.getSourcesForSearch());
        lstCompanies.setCellFactory(CheckBoxListCell.forListView(SearchViewModel.SearchSource::selectedProperty, new StringConverter<SearchViewModel.SearchSource>() {
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


        selectAll = new CheckBox() {
            {
                setPadding(new Insets(8));
            }
        };
        selectAll.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        lstCompanies.getItems().forEach(searchSource -> searchSource.setSelected(newValue))
        );
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
                            .filter(SearchViewModel.SearchSource::isSelected)
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
            body.getItems().remove(companiesVBox);
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
            body.getItems().add(0, companiesVBox);
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
                configuration.setOnAction(event -> {
                    new ApplicationConfigurationWindow(viewModel.getStage()) {
                        {
                            showAndWait();
                        }
                    };
                    if (viewModel.getSourcesForSearch().isEmpty()) {
                        body.getItems().set(0, initConfBtn);
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
