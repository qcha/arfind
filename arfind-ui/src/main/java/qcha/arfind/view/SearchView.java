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
import javafx.util.StringConverter;
import qcha.arfind.SearchModelCache;
import qcha.arfind.excel.TextCrawler;
import qcha.arfind.model.SearchDetails;
import qcha.arfind.model.SearchResult;
import qcha.arfind.view.SearchViewModel.SearchResultModelDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchView extends BorderPane {
    private HBox searchPanel;
    private HBox newSearchPanel;
    private TextField textSearchLine;
    private SearchViewModel viewModel;
    //left view for companies
    private ListView<SearchDetails> lstCompanies;
    //table for results
    private TableView<SearchResultModelDto> resultTableView;

    public SearchView() {
        viewModel = new SearchViewModel();

        //list view with companies names
        initCompaniesListView();

        //table view
        initCompanyTableView();
        resultTableView.setItems(viewModel.getResults());

        //search panel
        initSearchPanel();

        //init new search panel
        initNewSearchPanel();

        //init body
        SplitPane body = new SplitPane();
        body.getItems().addAll(
                lstCompanies,
                resultTableView
        );

        //init pane
        setTop(createMenuBar());
        setCenter(body);
        setBottom(searchPanel);
    }

    private void initCompaniesListView() {
        //list of companies for search
        lstCompanies = new ListView<>(viewModel.getCompanies());
        lstCompanies.setCellFactory(l -> new ListCell<SearchDetails>() {

            @Override
            protected void updateItem(SearchDetails item, boolean empty) {
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
        }, new StringConverter<SearchDetails>() {
            @Override
            public String toString(SearchDetails details) {
                return details.getName();
            }

            @Override
            public SearchDetails fromString(String name) {
                return SearchModelCache.getOrCreateCache().get(name);
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
            viewModel.getResults().addAll(
                    anyMatches.stream()
                            .map(result ->
                                    new SearchResultModelDto(
                                            result.getName(),
                                            result.getResult())
                            )
                            .collect(Collectors.toList()));
            this.setBottom(newSearchPanel);
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
        });

        newSearchPanel = new HBox() {
            {
                getChildren().add(prepareToSearchBtn);
            }
        };

        HBox.setHgrow(prepareToSearchBtn, Priority.ALWAYS);
    }

    private void initCompanyTableView() {
        resultTableView = new TableView<SearchResultModelDto>() {
            {
                setStyle("-fx-font-size: 16px;");
                setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                setFocusTraversable(false);

                TableColumn<SearchResultModelDto, String> companyColumn = new TableColumn<>("Название фирмы");
                TableColumn<SearchResultModelDto, ListView<String>> filterResultColumn = new TableColumn<>("Результат поиска");

                companyColumn.prefWidthProperty().bind(widthProperty().multiply(0.2));
                filterResultColumn.prefWidthProperty().bind(widthProperty().multiply(0.8));

//                companyColumn.setResizable(false);
//                filterResultColumn.setResizable(false);

                companyColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

                filterResultColumn.setCellValueFactory(cellData -> cellData.getValue().getResult());

                //noinspection unchecked
                getColumns().addAll(companyColumn, filterResultColumn);
            }
        };
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
                configuration.setOnAction(event -> new ApplicationConfigurationWindow() {
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
