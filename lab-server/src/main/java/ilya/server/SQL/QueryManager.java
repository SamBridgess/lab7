package ilya.server.SQL;
import ilya.common.Classes.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class QueryManager {
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS ROUTES "
            + "("
            + " ID SERIAL PRIMARY KEY,"
            + " ROUTE_NAME VARCHAR(100) NOT NULL,"
            + " COORDINATE_X INT NOT NULL,"
            + " COORDINATE_Y BIGINT NOT NULL CHECK(COORDINATE_Y > -673),"
            + " CREARION_DATE VARCHAR(100) NOT NULL,"
            + " FROM_X INT,"
            + " FROM_Y BIGINT,"
            + " FROM_Z DOUBLE PRECISION,"
            + " FROM_NAME varchar(100),"
            + " TO_X INT NOT NULL,"
            + " TO_Y BIGINT NOT NULL,"
            + " TO_Z DOUBLE PRECISION NOT NULL,"
            + " TO_NAME VARCHAR(100),"
            + " DISTANCE FLOAT NOT NULL"
            + " OWNER VARCHAR(100) NOT NULL"
            + ")";
    private Connection connection;
    public QueryManager(String username, String password, String url) throws SQLException, ClassNotFoundException {
        this.connection = DriverManager.getConnection(url, username, password);
        Class.forName("org.postgresql.Driver");
    }
    public void CreateTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(SQL_CREATE);
    }
    public void sort() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mytable ORDER BY DISTANCE ASC, ROUTE_NAME ASC, ID ASC");
        preparedStatement.execute();
    }
    public void clearOwned(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ROUTES WHERE OWNER=?");
        preparedStatement.setString(1, username);
        preparedStatement.execute();
    }
    public boolean update(Long id, String username, Route route) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ROUTES WHERE ID =" + id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String rowCreatorName = resultSet.getString("USER_NAME");
        if(!Objects.equals(rowCreatorName, username)) {
            return false;
        }
        PreparedStatement preparedStatement2 = connection.prepareStatement(
                "UPDATE ROUTES SET ROUTE_NAME=?, COORDINATE_X=?, COORDINATE_Y=?, CREARION_DATE=?,"
                        + "FROM_X=?, FROM_Y=?, FROM_Z=?, TO_X=?, TO_Y=?, TO_Z=?, TO_NAME=?, DISTANCE=?"
                + "WHERE ID=?");
        prepare(preparedStatement2, route);
        preparedStatement2.setLong(13, id);
        preparedStatement2.execute();
        return true;
    }
    public Long add(Route route, String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO ROUTES (ID, ROUTE_NAME, COORDINATE_X, COORDINATE_Y, CREARION_DATE, "
                        + "FROM_X, FROM_Y, FROM_Z, FROM_NAME, TO_X, TO_Y, TO_Z, TO_NAME, DISTANCE, OWNER)"
                        + "VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID;");
        prepare(preparedStatement, route);
        preparedStatement.setString(14, username);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        long id = resultSet.getLong("ID");
        return id;
    }
    private void prepare(PreparedStatement preparedStatement, Route route) throws SQLException {
        preparedStatement.setString(1, route.getName());
        preparedStatement.setInt(2, route.getCoordinates().getX());
        preparedStatement.setLong(3, route.getCoordinates().getY());

        preparedStatement.setString(4, route.getCreationDate().toString());

        if(route.getFrom() != null) {
            preparedStatement.setInt(5, route.getFrom().getX());
            preparedStatement.setLong(6, route.getFrom().getY());
            preparedStatement.setDouble(7, route.getFrom().getZ());
            preparedStatement.setString(8, route.getFrom().getName());
        } else {
            preparedStatement.setNull(5, Types.NULL);
            preparedStatement.setNull(6, Types.NULL);
            preparedStatement.setNull(7, Types.NULL);
            preparedStatement.setNull(8, Types.NULL);
        }

        preparedStatement.setInt(9, route.getTo().getX());
        preparedStatement.setLong(10, route.getTo().getY());
        preparedStatement.setDouble(11, route.getTo().getZ());
        preparedStatement.setString(12, route.getTo().getName());

        preparedStatement.setFloat(13, route.getDistance());
    }
}
