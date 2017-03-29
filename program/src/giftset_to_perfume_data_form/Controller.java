package giftset_to_perfume_data_form;

import base.DataFormComboBoxControllerAdditional;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import models.GiftSet;
import models.PerfumeToGiftSet;

/**
 * Created by shrralis on 3/14/17.
 */
public class Controller extends DataFormComboBoxControllerAdditional {
    {
        objectToProcess = new PerfumeToGiftSet();
    }

    @FXML public ComboBox<GiftSet> giftSet;

    @Override
    protected boolean areTheFieldsValidForAdding() {
        return !isAnyTextFieldEmpty() && !isAnyComboBoxEmpty();
    }
    @Override
    protected void objectToProcessBasedOnFields() {
        ((PerfumeToGiftSet) objectToProcess).setGiftSet(giftSet.getValue());
    }
    @Override
    protected String getDatabaseTableName() {
        return "giftSets_has_perfumes";
    }
}
