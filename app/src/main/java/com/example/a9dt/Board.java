package com.example.a9dt;

/**
 * Board class for connect 4.
 *
 * @version 1.0
 * @author Andrew Graff
 */
public class Board {
    //Private variables.
    private int numCol, numRow; // Variables for number of rows and columns.
    private int [][] board; //Board Array
    private int [] moves; //Array of logged moves.
    private int turn;   // Varible to indicate turn;
    private int totalMoves; // Size variable for []moves.
    private boolean winner = false;  // Boolean for game winner


    //Public variables
    public int numMoves;    // Counter variable for []moves.

    /**
     * Constructor for board with same width and height (square box). Assuming passing one variable
     * means a square box.
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

    /**
     * Function to toggle player turn between 1 and 2
     */
    public void switchTurn(){
        if (turn == 1){
            turn = 2;
        }
        else{
            turn = 1;
        }
    }

    /**
     * Function to return which player's turn is currently is.
     * @return turn
     */
    public int getTurn(){
        return turn;
    }

    /**
     * Setter function for number of rows
     * @param numRow
     */
    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }

    /**
     * Setter function for number of columns
     * @param numCol
     */
    public void setNumCol(int numCol){
        this.numCol = numCol;
    }

    /**
     * Function takes in the column number and returns the index of the last available row in that
     * column.
     * @param col
     * @return row number or -1 if row is full.
     */
    public int openRow(int col) {
        for (int i = numRow - 1; i > -1; i --) {
            if (board[i][col] == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Function places player token (turn) into the array, adds the column selection to the array
     * of logged moves and increments the numMove counter. The input is then checked for a win.
     * @param row
     * @param col
     */
    public void putToken(int row, int col){
        board[row][col] = turn;
        moves[numMoves] = col;
        numMoves++;
        checkWin(turn, row, col);

    }

    /**
     * Function to traverse the area of the last token dropped onto the board for a possible win.
     * @param player turn variable
     * @param row row of token
     * @param col column of token
     */
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
            if (j + 3 <= maxRight)
            {
                if(board[row][j] == player && j + 3 <= maxRight && board[row][j + 1] == player &&
                        board[row][j + 2] == player && board[row][j + 3] == player) {
                    winner = true;

                }
            }
        }
        // Check Up to down for possible win.
        for (int i = maxUp; i <= maxDown; i++){
            if (i + 3 <= maxDown)
            {
                int check = board[i][col];
                if(board[i][col] == player && board[i+1][col] == player &&
                        board[i + 2][col] == player && board[i + 3][col] == player){
                    winner = true;

                }
            }
        }
        // Check diagonally from top left to bottom right for possible win.
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
        // Check diagonally from bottom left to top right for possible win.
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
    }

    /**
     * Function returns boolean of winner
     * @return winner
     */
    public boolean getWinner(){
        return winner;
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

    /**
     * Function check board for draw by search the top row [0] and returns true if full and false
     * if there is still an open index.
     * @return boolean
     */
    public boolean draw(){

        //Finds first instace of empty index at top row (row 0).
        for (int j = 0; j < numCol; j++){
            if (board[0][j] == 0)
                return false;
        }
        return true;
    }

    /**
     * Function for checking if game is over. Created assuming computer is playing and a check is
     * needed before call.
     * @return
     */
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
