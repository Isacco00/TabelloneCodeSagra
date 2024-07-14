package com.example.tabellonecodesagra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TabelloneCodeSagraController {

    private Stage secondaryMonitor;

    @FXML
    private Label numero1;
    @FXML
    private Label numero2;
    @FXML
    private Label numero3;
    @FXML
    private Label numero4;

    @FXML
    public void initialize() {
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            dbManager.loadNumbers(Arrays.asList(numero1, numero2, numero3, numero4));
            dbManager.interceptor(Arrays.asList(numero1, numero2, numero3, numero4));
        } catch (Exception ex){
            if (dbManager != null) {
                dbManager.destroy();
            }
        }
    }

    @FXML
    protected void openSecondaryStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TabelloneCodeSagra.class.getResource("monitor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        // Get the list of screens
        var screens = Screen.getScreens();
        // Create a secondary stage
        if (secondaryMonitor == null) {
            secondaryMonitor = new Stage();
        }

        // Get the list of screens
        if (screens.size() > 1) {
            // Get the second screen (if available)
            Screen secondScreen = screens.get(1);
            Rectangle2D bounds = secondScreen.getVisualBounds();

            // Set the stage properties to show on the second screen
            secondaryMonitor.setX(bounds.getMinX());
            secondaryMonitor.setY(bounds.getMinY());
            secondaryMonitor.setWidth(bounds.getWidth());
            secondaryMonitor.setHeight(bounds.getHeight());
            secondaryMonitor.setFullScreen(true);
        } else {
            // If no second monitor is found, use default size and position
            secondaryMonitor.setWidth(400);
            secondaryMonitor.setHeight(300);
        }
        secondaryMonitor.setScene(scene);
        secondaryMonitor.setTitle("Show numbers");
        secondaryMonitor.show();
    }

}