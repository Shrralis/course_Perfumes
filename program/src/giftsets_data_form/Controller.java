package giftsets_data_form;

import base.AlertsBuilder;
import base.DataFormController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import models.GiftSet;

/**
 * Created by shrralis on 3/13/17.
 */
@SuppressWarnings("unused")
public class Controller extends DataFormController {
    {
        objectToProcess = new GiftSet();
    }

    @FXML public TextField name;
    @FXML public TextField packaging;
    @FXML public TextField photo;
    @FXML public TextField other;
    @FXML public CheckBox bName;
    @FXML public CheckBox bPackaging;
    @FXML public CheckBox bPhoto;
    @FXML public CheckBox bOther;

    @Override
    protected boolean areTheFieldsValidForAdding() {
        if (isAnyTextFieldEmpty()) {
            return false;
        }

        if (!photo.getText().trim().matches("^https?://.*$")) {
            AlertsBuilder.start()
                    .setAlertType(Alert.AlertType.WARNING)
                    .setTitle("Помилка")
                    .setHeaderText("Помилка вводу")
                    .setContentText("Поле " + photo.getPromptText() + " повинне починатися з http:// або https://!")
                    .build()
                    .showAndWait();
            return false;
        }
        return true;
    }
    @Override
    protected void objectToProcessBasedOnFields() {
        if (bName.isSelected()) {
            ((GiftSet) objectToProcess).setName(name.getText().trim().isEmpty() ? null : name.getText().trim());
        }

        if (bPackaging.isSelected()) {
            ((GiftSet) objectToProcess).setPackaging(packaging.getText().trim().isEmpty() ? null :
                    packaging.getText().trim());
        }

        if (bPhoto.isSelected()) {
            ((GiftSet) objectToProcess).setPhoto(photo.getText().trim().isEmpty() ? null : photo.getText().trim());
        }

        if (bOther.isSelected()) {
            ((GiftSet) objectToProcess).setOther(other.getText().trim().isEmpty() ? null : other.getText().trim());
        }
    }
    @Override
    protected String getDatabaseTableName() {
        return "giftSets";
    }
}
