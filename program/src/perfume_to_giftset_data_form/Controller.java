package perfume_to_giftset_data_form;

import base.DataFormComboBoxControllerAdditional;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.Perfume;
import models.PerfumeToGiftSet;

/**
 * Created by shrralis on 3/14/17.
 */
public class Controller extends DataFormComboBoxControllerAdditional {
    {
        objectToProcess = new PerfumeToGiftSet();
    }

    @FXML public ComboBox<Perfume> perfume;

    @Override
    protected boolean areTheFieldsValidForAdding() {
        return !isAnyTextFieldEmpty() && !isAnyComboBoxEmpty();
    }
    @Override
    protected void objectToProcessBasedOnFields() {
        ((PerfumeToGiftSet) objectToProcess).setPerfume(perfume.getValue());
    }
    @Override
    protected String getDatabaseTableName() {
        return "giftSets_has_perfumes";
    }
}
