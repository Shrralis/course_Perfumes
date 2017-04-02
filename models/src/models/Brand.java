package models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shrralis on 3/15/17.
 */
public class Brand extends Owner {
    public String type_of_company = null;
    public Integer creation_year = null;
    public String creator = null;
    public String headquarters = null;
    public String website = null;
    public String other = null;

    public Brand() {}
    @SuppressWarnings("unused")
    public Brand(ResultSet from) {
        parse(from);
    }

    @Override
    public Brand parse(ResultSet from) {
        super.parse(from);

        try {
            type_of_company = from.getString("type_of_company");
            creation_year = from.getInt("creation_year");
            creator = from.getString("creator");
            headquarters = from.getString("headquarters");
            website = from.getString("website");
            other = from.getString("other");
        } catch (SQLException ignored) {}
        return this;
    }

    public String getType_of_company() {
        return type_of_company;
    }

    public void setType_of_company(String type_of_company) {
        this.type_of_company = type_of_company;
    }

    public Integer getCreation_year() {
        return creation_year;
    }

    public void setCreation_year(Integer creation_year) {
        this.creation_year = creation_year;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
