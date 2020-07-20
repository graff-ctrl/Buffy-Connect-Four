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
    private ViewHolder viewHolder;
    private RequestQueue mQueue;
    private Human human;
    private Computer computer;
    private String baseUrl = "https://w0ayb2ph1k.execute-api.us-west-2.amazonaws.com/production?moves=";
    private String query = "[]";
    private String url;
    Handler handler;


    private class ViewHolder {
        public ImageView turnIndicatorImageView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = new Board(COLS, ROWS);
        playingView = findViewById(R.id.board_view);
        buildPlayingBoard();
        human = new Human("human");
        human.setPlayerToken(1);
        handler = new Handler();

        if (human.getPlayerToken() == 2)
        {
            buildQuery();
            serviceRequest();
        }



        //Creating buttons types for the row of buttons at the bottom of the board
        //to select the column.
        ImageButton button1 = findViewById(R.id.col_0);
        ImageButton button2 = findViewById(R.id.col_1);
        ImageButton button3 = findViewById(R.id.col_2);
        ImageButton button4 = findViewById(R.id.col_3);

        //Setting listener.
        button1.setOnClickListener((View.OnClickListener) this);
        button2.setOnClickListener((View.OnClickListener) this);
        button3.setOnClickListener((View.OnClickListener) this);
        button4.setOnClickListener((View.OnClickListener) this);

        viewHolder = new ViewHolder();
        viewHolder.turnIndicatorImageView = (ImageView) findViewById(R.id.turn_indicator_image_view);
        viewHolder.turnIndicatorImageView.setImageResource(turnIndicator());
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.col_0:
                dropToken(0);
                appendHumanPlayerToQuery(0);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!board.gameOver())
                            serviceRequest();
                    }
                }, 2000);

                break;
            case R.id.col_1:
                dropToken(1);
                appendHumanPlayerToQuery(1);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!board.gameOver())
                            serviceRequest();
                    }
                }, 2000);
                break;
            case R.id.col_2:
                dropToken(2);
                appendHumanPlayerToQuery(2);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!board.gameOver())
                            serviceRequest();
                    }
                }, 2000);
                break;
            case R.id.col_3:
                dropToken(3);
                appendHumanPlayerToQuery(3);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!board.gameOver())
                            serviceRequest();
                    }
                }, 2000);
                break;

        }
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
        if (board.winner) {
            win();
        } else {
            board.switchTurn();
            setTurnIndictor();
        }
    }

    private void computerMove(int x, String moves){

        if(board.openRow(x) < 0){
            serviceRequest();
        }
        else
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

    private void appendHumanPlayerToQuery (int x){

        StringBuilder q = new StringBuilder();
        q.append(query);

        if (board.numMoves == 1){
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

    private void serviceRequest(){

        mQueue = Volley.newRequestQueue(this);

        //String url = "https://w0ayb2ph1k.execute-api.us-west-2.amazonaws.com/production?moves=[2]";

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

    private void win(){

    }



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