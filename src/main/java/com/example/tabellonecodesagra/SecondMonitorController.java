package com.example.tabellonecodesagra;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondMonitorController {

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        gridPane.getChildren().removeAll(gridPane.getChildren());
        createCell(0, 0, "SERVENDO IL");
        Label numero1 = createCell(0, 1, "1");
        Label numero2 = createCell(1, 1, "");
        Label numero3 = createCell(2, 1, "");
        List<Label> labels = Arrays.asList(numero1, numero2, numero3, new Label());
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            dbManager.loadNumbers(labels);
            dbManager.interceptor(labels);
        } catch (Exception ex) {
            if (dbManager != null) {
                dbManager.destroy();
            }
        }

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        CompletableFuture.runAsync(this::listenForKeyboard, executor)
//                .thenAccept(result -> {
//                    System.out.println("Listening finished.");
//                    executor.shutdown();
//                })
//                .exceptionally(ex -> {
//                    ex.printStackTrace();
//                    executor.shutdown();
//                    return null;
//                });
        createImageBox();
    }

    private void createImageBox() {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView("test.png");
        stackPane.getChildren().add(imageView);
        stackPane.getStyleClass().add("stack-pane-cell");
        gridPane.add(stackPane, 0, 1, 1, 2);
    }

    private Label createCell(int rowIndex, int columnIndex, String titolo) {
        StackPane stackPane = new StackPane();
        Label label = new Label();
        label.setText(titolo);
        label.getStyleClass().add("label-cell-style");
        stackPane.getChildren().add(label);
        bindFontSize(label, stackPane);
        stackPane.getStyleClass().add("stack-pane-cell");
        gridPane.add(stackPane, columnIndex, rowIndex);
        return label;
    }

    private void bindFontSize(Label label, StackPane pane) {
        label.styleProperty().bind(Bindings.createStringBinding(() -> {
            double fontSize = Math.min(pane.getWidth(), pane.getHeight()) * 0.5;
            return "-fx-font-size: " + fontSize + "px;";
        }, pane.widthProperty(), pane.heightProperty()));
    }

}
