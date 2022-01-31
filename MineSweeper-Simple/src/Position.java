public class Position {
    private int[] position = new int[2];
    private int bombsAround;
    private boolean revealed, marked, ifBomb;

    public Position(int x, int y, boolean ifbomb ){
        position[0] = x;
        position[1] = y;
        this.ifBomb = ifbomb;
    }

    public int[] getPosition() {
        return position;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isIfBomb() {
        return ifBomb;
    }

    public void setIfBomb(boolean ifBomb) {
        this.ifBomb = ifBomb;
    }
}
