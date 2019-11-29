package sample;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class loadScreenController {

    public Button loadScreenButton;
    public void loadScreenButtonEventHandler(ActionEvent e) throws IOException {
//        System.out.println("YO AKKK and #wat");
        Parent mainMenuRoot= FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene mainMenuScene=new Scene(mainMenuRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(mainMenuScene);
        appWindow.show();
    }


}
