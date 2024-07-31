package com.example.tabellonecodesagra;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.List;

public class SecondMonitorController {

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        gridPane.getChildren().removeAll(gridPane.getChildren());
        addMainTitle("SCEGLIERE IL NUMERO DEL TAVOLO E PRENDERE IL NUMERO");
        createCell(1, 0, "CASSA 1");
        createCell(1, 1, "CASSA 2");
        createCell(1, 2, "CASSA 3");
        Label numero1 = createCell(2, 0, "-");
        Label numero2 = createCell(2, 1, "-");
        Label numero3 = createCell(2, 2, "-");
        List<Label> labels = Arrays.asList(numero1, numero2, numero3, new Label());
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            dbManager.loadNumbersSecondaryMonitor(labels);
            dbManager.interceptorSecondaryMonitor(labels);
        } catch (Exception ex) {
            if (dbManager != null) {
                dbManager.destroy();
            }
        }
//        createImageBox();
    }

    private void addMainTitle(String titolo) {
        StackPane stackPane = new StackPane();
        Label label = new Label();
        label.setText(titolo);
        label.setWrapText(true);
        label.getStyleClass().add("label-cell-style");
        label.setTextAlignment(TextAlignment.CENTER);
        bindFontSize(label, stackPane, 0.3);
        stackPane.getChildren().add(label);
        stackPane.getStyleClass().add("stack-pane-cell");
        gridPane.add(stackPane, 0, 0, 3, 1);
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
        label.setWrapText(true);
        stackPane.getChildren().add(label);
        bindFontSize(label, stackPane, 0.4);
        stackPane.getStyleClass().add("stack-pane-cell");
        gridPane.add(stackPane, columnIndex, rowIndex);
        return label;
    }

    private void bindFontSize(Label label, StackPane pane, double multiplier) {
        label.styleProperty().bind(Bindings.createStringBinding(() -> {
            double fontSize = Math.min(pane.getWidth(), pane.getHeight()) * multiplier;
            return "-fx-font-size: " + fontSize + "px;";
        }, pane.widthProperty(), pane.heightProperty()));
    }

}
