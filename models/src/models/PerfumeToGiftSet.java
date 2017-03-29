package models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shrralis on 3/15/17.
 */
public class PerfumeToGiftSet extends Model {
    public Perfume perfume = null;
    public GiftSet giftSet = null;

    public PerfumeToGiftSet() {}
    @Override
    public PerfumeToGiftSet parse(ResultSet from, Connection connection) {
        try {
            perfume = new List<>(get("SELECT * FROM `perfumes` WHERE `id` = " +
                    from.getInt("perfume") + ";", connection), Perfume.class, connection).get(0);
            giftSet = new List<>(get("SELECT * FROM `giftSets` WHERE `id` = " +
                    from.getInt("giftSets") + ";", connection), GiftSet.class, connection).get(0);
        } catch (SQLException ignored) {}
        return this;
    }

    public ResultSet get(String sql, Connection connection) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public GiftSet getGiftSet() {
        return giftSet;
    }

    public void setGiftSet(GiftSet giftSet) {
        this.giftSet = giftSet;
    }
}
