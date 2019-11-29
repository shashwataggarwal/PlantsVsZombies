package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class mainMenuController {
    public void exitEventHandler(ActionEvent e) {
        System.out.println("EXITING");
        System.exit(0);
    }
    public void startGameEventHandler(ActionEvent e) throws IOException {
//        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("gameLevel.fxml"));
//        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
//        appWindow.setScene(gameLevelScene);
//        appWindow.show();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameLevel.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        gameLevelController controller = fxmlLoader.<gameLevelController>getController();
        Scene scene = new Scene(root);

        appWindow.setScene(scene);

        appWindow.show();
        controller.startLevel(1);
    }
    public void loadGameEventHandler(ActionEvent e) throws IOException {
    }



}
