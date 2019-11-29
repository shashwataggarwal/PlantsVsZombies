package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.lang.Math.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;

public class gameLevelController implements Initializable {
    @FXML
    public ArrayList<ImageView> row2;
    public ArrayList<ImageView> row3;
    public ArrayList<ImageView> row4;
    public Label peashooterTimer;
    public Label sunflowerTimer;
    public Label cherrybombTimer;
    public Label wallnutTimer;
    public Label specialTimer;
    public Pane gamePane;
    public Pane pausePane;
    public Pane pane;
    public Label sunCount;
    public ImageView peaShooterCard;
    public ImageView sunflowerCard;
    public ImageView cherryBombCard;
    public ImageView wallnutCard;
    public ImageView specialCard;
    public ImageView l1;
    public ImageView l2;
    public ImageView l3;
    public ImageView l4;
    public ImageView l5;
    public Label  progressTimer;
    public ProgressBar progressBar;
    private Image sunImage;
    private Image peaShooterImage;
    private Image sunflowerImage;
    private Image peaImage;
    private ArrayList<Animation> allAnims;
    private ArrayList<ImageView> gridPositions;
    private boolean peaShooterRefreshed;
    private boolean sunflowerRefreshed;
    private boolean cherryBombRefreshed;
    private boolean wallnutRefreshed;
    private boolean specialRefreshed;
    public int initProgressTimerValue;
    public int currProgress;
    private Level level;
    private Random random;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void pauseEventHandler(ActionEvent e) {

        for(Timeline anim:level.getAllTimeLines()) {
            if(anim!=null) {
                anim.pause();
            }
        }
        gamePane.setDisable(true);
        pausePane.setVisible(true);
        pausePane.setDisable(false);
    }
    public void resumeGameEventHandler(ActionEvent e) {
        gamePane.setDisable(false);
        pausePane.setVisible(false);
        pausePane.setDisable(true);
        for(Timeline anim:level.getAllTimeLines()) {
            if(anim!=null) {
                anim.play();
            }
        }
    }

    public void exitGameEventHandler(ActionEvent e) throws IOException {
        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(gameLevelScene);
        appWindow.show();
    }

    public void startLevel(int i) {
        ArrayList<ImageView> cards=new ArrayList<ImageView>();
        cards.add(peaShooterCard);
        cards.add(sunflowerCard);
        cards.add(cherryBombCard);
        cards.add(wallnutCard);
        cards.add(specialCard);
        ArrayList<Label> labels=new ArrayList<Label>();
        labels.add(peashooterTimer);
        labels.add(sunflowerTimer);
        labels.add(cherrybombTimer);
        labels.add(wallnutTimer);
        labels.add(specialTimer);
        ArrayList<ImageView> ls=new ArrayList<ImageView>();
        ls.add(l1);
        ls.add(l2);
        ls.add(l3);
        ls.add(l4);
        ls.add(l5);
        level=new Level(i,gamePane,cards,labels,ls,sunCount,progressTimer,progressBar);
    }
}
