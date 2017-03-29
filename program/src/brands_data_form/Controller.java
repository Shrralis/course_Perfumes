package brands_data_form;

import base.AlertsBuilder;
import base.DataFormController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import models.Brand;

import java.util.Calendar;

/**
 * Created by shrralis on 3/13/17.
 */
@SuppressWarnings("unused")
public class Controller extends DataFormController {
    {
        objectToProcess = new Brand();
    }

    @FXML public TextField name;
    @FXML public TextField type_of_company;
    @FXML public TextField creation_year;
    @FXML public TextField creator;
    @FXML public TextField headquarters;
    @FXML public TextField website;
    @FXML public TextField other;
    @FXML public CheckBox bName;
    @FXML public CheckBox bType_of_company;
    @FXML public CheckBox bCreation_year;
    @FXML public CheckBox bCreator;
    @FXML public CheckBox bHeadquarters;
    @FXML public CheckBox bWebsite;
    @FXML public CheckBox bOther;

    @Override
    protected boolean isAnyTextFieldEmpty() {
        boolean empty = false;
        String fieldDaoName = "";

        if (name.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = name.getPromptText();
        } else if (type_of_company.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = type_of_company.getPromptText();
        } else if (creation_year.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = creation_year.getPromptText();
        } else if (creator.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = creator.getPromptText();
        } else if (website.getText().equalsIgnoreCase("")) {
            empty = true;
            fieldDaoName = website.getPromptText();
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
        if (isAnyTextFieldEmpty()) {
            return false;
        }

        if (!creation_year.getText().trim().matches("^\\d+$")) {
            AlertsBuilder.start()
                    .setAlertType(Alert.AlertType.WARNING)
                    .setTitle("Помилка")
                    .setHeaderText("Помилка вводу")
                    .setContentText("Поле " + creation_year.getPromptText() + " повинно містити ціле число!")
                    .build()
                    .showAndWait();
            return false;
        } else {
            int iYear = Integer.parseInt(creation_year.getText().trim());

            if (iYear < 1970 || iYear > Calendar.getInstance().get(Calendar.YEAR)) {
                AlertsBuilder.start()
                        .setAlertType(Alert.AlertType.WARNING)
                        .setTitle("Помилка")
                        .setHeaderText("Помилка вводу")
                        .setContentText("Поле " + creation_year.getPromptText() + " повинно містити рік в межах від 1970–" +
                                Calendar.getInstance().get(Calendar.YEAR) + "!")
                        .build()
                        .showAndWait();
                return false;
            }
        }

        if (!website.getText().trim().matches("^https?://.*$")) {
            AlertsBuilder.start()
                    .setAlertType(Alert.AlertType.WARNING)
                    .setTitle("Помилка")
                    .setHeaderText("Помилка вводу")
                    .setContentText("Поле " + website.getPromptText() + " повинно починатися з http:// або https://!")
                    .build()
                    .showAndWait();
            return false;
        }
        return true;
    }
    @Override
    protected void objectToProcessBasedOnFields() {
        if (bName.isSelected()) {
            ((Brand) objectToProcess).setName(name.getText().trim().isEmpty() ? null : name.getText().trim());
        }

        if (bType_of_company.isSelected()) {
            ((Brand) objectToProcess).setType_of_company(type_of_company.getText().trim().isEmpty() ? null :
                    type_of_company.getText().trim());
        }

        if (bCreation_year.isSelected()) {
            ((Brand) objectToProcess).setCreation_year(creation_year.getText().trim().isEmpty() ? null :
                    Integer.parseInt(creation_year.getText().trim()));
        }

        if (bCreator.isSelected()) {
            ((Brand) objectToProcess).setCreator(creator.getText().trim().isEmpty() ? null :
                    creator.getText().trim());
        }

        if (bHeadquarters.isSelected()) {
            ((Brand) objectToProcess).setHeadquarters(headquarters.getText().trim().isEmpty() ? null :
                    headquarters.getText().trim());
        }

        if (bWebsite.isSelected()) {
            ((Brand) objectToProcess).setWebsite(website.getText().trim().isEmpty() ? null : website.getText().trim());
        }

        if (bOther.isSelected()) {
            ((Brand) objectToProcess).setOther(other.getText().trim().isEmpty() ? null : other.getText().trim());
        }
    }
    @Override
    protected String getDatabaseTableName() {
        return "brands";
    }
}
