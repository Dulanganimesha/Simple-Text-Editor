import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

public class AppInitializer extends Application {

    private Properties prop = new Properties();

    private File propFile = new File("application.properties");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/View/EditorForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Simple Text Editor");
        primaryStage.show();

        /* Reading preferences for the logged user (Current Logged User on OS) */
        String color = Preferences.userRoot().node("com.txtedit").get("color", "white");

        boolean isMaximized = Preferences.userRoot().node("com.txtedit").getBoolean("is-maximized", false);
        double xPos = Preferences.userRoot().node("com.txtedit").getDouble("x-pos", -1);
        double yPos = Preferences.userRoot().node("com.txtedit").getDouble("y-pos", -1);
        double width = Preferences.userRoot().node("com.txtedit").getDouble("width", -1);
        double height = Preferences.userRoot().node("com.txtedit").getDouble("height", -1);

        /* Setting preferences */

        root.setBackground(new Background(new BackgroundFill(Color.valueOf(color), null, null)));
        primaryStage.setMaximized(isMaximized);

        if (!isMaximized){
            if (width == -1 && height == -1){
                primaryStage.setWidth(root.getPrefWidth());
                primaryStage.setHeight(root.getPrefHeight());
            }else{
                primaryStage.setWidth(width);
                primaryStage.setHeight(height);
            }

            if (xPos == -1 && yPos == -1){
                primaryStage.centerOnScreen();
            }else{
                primaryStage.setX(xPos);
                primaryStage.setY(yPos);
            }
        }

        /* Saving user preferences */
        primaryStage.setOnCloseRequest(event -> {

            Preferences.userRoot().node("com.txtedit").putBoolean("is-maximized", primaryStage.isMaximized());
            if (!primaryStage.isMaximized()){
                Preferences.userRoot().node("com.txtedit").putDouble("x-pos", primaryStage.getX());
                Preferences.userRoot().node("com.txtedit").putDouble("y-pos", primaryStage.getY());
                Preferences.userRoot().node("com.txtedit").putDouble("width", primaryStage.getWidth());
                Preferences.userRoot().node("com.txtedit").putDouble("height", primaryStage.getHeight());
            }
        });
    }

}
