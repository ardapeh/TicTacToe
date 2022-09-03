import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * This class implements and evaluates game situations of a TicTacToe game.
 */
public class TicTacToe {

    /**
     * Returns an evaluation for player at the current board state.
     * Arbeitet nach dem Prinzip der Alphabeta-Suche. Works with the principle of Alpha-Beta-Pruning.
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     * @return rating of game situation from player's point of view
     **/
    public static int alphaBeta(Board board, int player) {
        // TODO
        switch (player) {
            case 1:

                return alphaBeta(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            case -1:

                return -alphaBeta(board, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            default:
                throw new InputMismatchException("Entered player is not valid!");
        }

    }

    public static Board copyBoard(Board initBoard) {
        Board tempBoard = new Board(initBoard.getN());

        for (int i = 0; i < initBoard.getN(); i++) {
            for (int k = 0; k < initBoard.getN(); k++) {
                if (initBoard.tahta[i][k] == 'x') {
                    tempBoard.tahta[i][k] = 'x';
                } else if (initBoard.tahta[i][k] == 'o') {
                    tempBoard.tahta[i][k] = 'o';
                } else
                    tempBoard.tahta[i][k] = '-';
            }
        }

        return tempBoard;
    }

    public static int calculateScore(Board board) {
        if (getWinner(board) == 1 && board.nFreeFields() == 0) {
            return 1;
        } else if (getWinner(board) == 1 && board.nFreeFields() != 0) {
            return board.nFreeFields() + 1;
        } else if (getWinner(board) == -1 && board.nFreeFields() == 0) {
            return -1;
        } else if (getWinner(board) == -1 && board.nFreeFields() != 0) {
            return -board.nFreeFields() - 1;
        }

        return 0;
    }

    public static ArrayList<Board> childrenBoards(Board board, int player) {
        ArrayList<Board> children = new ArrayList<>();
        Board temp = copyBoard(board);

        if (player == 1) {
            for (Position pos : board.validMoves()) {
                temp.doMove(pos, 1);
                Board temp1 = copyBoard(temp);
                children.add(temp1);
                temp.undoMove(pos);
            }

        } else {

            for (Position pos : board.validMoves()) {
                temp.doMove(pos, -1);
                Board temp1 = copyBoard(temp);
                children.add(temp1);
                temp.undoMove(pos);
            }

        }
        return children;

    }


    public static int getWinner(Board board) {
        int xAnzahl = 0;
        int oAnzahl = 0;

        if (board.isGameWon()) {
            for (int i = 0; i < board.getN(); i++) {
                for (int j = 0; j < board.getN(); j++) {
                    if (board.tahta[i][j] == 'x') {
                        xAnzahl++;
                    } else if (board.tahta[i][j] == 'o')
                        oAnzahl++;
                }

            }

            if (xAnzahl > oAnzahl)
                return 1;
            return -1;
        }

        return 0;

    }

    public static int alphaBeta(Board board, int player, int alpha, int beta) {
        if (board.isGameWon() || board.nFreeFields() == 0 || (board.nFreeFields() == board.getN() * board.getN() && board.getN() > 2)) {
            return calculateScore(board);
        }

        if (player == 1) {
            int value = Integer.MIN_VALUE;
            for (Position pos : board.validMoves()) {
                //for (Board child : childrenBoards(board, 1)) {
                board.doMove(pos, 1);
                value = Math.max(value, alphaBeta(board, -player, alpha, beta));
                board.undoMove(pos);
                alpha = Math.max(alpha, value);
                if (beta <= alpha)
                    break;
            }
            return value;
        }
        int value = Integer.MAX_VALUE;
        for (Position pos : board.validMoves()) {
            //for (Board child : childrenBoards(board, -1)) {
            board.doMove(pos, -1);
            value = Math.min(value, (alphaBeta(board, -player, alpha, beta)));
            board.undoMove(pos);
            beta = Math.min(beta, value);
            if (beta <= alpha)
                break;
        }
        return value;

    }


    /**
     * Vividly prints a rating for each currently possible move out at System.out.
     * (from player's point of view)
     * Uses Alpha-Beta-Pruning to rate the possible moves.
     * formatting: See "Beispiel 1: Bewertung aller Zugmöglichkeiten" (Aufgabenblatt 4).
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     **/
    public static void evaluatePossibleMoves(Board board, int player) {
        // TODO
        if (player == 1) {
            int index = 0;
            String[][] tahta = createBoard(board);
            ArrayList<Integer> evals = new ArrayList<>();
            System.out.println("Evaluation for player 'x':");
            for (Position pos : board.validMoves()) {
                board.doMove(pos, 1);
                evals.add(-(alphaBeta(board, -1)));
                board.undoMove(pos);
            }


            for (int i = 0; i < board.getN(); i++) {
                for (int j = 0; j < board.getN(); j++) {
                    if (board.tahta[i][j] == 'x') {
                        tahta[i][j] = "  x";
                    } else if (board.tahta[i][j] == 'o') {
                        tahta[i][j] = "  o";
                    } else if (board.tahta[i][j] == '-' && evals.get(index) >= 0) {
                        tahta[i][j] = "  " + evals.get(index);
                        index++;
                    } else if (board.tahta[i][j] == '-' && evals.get(index) < 0) {
                        tahta[i][j] = " -" + (-evals.get(index));
                        index++;
                    }
                }

            }

            printBoard(tahta, board.getN());

        } else if (player == -1) {
            System.out.println("Evaluation for player 'o':");
            int index = 0;
            String[][] tahta = createBoard(board);
            ArrayList<Integer> evals = new ArrayList<>();

            for (Position pos : board.validMoves()) {
                board.doMove(pos, -1);
                evals.add(-(alphaBeta(board, 1)));
                board.undoMove(pos);
            }

            for (int i = 0; i < board.getN(); i++) {
                for (int j = 0; j < board.getN(); j++) {
                    if (board.tahta[i][j] == 'x') {
                        tahta[i][j] = "  x";
                    } else if (board.tahta[i][j] == 'o') {
                        tahta[i][j] = "  o";
                    } else if (board.tahta[i][j] == '-' && evals.get(index) >= 0) {
                        tahta[i][j] = "  " + evals.get(index);
                        index++;
                    } else if (board.tahta[i][j] == '-' && evals.get(index) < 0) {
                        tahta[i][j] = " -" + (-evals.get(index));
                        index++;
                    }
                }

            }

            printBoard(tahta, board.getN());
        }
    }


    public static String[][] createBoard(Board board) {

        String[][] tahta = new String[board.getN()][board.getN()];

        for (int i = 0; i < board.getN(); i++) {
            for (int j = 0; j < board.getN(); j++) {
                tahta[i][j] = "[-]";
            }
        }


        return tahta;
    }

    public static void printBoard(String[][] board, int size) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        //Board oyun = new Board(new int[][]{{0, 0}, {0, 0}});
        Board oyun = new Board(3);
        Position xd1 = new Position(0, 0);
        Position xd2 = new Position(0, 1);
        Position xd3 = new Position(0, 2);
        Position xd4 = new Position(0, 3);
        Position xd5 = new Position(0, 4);
        Position xd6 = new Position(0, 5);
        Position xd7 = new Position(0, 6);
        Position xd8 = new Position(0, 7);
        Position xd9 = new Position(0, 8);
        Position xd10 = new Position(0, 9);

        oyun.doMove(xd1, 1);
        oyun.print();
        oyun.doMove(xd2, -1);
        oyun.print();
        /*

        oyun.doMove(xd2, 1);
        oyun.doMove(xd3, 1);
        oyun.doMove(xd4, 1);
        oyun.doMove(xd5, 1);
        oyun.doMove(xd6, 1);
        oyun.doMove(xd7, 1);
        oyun.doMove(xd8, 1);
        oyun.doMove(xd9, 1);
        oyun.doMove(xd10, 1);

         */





        //System.out.println(oyun.isGameWon());
        //evaluatePossibleMoves(oyun, 1);
        //System.out.println(alphaBeta(oyun, 1));

    }
}

