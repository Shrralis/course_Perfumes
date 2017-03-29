package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shrralis on 3/15/17.
 */
public class Perfume extends Owner {
    public enum Note {
        початкова,
        серця,
        базова
    }

    public Brand brand = null;
    public String type_of_product = null;
    public String container = null;
    public String description = null;
    public Note note = null;
    public String collection = null;
    public String other = null;

    public Perfume() {}
    @SuppressWarnings("unused")
    public Perfume(ResultSet from) {
        parse(from);
    }
    @Override
    public Perfume parse(ResultSet from, Connection connection) {
        super.parse(from);

        try {
            brand = new List<>(get("SELECT * FROM `brands` WHERE `id` = " +
                    from.getInt("brand") + ";", connection), Brand.class, connection).get(0);
            type_of_product = from.getString("type_of_product");
            container = from.getString("container");
            description = from.getString("description");
            note = parseNote(from.getString("note"));
            collection = from.getString("collection");
            other = from.getString("other");
        } catch (SQLException ignored) {}
        return this;
    }

    public ResultSet get(String sql, Connection connection) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    private Note parseNote(String src) {
        for (Note note : Note.values()) {
            if (src.equalsIgnoreCase(note.toString())) {
                return note;
            }
        }
        return null;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getType_of_product() {
        return type_of_product;
    }

    public void setType_of_product(String type_of_product) {
        this.type_of_product = type_of_product;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
