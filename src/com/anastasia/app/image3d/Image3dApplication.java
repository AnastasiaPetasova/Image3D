package com.anastasia.app.image3d;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Image3dApplication extends Application {

    volatile static boolean appIsAlive = true;

    @Override
    public void start(Stage primaryStage) throws Exception{
        initScene(primaryStage);
        primaryStage.show();

    }

    private void initScene(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("image3d_viewer.fxml"));
        primaryStage.setTitle("Petasova Image3D app");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setScene(
                new Scene(root, screenSize.width / 2, screenSize.height / 2)
        );

        primaryStage.setOnCloseRequest(event -> {
            appIsAlive = false;

            Platform.exit();
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
