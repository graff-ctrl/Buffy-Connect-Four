package com.example.a9dt;

public class Board {
    private int numCol, numRow;
    private int [][] board;
    private int gridMaxHeight;
    public int [] moves;
    private int totalPlayers;
    private int turn;
    private int totalMoves;
    public int numMoves;
    public boolean winner = false;



    /**
     * Constructor for board with same width and height (square box).
     * @param
     */
    public Board(int square) {
        numCol = square;
        numRow = square;
        board = new int[numCol][numRow];
        turn = 1;
        totalMoves = numCol * numRow;
        moves = new int [totalMoves];
        numMoves = 0;
    }

    /**
     * Constructor to craete a playing board with custom row and column lengths.
     * @param col number of columns the board has
     * @param row number of rows the board has.
     */
    public Board(int col, int row){

        numCol = col;
        numRow = row;
        board = new int[numCol][numRow];
        turn = 1;
        totalMoves = numCol * numRow;
        moves = new int [totalMoves];
        numMoves = 0;
    }

    public void switchTurn(){
        if (turn == 1){
            turn = 2;
        }
        else{
            turn = 1;
        }
    }

    public int getTurn(){
        return turn;
    }


    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }

    public void setNumCol(int numCol){
        this.numCol = numCol;
    }


    public int openRow(int col) {
        for (int j = numRow - 1; j > -1; j --) {
            if (board[col][j] == 0) {
                return j;
            }
        }
        return -1;
    }

    public boolean isValid(int col, int row){
        if (col > numCol - 1 || col < 0 || row > numRow || row < 0){
            return false;
        }
        if (openRow(col) == -1){
            return false;
        }

        return true;
    }

    public boolean putToken(int row, int col){
        if (row == -1 || !isValid(col, row))
            return false;

        board[col][row] = turn;
        moves[numMoves] = col;
        numMoves++;
        checkWin(turn, col, row);

        return true;
    }

    public boolean checkWin(int player, int col, int row) {

        // Check max height. Height is in descending order due to row numbering of 2D array.
        int i; // variable in for loop represents row index;
        int j; // variable in for loop represents column index.
        int maxLeft;
        int maxRight;
        int maxUp;
        int maxDown;

        // Sets left edge of grid
        if (col - 3 < 0)
            maxLeft = 0;
        else
            maxLeft = col - 3;


        //Sets right edge of grid
        if (col + 3 > numCol -1)
            maxRight = numCol -1;
        else
            maxRight = col +3;


        //Sets top edge of grid.
        if (row - 3 < 0)
            maxUp = 0;
        else
            maxUp = row - 3;


        //Sets bottom edge of grid.
        if (row + 3 > numRow - 1)
            maxDown = numRow - 1;
        else
            maxDown = row + 3;
        //Check Left to Right of possible win in grid.

        for (i = maxUp; i < maxDown + 1; i++) {
            for (j = maxLeft; j < maxRight + 1; j++) {

                if (board[i][j] == player) {

                    //Check grid left to right
                    if (j + 2 < maxRight) {
                        if (board[i][j + 1] == player && board[i][j + 2] == player && board[i][j + 3] == player) {
                            winner = true;
                            return true;
                        }
                    }

                    // Check grid down
                    if (i + 2 < maxDown) {
                        if (board[i + 1][j] == player && board[i + 2][j] == player && board[i + 3][j] == player) {
                            winner = true;
                            return true;
                        }
                    }

                    //Check Diagonally left-top to right-bottom
                    if (i < maxUp + 2 && j < maxRight - 2){
                        if (board[i + 1][j + 1] == player && board[i + 2][j + 2] == player && board[i + 3][j + 3] == player){
                            winner = true;
                            return true;
                        }
                    }

                    // Check Diagonally right-top to left-bottom
                    if (i < maxDown - 2 && j > maxLeft + 2){
                        if (board[i + 1][j - 1]== player && board[i + 2][j - 2]== player && board[i + 3][j - 3]== player){
                            winner = true;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void restart() {
        winner = false;
        turn = 1;
        int i;
        for (i = 0; i < moves.length; i++)
            moves[i] = 0;

        for (i = 0; i < board.length; i ++){
            for (int j = 0; j < board[i].length; i++){
                board[i][j] = 0;
            }
        }
    }

    public boolean draw(){

        //Finds first instace of empty index at top row (row 0).
        for (int i = 0; i < numCol -1; i++){
            if (board[0][i] == 0)
                return false;
        }
        return true;
    }

    public boolean gameOver()
    {
        if(winner){
            return true;
        }
        if (draw()){
            return true;
        }

        return false;
    }


}
