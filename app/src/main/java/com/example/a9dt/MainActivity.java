package com.example.a9dt;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Board board;
    private static int ROWS = 4, COLS = 4;
    private ImageView[][] playingBoard;
    private View playingView;
    private ViewHolder viewHolder;
    private RequestQueue mQueue;
    private Human human;
    private Computer computer;

    private class ViewHolder {
        public ImageView turnIndicatorImageView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = new Board(COLS, ROWS);
        playingView = findViewById(R.id.board_view);
        mQueue = Volley.newRequestQueue(this);
        buildPlayingBoard();

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
                computerMove();
                break;
            case R.id.col_1:
                dropToken(1);
                computerMove();
                break;
            case R.id.col_2:
                dropToken(2);
                computerMove();
                break;
            case R.id.col_3:
                dropToken(3);
                computerMove();
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

    private void computerMove(){

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