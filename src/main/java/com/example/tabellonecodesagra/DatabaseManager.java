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

    private static final String URL = "jdbc:postgresql://localhost:5432/sagra";
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

    public void interceptor(List<Label> labelList, Integer numeroCassa) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> listenForNotifications(labelList, numeroCassa), executor).thenAccept(result -> {
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

    private void listenForNotifications(List<Label> labelList, Integer numeroCassa) {
        try {
            // Ascolta il canale 'new_row'
            stmt.execute("LISTEN new_row");
            System.out.println("In attesa di notifiche...");
            while (true) {
                // Aspetta la notifica
                PGNotification[] notifications = pgConn.getNotifications(5000);
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        System.out.println("Notifica");
                        Platform.runLater(() -> loadNumbers(labelList, numeroCassa));
                    }
                }
                // Evita il blocco del ciclo
                Thread.sleep(500);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadNumbers(List<Label> labels, Integer numeroCassa) {
        List<Integer> last4Numbers = new ArrayList<>();
        try {
            // Execute the query
            if (numeroCassa != null) {
                rs = stmt.executeQuery("SELECT * from tabellone_code " +
                        "WHERE numero_cassa = " + numeroCassa + " " +
                        "ORDER BY numero_sequenza desc limit 4");
            } else {
                rs = stmt.executeQuery("SELECT * from tabellone_code ORDER BY numero_sequenza desc limit 4");
            }
            // Process the results
            while (rs.next()) {
                int id = rs.getInt("numero_sequenza");
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


    private void listenForNotificationsSecondaryMonitor(List<Label> labelList) {
        try {
            // Ascolta il canale 'new_row'
            stmt.execute("LISTEN new_row");
            System.out.println("In attesa di notifiche...");
            while (true) {
                // Aspetta la notifica
                PGNotification[] notifications = pgConn.getNotifications(5000);
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        System.out.println("Notifica");
                        Platform.runLater(() -> loadNumbersSecondaryMonitor(labelList));
                    }
                }
                // Evita il blocco del ciclo
                Thread.sleep(500);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void interceptorSecondaryMonitor(List<Label> labelList) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> listenForNotificationsSecondaryMonitor(labelList), executor).thenAccept(result -> {
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

    public void loadNumbersSecondaryMonitor(List<Label> labels) {
        List<String> last3Numbers = new ArrayList<>();
        try {
            // Execute the query
            rs = stmt.executeQuery("SELECT * from tabellone_code " +
                    "WHERE numero_cassa = 1 ORDER BY numero_sequenza desc limit 1");
            // Process the results
            if (rs.next()) {
                int id = rs.getInt("numero_sequenza");
                last3Numbers.add(Integer.toString(id));
            } else {
                last3Numbers.add("-");
            }
            rs = stmt.executeQuery("SELECT * from tabellone_code " +
                    "WHERE numero_cassa = 2 ORDER BY numero_sequenza desc limit 1");
            // Process the results
            if (rs.next()) {
                int id = rs.getInt("numero_sequenza");
                last3Numbers.add(Integer.toString(id));
            } else {
                last3Numbers.add("-");
            }
            rs = stmt.executeQuery("SELECT * from tabellone_code " +
                    "WHERE numero_cassa = 3 ORDER BY numero_sequenza desc limit 1");
            // Process the results
            if (rs.next()) {
                int id = rs.getInt("numero_sequenza");
                last3Numbers.add(Integer.toString(id));
            } else {
                last3Numbers.add("-");
            }
            for (int i = 0; i < last3Numbers.size(); i++) {
                switch (i) {
                    case 0:
                        labels.get(0).setText(last3Numbers.get(i));
                        break;
                    case 1:
                        labels.get(1).setText(last3Numbers.get(i));
                        break;
                    case 2:
                        labels.get(2).setText(last3Numbers.get(i));
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

    public void insertNumber(int numeroCassa) {
        try {
            // Execute the query
            stmt.execute("insert into tabellone_code values ((select nextval('tabellone_code_seq')), " + numeroCassa + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
