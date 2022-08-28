package ilya.server.SQL;
import ilya.common.Classes.Route;

import java.sql.*;

public class SQLManager {
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS ROUTES "
            + "("
            + " ID SERIAL PRIMARY KEY,"
            + " OWNER VARCHAR(100) NOT NULL,"
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
            + ")";
    private Connection connection;
    public SQLManager(String username, String password, String url) throws SQLException, ClassNotFoundException {
        this.connection = DriverManager.getConnection(url, username, password);
        Class.forName("org.postgresql.Driver");
    }
    public void CreateTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(SQL_CREATE);
    }

    public void clearOwned() throws SQLException {
        String username = "SomeOwner";

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ROUTES WHERE OWNER=?");
        preparedStatement.setString(1, username);
        preparedStatement.execute();
    }

    public String getAllElements() {

    }
    public Long add(Route route) throws SQLException {
        String username = "SomeOwner";

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO ROUTES (ID, OWNER, ROUTE_NAME, COORDINATE_X, COORDINATE_Y, CREARION_DATE, "
                        + "FROM_X, FROM_Y, FROM_Z, FROM_NAME, TO_X, TO_Y, TO_Z, TO_NAME, DISTANCE)"
                        + "VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID;");
        prepare(preparedStatement, route);
        preparedStatement.setString(1, username);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        long id = resultSet.getLong("ID");
        return id;
    }
    private void prepare(PreparedStatement preparedStatement, Route route) throws SQLException {
        preparedStatement.setString(2, route.getName());
        preparedStatement.setInt(3, route.getCoordinates().getX());
        preparedStatement.setLong(4, route.getCoordinates().getY());

        preparedStatement.setString(5, route.getCreationDate().toString());

        if(route.getFrom() != null) {
            preparedStatement.setInt(6, route.getFrom().getX());
            preparedStatement.setLong(7, route.getFrom().getY());
            preparedStatement.setDouble(8, route.getFrom().getZ());
            preparedStatement.setString(9, route.getFrom().getName());
        } else {
            preparedStatement.setNull(6, Types.NULL);
            preparedStatement.setNull(7, Types.NULL);
            preparedStatement.setNull(8, Types.NULL);
            preparedStatement.setNull(9, Types.NULL);
        }

        preparedStatement.setInt(10, route.getTo().getX());
        preparedStatement.setLong(11, route.getTo().getY());
        preparedStatement.setDouble(12, route.getTo().getZ());
        preparedStatement.setString(13, route.getTo().getName());

        preparedStatement.setFloat(14, route.getDistance());
    }
}
