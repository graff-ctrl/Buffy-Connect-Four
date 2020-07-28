package com.example.a9dt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Player class implements a Player parent class and two children classes; Human and Computer
 */
public class Player
{

    private String name;  //Player name
    private int score;  //Players score from games
    private int tokenNum;   //Token number assigned to player

    /**
     * Player no-arg constructor
     */
    public Player () {
        name = null;
        score = 0;
    }

    /**
     * Player name argument constructor
     * @param name
     */
    public Player (String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Player name and score constructor
     * @param name
     * @param score
     */
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Set score function
     * @param s
     */
    public void setScore(int s) {
        score = s;
    }

    /**
     * Increment Score fucntion
     */
    public void incScore() {
        score++;
    }

    /**
     * Function to return score of player.
     * @return
     */
    public int getScore() {
        return score;
    }
    /**
     *
     * @param token
     */
    public void setPlayerToken(int token){
        tokenNum = token;
    }

    /**
     * Function to return player's token.
     * @return
     */
    public int getPlayerToken() {
        return tokenNum;
    }
}

/**
 * Child class of Player class.
 */
class Human extends Player {
    private String userName;
    private int level;

    public Human(String name) {
        super(name);
    }

    public Human(String name, int score) {
        super(name, score);
    }

    public void setUserName (String userName){
        this.userName = userName;
    }

    public String getUserName(){
        if (userName != null)
            return userName;

        return "Error";
    }
}


class Computer extends Player {
    private int movesSize;
    private int [] moves; //Array of moves from endpoint player


    public Computer(String name) {
        super(name);
    }

    public Computer(String name, int score) {
        super(name, score);
    }

    /**
     *
     * @param arr
     */
    public void setMoves(int [] arr) {
        this.moves = new int[arr.length];

        for (int i = 0; i < moves.length; i++)
        {
            moves[i] = arr[i];
        }
    }

}
