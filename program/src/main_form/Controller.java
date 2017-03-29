package main_form;

import base.AlertsBuilder;
import base.DataFormComboBoxControllerAdditional;
import base.DataFormControllerInterface;
import base.OnMouseClickListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;
import server.DatabaseWorker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Controller {
    @FXML private TabPane tabs;
    @FXML private TableView<Brand> tableBrands;
    @FXML private TableView<Perfume> tablePerfumes;
    @FXML private TableView<GiftSet> tableGiftSets;

    @FXML
    public void initialize() {
        tablePerfumes.setRowFactory(param -> {
            final TableRow<Perfume> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem addToGiftSet = new MenuItem("Додати до подарункового набору");
            final MenuItem findGiftSets = new MenuItem("Подарункові набори з цим парфумом");

            addToGiftSet.setOnAction(event -> openAddPerfumeForm(tablePerfumes.getSelectionModel().getSelectedItem()));
            findGiftSets.setOnAction(event -> {
                HashMap<String, Object> params = new HashMap<>();

                params.put("perfume", tablePerfumes.getSelectionModel().getSelectedItem().getId());
                tableGiftSets.setItems(
                        FXCollections.observableArrayList(
                                DatabaseWorker.processQuery(
                                        ServerQuery.create(
                                                "giftSets_has_perfumes",
                                                "get",
                                                null,
                                                params
                                        )
                                ).getObjects()
                        )
                );
                tabs.getSelectionModel().select(2);
            });
            contextMenu.getItems().addAll(addToGiftSet, findGiftSets);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
        tableGiftSets.setRowFactory(param -> {
            final TableRow<GiftSet> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem addPerfume = new MenuItem("Додати парфум");
            final MenuItem findPerfumes = new MenuItem("Парфуми, які є у наборі");

            addPerfume.setOnAction(event -> openAddToGiftSetForm(tableGiftSets.getSelectionModel().getSelectedItem()));
            findPerfumes.setOnAction(event -> {
                HashMap<String, Object> params = new HashMap<>();

                params.put("giftSet", tableGiftSets.getSelectionModel().getSelectedItem().getId());
                tablePerfumes.setItems(
                        FXCollections.observableArrayList(
                                DatabaseWorker.processQuery(
                                        ServerQuery.create(
                                                "giftSets_has_perfumes",
                                                "get",
                                                null,
                                                params
                                        )
                                ).getObjects()
                        )
                );
                tabs.getSelectionModel().select(1);
            });
            contextMenu.getItems().addAll(addPerfume, findPerfumes);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }
    @FXML
    private void onButtonAddClick() throws IOException {
        openDataForm(DataFormControllerInterface.Type.Add);
    }
    @FXML
    private void onButtonEditClick() throws IOException {
        openDataForm(DataFormControllerInterface.Type.Edit);
    }
    @FXML
    private void onButtonDeleteClick() {
        String sSelectedTab = tabs.getSelectionModel().getSelectedItem().getText();

        if (sSelectedTab.equalsIgnoreCase("бренди")) {
            deleteRecord(tableBrands);
        } else if (sSelectedTab.equalsIgnoreCase("парфуми")) {
            deleteRecord(tablePerfumes);
        } else {
            deleteRecord(tableGiftSets);
        }
    }
    @FXML
    private void onButtonSearchClick() throws IOException {
        openDataForm(DataFormControllerInterface.Type.Search);
    }
    @FXML
    private void onButtonRefreshClick() {
        String sSelectedTab = tabs.getSelectionModel().getSelectedItem().getText();

        if (sSelectedTab.equalsIgnoreCase("бренди")) {
            loadDataToTableFromDatabase(tableBrands, null);
        } else if (sSelectedTab.equalsIgnoreCase("парфуми")) {
            loadDataToTableFromDatabase(tablePerfumes, null);
        } else {
            loadDataToTableFromDatabase(tableGiftSets, null);
        }
    }

    private void openDataForm(DataFormControllerInterface.Type type) throws IOException {
        String sSelectedTab = tabs.getSelectionModel().getSelectedItem().getText();
        TableView tableView;
        FXMLLoader loader;

        if (sSelectedTab.equalsIgnoreCase("бренди")) {
            tableView = tableBrands;
            loader = new FXMLLoader(getClass().getResource("/brands_data_form/data.fxml"));
        } else if (sSelectedTab.equalsIgnoreCase("парфуми")) {
            tableView = tablePerfumes;
            loader = new FXMLLoader(getClass().getResource("/perfumes_data_form/data.fxml"));
        } else {
            tableView = tableGiftSets;
            loader = new FXMLLoader(getClass().getResource("/giftsets_data_form/data.fxml"));
        }
        loader.load();

        DataFormControllerInterface controller = loader.getController();
        Stage dataFormStage = new Stage();

        if (type == DataFormControllerInterface.Type.Edit) {
            if (tableView.getSelectionModel().getSelectedItem() == null) {
                AlertsBuilder.start()
                        .setAlertType(Alert.AlertType.WARNING)
                        .setTitle("Помилка")
                        .setHeaderText("Помилка вибору")
                        .setContentText("Для редагування не було вибрано жодного значення у таблиці зверху!")
                        .build()
                        .showAndWait();
                return;
            }
            controller.setObjectToProcess((Owner) tableView.getSelectionModel().getSelectedItem());
        }
        setDataFormClickListeners(controller, type);
        dataFormStage.setScene(new Scene(loader.getRoot()));
        controller.setType(type);
        controller.setPrimaryStage(dataFormStage);
        dataFormStage.show();
    }

    private void setDataFormClickListeners(DataFormControllerInterface controller, DataFormControllerInterface.Type type) {
        OnMouseClickListener okListener = null;
        String sSelectedTab = tabs.getSelectionModel().getSelectedItem().getText();
        TableView tableView;

        if (sSelectedTab.equalsIgnoreCase("бренди")) {
            tableView = tableBrands;
        } else if (sSelectedTab.equalsIgnoreCase("парфуми")) {
            tableView = tablePerfumes;
        } else {
            tableView = tableGiftSets;
        }

        switch (type) {
            case Add:
                okListener = () -> controller.addObjectToTableView(tableView);
                break;
            case Edit:
                okListener = () -> controller.editObjectInTableView(tableView);
                break;
            case Search:
                okListener = () -> controller.search(this, tableView);
                break;
        }
        controller.setOnMouseOkClickListener(okListener);
    }

    void setupAllTables() {
        loadDataToTableFromDatabase(tableBrands, null);
        loadDataToTableFromDatabase(tablePerfumes, null);
        loadDataToTableFromDatabase(tableGiftSets, null);
    }
    @SuppressWarnings("unchecked")
    public void loadDataToTableFromDatabase(TableView tableView, HashMap<String, Object> params) {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.getType() == TableView.class && field.get(this) == tableView) {
                    tableView.setItems(
                            FXCollections.observableArrayList(
                                    DatabaseWorker.processQuery(
                                            ServerQuery.create(
                                                    field.getName().substring("table".length()).toLowerCase(),
                                                    "get", null, params
                                            )
                                    ).getObjects()
                            )
                    );
                    break;
                }
            } catch (IllegalAccessException ignored) {}
        }
    }

    private void deleteRecord(TableView tableView) {
        String tableName = null;

        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.getType() == TableView.class && field.get(this) == tableView) {
                    tableName = field.getName().substring("table".length()).toLowerCase();

                    break;
                }
            } catch (IllegalAccessException ignored) {}
        }

        ServerResult result = DatabaseWorker.processQuery(
                ServerQuery.create(
                        tableName,
                        "delete",
                        (Owner) tableView.getSelectionModel().getSelectedItem(),
                        null
                )
        );

        if (result != null && result.getResult() == 0) {
            tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
        } else {
            AlertsBuilder.start()
                    .setAlertType(Alert.AlertType.WARNING)
                    .setTitle("Видалення")
                    .setHeaderText("Помилка видалення")
                    .build()
                    .showAndWait();
        }
    }

    private <E extends DataFormComboBoxControllerAdditional> void openAddPerfumeForm(Perfume perfume) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/giftset_to_perfume_data_form/data.fxml"));

        try {
            loader.load();
        } catch (IOException ignored) {}

        E controller = loader.getController();
        Stage dataFormStage = new Stage();

        controller.setObjectToSearch(perfume);
        dataFormStage.setScene(new Scene(loader.getRoot()));
        controller.setPrimaryStage(dataFormStage);
        dataFormStage.showAndWait();
    }

    private <E extends DataFormComboBoxControllerAdditional> void openAddToGiftSetForm(GiftSet giftSet) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfume_to_giftset_data_form/data.fxml"));

        try {
            loader.load();
        } catch (IOException ignored) {}

        E controller = loader.getController();
        Stage dataFormStage = new Stage();

        controller.setObjectToSearch(giftSet);
        dataFormStage.setScene(new Scene(loader.getRoot()));
        controller.setPrimaryStage(dataFormStage);
        dataFormStage.showAndWait();
    }
}
