package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class loadMenuController implements Initializable {
    @FXML
    public ComboBox dropDownMenu;

    private levelControllers mainMenuController;

    public void exitEventHandler(ActionEvent e) throws IOException {
        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(gameLevelScene);
        appWindow.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> fileNames=new ArrayList<>();
        File folder=new File("./SavedGames");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles!=null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String fileName = listOfFiles[i].getName();
                    if(fileName.contains(".bin")){
                        fileNames.add(fileName);
                    }

                }
            }
        }

        dropDownMenu.getItems().addAll(fileNames);

    }

    public void setMainMenuController(levelControllers mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    public void loadGameEventHandler(ActionEvent actionEvent) throws IOException {
        ObjectInputStream in = null;
        LevelData levelData = null;
        try{
            System.out.println(dropDownMenu.getValue());
            in = new ObjectInputStream(new FileInputStream("./SavedGames/"+(String) dropDownMenu.getValue()));
            levelData = (LevelData) in.readObject();
        } catch (Exception e) {
        } finally {
            if(in!=null){
                in.close();
            }
        }
        mainMenuController.playLevel(levelData.getLevelNumber(),1,levelData);

    }
}
