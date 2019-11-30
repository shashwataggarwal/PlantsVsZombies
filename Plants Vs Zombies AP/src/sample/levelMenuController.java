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

interface levelControllers {
    public void playLevel(int levelNumber);
}

public class levelMenuController implements levelControllers{
    public Stage appWindow;
    public void exitEventHandler(ActionEvent e) throws IOException {
        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(gameLevelScene);
        appWindow.show();
    }
    public void initAppWindow(ActionEvent e) {
        appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
    }
    public void l1(ActionEvent e) {
        initAppWindow(e);
        playLevel(1);
    }
    public void l2(ActionEvent e) {
        initAppWindow(e);
        playLevel(2);
    }
    public void l3(ActionEvent e) {
        initAppWindow(e);
        playLevel(3);
    }
    public void l4(ActionEvent e) {
        initAppWindow(e);
        playLevel(4);
    }
    public void l5(ActionEvent e) {
        initAppWindow(e);
        playLevel(5);
    }

    @Override
    public void playLevel(int levelNumber) {
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
}
