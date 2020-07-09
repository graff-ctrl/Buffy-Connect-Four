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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Board board;
    private static int ROWS = 4, COLS = 4;
    private ImageView[][] playingBoard;
    private View playingView;
    private ViewHolder viewHolder;
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
        buildCells();


        ImageButton button1 = findViewById(R.id.col_0);
        ImageButton button2 = findViewById(R.id.col_1);
        ImageButton button3 = findViewById(R.id.col_2);
        ImageButton button4 = findViewById(R.id.col_3);

        button1.setOnClickListener((View.OnClickListener) this);
        button2.setOnClickListener((View.OnClickListener) this);
        button3.setOnClickListener((View.OnClickListener) this);
        button4.setOnClickListener((View.OnClickListener) this);

        viewHolder = new ViewHolder();
        viewHolder.turnIndicatorImageView = (ImageView) findViewById(R.id.turn_indicator_image_view);
        viewHolder.turnIndicatorImageView.setImageResource(token());
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.col_0:
                move(0);
                break;
            case R.id.col_1:
                move(1);
                break;
            case R.id.col_2:
                move(2);
                break;
            case R.id.col_3:
                move(3);
                break;

        }
    }


    private void buildCells() {
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

    private int colAtX(float x) {
        float colWidth = playingBoard[0][0].getWidth();
        int col = (int) x / (int) colWidth;
        if (col < 0 || col > COLS)
            return -1;
        return col;
    }
    public void createGameBoard() {
        ViewGroup rows;
        ImageView cols;
        playingBoard = new ImageView[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            rows = (ViewGroup) ((ViewGroup) playingView).getChildAt(i);
            rows.setClipChildren(false);
            for (int j = 0; j < COLS; j++) {
                cols = (ImageView) rows.getChildAt(j);

                playingBoard[i][j] = cols;
            }
        }
    }


    private int token(){
        if (board.getTurn() == 1){
            return R.drawable.red_token;
        }
        return R.drawable.black;
    }

    private void move(int col) {
        int turn = board.getTurn();
        int row = board.openRow(col);
        int token = turn;
        if (!board.putToken(token, col)){
            return;
        }
        final ImageView cell = playingBoard[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 8);
        cell.setY(move);
        cell.setImageResource(token());
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, Math.abs(move));
        anim.setDuration(850);
        anim.setFillAfter(true);
        cell.startAnimation(anim);
        if (board.winner) {
            win();
        } else {
            board.switchTurn();
        }
    }

    private void win(){

    }


}