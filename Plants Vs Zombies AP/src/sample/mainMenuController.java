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

public class mainMenuController implements levelControllers{
    private Stage appWindow;
    public void exitEventHandler(ActionEvent e) {
        System.out.println("EXITING");
        System.exit(0);
    }
    public void startGameEventHandler(ActionEvent e) throws IOException {
//        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("gameLevel.fxml"));
//        Scene gameLevelScene=new Scene(gameLevelRoot);
        appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
//        appWindow.setScene(gameLevelScene);
//        appWindow.show();

        playLevel(1);
    }
    public void playLevel(int levelNumber) {
//        appWindow.setScene(gameLevelScene);
//        appWindow.show();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameLevel.fxml"));

        Parent root = null;
        try {
            root = (Parent)fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameLevelController controller = fxmlLoader.<gameLevelController>getController();
        Scene scene = new Scene(root);

        appWindow.setScene(scene);

        appWindow.show();
        controller.startLevel(levelNumber,this);
    }
    public void loadGameEventHandler(ActionEvent e) throws IOException {
    }

    public void selectLevelEventHandler(ActionEvent e) throws IOException {
        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("levelMenu.fxml"));
        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(gameLevelScene);
        appWindow.show();
    }

}
