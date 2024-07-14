package com.example.tabellonecodesagra;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5433/sagra";
    private static final String USER = "sagra";
    private static final String PASSWORD = "sagra";

    private Connection conn;
    private PGConnection pgConn;
    private Statement stmt;
    ResultSet rs = null;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            pgConn = conn.unwrap(PGConnection.class);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void interceptor(List<Label> labelList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> listenForNotifications(labelList), executor).thenAccept(result -> {
            System.out.println("Listening finished.");
            executor.shutdown();
        }).exceptionally(ex -> {
            ex.printStackTrace();
            executor.shutdown();
            return null;
        });
        System.out.println("Main thread continues...");
        // Esegui altre operazioni nel thread principale qui
    }

    private void listenForNotifications(List<Label> labelList) {
        try {
            // Ascolta il canale 'new_row'
            stmt.execute("LISTEN new_row");
            System.out.println("In attesa di notifiche...");
            while (true) {
                // Aspetta la notifica
                PGNotification[] notifications = pgConn.getNotifications(5000);
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        Platform.runLater(() -> loadNumbers(labelList));
                    }
                }
                // Evita il blocco del ciclo
                Thread.sleep(100);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadNumbers(List<Label> labels) {
        List<Integer> last4Numbers = new ArrayList<>();
        try {
            // Execute the query
            rs = stmt.executeQuery("SELECT * from tabellone_code ORDER BY numero_sequenza desc limit 4");
            // Process the results
            while (rs.next()) {
                int id = rs.getInt("numero_sequenza");
                System.out.println("ID: " + id);
                last4Numbers.add(id);
            }
            for (int i = 0; i < last4Numbers.size(); i++) {
                switch (i) {
                    case 0:
                        labels.get(0).setText(last4Numbers.get(i).toString());
                        break;
                    case 1:
                        labels.get(1).setText(last4Numbers.get(i).toString());
                        break;
                    case 2:
                        labels.get(2).setText(last4Numbers.get(i).toString());
                        break;
                    case 3:
                        labels.get(3).setText(last4Numbers.get(i).toString());
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNumber() {
        try {
            // Execute the query
            stmt.execute("insert into\n" +
                    "    tabellone_code values ((select max(numero_sequenza) +1 from tabellone_code));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
