package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static StageLoader stageLoader;


    public static void setStageLoader(StageLoader stageLoader) {
        Main.stageLoader = stageLoader;
    }

    public static StageLoader getStageLoader() {
        return stageLoader;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sampleLoginPage.fxml"));
        primaryStage.setTitle("WELCOME TO LEAVEWISE");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();


    }
}


