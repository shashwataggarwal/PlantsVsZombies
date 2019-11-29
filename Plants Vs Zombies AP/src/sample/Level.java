package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Level implements Serializable {

    private final int  levelNumber;
    private int stripCount;
    private int sunCount;
    private Pane gamePane;
    private HashMap<Integer,ArrayList<ImageView>> grid;
    private ArrayList<Zombie> zombies;
    private ArrayList<String> availablePlants;
    private boolean levelComplete;
    private final int SUN_TIMER = 10;
    private ArrayList<ImageView> cards;
    private ArrayList<Label> cardLabels;
    private ArrayList<ImageView> lawnMowersImages;
    private HashMap<Integer,LawnMower> lawnMowers;
    private final int finalWaveTime=30;
    private final HashMap<Integer,ArrayList<Zombie>> zombiesPos;
    private final HashMap<Integer,HashMap<Integer,Plant>> plants;
    private HashSet<Sun> suns;
    private int c=0;
    private Timeline sunGeneratingTimeline;
    private Image transparentImage;
    private HashMap<Integer,ArrayList<Positionable>> bullets;
    private Timeline collisionDetection;
    public Level(int levelNumber, Pane gamePane, ArrayList<ImageView> cards, ArrayList<Label> cardLabels, ArrayList<ImageView> lawnMowersImages) {
        this.levelNumber = levelNumber;
        this.cardLabels=cardLabels;
        this.lawnMowersImages=lawnMowersImages;
        this.gamePane=gamePane;
        this.cards=cards;
        bullets=new HashMap<Integer,ArrayList<Positionable>>();
        try {
            transparentImage=new Image(new FileInputStream("./images/transparent.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sunCount=0;
        suns=new HashSet<Sun>();
        lawnMowers=new HashMap<Integer, LawnMower>();
        zombiesPos=new HashMap<Integer, ArrayList<Zombie>>();
        plants=new HashMap<Integer, HashMap<Integer, Plant>>();
        switch (levelNumber) {
            case 1:
                this.stripCount=1;
                break;
            case 2:
            case 3:
                this.stripCount=3;
                break;
            case 4:
            case 5:
                this.stripCount=5;
                break;
        }
        grid= new HashMap<>();
        initLevel();
    }

    private void initPeaShooter(Peashooter peashooter,int row) {
        Timeline shootTimeline=new Timeline(new KeyFrame(Duration.seconds(peashooter.getShootingSpeed()),e-> {
            if(aliveZombieInRow(row)) {
                Peashooter.PeaBullet pea = peashooter.new PeaBullet(zombiesPos.get(row));
                ImageView peaImageView = new ImageView(pea.getImage());
                pea.setImageView(peaImageView);
                peaImageView.setFitHeight(20);
                peaImageView.setFitWidth(20);
                peaImageView.setLayoutX(peashooter.getImageView().getLayoutX()+42);
                peaImageView.setLayoutY(peashooter.getImageView().getLayoutY()+11);
                gamePane.getChildren().add(peaImageView);
                pea.startMovement(2,pea.getBulletSpeed(),720-(int)peaImageView.getLayoutX(),100,true,gamePane);
                bullets.get(row).add(pea);
            }
        }));
        shootTimeline.setCycleCount(Timeline.INDEFINITE);
        shootTimeline.play();
        peashooter.setShootTimeline(shootTimeline);
    }

    private void initSunflower(Sunflower sunflower,int row) {
        Timeline sunTimeline=new Timeline(new KeyFrame(Duration.seconds(sunflower.getProductionTime()),e-> {
            Sun dropSun=new Sun();
            ImageView sun=new ImageView(dropSun.getImage());
            sun.setFitWidth(40);
            sun.setFitHeight(40);
            sun.setLayoutX(sunflower.getImageView().getLayoutX()-10);
            sun.setLayoutY(sunflower.getImageView().getLayoutY()+10);
            gamePane.getChildren().add(sun);
            dropSun.setImageView(sun);
            suns.add(dropSun);
            initSun(dropSun);
        }));
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        sunflower.setSunTimeline(sunTimeline);
    }

    private void initSun(Sun x) {
        x.getImageView().setOnMouseClicked(e-> {
            increaseSunCount();
            gamePane.getChildren().remove(x.getImageView());
        });
    }

    private void initSpecial(ShooterBombSunflower plant,int row) {
        Timeline sunTimeline=new Timeline(new KeyFrame(Duration.seconds(plant.getSblProductionTime()),e-> {
            Sun dropSun=new Sun();
            ImageView sun=new ImageView(dropSun.getImage());
            sun.setFitWidth(40);
            sun.setFitHeight(40);
            sun.setLayoutX(plant.getImageView().getLayoutX()-10);
            sun.setLayoutY(plant.getImageView().getLayoutY()+10);
            gamePane.getChildren().add(sun);
            dropSun.setImageView(sun);
            suns.add(dropSun);
            initSun(dropSun);
        }));
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        plant.setSunTimeline(sunTimeline);

        Timeline shootTimeline=new Timeline(new KeyFrame(Duration.seconds(plant.getSbsShootingSpeed()),e-> {
            if(aliveZombieInRow(row)) {
                ShooterBombSunflower.SpecialBullet sbl = plant.new SpecialBullet(zombiesPos.get(row));
                ImageView peaImageView = new ImageView(sbl.getImage());
                sbl.setImageView(peaImageView);
                peaImageView.setFitHeight(20);
                peaImageView.setFitWidth(20);
                peaImageView.setLayoutX(plant.getImageView().getLayoutX()+42);
                peaImageView.setLayoutY(plant.getImageView().getLayoutY()+11);
                gamePane.getChildren().add(peaImageView);
                sbl.startMovement(2,sbl.getBulletSpeed(),720-(int)peaImageView.getLayoutX(),100,true,gamePane);
                bullets.get(row).add(sbl);
            }
        }));
        shootTimeline.setCycleCount(Timeline.INDEFINITE);
        shootTimeline.play();
        plant.setShootingTimeline(shootTimeline);
    }



    public void increaseSunCount() {

    }

    private boolean aliveZombieInRow(int row) {
        for(Zombie zombie:zombiesPos.get(row)) {
            if(zombie.getImageView().getLayoutX()<720 && zombie.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private void initLevel() {
        createGrid();
        initCards();
        initLawnMowers();
        initZombies();
        addDragEventHandlers();
//        initCollisions();
        play();
    }

//    private void initCollisions() {
//        collisionDetection=new Timeline(new KeyFrame(Duration.millis(20),e-> {
//            for(Integer row:grid.keySet()) {
//                ArrayList<Positionable> bulletRow=bullets.get(row);
//                for(int i=bulletRow.size()-1;i>=0;i--) {
//                    if(bulletRow.get(i).getImageView().isDisabled()) {
//                        bulletRow.remove(i);
//                        continue;
//                    }
//                    ArrayList<Zombie> zombieRow=zombiesPos.get(i);
//                    for(int j=zombieRow.size()-1;j>=0;j--) {
////                        System.out.println("YO");
//                        if(intersects(bulletRow.get(i).getImageView(),zombieRow.get(j).getImageView())) {
//                            System.out.println(bulletRow.get(i).getImageView().getLayoutBounds().getMaxX());
//                            System.out.println(zombieRow.get(j).getImageView().getLayoutBounds().getMinX());
////                            System.out.println("YOLO");
//                            System.out.println(bulletRow.get(i).getImageView().getLayoutX());
//                            System.out.println(zombieRow.get(j).getImageView().getLayoutX());
//                            gamePane.getChildren().remove(bulletRow.get(i).getImageView());
//                            bulletRow.get(i).getImageView().setDisable(true);
//                            bulletRow.remove(i);
//                            zombieRow.get(j).reduceHealth();
//                            if(!zombieRow.get(i).isAlive()) {
//                                gamePane.getChildren().remove(zombieRow.get(j).getImageView());
//                                zombieRow.remove(j);
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        }));
//        collisionDetection.setCycleCount(Timeline.INDEFINITE);
//        collisionDetection.play();
//    }

    private boolean intersects(ImageView x,ImageView y) {
        int offset=(int)(y.getLayoutX()-x.getLayoutX());
        offset=(offset>0)?offset:-offset;
        if(offset<5) return true;
        return false;

    }

    private void play() {
        moveZombies();
        startGeneratingSunTokens();
    }

    private void startGeneratingSunTokens() {
        Random random=new Random();
        sunGeneratingTimeline=new Timeline(new KeyFrame(Duration.seconds(10),e-> {
            Sun sun=new Sun();
            ImageView sunImage=new ImageView(sun.getImage());
            sunImage.setFitHeight(40);
            sunImage.setFitWidth(40);
            int x,y;
            x=150+random.nextInt(450);
            y=90+random.nextInt(300);
            sunImage.setLayoutX(x);
            sunImage.setLayoutY(0);
            sunImage.setVisible(true);
            sunImage.setDisable(false);
            gamePane.getChildren().add(sunImage);
            sun.setImageView(sunImage);
            sun.startMovement(0,2,y);
            suns.add(sun);
            initSun(sun);
        }));
        sunGeneratingTimeline.setCycleCount(Timeline.INDEFINITE);
        sunGeneratingTimeline.play();
    }

    private void moveZombies() {
        zombiesPos.forEach((k,v)-> {
            v.forEach(zombie -> {
                zombie.startMovement(1,2,50000);
            });
        });
    }

    private void initLawnMowers() {
        for(int i=0;i<5;i++) {
            if(grid.containsKey(i)) {
                LawnMower temp=new LawnMower(i);
                temp.setImageView(lawnMowersImages.get(i));
                lawnMowersImages.get(i).setDisable(false);
                lawnMowersImages.get(i).setVisible(true);
                lawnMowers.put(i,temp);
            }
            else {
                lawnMowersImages.get(i).setDisable(true);
                lawnMowersImages.get(i).setVisible(false);
            }
        }
    }

    private void initZombies() {
        Random random=new Random();
        grid.forEach((k,v) -> {
            zombiesPos.put(k,new ArrayList<Zombie>());
            int numZombiesPerRow=2+2*levelNumber+random.nextInt((int)(levelNumber*1.5));
            int scalingFactor=200-(levelNumber-1)*10;
            int numBoostedZombies=(levelNumber>3)?(int)(numZombiesPerRow*0.4):0;
            for(int i=0;i<numZombiesPerRow;i++) {
                    int type=0;
                    if(numBoostedZombies>0) {
                        type=1;
                        numBoostedZombies--;
                    }
                    genZombie(800 + i * scalingFactor + random.nextInt(scalingFactor), k, type);
            }
        });
    }

    private void genZombie(int x,int row,int type) {
        Zombie gen=null;
        ImageView imageView=null;
        if(type==0) {
            gen=new BasicZombie();
            imageView=new ImageView(gen.getImage());
            gen.setImageView(imageView);
            imageView.setFitHeight(65);
            imageView.setFitWidth(45);
        }
        else {
            gen=new BoostedZombie();
            imageView=new ImageView(gen.getImage());
            gen.setImageView(imageView);
            imageView.setFitHeight(65);
            imageView.setFitWidth(57);
        }
        imageView.setLayoutX(x);
        imageView.setLayoutY(79+76*row);
        imageView.setVisible(true);
        imageView.setDisable(false);
        zombiesPos.get(row).add(gen);
        gamePane.getChildren().add(imageView);
    }

    private void initCards() {
        for(int i=0;i<levelNumber;i++) {
            cards.get(i).setDisable(false);
            cards.get(i).setVisible(true);
            cardLabels.get(i).setDisable(false);
            cardLabels.get(i).setVisible(false);
        }
        for(int i=levelNumber;i<cards.size();i++) {
            cards.get(i).setDisable(true);
            cards.get(i).setVisible(false);
            cardLabels.get(i).setDisable(true);
            cardLabels.get(i).setVisible(false);
        }
    }

    private void createRow(int y) {
        int y_cord=79+76*y;
        ArrayList<ImageView> temp=new ArrayList<ImageView>();
        bullets.put(y,new ArrayList<Positionable>());
        for(int i=0;i<8;i++) {
            ImageView imageView=new ImageView();
            imageView.setFitWidth(47);
            imageView.setFitHeight(60);
            imageView.setLayoutY(y_cord);
            int x_cord=220+i*56;
            imageView.setLayoutX(x_cord);
            gamePane.getChildren().add(imageView);
            temp.add(imageView);
        }
        grid.put(y,temp);
    }

    private void createGrid() {
        for(int i=0;i<stripCount;i++) {
            switch (i) {
                case 0:
                    createRow(2);
                    plants.put(2,new HashMap<Integer, Plant>());
                    break;
                case 1:
                    createRow(3);
                    plants.put(3,new HashMap<Integer, Plant>());
                    break;
                case 2:
                    createRow(1);
                    plants.put(1,new HashMap<Integer, Plant>());
                    break;
                case 3:
                    createRow(0);
                    plants.put(0,new HashMap<Integer, Plant>());
                    break;
                case 4:
                    createRow(4);
                    plants.put(4,new HashMap<Integer, Plant>());
                    break;
            }
        }
    }

    public void addDragEventHandlers() {
        for(int i=0;i<cards.size();i++) {
            if(!cards.get(i).isDisable()) {
                addCardDrags(cards.get(i),i);
            }
        }
        grid.forEach((k,v)->{
            v.forEach(gridPos-> {
//                System.out.println(gridPos.getViewOrder());
                gridPos.setImage(transparentImage);
                gridPos.setOpacity(0);
                gridPos.setOnDragOver(e -> {
                    if(gridPos.getImage()==transparentImage && cards.contains(e.getGestureSource())) {
                        e.acceptTransferModes(TransferMode.ANY);
                    }
                    e.consume();
                });
                gridPos.setOnDragDropped(e -> {
                    Plant temp=null;
                    switch (cards.indexOf(e.getGestureSource())) {
                        case 0:
                            temp=new Peashooter();
                            initPeaShooter((Peashooter) temp,k);
                            break;
                        case 1:
                            temp=new Sunflower();
                            initSunflower((Sunflower)temp,k);
                            break;
                        case 2:
                            temp=new CherryBomb();
                            break;
                        case 3:
                            temp=new Wallnut();
                            break;
                        case 4:
                            temp=new ShooterBombSunflower();
                            initSpecial((ShooterBombSunflower)temp,k);
                            break;
                    }
                    temp.setImageView(gridPos);
                    gridPos.setImage(temp.getImage());
                    gridPos.setOpacity(1);
                    plants.get(k).put(v.indexOf(gridPos),temp);
                    e.setDropCompleted(true);
                    e.consume();
                });
            });
        });

    }

    private void addCardDrags(ImageView card,int index) {
        card.setOnDragDetected(e -> {
            Dragboard cardDragboard=card.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(index));
            cardDragboard.setContent(content);
            e.consume();
        });
//        card.setOnDragDone(e -> {
//            if(e.getTransferMode()!=null) {
//                card.setOpacity(0.25);
//                card.setDisable(true);
//                if(card.equals(peaShooterCard)) {
//                    peashooterTimer.setVisible(true);
//                    peashooterTimer.setText("5");
//                    peaShooterRefreshed=false;
//                    Timeline peaShooterRefresh = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent actionEvent) {
//                            peashooterTimer.setText(Integer.toString(Integer.parseInt(peashooterTimer.getText())-1));
//                        }
//                    }));
//                    peaShooterRefresh.setOnFinished(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent actionEvent) {
//                            peaShooterRefreshed = true;
//                            peashooterTimer.setVisible(false);
//                            if (getSunCount() >= 100) {
//                                peaShooterCard.setOpacity(1);
//                                peaShooterCard.setDisable(false);
//                            }
//                        }
//                    });
//                    peaShooterRefresh.setCycleCount(5);
//                    peaShooterRefresh.play();
//                    allAnims.add(peaShooterRefresh);
//                    reduceSunCount(100);
//                }
//                else {
//                    sunflowerRefreshed=false;
//                    sunflowerTimer.setVisible(true);
//                    sunflowerTimer.setText("5");
//                    Timeline sunflowerRefresh = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent actionEvent) {
//                            sunflowerTimer.setText(Integer.toString(Integer.parseInt(sunflowerTimer.getText())-1));
//                        }
//                    }));
//                    sunflowerRefresh.setOnFinished(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent actionEvent) {
//                            sunflowerRefreshed = true;
//                            sunflowerTimer.setVisible(false);
//                            if (getSunCount() >= 50) {
//                                sunflowerCard.setOpacity(1);
//                                sunflowerCard.setDisable(false);
//                            }
//                        }
//                    });
//                    sunflowerRefresh.setCycleCount(5);
//                    sunflowerRefresh.play();
//                    allAnims.add(sunflowerRefresh);
//                    reduceSunCount(50);
//                }
//            }
//            e.consume();
//        });
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void buyPlant(String type){

    }



    public boolean gameWon(){
        return levelComplete;
    }

    public void pause(){

    }

    public void resume(){

    }

    public void generateSun(){

    }

    public void pickupSun(){

    }

    public int getSunCount() {
        return sunCount;
    }
}
