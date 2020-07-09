package com.example.a9dt;

public class Player
{

    private String name;
    private int score;
    private int tokenNum;

    // Constructor function to set player name
    public Player(String name)
    {
        this.name = name;
        this.score = 0;
    }
    //Constructor function to set player name and score. Assuming player needs to be restored.
    public Player(String name, int score)
    {
        this.name = name;
        this.score = score;

    }

    public void setScore(int s)
    {
        score = s;
    }
    public void incScore()
    {
        score++;
    }

    public void setPlayerToken(int token){
        tokenNum = token;
    }
    public int getPlayerToken()
    {
        return tokenNum;
    }
}

class Human extends Player
{


    public Human(String name) {
        super(name);
    }

    public Human(String name, int score) {
        super(name, score);
    }
}

class Computer extends Player
{
    private int movesSize;
    private int [] moves; //Array of moves from endpoint player

    public Computer(String name) {
        super(name);
    }

    public Computer(String name, int score) {
        super(name, score);
    }


    public void setMoves(int [] arr)
    {

        this.moves = new int[arr.length];

        for (int i = 0; i < moves.length; i++)
        {
            moves[i] = arr[i];
        }
    }

}
