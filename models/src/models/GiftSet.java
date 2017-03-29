package models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shrralis on 3/15/17.
 */
public class GiftSet extends Owner {
    public String packaging = null;
    public String photo = null;
    public String other = null;

    public GiftSet() {}
    @SuppressWarnings("unused")
    public GiftSet(ResultSet from) {
        parse(from);
    }
    @Override
    public GiftSet parse(ResultSet from) {
        super.parse(from);

        try {
            packaging = from.getString("packaging");
            photo = from.getString("photo");
            other = from.getString("other");
        } catch (SQLException ignored) {}
        return this;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
