
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    static final int W = 720;
    static final int H = 670;
    static int XOFFSET = 75;
    static  int YOFFSET = 80;
    static double difficulty = 0.2;



    static int X = 15;
    static int Y = 15;

    static int area = (W - 150) * (H - 100);

    static int box_SIZE = (int) Math.sqrt(area / Math.pow(Math.max(X, Y), 2));
    static int count;
    static int click;

    public static Position[][] grid = new Position[X][Y];
    public static Scene scene;
    private static Hyperlink buttonStart;
    private static Hyperlink buttonStart2;
    private static Hyperlink playerNum1;
    private static Hyperlink playerNum2;
    private static Hyperlink easy;
    private static Hyperlink medium;
    private static Hyperlink hard;
    private static Hyperlink expert;
    static Label lab1;
    static Label lab2;
    static Text timer;
    static boolean twoPlayers;
    private static int duration=-5;
    static int innerTime=-5;
    static boolean isTime;
    private static boolean wrongInputWW;
    static String font = "Mongolian Baiti";

    static Image image = new Image("file:Wall_Breaker_info.png");
    static Image loading = new Image("file:loadgif.gif", 500, 180, false, false);
    static Image bombGif = new Image("file:bomb2.gif", 60, 80, true, true);
    static Image bombGif2 = new Image("file:bomb2.gif", box_SIZE - 5, box_SIZE - 5, true, true);
    static Image background = new Image("file:background1.jpg", 720, 670, false, true);
    static BackgroundImage backgroundIv = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    static Image flagGif = new Image("file:red-flag.gif", box_SIZE - 5, box_SIZE - 5, true, true);
    static player p1;
    static player p2;
    static Text pl1;
    static Text pl2;

    private static Parent welcomeImg() {
        ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setX(120);
        iv.setY(55);

        ImageView loadiv = new ImageView();
        loadiv.setImage(loading);
        loadiv.setX(115);
        loadiv.setY(540);

        Text text = new Text(150, 50, "MINESWEEPER");
        text.setFont(Font.font("Mongolian Baiti", 60));
        text.setFill(Color.gray(0.3));

        Pane root = new Pane();
        root.setPrefSize(W, H);
        root.getChildren().add(iv);
        root.getChildren().add(text);
        root.getChildren().add(loadiv);
        return root;
    }
    public static void display(String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        Label gameOver = new Label("[GAME OVER]");
        gameOver.setFont(Font.font(font, 40));
        gameOver.setLayoutY(80);
        gameOver.setLayoutX(75);
        gameOver.setTextFill(Color.BLACK);

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font(font, 20));
        messageLabel.setLayoutY(140);
        messageLabel.setLayoutX(70);
        messageLabel.setTextFill(Color.RED);

        Hyperlink menu = new Hyperlink("[MENU]");
        menu.setFont(Font.font(font, 20));
        menu.setLayoutX(75);
        menu.setLayoutY(200);
        menu.setTextFill(Color.BLACK);
        menu.setOnMouseEntered(e -> menu.setTextFill(Color.RED));
        menu.setOnMouseExited(e -> menu.setTextFill(Color.BLACK));
        menu.setBorder(Border.EMPTY);
        menu.setOnMouseClicked(e -> {
                    window.close();
                    duration = -5;
                    isTime = false;
                    X = 15;
                    Y = 15;
                    bombGif2 = new Image("file:bomb2.gif", box_SIZE - 5, box_SIZE - 5, true, true);
                    flagGif = new Image("file:red-flag.gif", box_SIZE - 5, box_SIZE - 5, true, true);
                    XOFFSET = 75;
                    YOFFSET = 80;
                    grid = new Position[X][Y];
                    box_SIZE = (int) Math.sqrt(area / Math.pow(Math.max(X, Y), 2));
                    difficulty = 0.2;
                    twoPlayers = false;
                    scene.setRoot(welcomeWindow());
                }
        );


        Hyperlink playAgain = new Hyperlink("[PLAY AGAIN]");
        playAgain.setFont(Font.font(font, 20));
        playAgain.setLayoutY(200);
        playAgain.setLayoutX(200);
        playAgain.setTextFill(Color.BLACK);
        playAgain.setOnMouseEntered(e -> playAgain.setTextFill(Color.RED));
        playAgain.setOnMouseExited(e -> playAgain.setTextFill(Color.BLACK));
        playAgain.setBorder(Border.EMPTY);
        playAgain.setOnMouseClicked(e -> {
                    scene.setRoot(newGame());
                    innerTime = duration;
                    if (isTime) doTime();
                    window.close();
                }
        );

        Pane root = new Pane();
        root.setPrefSize(400, 300);
        root.getChildren().addAll(gameOver, messageLabel, menu, playAgain);
        root.setBackground(new Background(backgroundIv));

        Scene boxScene = new Scene(root);
        window.setScene(boxScene);
        window.show();

    }

    private static Parent welcomeWindow() {
        Pane root = new Pane();
        root.setPrefSize(W, H);

        root.setBackground(new Background(backgroundIv));

        ImageView bombIv1 = new ImageView(bombGif); // LEFT UPPER BOMB
        bombIv1.setX(15);
        bombIv1.setY(20);
        TranslateTransition trans1 = new TranslateTransition();
        trans1.setDuration(Duration.seconds(3));
        trans1.setToX(285);
        trans1.setAutoReverse(true);
        trans1.setCycleCount(2);
        trans1.setNode(bombIv1);

        TranslateTransition trans12 = new TranslateTransition();
        trans12.setDuration(Duration.seconds(3));
        trans12.setToY(255);
        trans12.setAutoReverse(true);
        trans12.setCycleCount(2);
        trans12.setNode(bombIv1);

        RotateTransition rot1 = new RotateTransition(Duration.seconds(2), bombIv1);
        rot1.setByAngle(360);
        rot1.setCycleCount(Animation.INDEFINITE);
        rot1.play();

        SequentialTransition seqTrans1 = new SequentialTransition(trans1, trans12);
        seqTrans1.setCycleCount(Animation.INDEFINITE);
        seqTrans1.play();

        ImageView bombIv2 = new ImageView(bombGif); // RIGHT DOWN BOMB
        bombIv2.setX(635);
        bombIv2.setY(570);

        TranslateTransition trans2 = new TranslateTransition();
        trans2.setDuration(Duration.seconds(3));
        trans2.setToX(-275);
        trans2.setAutoReverse(true);
        trans2.setCycleCount(2);
        trans2.setNode(bombIv2);
        trans2.play();

        TranslateTransition trans22 = new TranslateTransition();
        trans22.setDuration(Duration.seconds(3));
        trans22.setToY(-245);
        trans22.setAutoReverse(true);
        trans22.setCycleCount(2);
        trans22.setNode(bombIv2);

        RotateTransition rot2 = new RotateTransition(Duration.seconds(2), bombIv2);
        rot2.setByAngle(-360);
        rot2.setCycleCount(Animation.INDEFINITE);
        rot2.play();

        SequentialTransition seqTrans2 = new SequentialTransition(trans2, trans22);
        seqTrans2.setCycleCount(Animation.INDEFINITE);
        seqTrans2.play();

        ImageView bombIv3 = new ImageView(bombGif); // RIGHT UP BOMB
        bombIv3.setX(635);
        bombIv3.setY(20);

        TranslateTransition trans3 = new TranslateTransition();
        trans3.setDuration(Duration.seconds(3));
        trans3.setToX(-275);
        trans3.setAutoReverse(true);
        trans3.setCycleCount(2);
        trans3.setNode(bombIv3);

        TranslateTransition trans32 = new TranslateTransition();
        trans32.setDuration(Duration.seconds(3));
        trans32.setToY(255);
        trans32.setAutoReverse(true);
        trans32.setCycleCount(2);
        trans32.setNode(bombIv3);

        RotateTransition rot3 = new RotateTransition(Duration.seconds(2), bombIv3);
        rot3.setByAngle(-360);
        rot3.setCycleCount(Animation.INDEFINITE);
        rot3.play();

        SequentialTransition seqTrans3 = new SequentialTransition(trans3, trans32);
        seqTrans3.setCycleCount(Animation.INDEFINITE);
        seqTrans3.play();

        ImageView bombIv4 = new ImageView(bombGif); // LEFT DOWN BOMB
        bombIv4.setX(15);
        bombIv4.setY(570);
        TranslateTransition trans4 = new TranslateTransition();

        trans4.setDuration(Duration.seconds(3));
        trans4.setToX(285);
        trans4.setNode(bombIv4);
        trans4.setAutoReverse(true);
        trans4.setCycleCount(2);
        trans4.play();

        TranslateTransition trans42 = new TranslateTransition();
        trans42.setDuration(Duration.seconds(3));
        trans42.setToY(-245);
        trans42.setAutoReverse(true);
        trans42.setCycleCount(2);
        trans42.setNode(bombIv4);

        RotateTransition rot4 = new RotateTransition(Duration.seconds(2), bombIv4);
        rot4.setByAngle(360);
        rot4.setCycleCount(Animation.INDEFINITE);
        rot4.play();

        SequentialTransition seqTrans4 = new SequentialTransition(trans4, trans42);
        seqTrans4.setCycleCount(Animation.INDEFINITE);
        seqTrans4.play();

        buttonStart = new Hyperlink();
        Text startText = new Text("START");
        startText.setFont(Font.font("Mongolian Baiti", 40));
        buttonStart.setLayoutX(295);
        buttonStart.setLayoutY(495);
        buttonStart.setGraphic(startText);
        buttonStart.setOnMouseEntered(e -> startText.setFill(Color.RED));
        buttonStart.setOnMouseExited(e -> startText.setFill(Color.BLACK));
        buttonStart.setBorder(Border.EMPTY);

        playerNum1 = new Hyperlink();
        Text playerButton1 = new Text("[Single Player]");
        playerButton1.setFont(Font.font("Mongolian Baiti", 25));
        playerNum1.setGraphic(playerButton1);
        playerNum1.setLayoutX(110);
        playerNum1.setLayoutY(280);
        playerNum1.setBorder(Border.EMPTY);

        playerNum2 = new Hyperlink();
        Text playerButton2 = new Text("[Two Players]");
        playerButton2.setFont(Font.font("Mongolian Baiti", 25));
        playerNum2.setLayoutX(110);
        playerNum2.setLayoutY(376);
        playerNum2.setGraphic(playerButton2);
        playerNum2.setBorder(Border.EMPTY);

        playerNum1.setOnMouseClicked(e -> {
            playerButton1.setFill(Color.RED);
            playerButton2.setFill(Color.BLACK);
            twoPlayers = false;

        });

        playerNum2.setOnMouseClicked(e -> {
            playerButton1.setFill(Color.BLACK);
            playerButton2.setFill(Color.RED);
            twoPlayers = true;
        });

        Text menuText = new Text("[MINESWEEPER]");
        menuText.setFont(Font.font("Mongolian Baiti", 55));
        menuText.setLayoutX(150);
        menuText.setLayoutY(190);

        easy = new Hyperlink();
        Text easyText = new Text("[Easy]");
        easyText.setFont(Font.font("Mongolian Baiti", 25));
        easy.setLayoutX(320);
        easy.setLayoutY(260);
        easy.setGraphic(easyText);
        easy.setBorder(Border.EMPTY);

        medium = new Hyperlink();
        Text mediumText = new Text("[Medium]");
        mediumText.setFont(Font.font("Mongolian Baiti", 25));
        medium.setLayoutX(305);
        medium.setLayoutY(310);
        medium.setGraphic(mediumText);
        medium.setBorder(Border.EMPTY);

        hard = new Hyperlink();
        Text hardText = new Text("[Hard]");
        hardText.setFont(Font.font("Mongolian Baiti", 25));
        hard.setLayoutX(320);
        hard.setLayoutY(360);
        hard.setGraphic(hardText);
        hard.setBorder(Border.EMPTY);

        expert = new Hyperlink();
        Text expertText = new Text("[Expert]");
        expertText.setFont(Font.font("Mongolian Baiti", 25));
        expert.setLayoutX(313);
        expert.setLayoutY(410);
        expert.setGraphic(expertText);
        expert.setBorder(Border.EMPTY);

        easy.setOnMouseClicked(e -> {
            easyText.setFill(Color.RED);
            mediumText.setFill(Color.BLACK);
            hardText.setFill(Color.BLACK);
            expertText.setFill(Color.BLACK);
            difficulty = 0.1;
        });
        medium.setOnMouseClicked(e -> {
            easyText.setFill(Color.BLACK);
            mediumText.setFill(Color.RED);
            hardText.setFill(Color.BLACK);
            expertText.setFill(Color.BLACK);
            difficulty = 0.2;
        });
        hard.setOnMouseClicked(e -> {
            easyText.setFill(Color.BLACK);
            mediumText.setFill(Color.BLACK);
            hardText.setFill(Color.RED);
            expertText.setFill(Color.BLACK);
            difficulty = 0.3;
        });
        expert.setOnMouseClicked(e -> {
            easyText.setFill(Color.BLACK);
            mediumText.setFill(Color.BLACK);
            hardText.setFill(Color.BLACK);
            expertText.setFill(Color.RED);
            difficulty = 0.4;
        });

        Text gridText = new Text("[Grid (Optional)]");
        gridText.setFont(Font.font("Mongolian Baiti", 25));
        gridText.setX(453);
        gridText.setY(296);

        Text timerText = new Text("[Timer (Optional]");
        timerText.setFont(Font.font("Mongolian Baiti", 25));
        timerText.setX(450);
        timerText.setY(393);

        TextField gridFieldX = new TextField();
        gridFieldX.setLayoutX(475);
        gridFieldX.setLayoutY(308);
        gridFieldX.setPrefSize(40, 20);

        TextField gridFieldY = new TextField();
        gridFieldY.setLayoutX(555);
        gridFieldY.setLayoutY(308);
        gridFieldY.setPrefSize(40, 20);

        TextField timeField = new TextField();
        timeField.setLayoutX(467);
        timeField.setLayoutY(409);

        buttonStart.setOnAction(e -> {
            try {
                if (gridFieldX.getText().isEmpty() == false && gridFieldY.getText().isEmpty() == true
                        || gridFieldX.getText().isEmpty() == true && gridFieldY.getText().isEmpty() == false) {
                    throw new NumberFormatException();
                } else {
                    if (gridFieldX.getText().isEmpty() == false && gridFieldY.getText().isEmpty() == false) {
                        X = Integer.parseInt(gridFieldX.getText());
                        Y = Integer.parseInt(gridFieldY.getText());
                        if (X < 1 || Y < 1){
                            throw new NumberFormatException();
                        }
                        else {
                            box_SIZE = (int) Math.sqrt(area / Math.pow(Math.max(X, Y), 2));
                            XOFFSET = (X < Y) ? (75 + (Y - X) * box_SIZE / 2) : 75;
                            YOFFSET = (Y < X) ? (60 + (X - Y) * box_SIZE / 2) : 80;
                            grid = new Position[X][Y];
                            bombGif2 = new Image("file:bomb2.gif", box_SIZE - 5, box_SIZE - 5, true, true);
                            flagGif = new Image("file:red-flag.gif", box_SIZE - 5, box_SIZE - 5, true, true);
                        }
                    }
                }
                if (!timeField.getText().isEmpty())
                    if(Integer.parseInt(timeField.getText()) < 1) throw new NumberFormatException();
                    else{
                        duration = Integer.parseInt(timeField.getText());
                        innerTime = duration;
                        isTime = true;
                    }


                if (twoPlayers == true) {
                    scene.setRoot(namePlayers());
                }
                else {
                    scene.setRoot(newGame());
                    if(isTime) doTime();
                }
            } catch (NumberFormatException exception) {
                scene.setRoot(welcomeWindow());
            }

        });

        root.getChildren().add(buttonStart);
        root.getChildren().addAll(playerNum1, playerNum2, bombIv1, bombIv2, bombIv3, bombIv4, menuText, easy, medium,
                hard, expert, gridText, timerText, gridFieldX, gridFieldY, timeField);
        return root;
    }

    private static Parent namePlayers() {
        Pane root = new Pane();
        root.setPrefSize(W, H);
        root.setBackground(new Background(backgroundIv));

        Text menuText = new Text("[MINESWEEPER]");
        menuText.setFont(Font.font("Mongolian Baiti", 55));
        menuText.setLayoutX(150);
        menuText.setLayoutY(190);

        Text player1 = new Text("[Player 1:]");
        player1.setFont(Font.font("Mongolian Baiti", 40));
        player1.setLayoutX(145);
        player1.setLayoutY(280);

        Text player2 = new Text("[Player 2:]");
        player2.setFont(Font.font("Mongolian Baiti", 40));
        player2.setLayoutX(145);
        player2.setLayoutY(380);

        TextField pl1Field = new TextField();
        pl1Field.setLayoutX(340);
        pl1Field.setLayoutY(255);
        pl1Field.setPrefSize(200, 30);

        TextField pl2Field = new TextField();
        pl2Field.setLayoutX(340);
        pl2Field.setLayoutY(355);
        pl2Field.setPrefSize(200, 30);

        buttonStart2 = new Hyperlink();
        Text startText = new Text("START");
        startText.setFont(Font.font("Mongolian Baiti", 40));
        buttonStart2.setLayoutX(295);
        buttonStart2.setLayoutY(495);
        buttonStart2.setGraphic(startText);
        buttonStart2.setOnMouseEntered(e -> startText.setFill(Color.RED));
        buttonStart2.setOnMouseExited(e -> startText.setFill(Color.BLACK));
        buttonStart2.setBorder(Border.EMPTY);

        buttonStart2.setOnMouseClicked(e -> {
            if (!pl1Field.getText().isEmpty() && !pl2Field.getText().isEmpty()) {
                p1 = new Main(). new player(pl1Field.getText());
                p2 = new Main(). new player(pl2Field.getText());
                scene.setRoot(newGame());
                if (isTime) doTime();
            } else {
                scene.setRoot(namePlayers());
                wrongInputWW = true;
            }
        });

        root.getChildren().addAll(menuText, player1, player2, pl1Field, pl2Field, buttonStart2);

        return root;
    }

    static Parent newGame() {
        BorderPane root = new BorderPane();
        root.setPrefSize(W, H);
        root.setBackground(new Background(backgroundIv));
        root.setTop(topDisplay());

        for (int i = 0; i < Y; i++) {
            for (int j = 0; j < X; j++) {
                Position box = new Position(j, i, Math.random() < difficulty);

                grid[j][i] = box;
                root.getChildren().add(box);
            }
        }

        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                Position box = grid[x][y];
                if (box.isBomb)
                    continue;

                int bombs = 0;
                for (Position pos : getBoxesAround(box))
                {
                    if(pos.isBomb) bombs++;
                }
                if (bombs > 0)
                    box.text.setText(String.valueOf(bombs));
            }
        }

        return root;
    }

    private static Parent topDisplay() {
        Pane root = new Pane();


        timer = new Text("00:00");
        timer.setFont(Font.font("Forte", 60));
        timer.setLayoutY(60);
        timer.setLayoutX(280);

        if (twoPlayers) {
            pl1 = new Text(p1.getName() + ":");
            pl1.setFont(Font.font("Forte", 25));
            pl1.setLayoutY(45);
            pl1.setLayoutX(75);
            pl1.setFill(Color.GREEN);

            pl2 = new Text(p2.getName() + ":");
            pl2.setFont(Font.font("Forte", 25));
            pl2.setLayoutY(45);
            pl2.setLayoutX(595 - 15 * p2.getName().length());

            lab1 = new Label();
            lab1.setLayoutX(100 + 15 * p1.getName().length());
            lab1.setLayoutY(23);
            lab1.setFont(Font.font("Forte", 25));
            lab1.setTextFill(Color.RED);
            lab1.setText("0");

            lab2 = new Label();
            lab2.setLayoutX(625);
            lab2.setLayoutY(23);
            lab2.setFont(Font.font("Forte", 25));
            lab2.setTextFill(Color.RED);
            lab2.setText("0");
            root.getChildren().addAll(pl1, timer, pl2, lab1, lab2);
        } else
            root.getChildren().addAll(timer);

        return root;
    }


    static List<Position> getBoxesAround(Position box) {
        List<Position> BoxesAround = new ArrayList<>();
        int[] increment = new int[] { -1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1 };

        for (int i = 0; i < increment.length; i=i+2) {
            int newX = box.x + increment[i];
            int newY = box.y + increment[i+1];
            if ((newY >= 0 && newY < Y )&&(newX >= 0 && newX < X )) {
                BoxesAround.add(grid[newX][newY]);
            }
        }

        return BoxesAround;
    }
    public class player {

        int score;
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public player(String name) {
            this.name = name;
        }
    }


    public static void doTime() {
        Timeline time = new Timeline();

        KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (innerTime > 0) {
                    String minutes = (innerTime/60 < 10) ? ("0" + innerTime/60 + ":") : (innerTime/60 + ":");
                    String seconds = (innerTime%60 < 10) ? ("0" + innerTime%60) : (String.valueOf(innerTime%60));
                    timer.setText(minutes + seconds);
                    innerTime--;
                    System.out.println("Countdown: " + innerTime);
                }
                else if(innerTime == 0) {
                    time.stop();
                    if(twoPlayers){
                        if (p2.score > p1.score) {
                            display(p2.name + " Win with score " + (p2.score + 1));
                            click = 1999;
                            p1.score = 0;
                            p2.score = 0;

                        } else if (p2.score < p1.score) {
                            display(p1.name + " Win with score " + (p1.score + 1));
                            click = 1999;
                            p1.score = 0;
                            p2.score = 0;
                        } else {
                            display("Draw! Friendship wins. Another one?");
                            click = 1999;
                            p1.score = 0;
                            p2.score = 0;
                        }
                    }
                    else display("Oops! Time is up! You lose. Another game?");

                    for (int y = 0; y < Y; y++) {
                        for (int x = 0; x < X; x++) {
                            Position curPos = grid[x][y];
                            if(curPos.flag){
                                if (curPos.isBomb){
                                    curPos.getChildren().remove(3);
                                }
                                else {
                                    curPos.getChildren().remove(2);
                                }
                            }
                            curPos.text.setVisible(true);
                            curPos.border.setFill(null);
                            curPos.isOpen = true;
                            if(curPos.isBomb){
                                curPos.getChildren().get(2).setVisible(true);
                            }
                        }
                    }
                }
                else time.stop();
            }
        });
        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(frame);
        if (time != null) {
            time.stop();
        }
        time.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(welcomeImg());

        stage.setScene(scene);
        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.setRoot(welcomeWindow());
            }
        }, 1000);
    }

    public static void main(String[] args) {
        launch(args);
    }
}