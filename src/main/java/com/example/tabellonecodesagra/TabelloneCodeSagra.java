package com.example.tabellonecodesagra;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class TabelloneCodeSagra extends Application {
    @Override
    public void start(Stage primaryStage) {
        URL resourceUrl = TabelloneCodeSagra.class.getResource("");
        if (resourceUrl != null) {
            File currentDir = new File(resourceUrl.getFile());
            File parentCom = currentDir.getParentFile().getParentFile().getParentFile();
            File configFile = new File(parentCom, "config.txt");
            if (configFile.exists() && configFile.isFile()) {
                System.out.println("File 'config.txt' trovato: " + configFile.getAbsolutePath());
                // Creazione di un oggetto Properties
                Properties properties = new Properties();
                try (InputStream input = new FileInputStream(configFile)) {
                    properties.load(input);
                    // Lettura delle proprietà
                    String mainTitleMultiplier = properties.getProperty("mainTitleMultiplier");
                    String numeroCassa = properties.getProperty("numeroCassa");

                    // Conversione delle proprietà ai tipi desiderati
                    double mainTitleMultiplierValue = Double.parseDouble(mainTitleMultiplier);


                    // Stampa dei valori letti
                    //System.out.println("mainTitleMultiplier: " + mainTitleMultiplierValue);
                    Integer numeroCassaValue = null;
                    if (numeroCassa != null) {
                        // Add the key listener
                        try {
                            // Register the native hook
                            GlobalScreen.registerNativeHook();
                        } catch (NativeHookException ex) {
                            System.err.println("There was a problem registering the native hook.");
                            System.err.println(ex.getMessage());
                            System.exit(1);
                        }
                        numeroCassaValue = Integer.parseInt(numeroCassa);
                        switch (numeroCassaValue) {
                            case 1:
                                GlobalScreen.addNativeKeyListener(new GlobalF5KeyListener());
                                break;
                            case 2:
                                GlobalScreen.addNativeKeyListener(new GlobalF6KeyListener());
                                break;
                            case 3:
                                GlobalScreen.addNativeKeyListener(new GlobalF7KeyListener());
                                break;
                        }
                        System.out.println("NumeroCassa: " + numeroCassaValue);
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(TabelloneCodeSagra.class.getResource("mainconfig-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                    TabelloneCodeSagraController controller = fxmlLoader.getController();
                    // Passa i dati al controller
                    controller.runPreliminary(numeroCassaValue);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Full Screen on Second Monitor");
                    primaryStage.show();
                    //this makes all stages close and the app exit when the main stage is closed
                    primaryStage.setOnCloseRequest(event -> {
                        Platform.exit();
                        System.exit(0);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File 'config.txt' non trovato nella directory: " + parentCom.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}