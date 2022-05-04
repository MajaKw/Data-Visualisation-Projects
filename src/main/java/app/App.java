package app;

import java.sql.*;

public class App {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres"; // jdbc:postgresql://server-name:server-port/database-name
    private static final String user = "postgres";
    private static final String password = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void printJans(ResultSet rs) throws SQLException {
        while(rs.next()){
            System.out.println(rs.getString("owner_id") + " " +
                    rs.getString("adres") + " " +
                    rs.getString("name") + " " +
                    rs.getString("income")
            );
        }
    }

    public static void main(String[] args) {
        App app = new App();
//        app.connect();
        String SQLquery = "SELECT *" +
                "FROM owner_house JOIN houses USING (house_id) JOIN owners USING (owner_id) " +
                "WHERE name='JAN'";
        try(Connection conn = app.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQLquery)) {
            printJans(rs);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
