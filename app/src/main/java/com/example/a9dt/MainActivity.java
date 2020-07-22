package com.example.a9dt;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Board board;
    private static int ROWS = 4, COLS = 4;
    private ImageView[][] playingBoard;
    private View playingView;
    private View buttonToggle;
    private View textToggle;
    private ViewHolder viewHolder;
    private RequestQueue mQueue;
    private Human human;
    private Computer computer;
    private String baseUrl = "https://w0ayb2ph1k.execute-api.us-west-2.amazonaws.com/production?moves=";
    private String query = "[]";
    private String url;
    private Handler handler;


    private class ViewHolder {
        public ImageView turnIndicatorImageView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = new Board(ROWS, COLS);
        playingView = findViewById(R.id.board_view);
        buildPlayingBoard();
        human = new Human("human");
        computer = new Computer("computer_1");
        handler = new Handler();
        mQueue = Volley.newRequestQueue(this);




        //Creating buttons types for the row of buttons at the bottom of the board
        //to select the column.
        ImageButton button1 = findViewById(R.id.col_0);
        ImageButton button2 = findViewById(R.id.col_1);
        ImageButton button3 = findViewById(R.id.col_2);
        ImageButton button4 = findViewById(R.id.col_3);

        Button first = (Button) findViewById(R.id.first_player);
        Button second = (Button) findViewById(R.id.second_player);
        Button replay = (Button) findViewById(R.id.replay);
        buttonToggle = findViewById(R.id.replay);
        buttonToggle.setVisibility(View.GONE);

        //Setting listener.
        button1.setOnClickListener((View.OnClickListener) this);
        button2.setOnClickListener((View.OnClickListener) this);
        button3.setOnClickListener((View.OnClickListener) this);
        button4.setOnClickListener((View.OnClickListener) this);
        first.setOnClickListener((View.OnClickListener) this);
        second.setOnClickListener((View.OnClickListener) this);
        replay.setOnClickListener((View.OnClickListener) this);

        //moves();

        //viewHolder = new ViewHolder();
        //viewHolder.turnIndicatorImageView = (ImageView) findViewById(R.id.turn_indicator_image_view);
        //viewHolder.turnIndicatorImageView.setImageResource(turnIndicator());
    }

    /**
     *
     * @param v view
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.col_0:
                if (board.openRow(0) < 0){
                    break;
                }
                dropToken(0);
                appendHumanPlayerToQuery(0);
                if (!board.gameOver())
                    serviceRequest();
                break;

            case R.id.col_1:
                if (board.openRow(1) < 0){
                    break;
                }
                dropToken(1);
                appendHumanPlayerToQuery(1);
                if (!board.gameOver())
                    serviceRequest();
                break;
            case R.id.col_2:
                if (board.openRow(2) < 0){
                    break;
                }
                dropToken(2);
                appendHumanPlayerToQuery(2);
                if (!board.gameOver())
                    serviceRequest();
                break;
            case R.id.col_3:
                if (board.openRow(3) < 0){
                    break;
                }
                dropToken(3);
                appendHumanPlayerToQuery(3);
                if (!board.gameOver())
                    serviceRequest();
                break;

            case R.id.first_player:
                // Set players tokens
                human.setPlayerToken(1);
                computer.setPlayerToken(2);

                //Remove buttons
                buttonToggle = findViewById(R.id.first_player);
                buttonToggle.setVisibility(View.GONE);
                buttonToggle = findViewById(R.id.second_player);
                buttonToggle.setVisibility(View.GONE);

                //Make button visable.
                buttonToggle = findViewById(R.id.replay);
                buttonToggle.setVisibility(View.VISIBLE);


                break;

            case R.id.second_player:
                //Set players tokens.
                computer.setPlayerToken(1);
                human.setPlayerToken(2);

                //Hide Buttons after click
                buttonToggle = findViewById(R.id.first_player);
                buttonToggle.setVisibility(View.GONE);
                buttonToggle = findViewById(R.id.second_player);
                buttonToggle.setVisibility(View.GONE);

                //Make Replay button visable on click.
                buttonToggle = findViewById(R.id.replay);
                buttonToggle.setVisibility(View.VISIBLE);

                buildQuery();
                serviceRequest();   //Initial service request for humnan set as player 2.

                break;
            case R.id.replay:

                //Remove button after click
                buttonToggle = findViewById(R.id.replay);
                buttonToggle.setVisibility(View.GONE);

                // Make buttons visable after click
                buttonToggle = findViewById(R.id.first_player);
                buttonToggle.setVisibility(View.VISIBLE);
                buttonToggle = findViewById(R.id.second_player);
                buttonToggle.setVisibility(View.VISIBLE);

                //Restart Game
                restart();
                break;


        }
    }

    private void moves (){
        dropToken(3);
        dropToken(0);

        dropToken(2);
        dropToken(2);

        dropToken(1);
        dropToken(1);

        dropToken(0);
        dropToken(2);

        dropToken(1);
        dropToken(2);

        dropToken(0);
        dropToken(1);

        dropToken(0);
    }

    /**
     * Function for building the view of the playing board.
     */
    private void buildPlayingBoard() {
        playingBoard = new ImageView[ROWS][COLS];
        for (int r=0; r< ROWS; r++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) playingView).getChildAt(r);
            row.setClipChildren(false);
            for (int c=0; c<COLS; c++) {
                ImageView imageView = (ImageView) row.getChildAt(c);
                imageView.setImageResource(android.R.color.transparent);
                playingBoard[r][c] = imageView;
            }
        }
    }


    private void setTurnIndictor(){
        if (board.getTurn() == 1)
            viewHolder.turnIndicatorImageView.setImageResource(R.drawable.red_token);
        else
            viewHolder.turnIndicatorImageView.setImageResource(R.drawable.black_token);
    }

    private int turnIndicator(){
        if (board.getTurn() == 1){
            return R.drawable.red_token;
        }
        return R.drawable.black_token;
    }

    private void dropToken(int col) {
        if (board.gameOver())
            return;
        int row = board.openRow(col);
        if (row == -1)
            return;
        board.putToken(row, col);
        animation(row, col);
        if (board.getWinner()) {
            win();
        }else if (board.draw())
        {
            draw();
        }
        else {
            board.switchTurn();
            //setTurnIndictor();
        }
    }

    private void computerMove(final int x, String moves){

            dropToken(x);
            query = moves;
            buildQuery();

    }

    private void buildQuery(){

        StringBuilder q = new StringBuilder();

        q.append(baseUrl);
        q.append(query);
        url = q.toString();

    }

    /**
     * Function to append the human players move to the query sting that is sent to
     * endpoint.
     * @param x
     */
    private void appendHumanPlayerToQuery (int x){

        StringBuilder q = new StringBuilder();
        q.append(query);

        if (board.numMoves < 2){
            q.replace(q.length()-1, q.length(), "");
            q.append(x);
            q.append("]");
        }
        else {
            q.replace(q.length() - 1, q.length(), ",");
            q.append(x);
            q.append("]");
        }
        query = q.toString();
        buildQuery();
    }

    /**
     * Function to call service endpoint.
     */
    private void serviceRequest(){


        boolean valid = false;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int col = response.getInt(response.length() - 1);
                    if (board.openRow(col) < 0)
                        serviceRequest();
                    else
                        computerMove(col, response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String resp = response.toString();
                Log.e("RESPONSE", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("Made it to failure");
                error.printStackTrace();
            }
        });

        mQueue.add(jsonArrayRequest);
    }

    /**
     * Funtion for when a win is detected on the board.
     */
    private void win(){

        String humanWin = "You are the winner!";
        String compWin = "Computer is the winner";
        TextView winText = (TextView) findViewById(R.id.winner_text);
        if (human.getPlayerToken() == board.getTurn())
            winText.setText(humanWin);
        else
            winText.setText(compWin);
        buttonToggle = findViewById(R.id.replay);
        buttonToggle.setVisibility(View.VISIBLE);

    }

    /**
     * Function that displays text when there is a draw and makes replay button visable.
     */
    private void draw()
    {
        String winner = "DRAW!!!!";
        TextView winText = (TextView) findViewById(R.id.winner_text);
        winText.setText(winner);
        buttonToggle = findViewById(R.id.replay);
        buttonToggle.setVisibility(View.VISIBLE);
    }

    /**
     * Function to reset the game play.
     */
    private void restart() {
        board.restart();
        query = "[]";
        textToggle = findViewById(R.id.winner_text);
        TextView winText = (TextView) findViewById(R.id.winner_text);
        winText.setText("");

        // Rebuild playing board image.
        buildPlayingBoard();
        for (int r=0; r< ROWS; r++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) playingView).getChildAt(r);
            row.setClipChildren(false);
            for (int c = 0; c < COLS; c++) {
                ImageView imageView = (ImageView) row.getChildAt(c);
                imageView.setImageResource(android.R.color.transparent);
                playingBoard[r][c] = imageView;
            }
        }

    }

    /**
     * Function for coin drop animation in board.
     * @param row
     * @param col
     */
    private void animation(int row, int col){
        final ImageView cell = playingBoard[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 15);
        cell.setY(move);
        cell.setImageResource(turnIndicator());
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, Math.abs(move));
        anim.setDuration(850);
        anim.setFillAfter(true);
        cell.startAnimation(anim);
    }


}