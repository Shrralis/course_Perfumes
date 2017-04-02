package server;

import models.*;

import java.sql.*;

/**
 * Created by shrralis on 3/12/17.
 */
@SuppressWarnings("unchecked")
public class DatabaseWorker {
    private static DatabaseWorker iam = null;
    private Connection connection = null;

    private DatabaseWorker() {}

    public static boolean openConnection() {
        if (iam == null) {
            iam = new DatabaseWorker();
        } else {
            closeConnection();
            return openConnection();
        }

        final String sDatabaseName = "perfumes";
        final String sServerUser = "root";       //зазвичай root
        final String sServerPassword = "zolotorig91";

        try {
            if (iam.connection == null || iam.connection.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();

                iam.connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + sDatabaseName + "?useUnicode=true&characterEncoding=UTF-8",
                        sServerUser,
                        sServerPassword
                );
            }
            return true;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnection() {
        try {
            if (iam != null) {
                if (iam.connection != null && !iam.connection.isClosed()) {
                    iam.connection.close();
                }
                iam = null;
            }
        } catch (SQLException ignored) {}
    }

    public static ServerResult processQuery(ServerQuery query) {
        if (query == null) {
            System.out.println("No query from the server!");
            return ServerResult.create(1, "No query");
        } else {
            String method = query.getMethodName();

            if (method.equalsIgnoreCase("disconnect")) {
                return ServerResult.create(0, "disconnect");
            }

            if (method.equalsIgnoreCase("get")) {
                return iam.get(query);
            }

            if (method.equalsIgnoreCase("add")) {
                return iam.add(query);
            }

            if (method.equalsIgnoreCase("delete")) {
                return iam.delete(query);
            }

            if (method.equalsIgnoreCase("edit")) {
                return iam.edit(query);
            }
        }
        return null;
    }

    private ServerResult get(ServerQuery query) {
        ServerResult result = null;

        try {
            String table = query.getTableName().toLowerCase();

            if (connection != null && !connection.isClosed()) {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                if (table.matches("^(brands)|(perfumes)|(gift(s|S)ets)|(gift(s|S)ets_has_perfumes)$")) {
                    if (table.equalsIgnoreCase("giftSets_has_perfumes")) {
                        String sql;

                        if (query.getMySQLCondition().matches("(\\D|\\d)+gift(s|S)et(\\D|\\d)+")) {
                            sql = "SELECT p.* FROM `giftSets_has_perfumes` ghp, `perfumes` p" +
                                    query.getMySQLCondition() + " AND ghp.`perfume` = p.`id`;";
                            result = ServerResult.create(new List(statement.executeQuery(sql), Perfume.class, connection));
                        } else {
                            sql = "SELECT g.* FROM `giftSets_has_perfumes` ghp, `giftSets` g" +
                                    query.getMySQLCondition() + " AND ghp.`giftSet` = g.`id`;";
                            result = ServerResult.create(new List(statement.executeQuery(sql), GiftSet.class));
                        }
                        return result;
                    }

                    ResultSet resultSet = statement.executeQuery("SELECT * FROM `" + table + "`" +
                            query.getMySQLCondition() + ";");

                    if (table.equalsIgnoreCase("brands")) {
                        result = ServerResult.create(new List(resultSet, Brand.class));
                    } else if (table.equalsIgnoreCase("perfumes")) {
                        result = ServerResult.create(new List(resultSet, Perfume.class, connection));
                    } else if (table.equalsIgnoreCase("giftSets")) {
                        result = ServerResult.create(new List(resultSet, GiftSet.class));
                    }
                } else {
                    System.out.println("Unknown table (" + table + ") for get()");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ServerResult add(ServerQuery query) {
        ServerResult result = null;

        try {
            String table = query.getTableName().toLowerCase();

            if (connection != null && !connection.isClosed()) {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                if (table.matches("^(brands)|(perfumes)|(gift(s|S)ets)|(gift(s|S)ets_has_perfumes)$")) {
                    int iResult = statement.executeUpdate(query.getInsertMysqlQuery(), Statement.RETURN_GENERATED_KEYS);

                    if (iResult >= 0) {
                        ResultSet rs = statement.getGeneratedKeys();
                        int id = 0;

                        if (rs.next()) {
                            id = rs.getInt(1);
                        }

                        if (table.matches("^gift(s|S)ets_has_perfumes$")) {
                            result = ServerResult.create(0, "successfully added");
                        } else {
                            result = ServerResult.create(
                                    new List(statement.executeQuery("SELECT * FROM `" + table + "` WHERE `id` = " + id + ";"),
                                            query.getObjectToProcess().getClass(), connection)
                            );
                        }
                    } else {
                        result = ServerResult.create(1, "not added");
                    }
                }
            }
        } catch (SQLException | IllegalAccessException ignored) {}
        return result;
    }

    private ServerResult delete(ServerQuery query) {
        ServerResult result = null;

        try {
            String table = query.getTableName().toLowerCase();

            if (connection != null && !connection.isClosed()) {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                if (table.matches("^(brands)|(perfumes)|(gift(s|S)ets)|(gift(s|S)ets_has_perfumes)$")) {
                    int iResult = statement.executeUpdate("DELETE FROM `" + table + "` WHERE `id` = "
                            + ((Owner) query.getObjectToProcess()).getId() + ";");

                    if (iResult >= 0) {
                        result = ServerResult.create(0, "deleted");
                    } else {
                        result = ServerResult.create(1, "not deleted");
                    }
                }
            }
        } catch (SQLException ignored) {}
        return result;
    }

    private ServerResult edit(ServerQuery query) {
        ServerResult result = null;

        try {
            String table = query.getTableName();

            if (connection != null && !connection.isClosed()) {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                if (table.matches("^(brands)|(perfumes)|(gift(s|S)ets)|(gift(s|S)ets_has_perfumes)$")) {
                    System.out.println(query.getUpdateMysqlQuery());
                    int iResult = statement.executeUpdate(query.getUpdateMysqlQuery(), Statement.RETURN_GENERATED_KEYS);

                    if (iResult >= 0) {
                        int id = ((Owner) query.getObjectToProcess()).getId();

                        result = ServerResult.create(
                                new List(statement.executeQuery("SELECT * FROM `" + table + "` WHERE `id` = " + id + ";"),
                                        query.getObjectToProcess().getClass(), connection)
                        );
                    } else {
                        result = ServerResult.create(1, "not updated");
                    }
                }
            }
        } catch (SQLException | IllegalAccessException ignored) {}
        return result;
    }
}
