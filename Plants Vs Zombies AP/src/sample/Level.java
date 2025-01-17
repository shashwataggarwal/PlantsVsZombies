package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private boolean finished=false;
    private ArrayList<Boolean> plantStatus;
    private ArrayList<Integer> cost;
    private ArrayList<Integer> refreshTime;
    private ArrayList<Timeline> cardTimeline;
    private Label progressTimer;
    private ProgressBar progressBar;
    private int initialTimerValue;
    private int currentTimerValue;
    private Label sunLabel;
    private Timeline progressTimeline;
    private ArrayList<Timeline> allTimeLines;
    private Timeline checkWinTimeline;
    private ArrayList<Integer> currRefreshTimes;
    private gameLevelController gameLevelController;
    public Level(int levelNumber, Pane gamePane, ArrayList<ImageView> cards, ArrayList<Label> cardLabels, ArrayList<ImageView> lawnMowersImages, Label sunLabel, Label progressTimer, ProgressBar progressBar, gameLevelController gameLevelController) {
        this.levelNumber = levelNumber;
        this.cardLabels=cardLabels;
        this.lawnMowersImages=lawnMowersImages;
        currRefreshTimes=new ArrayList<>();
        this.sunLabel=sunLabel;
        this.gamePane=gamePane;
        this.progressTimer=progressTimer;
        this.progressBar=progressBar;
        allTimeLines=new ArrayList<Timeline>();
        initialTimerValue=0;
        this.cards=cards;
        this.gameLevelController = gameLevelController;
        cardTimeline=new ArrayList<Timeline>();
        bullets=new HashMap<Integer,ArrayList<Positionable>>();
        try {
            transparentImage=new Image(new FileInputStream("./images/transparent.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        plantStatus=new ArrayList<Boolean>();
        cost=new ArrayList<Integer>();
        refreshTime=new ArrayList<Integer>();
        sunCount=100;
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
    }


    public void startGame() {
        initLevel();
    }

    public void loadGame(LevelData levelData) {
        createGrid();
        this.sunCount=levelData.getSunCount();
        initCards();
        addDragEventHandlers();
        initSuns(levelData);
        initLoadedZombies(levelData);
        initLoadedPlants(levelData);
        initLawnMowers();
        initLoadedLawnMowers(levelData);
        initLoadedCards(levelData);
        play();
        initLoadedProgressBar(levelData);
        //progress
    }

    private void initLoadedProgressBar(LevelData levelData){
        initialTimerValue = levelData.getInitialTimerValue();
        currentTimerValue = levelData.getCurrentTimerValue();
        String st="";
        st+= currentTimerValue /60;
        st+=":";
        int ze= currentTimerValue %60;
        if(ze<10) {
            st+="0";
        }
        st+=ze;
        progressTimer.setText(st);
        progressBar.setProgress(1- currentTimerValue /(double)initialTimerValue);

        System.out.println("CURRENT VALUE " + currentTimerValue);
        progressTimeline=new Timeline(new KeyFrame(Duration.seconds(1),e-> {
            currentTimerValue--;
            if(currentTimerValue<0) {
                return;
            }
            String t="";
            t+= currentTimerValue /60;
            t+=":";
            int z= currentTimerValue %60;
            if(z<10) {
                t+="0";
            }
            t+=z;
            progressTimer.setText(t);
            progressBar.setProgress(1- currentTimerValue /(double)initialTimerValue);
        }));
        allTimeLines.add(progressTimeline);
        progressTimeline.setCycleCount(currentTimerValue);
        progressTimeline.play();
    }

    private void initLoadedCards(LevelData levelData) {
        ArrayList<Integer> refreshTimes = levelData.getRefreshTimes();
        for(int i = 0; i<refreshTimes.size();i++){
            if(refreshTimes.get(i)>0){
                ImageView card = cards.get(i);
                card.setOpacity(0.25);
                card.setDisable(true);

                cardLabels.get(i).setVisible(true);
                cardLabels.get(i).setText(Integer.toString(refreshTimes.get(i)));
                currRefreshTimes.set(i,refreshTimes.get(i));
                plantStatus.set(i,false);
                int finalI = i;
                cardTimeline.set(i,new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        currRefreshTimes.set(finalI,currRefreshTimes.get(finalI)-1);
                        cardLabels.get(finalI).setText(Integer.toString(Integer.parseInt(cardLabels.get(finalI).getText())-1));
                    }
                })));
                allTimeLines.add(cardTimeline.get(i));
                cardTimeline.get(i).setCycleCount(refreshTimes.get(i));
                int finalI1 = i;
                cardTimeline.get(i).setOnFinished(f-> {
                    plantStatus.set(finalI1,true);
                    cardLabels.get(finalI1).setVisible(false);
                    if(sunCount>=cost.get(finalI1)) {
                        cards.get(finalI1).setOpacity(1);
                        cards.get(finalI1).setDisable(false);
                    }
                });
                cardTimeline.get(i).play();
            }
        }
    }

    private void initLoadedLawnMowers(LevelData levelData) {
        ArrayList<LawnMowerData> lawnMowerData=levelData.getLawnMowers();
        for(int i=0;i<lawnMowerData.size();i++) {
            LawnMower curr=lawnMowers.get(lawnMowerData.get(i).getY());
            if(lawnMowerData.get(i).isUsed()) {
                curr.getImageView().setVisible(false);
                curr.setUsed(true);
            }
            if(lawnMowerData.get(i).getX()>150) {
                curr.getImageView().setVisible(true);
                int initialX=(int)curr.getImageView().getLayoutX();
                curr.getImageView().setLayoutX(lawnMowerData.get(i).getX());
                curr.use(gamePane,this);
                curr.setInitialX(initialX);
            }
        }
    }


    private void initLoadedPlants(LevelData levelData) {
        ArrayList<PlantData> plantData=levelData.getPlants();
        plantData.forEach(data -> {
            Plant temp=null;
            ImageView gridPos=grid.get(data.getY()).get(data.getX());
            switch (data.getType()) {
                case 0:
                    temp=new Peashooter();
                    initPeaShooter((Peashooter) temp,data.getY());
                    break;
                case 1:
                    temp=new Sunflower();
                    initSunflower((Sunflower)temp,data.getY());
                    break;
                case 2:
                    temp=new CherryBomb(zombiesPos);
                    initCherryBomb((CherryBomb)temp);
                    break;
                case 3:
                    temp=new Wallnut();
                    break;
                case 4:
                    temp=new ShooterBombSunflower(zombiesPos);
                    initSpecial((ShooterBombSunflower)temp,data.getY());
                    break;
            }
            temp.setHealth(data.getHealth());
            temp.setImageView(gridPos);
            gridPos.setImage(temp.getImage());
            gridPos.setOpacity(1);
            plants.get(data.getY()).put(data.getX(),temp);
        });
    }

    private void initLoadedZombies(LevelData levelData) {
        grid.forEach((k,v) -> {
            zombiesPos.put(k,new ArrayList<Zombie>());
        });
        ArrayList<ZombieData> zombieData=levelData.getZombies();
        zombieData.forEach(data -> {
            int type=data.getType();
//            System.out.println(type);
            genZombie(data.getX(),data.getY(),type,data.getHealth());
        });
    }

    private void initSuns(LevelData levelData) {
        ArrayList<SunData> sunData=levelData.getSuns();
        sunData.forEach(data-> {
            Sun sun=new Sun();
            ImageView sunImage=new ImageView(sun.getImage());
            sunImage.setVisible(true);
            sunImage.setDisable(false);
            sunImage.setLayoutX(data.getX());
            sunImage.setLayoutY(data.getY());
            sunImage.setFitHeight(40);
            sunImage.setFitWidth(40);
            gamePane.getChildren().add(sunImage);
            sun.setImageView(sunImage);
            initSun(sun);
            suns.add(sun);
            if(data.getFinalY()!=0) {
                sun.startMovement(0,2,data.getFinalY());
            }
            allTimeLines.add(sun.getMovementTimeline());
        });

    }

    private void play() {
        moveZombies();
        startGeneratingSunTokens();
        initCheckWin();
    }

    public void loseGame() {
        if(!finished) {
            finished=true;
        }
        gameLevelController.gameLost();
    }

    private void initPeaShooter(Peashooter peashooter,int row) {
        Timeline shootTimeline=new Timeline(new KeyFrame(Duration.seconds(peashooter.getShootingSpeed()),e-> {
            if(aliveZombieInRow(row,(int)peashooter.getImageView().getLayoutX())) {
                Peashooter.PeaBullet pea = peashooter.new PeaBullet(zombiesPos.get(row));
                ImageView peaImageView = new ImageView(pea.getImage());
                pea.setImageView(peaImageView);
                peaImageView.setFitHeight(20);
                peaImageView.setFitWidth(20);
                peaImageView.setLayoutX(peashooter.getImageView().getLayoutX()+42);
                peaImageView.setLayoutY(peashooter.getImageView().getLayoutY()+11);
                gamePane.getChildren().add(peaImageView);
                pea.startMovement(2,pea.getBulletSpeed(),720-(int)peaImageView.getLayoutX(),100,true,gamePane);
                allTimeLines.add(pea.getMovementTimeline());
                bullets.get(row).add(pea);
            }
        }));
        allTimeLines.add(shootTimeline);
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
        allTimeLines.add(sunTimeline);
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        sunflower.setSunTimeline(sunTimeline);
    }

    private void initSun(Sun x) {
        x.getImageView().setOnMouseClicked(e-> {
            x.pickUp();
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
        allTimeLines.add(sunTimeline);
        sunTimeline.setCycleCount(Timeline.INDEFINITE);
        sunTimeline.play();
        plant.setSunTimeline(sunTimeline);

        Timeline shootTimeline=new Timeline(new KeyFrame(Duration.seconds(plant.getSbsShootingSpeed()),e-> {
            if(aliveZombieInRow(row,(int)plant.getImageView().getLayoutX())) {
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
                allTimeLines.add(sbl.getMovementTimeline());
            }
        }));
        allTimeLines.add(shootTimeline);
        shootTimeline.setCycleCount(Timeline.INDEFINITE);
        shootTimeline.play();
        plant.setShootingTimeline(shootTimeline);
    }



    public void increaseSunCount() {
        sunCount+=50;
        sunLabel.setText(Integer.toString(sunCount));
        for(int i=0;i<levelNumber;i++) {
            if(cards.get(i).isDisabled() && cost.get(i)<=sunCount && plantStatus.get(i)) {
                cards.get(i).setOpacity(1);
                cards.get(i).setDisable(false);
            }
        }
    }

    private boolean aliveZombieInRow(int row,int x) {
        for(Zombie zombie:zombiesPos.get(row)) {
            if(zombie.getImageView().getLayoutX()<720 && zombie.isAlive() && zombie.getImageView().getLayoutX()>x) {
                return true;
            }
        }
        return false;
    }

    private void initLevel() {
        createGrid();
        initCards();
        initZombies();
        initLawnMowers();
        addDragEventHandlers();
        play();
        startProgress();
    }


    private boolean intersects(ImageView x,ImageView y) {
        int offset=(int)(y.getLayoutX()-x.getLayoutX());
        offset=(offset>0)?offset:-offset;
        if(offset<5) return true;
        return false;

    }



    private void startProgress() {
        System.out.println(initialTimerValue);
        String s="";
        s+=initialTimerValue/60;
        s+=":";
        int y=initialTimerValue%60;
        if(y<10) {
            s+="0";
        }
        s+=y;
        progressTimer.setText(s);
        progressBar.setProgress(0);
        currentTimerValue =initialTimerValue;
        progressTimeline=new Timeline(new KeyFrame(Duration.seconds(1),e-> {
            currentTimerValue--;
            if(currentTimerValue<0) {
                return;
            }
            String t="";
            t+= currentTimerValue /60;
            t+=":";
            int z= currentTimerValue %60;
            if(z<10) {
                t+="0";
            }
            t+=z;
            progressTimer.setText(t);
            progressBar.setProgress(1- currentTimerValue /(double)initialTimerValue);
        }));
        allTimeLines.add(progressTimeline);
        progressTimeline.setCycleCount(initialTimerValue);
        progressTimeline.play();

    }

    private void startGeneratingSunTokens() {
        Random random=new Random();
        sunGeneratingTimeline=new Timeline(new KeyFrame(Duration.seconds(7),e-> {
            Sun sun=new Sun();
            ImageView sunImage=new ImageView(sun.getImage());
            sunImage.setFitHeight(40);
            sunImage.setFitWidth(40);
            int x,y;
            x=150+random.nextInt(450);
            y=90+random.nextInt(300);
            sun.setFinalY(y);
            sunImage.setLayoutX(x);
            sunImage.setLayoutY(0);
            sunImage.setVisible(true);
            sunImage.setDisable(false);
            gamePane.getChildren().add(sunImage);
            sun.setImageView(sunImage);
            sun.startMovement(0,2,y);
            allTimeLines.add(sun.getMovementTimeline());
            suns.add(sun);
            initSun(sun);
        }));
        allTimeLines.add(sunGeneratingTimeline);
        sunGeneratingTimeline.setCycleCount(Timeline.INDEFINITE);
        sunGeneratingTimeline.play();
    }

    private void moveZombies() {
        zombiesPos.forEach((k,v)-> {
            v.forEach(zombie -> {
                zombie.startMovement(1,1,50000,transparentImage,lawnMowers.get(k),this,gamePane);
                allTimeLines.add(zombie.getMovementTimeline());
                int temp=(int)(zombie.getImageView().getLayoutX()-720)*150;
                temp=temp/1000;
                if(temp>initialTimerValue) {
                    initialTimerValue=temp;
                }
//                if(zombie.getImageView().getLayoutX()>initialTimerValue) {
//                    initialTimerValue=(int)zombie.getImageView().getLayoutX();
//                }
            });
        });
    }

    private void initLawnMowers() {
        for(int i=0;i<5;i++) {
            if(grid.containsKey(i)) {
                LawnMower temp=new LawnMower(i,zombiesPos.get(i));
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
            int numZombiesPerRow=2+levelNumber+random.nextInt(1);
            int scalingFactor=300-(levelNumber-1)*10;
            int numBoostedZombies=(levelNumber>3)?(int)(numZombiesPerRow*0.4):0;
            ArrayList<Integer> types=new ArrayList<>();
            for(int i=0;i<numBoostedZombies;i++) {
                types.add(1);
            }
            while(types.size()<numZombiesPerRow) {
                types.add(0);
            }
            Collections.shuffle(types);
            for(int i=0;i<numZombiesPerRow;i++) {
                    genZombie(800 + i * scalingFactor + random.nextInt(scalingFactor-100), k, types.get(i));
            }
        });
    }

    private void genZombie(int x,int row,int type) {
        genZombie(x,row,type,-1);
    }

    private void genZombie(int x,int row,int type,int health) {
        Zombie gen=null;
        ImageView imageView=null;
        if(type==0) {
            gen=new BasicZombie(plants.get(row));
            imageView=new ImageView(gen.getImage());
            gen.setImageView(imageView);
            imageView.setFitHeight(65);
            imageView.setFitWidth(45);
        }
        else {
            gen=new BoostedZombie(plants.get(row));
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
        if(health!=-1) {
            gen.setHealth(health);
        }
    }

    private void initCards() {
        sunLabel.setText(Integer.toString(sunCount));
        for(int i=0;i<levelNumber;i++) {
            cardTimeline.add(null);
            switch (i) {
                case 0:
                    cost.add(100);
                    plantStatus.add(true);
                    refreshTime.add(5);
                    break;
                case 1:
                    cost.add(50);
                    plantStatus.add(true);
                    refreshTime.add(5);
                    break;
                case 2:
                    cost.add(150);
                    plantStatus.add(true);
                    refreshTime.add(15);
                    break;
                case 3:
                    cost.add(50);
                    plantStatus.add(true);
                    refreshTime.add(10);
                    break;
                case 4:
                    cost.add(250);
                    plantStatus.add(true);
                    refreshTime.add(5);
                    break;
            }
        }
        for(int i=0;i<levelNumber;i++) {
            cards.get(i).setDisable(false);
            cards.get(i).setVisible(true);
            cards.get(i).setOpacity(1);
            if(cost.get(i)>sunCount) {
                cards.get(i).setOpacity(0.25);
                cards.get(i).setDisable(true);
            }
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

    public ArrayList<Timeline> getAllTimeLines() {
        return allTimeLines;
    }

    public void addLawnMowerMovement(Timeline lawnMove) {
        allTimeLines.add(lawnMove);
    }

    public void saveGame() {
        LevelData currentLevel = new LevelData(initPlantData(), initZombieData(), initBulletData(),
                initSunData(), initLawnMowerData(),currRefreshTimes,this.levelNumber,this.sunCount, this.initialTimerValue, this.currentTimerValue);
        ObjectOutputStream out = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_hh_mm_ss");
        String dateAsString = simpleDateFormat.format(new Date());

        try{
            out=new ObjectOutputStream(new FileOutputStream("./SavedGames/"+Integer.toString(levelNumber)+"_"+dateAsString+".bin"));
            out.writeObject(currentLevel);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (Exception f){
                    f.printStackTrace();
                }
            }
        }
    }

    private ArrayList<BulletData> initBulletData(){
        ArrayList<BulletData> bulletData = new ArrayList<>();
        bullets.forEach((y, v) -> {
            for (Positionable bullet : v) {
                if(bullet.getImageView().isVisible()){
                    int type=0;
                    if(bullet instanceof ShooterBombSunflower.SpecialBullet) {
                        type=1;
                    }
                    bulletData.add(new BulletData((int)bullet.getImageView().getLayoutX(), y,type));
                }
            }
        });

        return bulletData;
    }


    private ArrayList<LawnMowerData> initLawnMowerData(){
        ArrayList<LawnMowerData> lawnMowerData = new ArrayList<>();
        lawnMowers.forEach((y, lawnMower) -> {
            lawnMowerData.add(new LawnMowerData((int)lawnMower.getImageView().getLayoutX(),y,lawnMower.isUsed()));

        });
        return lawnMowerData;
    }
    private ArrayList<SunData> initSunData(){
        ArrayList<SunData> sunData = new ArrayList<>();
        suns.forEach(sun->{
            if(!sun.isPickedUp()){
                sunData.add(new SunData((int)sun.getImageView().getLayoutX(),(int)sun.getImageView().getLayoutY(), sun.getFinalY()));
            }
        });

        return sunData;
    }

    private  ArrayList<PlantData> initPlantData(){
        ArrayList<PlantData> plantData = new ArrayList<PlantData>();
        System.out.println("PLANT DATA");
        plants.forEach((y,v)->{
            v.forEach((x,plant)->{
                if(plant.isAlive()){
                    plantData.add(new PlantData(x,y,plant.getHealth(),plant.getType()));
//                    System.out.println("X "+x+" Y "+y+" "+plant.getHealth()+" "+plant.getType());
                }
            });
        });
        return  plantData;
    }

    private  ArrayList<ZombieData> initZombieData(){
        ArrayList<ZombieData> zombieData = new ArrayList<ZombieData>();
        System.out.println("ZOMBIE DATA");
        zombiesPos.forEach((y,v)->{
            v.forEach((zombie)->{
                if(zombie.isAlive()){
                    zombieData.add(new ZombieData((int)zombie.getImageView().getLayoutX(),y,zombie.getType(),zombie.getHealth()));
//                    System.out.println("X "+zombie.getImageView().getLayoutX()+" Y "+y+" "+zombie.getHealth()+" "+zombie.getType());
                }
            });
        });
        return  zombieData;
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
        for(int i=0;i<levelNumber;i++) {
            addCardDrags(cards.get(i),i);
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
                            temp=new CherryBomb(zombiesPos);
                            initCherryBomb((CherryBomb)temp);
                            break;
                        case 3:
                            temp=new Wallnut();
                            break;
                        case 4:
                            temp=new ShooterBombSunflower(zombiesPos);
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

    private void initCherryBomb(CherryBomb bomb) {
        bomb.blast(gamePane,transparentImage);
        allTimeLines.add(bomb.getBlastTimeline());
    }

    private void reduceSunCount(int amount) {
        sunCount-=amount;
        sunLabel.setText(Integer.toString(sunCount));
        for(int i=0;i<levelNumber;i++) {
            if(cost.get(i)>sunCount) {
                cards.get(i).setOpacity(0.25);
                cards.get(i).setDisable(true);
            }
        }
    }

    private void initCheckWin() {
        checkWinTimeline=new Timeline(new KeyFrame(Duration.seconds(1),e-> {
            int count=0;
            for(int row:zombiesPos.keySet()) {
                if(zombiesPos.get(row).size()!=0) {
                    count++;
                }
            }
            if(count==0) {
                winGame();
            }
        }));
        checkWinTimeline.setCycleCount(Timeline.INDEFINITE);
        checkWinTimeline.play();
        allTimeLines.add(checkWinTimeline);
    }

    private void addCardDrags(ImageView card,int index) {
        currRefreshTimes.add(0);
        card.setOnDragDetected(e -> {
            Dragboard cardDragboard=card.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(index));
            cardDragboard.setContent(content);
            e.consume();
        });
        card.setOnDragDone(e-> {
            if(e.getTransferMode()!=null) {
                card.setOpacity(0.25);
                card.setDisable(true);
                int i=cards.indexOf(card);
                cardLabels.get(i).setVisible(true);
                cardLabels.get(i).setText(Integer.toString(refreshTime.get(i)));
                currRefreshTimes.set(i,refreshTime.get(i));
                plantStatus.set(i,false);
                cardTimeline.set(i,new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        currRefreshTimes.set(i,currRefreshTimes.get(i)-1);
                        cardLabels.get(i).setText(Integer.toString(Integer.parseInt(cardLabels.get(i).getText())-1));
                    }
                })));
                allTimeLines.add(cardTimeline.get(i));
                cardTimeline.get(i).setCycleCount(refreshTime.get(i));
                cardTimeline.get(i).setOnFinished(f-> {
                    plantStatus.set(i,true);
                    cardLabels.get(i).setVisible(false);
                    if(sunCount>=cost.get(i)) {
                        cards.get(i).setOpacity(1);
                        cards.get(i).setDisable(false);
                    }
                });
                cardTimeline.get(i).play();
                reduceSunCount(cost.get(i));
            }
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

    public void winGame() {
        levelComplete=true;
        gameLevelController.gameWon();

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
