import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
public  class Position extends StackPane {
    public int x, y;
    public boolean isBomb;
    public boolean isOpen = false;
    boolean flag = false;
    public Rectangle border = new Rectangle(Main.box_SIZE - 2, Main.box_SIZE - 2);
    public Text text = new Text();

    public Position(int x, int y, boolean isBomb) {
        this.x = x;
        this.y = y;
        this.isBomb = isBomb;

        border.setStroke(Color.WHITE);

        ImageView bgIV = new ImageView(Main.bombGif2);
        bgIV.setVisible(false);

        text.setFont(Font.font("Forte", 18));
        text.setVisible(false);
        getChildren().addAll(border, text);

        if (isBomb) {
            getChildren().add(bgIV);
        }

        setTranslateX(Main.XOFFSET + Main.box_SIZE / 2 + x * Main.box_SIZE);
        setTranslateY(Main.YOFFSET + Main.box_SIZE / 2 + y * Main.box_SIZE);
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                MouseButton button = event.getButton();
                if (button == MouseButton.PRIMARY) {
                    if (Main.twoPlayers) {
                        Main.click = Main.click + 1;
                        open();
                        if (!(Main.count == count())) {
                            if ((Main.click % 2 == 1) && (Main.click > 0) && (Main.click != 1999)) {
                                Main.p1.score = count() - Main.p2.score;
                                Main.lab1.setText(String.valueOf(Main.p1.score));
                                Main.count = count();
                                Main.pl1.setFill(Color.BLACK);
                                Main.pl2.setFill(Color.GREEN);
                                System.out.println(Main.p1.name + " \t " + Main.p1.score);
                            } else if ((Main.click % 2 == 0) && (Main.click > 0) && (Main.click != 1999)) {
                                Main.p2.score = count() - Main.p1.score;
                                Main.lab2.setText(String.valueOf(Main.p2.score));
                                Main.count = count();
                                Main.pl2.setFill(Color.BLACK);
                                Main.pl1.setFill(Color.GREEN);
                                System.out.println(Main.p2.name + " \t " + Main.p2.score);
                            }

                        } else {
                            Main.click = Main.click - 1;
                        }
                    } else
                        open();

                } else if (button == MouseButton.SECONDARY) {
                    flag();
                }
            }
        });
    }



    public int count() {
        Main.count = 0;
        for (int y = 0; y < Main.Y; y++) {
            for (int x = 0; x < Main.X; x++) {
                if (Main.grid[x][y].isOpen) {
                    Main.count++;
                }
            }
        }
        return Main.count;
    }

    public void flag() {
        if (!isOpen) {
            if (flag == true) {
                if (this.isBomb) {
                    getChildren().remove(3);
                }
                else getChildren().remove(2);
            }
            if (flag == false){
                ImageView bgIV = new ImageView(Main.flagGif);
                bgIV.setVisible(true);
                getChildren().add(bgIV);
            }
            flag = !flag;
        }
    }

    public void open() {
        if (!flag) {
            if (isOpen)
                return;

            if (isBomb) {
            	
                System.out.println("Game Over");
                Main.innerTime = -10;
                if (Main.twoPlayers) {

                    if (Main.click % 2 == 1) {
                        Main.display(Main.p2.name + " Wins because " + Main.p1.name + " exploded ");
                        
                    } else {
                        Main.display( Main.p1.name + " Wins because " + Main.p2.name + " exploded ");
                      
                    }
                    Main.p1.score = 0;
                    Main.p2.score = 0;
                    Main.click = 0;
                }
                else Main.display("Oops! You lose. Another game?");
                for (int y = 0; y < Main.Y; y++) {
                    for (int x = 0; x < Main.X; x++) {
                        Position curPos = Main.grid[x][y];
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
                return;
            }

            isOpen = true;
            text.setVisible(true);
            border.setFill(null);

            if (text.getText().isEmpty()) {
                Main.getBoxesAround(this).forEach(Position::open);
            }
            boolean won = true;
            for (int y = 0; y < Main.Y; y++) {
                for (int x = 0; x < Main.X; x++) {
                    if (!Main.grid[x][y].isBomb && !Main.grid[x][y].isOpen) {
                        won = false;
                        break;
                    }
                }
            }
            if (Main.twoPlayers) {
                if (won & (Main.click != 1999)) {
                	Main.twoPlayers=false;
                    if (Main.p2.score > Main.p1.score) {
                        System.out.println(Main.p2.name + " Win with score " + (Main.p2.score + 1));
                        Main.click = 1999;
                        Main.p1.score = 0;
                        Main.p2.score = 0;

                    } else if (Main.p2.score < Main.p1.score) {
                        System.out.println(Main.p1.name + " Win with score " + (Main.p1.score + 1));
                        Main.click = 1999;
                        Main.p1.score = 0;
                        Main.p2.score = 0;
                    } else {
                        System.out.println(" Draw ");
                        Main.click = 1999;
                        Main.p1.score = 0;
                        Main.p2.score = 0;
                    }

                    Main.scene.setOnMousePressed(e -> {
                        Main.scene.setRoot(Main.newGame());
                        Main.scene.setOnMousePressed(null);

                    });
                }
            } else if (won) {
                Main.display("Congratz! You won! Another one?");
            }
        }
    }
    
}