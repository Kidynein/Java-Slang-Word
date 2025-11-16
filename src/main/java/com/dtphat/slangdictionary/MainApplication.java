package com.dtphat.slangdictionary;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("Slang Word Dictionary");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            try {
                SlangDictionary.getInstance().saveData("slang.dat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.show();
    }
}
