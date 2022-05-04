package app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;

public class App extends Application {
    private final String url = "jdbc:postgresql://localhost:5432/"; // jdbc:postgresql://server-name:server-port/database-name
    private final String user = "postgres";
    private final String password = "";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /*public static void printJans(ResultSet rs) throws SQLException {
        while(rs.next()){
            System.out.println(rs.getString("owner_id") + " " +
                    rs.getString("adres") + " " +
                    rs.getString("name") + " " +
                    rs.getString("income")
            );
        }
    }*/

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        App app = new App();
//        app.connect();
        String SQLquery = "SELECT *" +
                "FROM azerbaijan " +
                "ORDER BY 1";
        try(Connection conn = app.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQLquery)) {
            TwoDimensionalBarChart twoDimensionalBarChartFirst = new TwoDimensionalBarChart(rs,1,2);
            TwoDimensionalBarChart twoDimensionalBarChartSecond = new TwoDimensionalBarChart(rs,1,3);
            //twoDimensionalBarChartFirst.graphChart(stage);
            twoDimensionalBarChartFirst.combine(twoDimensionalBarChartFirst).graphChart(stage);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
