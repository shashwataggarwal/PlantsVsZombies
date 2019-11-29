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

    private Random random;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        allAnims= new ArrayList<Animation>();
//        gridPositions=new ArrayList<ImageView>();
//        initProgressTimerValue=0;
//        random=new Random();
//        try {
//            sunImage=new Image(new FileInputStream("./images/sun.png"));
//            peaShooterImage=new Image(new FileInputStream("./images/peashooter_moving.gif"));
//            sunflowerImage=new Image(new FileInputStream("./images/sunflower_moving.gif"));
//            peaImage=new Image(new FileInputStream("./images/pea.png"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        peaShooterRefreshed=true;
//        sunflowerRefreshed=true;
//        startMovingZombies();
//        addDragEventHandlers();
//        startGeneratingSunTokens();
//        initProgress();
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
        Level level1=new Level(5,gamePane,cards,labels,ls,sunCount);
    }
    public void initProgress() {
        System.out.println(initProgressTimerValue);
        currProgress=initProgressTimerValue;
        int x=(int)(currProgress/60);
        String s="";
        s=s+Integer.toString(x);
        s+=":";
        s+=(currProgress%60);
        progressTimer.setText(s);
        progressBar.setProgress(0);
        Timeline progressTimeline=new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                currProgress--;
                int x=(int)(currProgress/60);
                String s="";
                s=s+Integer.toString(x);
                s+=":";
                int y=(currProgress%60);
                if(y<10) {
                    s+="0";
                }
                s+=y;
                progressTimer.setText(s);
                progressBar.setProgress(1-(double)(((double)currProgress)/initProgressTimerValue));
            }
        }));
        progressTimeline.setCycleCount(currProgress);
        progressTimeline.play();
        allAnims.add(progressTimeline);
    }
    public void addDragEventHandlers() {
        addCardDrags(peaShooterCard);
        addCardDrags(sunflowerCard);
        for(ImageView gridPos : gridPositions) {
            gridPos.setImage(null);
            gridPos.setOnDragOver(e -> {
                if(gridPos.getImage()==null && (sunflowerCard.equals(e.getGestureSource()) || peaShooterCard.equals(e.getGestureSource()))) {
                    e.acceptTransferModes(TransferMode.ANY);
                }
                e.consume();
            });
            gridPos.setOnDragDropped(e -> {
                if(sunflowerCard.equals(e.getGestureSource())) {
                    gridPos.setImage(sunflowerImage);
                    initSunflower(gridPos);
                }
                else if(peaShooterCard.equals(e.getGestureSource())) {
                    gridPos.setImage(peaShooterImage);
                    initPeaShooter(gridPos);
                }
                e.setDropCompleted(true);
                e.consume();
            });
        }
    }
    private void initSunflower(ImageView sunflower) {
        Timeline sunflowerTimer=new Timeline(new KeyFrame(Duration.seconds(8), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ImageView sun=new ImageView(sunImage);
                sun.setFitWidth(40);
                sun.setFitHeight(40);
                sun.setLayoutX(sunflower.getLayoutX()-10);
                sun.setLayoutY(sunflower.getLayoutY()+10);
                gamePane.getChildren().add(sun);
                sun.setOnMouseClicked(e-> {
                    sun.setVisible(false);
                    sun.setDisable(true);
                    increaseSunCount();
                });
                Timeline removeSunTimeline=new Timeline(new KeyFrame(Duration.seconds(25), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(!sun.isDisable()) {
                            sun.setVisible(false);
                            sun.setDisable(true);
                        }
                    }
                }));
                removeSunTimeline.setCycleCount(1);
                removeSunTimeline.play();
                allAnims.add(removeSunTimeline);
            }
        }));
        sunflowerTimer.setCycleCount(Timeline.INDEFINITE);
        sunflowerTimer.play();
        allAnims.add(sunflowerTimer);
    }
    private void initPeaShooter(ImageView peashooter) {
        Timeline peashootTimer=new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ImageView pea=new ImageView(peaImage);
                pea.setFitHeight(20);
                pea.setFitWidth(20);
                pea.setLayoutX(peashooter.getLayoutX()+42);
                pea.setLayoutY(peashooter.getLayoutY()+11);
                gamePane.getChildren().add(pea);
                int offset= (int) (1260-pea.getLayoutX());
                int duration= (int) (offset*0.75);
                TranslateTransition peaMove=new TranslateTransition(Duration.millis(duration),pea);
                peaMove.setToX(offset);
                peaMove.setCycleCount(1);
                peaMove.play();
                peaMove.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        pea.setVisible(false);
                        pea.setDisable(true);
                    }
                });
                allAnims.add(peaMove);
            }
        }));
        peashootTimer.setCycleCount(Timeline.INDEFINITE);
        peashootTimer.play();
        allAnims.add(peashootTimer);
    }
    private void addCardDrags(ImageView card) {
        card.setOnDragDetected(e -> {
            Dragboard cardDragboard=card.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("peashooter");
            cardDragboard.setContent(content);
            e.consume();
        });
        card.setOnDragDone(e -> {
            if(e.getTransferMode()!=null) {
                card.setOpacity(0.25);
                card.setDisable(true);
                if(card.equals(peaShooterCard)) {
                    peashooterTimer.setVisible(true);
                    peashooterTimer.setText("5");
                    peaShooterRefreshed=false;
                    Timeline peaShooterRefresh = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            peashooterTimer.setText(Integer.toString(Integer.parseInt(peashooterTimer.getText())-1));
                        }
                    }));
                    peaShooterRefresh.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            peaShooterRefreshed = true;
                            peashooterTimer.setVisible(false);
                            if (getSunCount() >= 100) {
                                peaShooterCard.setOpacity(1);
                                peaShooterCard.setDisable(false);
                            }
                        }
                    });
                    peaShooterRefresh.setCycleCount(5);
                    peaShooterRefresh.play();
                    allAnims.add(peaShooterRefresh);
                    reduceSunCount(100);
                }
                else {
                    sunflowerRefreshed=false;
                    sunflowerTimer.setVisible(true);
                    sunflowerTimer.setText("5");
                    Timeline sunflowerRefresh = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            sunflowerTimer.setText(Integer.toString(Integer.parseInt(sunflowerTimer.getText())-1));
                        }
                    }));
                    sunflowerRefresh.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            sunflowerRefreshed = true;
                            sunflowerTimer.setVisible(false);
                            if (getSunCount() >= 50) {
                                sunflowerCard.setOpacity(1);
                                sunflowerCard.setDisable(false);
                            }
                        }
                    });
                    sunflowerRefresh.setCycleCount(5);
                    sunflowerRefresh.play();
                    allAnims.add(sunflowerRefresh);
                    reduceSunCount(50);
                }
            }
            e.consume();
        });
    }
    public int getSunCount() {
        return Integer.parseInt(sunCount.getText());
    }
    public void increaseSunCount() {
        int currScore=Integer.parseInt(sunCount.getText());
        int newScore=currScore+50;
        String newText=Integer.toString(newScore);
        sunCount.setText(newText);
        if(newScore>=50 && sunflowerCard.isDisable() && sunflowerRefreshed) {
            sunflowerCard.setOpacity(1);
            sunflowerCard.setDisable(false);
        }
        if(newScore>=100 && peaShooterCard.isDisable() && peaShooterRefreshed) {
            peaShooterCard.setOpacity(1);
            peaShooterCard.setDisable(false);
        }
    }
    public void reduceSunCount(int count) {
        int currScore=Integer.parseInt(sunCount.getText());
        int newScore=currScore-count;
        String newText=Integer.toString(newScore);
        sunCount.setText(newText);
        if(newScore<50 && !sunflowerCard.isDisable()) {
            sunflowerCard.setOpacity(0.25);
            sunflowerCard.setDisable(true);
        }
        if(newScore<100 && !peaShooterCard.isDisable()) {
            peaShooterCard.setOpacity(0.25);
            peaShooterCard.setDisable(true);
        }
    }
    public void startMovingZombies() {
//        moveZombie(z1);
//        moveZombie(z2);
//        moveZombie(z3);
//        moveZombie(z4);
        initProgressTimerValue-=3;
    }
    public void moveZombie(ImageView zombie) {
        double offsetFinal=155-zombie.getLayoutX();
        System.out.println(offsetFinal);
        int milliDuration=(int)(-offsetFinal)*100;
        initProgressTimerValue= (initProgressTimerValue>Math.round(((double)milliDuration)/1000))?initProgressTimerValue: (int) Math.round(((double) milliDuration) / 1000);
        TranslateTransition moveZombie=new TranslateTransition(Duration.millis(milliDuration),zombie);
        moveZombie.setToX(offsetFinal);
        moveZombie.setCycleCount(1);
        moveZombie.play();
        allAnims.add(moveZombie);
    }

    public void pauseEventHandler(ActionEvent e) {
        for(Animation anim:allAnims) {
            anim.pause();
        }
        gamePane.setDisable(true);
        pausePane.setVisible(true);
        pausePane.setDisable(false);
    }
    public void resumeGameEventHandler(ActionEvent e) {
        gamePane.setDisable(false);
        pausePane.setVisible(false);
        pausePane.setDisable(true);
        for(Animation anim:allAnims) {
            anim.play();
        }
    }

    public void startGeneratingSunTokens() {
        Timeline sunTimeline = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("TIMELINE STARTED");
                ImageView sun=new ImageView(sunImage);
                sun.setFitHeight(40);
                sun.setFitWidth(40);
                int x,y;
                x=150+random.nextInt(450);
                y=90+random.nextInt(300);
                sun.setLayoutX(x);
                sun.setLayoutY(0);
                sun.setVisible(true);
                sun.setDisable(false);
                gamePane.getChildren().add(sun);
                TranslateTransition movesun=new TranslateTransition(Duration.millis(y*30),sun);
                movesun.setToY(y);
                movesun.setCycleCount(1);
                movesun.play();
                allAnims.add(movesun);
                sun.setOnMouseClicked(e-> {
                    sun.setDisable(true);
                    sun.setVisible(false);
                    increaseSunCount();
                });
                Timeline removeSunTimeline=new Timeline(new KeyFrame(Duration.seconds(25), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(!sun.isDisable()) {
                            sun.setVisible(false);
                            sun.setDisable(true);
                        }
                    }
                }));
                removeSunTimeline.setCycleCount(1);
                removeSunTimeline.play();
                allAnims.add(removeSunTimeline);
            }
        }));
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        allAnims.add(sunTimeline);
    }
    public void exitGameEventHandler(ActionEvent e) throws IOException {
        Parent gameLevelRoot= FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene gameLevelScene=new Scene(gameLevelRoot);
        Stage appWindow=(Stage)((Node)e.getSource()).getScene().getWindow();
        appWindow.setScene(gameLevelScene);
        appWindow.show();
    }
}
