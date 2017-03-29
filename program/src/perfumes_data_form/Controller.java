package perfumes_data_form;

import base.AlertsBuilder;
import base.DataFormComboBoxController;
import com.sun.tools.javac.parser.ParserFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Brand;
import models.Perfume;

import java.util.Calendar;

/**
 * Created by shrralis on 3/13/17.
 */
@SuppressWarnings("unused")
public class Controller extends DataFormComboBoxController {
    {
        objectToProcess = new Perfume();
    }

    @FXML public TextField name;
    @FXML public ComboBox<Brand> brand;
    @FXML public TextField type_of_product;
    @FXML public TextField container;
    @FXML public TextField description;
    @FXML public ComboBox<Perfume.Note> note;
    @FXML public TextField collection;
    @FXML public TextField other;
    @FXML public CheckBox bName;
    @FXML public CheckBox bBrand;
    @FXML public CheckBox bType_of_product;
    @FXML public CheckBox bContainer;
    @FXML public CheckBox bDescription;
    @FXML public CheckBox bNote;
    @FXML public CheckBox bCollection;
    @FXML public CheckBox bOther;

    @Override
    public void loadData() {
        super.loadData();
        setComboBox();
    }
    @Override
    protected boolean isAnyTextFieldEmpty() {
        boolean empty = false;
        String fieldDaoName = "";

        if (name.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = name.getPromptText();
        } else if (type_of_product.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = type_of_product.getPromptText();
        } else if (container.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = container.getPromptText();
        } else if (collection.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = collection.getPromptText();
        }

        if (empty) {
            AlertsBuilder.start()
                    .setAlertType(Alert.AlertType.WARNING)
                    .setTitle("Помилка")
                    .setHeaderText("Помилка вводу")
                    .setContentText("Ви залишили поле " + fieldDaoName + " порожнім!")
                    .buildOnFront(primaryStage)
                    .showAndWait();
        }
        return empty;
    }

    @Override
    protected boolean areTheFieldsValidForAdding() {
        return !isAnyTextFieldEmpty() && !isAnyComboBoxEmpty();
    }
    @Override
    protected void objectToProcessBasedOnFields() {
        if (bName.isSelected()) {
            ((Perfume) objectToProcess).setName(name.getText().trim().isEmpty() ? null : name.getText().trim());
        }

        if (bBrand.isSelected()) {
            ((Perfume) objectToProcess).setBrand(brand.getValue());
        }

        if (bType_of_product.isSelected()) {
            ((Perfume) objectToProcess).setType_of_product(type_of_product.getText().trim().isEmpty() ? null :
                    type_of_product.getText().trim());
        }

        if (bContainer.isSelected()) {
            ((Perfume) objectToProcess).setContainer(container.getText().trim().isEmpty() ? null :
                    container.getText().trim());
        }

        if (bDescription.isSelected()) {
            ((Perfume) objectToProcess).setDescription(description.getText().trim().isEmpty() ? null :
                    description.getText().trim());
        }

        if (bNote.isSelected()) {
            ((Perfume) objectToProcess).setNote(note.getValue());
        }

        if (bCollection.isSelected()) {
            ((Perfume) objectToProcess).setCollection(collection.getText().trim().isEmpty() ? null :
                    collection.getText().trim());
        }

        if (bOther.isSelected()) {
            ((Perfume) objectToProcess).setOther(other.getText().trim().isEmpty() ? null :
                    other.getText().trim());
        }
    }
    @Override
    protected String getDatabaseTableName() {
        return "perfumes";
    }

    private void setComboBox() {
        note.setItems(FXCollections.observableArrayList(Perfume.Note.values()));
    }
}
