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
        board = new int[numRow][numCol];
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
        board = new int[numRow][numCol];
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
        for (int i = numRow - 1; i > -1; i --) {
            if (board[i][col] == 0) {
                return i;
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

    public void putToken(int row, int col){
        board[row][col] = turn;
        moves[numMoves] = col;
        numMoves++;
        checkWin(turn, row, col);

    }

    public void checkWin(int player, int row, int col) {

        // Check max height. Height is in descending order due to row numbering of 2D array.
        //int i; // variable in for loop represents row index;
        //int j; // variable in for loop represents column index.
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
            maxRight = col + 3;


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
        for (int j = maxLeft; j <= maxRight; j++){
            //if (board[row][j] == player)
            if (j + 3 <= maxRight)
            {
                if(board[row][j] == player && j + 3 <= maxRight && board[row][j + 1] == player &&
                        board[row][j + 2] == player && board[row][j + 3] == player) {
                    winner = true;

                }
            }
        }

        for (int i = maxUp; i <= maxDown; i++){
            //if (board[i][col] == player)
            if (i + 3 <= maxDown)
            {
                int check = board[i][col];
                if(board[i][col] == player && board[i+1][col] == player &&
                        board[i + 2][col] == player && board[i + 3][col] == player){
                    winner = true;

                }
            }
        }

        for (int i = maxUp; i <= maxDown; i++) {
            for (int j = maxLeft; j <= maxRight; j++) {

                //if (board[i][j] == player)
                if (i + 3 <= maxDown && j + 3 <= maxRight)
                 {
                    if (board[i][j] == player  && board[i + 1][j + 1] == player
                            && board[i + 2][j + 2] == player && board[i + 3][j + 3] == player) {
                        winner = true;

                    }
                }
            }
        }

        for (int i = maxDown; i >= maxUp; i--) {
            for (int j = maxLeft; j <= maxRight; j++) {

                //if (board[i][j] == player)
                if (i - 3 >= maxUp && j + 3 <= maxRight){
                    if (board[i][j] == player && board[i - 1][j + 1] == player
                            && board[i - 2][j + 2] == player && board[i - 3][j + 3] == player) {
                        winner = true;
                    }
                }


            }

        }


       /* for (i = maxUp; i <= maxDown; i++) {
            for (j = maxLeft; j <= maxRight; j++) {

                if (board[i][j] == player) {

                    //Check grid left to right
                    if (j + 3 <= maxRight) {
                        if (board[i][j + 1] == player && board[i][j + 2] == player && board[i][j + 3] == player) {
                            winner = true;
                            return true;
                        }
                    }

                    // Check grid down
                    if (i + 3 <= maxDown) {
                        if (board[i + 1][j] == player && board[i + 2][j] == player && board[i + 3][j] == player) {
                            winner = true;
                            return true;
                        }
                    }

                    //Check Diagonally left-top to right-bottom
                    if (i <= maxUp && j <= maxRight + 3){
                        if (board[i + 1][j + 1] == player && board[i + 2][j + 2] == player && board[i + 3][j + 3] == player){
                            winner = true;
                            return true;
                        }
                    }

                    // Check Diagonally right-top to left-bottom
                    if (i <= maxDown - 3 && j > maxLeft + 2){
                        if (board[i + 1][j - 1]== player && board[i + 2][j - 2]== player && board[i + 3][j - 3]== player){
                            winner = true;
                            return true;
                        }
                    }
                }
            }
        }
*/
    }

    public void restart() {
        winner = false;
        turn = 1;
        numMoves = 0;
        for (int i = 0; i < numRow; i++){
            for (int j = 0; j < numCol; j++){
                board[i][j] = 0;
            }
        }
    }

    public boolean draw(){

        //Finds first instace of empty index at top row (row 0).
        for (int j = 0; j < numCol; j++){
            if (board[0][j] == 0)
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
