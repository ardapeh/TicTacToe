import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * This class represents a generic TicTacToe game board.
 */
public class Board {
    char[][] tahta;
    int free;
    int o;
    int x;
    private int n;

    /**
     * Creates Board object, am game board of size n * n with 1<=n<=10.
     */
    public Board(int n) {
        // TODO
        if (n < 1 || n > 10)
            throw new InputMismatchException("A board of size " + n + "x" + n + " can not be created!");
        this.n = n;
        this.tahta = new char[n][n];
        this.free = n * n;
        this.o = 0;
        this.x = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tahta[i][j] = '-';
            }
        }
    }

    //Board constructor for test in case the board is given as an int array
    public Board(int[][] board) {
        this.n = board.length;
        this.tahta = new char[this.n][this.n];
        this.free = 0;
        this.o = 0;
        this.x = 0;


        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 1) {
                    this.tahta[i][j] = 'x';
                    this.x++;
                } else if (board[i][j] == -1) {
                    this.tahta[i][j] = 'o';
                    this.o++;
                } else if (board[i][j] == 0) {
                    this.tahta[i][j] = '-';
                    this.free++;
                }
            }

        }

    }

    public static void main(String[] args) {
        Board oyun = new Board(5);
        Position xd1 = new Position(0, 0);
        Position xd2 = new Position(2, 0);
        Position xd3 = new Position(0, 1);
        Position xd4 = new Position(1, 1);
        Position xd5 = new Position(0, 2);
        Position xd6 = new Position(2, 2);
        Position xd7 = new Position(1, 0);
        oyun.doMove(xd1, -1);
        oyun.doMove(xd2, 1);
        oyun.doMove(xd3, -1);
        oyun.doMove(xd4, 1);
        oyun.doMove(xd5, -1);
        oyun.doMove(xd6, 1);

        oyun.print();


        System.out.println("Free: " + oyun.nFreeFields());
        System.out.println("O: " + oyun.o);
        System.out.println("X: " + oyun.x);


        System.out.println("==============");
        oyun.undoMove(xd1);
        oyun.undoMove(xd2);
        oyun.undoMove(xd4);
        oyun.undoMove(xd5);
        oyun.undoMove(xd7);
        oyun.print();

        System.out.println("Free: " + oyun.nFreeFields());
        System.out.println("O: " + oyun.o);
        System.out.println("X: " + oyun.x);


    }

    /**
     * @return length/width of the Board object
     */
    public int getN() {
        return n;
    }

    /**
     * @return number of currently free fields
     */
    public int nFreeFields() {
        // TODO

        return this.free;
    }

    /**
     * @return token at position pos
     */
    public int getField(Position pos) throws InputMismatchException {
        // TODO
        if (pos.x >= this.n || pos.y >= this.n || pos.x < 0 || pos.y < 0)
            throw new InputMismatchException("The entered Position is not valid!");

        if (tahta[pos.y][pos.x] == 'x')
            return 1;
        else if (tahta[pos.y][pos.x] == 'o')
            return -1;
        else if (tahta[pos.y][pos.x] == '-')
            return 0;

        throw new InputMismatchException("The entered Position is not valid!");
    }

    /**
     * Sets the specified token at Position pos.
     */
    public void setField(Position pos, int token) throws InputMismatchException {
        // TODO
        if (pos.x >= this.n || pos.x < 0 || pos.y >= this.n || pos.y < 0)
            throw new InputMismatchException();

        switch (token) {
            case 0:
                if (this.getField(pos) == 1) {
                    this.x--;
                    this.free++;
                    tahta[pos.y][pos.x] = '-';
                } else if (this.getField(pos) == -1) {
                    this.o--;
                    this.free++;
                    tahta[pos.y][pos.x] = '-';
                } else if (this.getField(pos) == 0) {
                    tahta[pos.y][pos.x] = '-';
                }
                break;

            case 1:
                if (this.getField(pos) == 1) {
                    tahta[pos.y][pos.x] = 'x';
                } else if (this.getField(pos) == -1) {
                    this.o--;
                    this.x++;
                    tahta[pos.y][pos.x] = 'x';
                } else if (this.getField(pos) == 0) {
                    this.x++;
                    this.free--;
                    tahta[pos.y][pos.x] = 'x';
                }
                break;
            case -1:
                if (this.getField(pos) == 1) {
                    this.x--;
                    this.o++;
                    tahta[pos.y][pos.x] = 'o';
                } else if (this.getField(pos) == -1) {
                    tahta[pos.y][pos.x] = 'o';
                } else if (this.getField(pos) == 0) {
                    this.o++;
                    this.free--;
                    tahta[pos.y][pos.x] = 'o';
                }
                break;
            default:
                throw new InputMismatchException();
        }
    }

    /**
     * Places the token of a player at Position pos.
     */
    public void doMove(Position pos, int player) {
        // TODO
        switch (player) {
            case -1:
                setField(pos, -1);
                break;

            case 1:
                setField(pos, 1);
                break;

            default:
                throw new InputMismatchException();
        }

    }

    /**
     * Clears board at Position pos.
     */
    public void undoMove(Position pos) {
        // TODO
        setField(pos, 0);
    }

    /**
     * @return true if game is won, false if not
     */
    public boolean isGameWon() {
        // TODO
        return checkWinHorizontal() || checkWinVertical() || checkWinDiagonal();
    }

    public boolean checkWinVertical() {
        boolean status = false;
        int columnNr = 0;
        int xAnzahl = 0;
        int oAnzahl = 0;

        while (columnNr < n) {
            for (int i = 0; i < n; i++) {
                if (tahta[i][columnNr] == 'x') {
                    xAnzahl++;
                } else if (tahta[i][columnNr] == 'o') {
                    oAnzahl++;
                }
            }

            if (xAnzahl == n || oAnzahl == n) {
                status = true;
                break;
            }

            xAnzahl = 0;
            oAnzahl = 0;
            columnNr++;

        }

        return status;
    }

    public boolean checkWinHorizontal() {
        boolean status = false;
        int lineNr = 0;
        int xAnzahl = 0;
        int oAnzahl = 0;

        while (lineNr < n) {
            for (int i = 0; i < n; i++) {
                if (tahta[lineNr][i] == 'x') {
                    xAnzahl++;
                } else if (tahta[lineNr][i] == 'o') {
                    oAnzahl++;
                }
            }

            if (xAnzahl == n || oAnzahl == n) {
                status = true;
                break;
            }

            xAnzahl = 0;
            oAnzahl = 0;
            lineNr++;

        }

        return status;
    }

    public boolean checkWinDiagonal() {
        int xPos = 0;
        int yPos = 0;
        int xAnzahl = 0;
        int oAnzahl = 0;
        int iteration = 0;

        //first diagonal
        while (iteration != n) {
            if (tahta[yPos][xPos] == 'x') {
                xAnzahl++;
            } else if (tahta[yPos][xPos] == 'o') {
                oAnzahl++;
            }

            xPos++;
            yPos++;
            iteration++;
        }

        if (xAnzahl == n || oAnzahl == n)
            return true;


        xPos = n - 1;
        yPos = 0;
        iteration = 0;
        xAnzahl = 0;
        oAnzahl = 0;

        //second diagonal
        while (iteration != n) {
            if (tahta[yPos][xPos] == 'x') {
                xAnzahl++;
            } else if (tahta[yPos][xPos] == 'o') {
                oAnzahl++;
            }

            yPos++;
            xPos--;
            iteration++;
        }

        return xAnzahl == n || oAnzahl == n;
    }

    /**
     * @return set of all free fields as some Iterable object
     */
    public Iterable<Position> validMoves() {
        // TODO
        LinkedList<Position> valid = new LinkedList<>();

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tahta[i][j] == '-')
                    valid.add(new Position(j, i));
            }
        }

        return valid;

    }

    /**
     * Outputs current state representation of the Board object.
     * Practical for debugging.
     */
    public void print() {
        // TODO
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                System.out.print(this.tahta[i][j] + " ");
            }
            System.out.println();
        }
    }
}
