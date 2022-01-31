import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static final int X = 9;
    public static final int Y = 9;
    public static Position[][] grid = new Position[X][Y]; //Array grid contains position elements // The main data structure
    public static void main(String[] args) {
        //Loop to create an array of positions
        for(int i = 0; i < X; i++){
            for(int j = 0; j < Y; j++){
                grid[i][j] = new Position(i,j, Math.random()<0.159);
            }
        }
        //Loop to update the variable bombsAround of the neighbouring positions to the bomb
        for(int i = 0; i < X; i++){
            for(int j = 0; j < Y; j++){
                if(grid[i][j].isIfBomb()){
                updateSides(i, j);  //the method is defined below
                }
            }
        }

        //GAME STARTS HERE
        while(true){
            //Printing the grid
            for(int i = 0; i < X; i++) {
                for(int j = 0; j < Y; j++){
                    if(grid[i][j].isRevealed()){
                        System.out.print(grid[i][j].getBombsAround() + "\t");
                    }
                    else{
                        System.out.print("\u25A1" + "\t");
                    }
                }
                System.out.print("\n");
            }
            //Taking inputs
            System.out.print("Please enter the position [1-9; 1-9]");
            Scanner input = new Scanner(System.in);
            try {
                int posX = input.nextInt() - 1;
                int posY = input.nextInt() - 1;
                //If bomb, lose
                if (grid[posX][posY].isIfBomb()) {
                    System.out.println("You lose!");
                    //Print and reveal everything show bombs
                    for (int i = 0; i < X; i++) {
                        for (int j = 0; j < Y; j++) {
                            if (grid[i][j].isIfBomb()) {
                                System.out.print("\uD83D\uDCA3" + "\t");
                            } else
                                System.out.print(grid[i][j].getBombsAround() + "\t");
                        }
                        System.out.print("\n");
                    }
                    break;
                }
                //If no bomb, reveal the position
                else {
                    grid[posX][posY].setRevealed(true);
                    //if no bombs around the position reveal the neighbours
                    if (grid[posX][posY].getBombsAround() == 0) {
                        revealAdjacent(posX, posY);
                    }
                }
            }catch (Exception exception){
                System.out.println("Invalid position, please try again!");
            }
            //Checking for winning condition
            boolean won = true;
            for(int i = 0; i < X; i++){
                for(Position box : grid[i]) {
                    //if there are "no bomb non revelaed" positions, won is false
                    if (!box.isIfBomb() && !box.isRevealed()) {
                        won = false;
                        break;
                    }
                }
            }
            //if the condition is satisfied, end the game
            if(won){
                System.out.println("You won!");
                break;
            }
        }
    }

    // method to increment bombaround number of ajdacent positions
    static public void updateSides(int x, int y){
        ArrayList<Position> neighbours = getNeighbours(x, y); //Assignging the neighbours to a new variable
        for (int i = 0; i < neighbours.size(); i++) {
            neighbours.get(i).setBombsAround(neighbours.get(i).getBombsAround() + 1);
        }
    }

    //method reveals adjacent positions when the clicked position has no bombs around
    static public void revealAdjacent(int x, int y) { // method to increment bombaround number of ajdacent grid
        grid[x][y].setRevealed(true);
        ArrayList<Position> neighbours = getNeighbours(x, y); //Assignging the neighbours to a new variable
        for (int i = 0; i < neighbours.size(); i++) {
                if (neighbours.get(i).getBombsAround() == 0 && !neighbours.get(i).isRevealed()) {
                    revealAdjacent(neighbours.get(i).getPosition()[0], neighbours.get(i).getPosition()[1]);
                }
                neighbours.get(i).setRevealed(true);
        }
    }

    //method returns the neighbouring positions in an arraylist
    static public ArrayList<Position> getNeighbours(int x, int y) {
        ArrayList<Position> ListOfNeighbours = new ArrayList<>();
        //defines neighbour positions when added by pair
        int[] points = new int[]{
                -1, -1,
                -1,  0,
                -1,  1,
                 0, -1,
                 0,  1,
                 1, -1,
                 1,  0,
                 1,  1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int neiX = x + dx;
            int neiY = y + dy;

            if (neiX >= 0 && neiX < X && neiY >= 0 && neiY < Y) {
                ListOfNeighbours.add(grid[neiX][neiY]);
            }
        }
        return ListOfNeighbours;
    }
}

