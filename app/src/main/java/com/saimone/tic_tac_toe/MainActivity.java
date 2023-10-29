package com.saimone.tic_tac_toe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final int EMPTY_CELL = 0;
    private static final int CROSS_CELL = 1;
    private static final int CIRCLE_CELL = 2;
    boolean gameIsActive = true;
    boolean crossPlayer = true;
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    private boolean isCellEmpty(int cell) {
        return gameState[cell] == EMPTY_CELL;
    }

    private boolean isGameBoardFull() {
        for (int cell : gameState) {
            if (cell == EMPTY_CELL) {
                return false;
            }
        }
        return true;
    }

    public void dropIn(View view) {
        if (!gameIsActive) {
            return;
        }

        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (isCellEmpty(tappedCounter)) {
            int currentPlayer = crossPlayer ? CROSS_CELL : CIRCLE_CELL;
            gameState[tappedCounter] = currentPlayer;

            counter.setTranslationY(-1000f);
            counter.setImageResource(currentPlayer == CROSS_CELL ? R.drawable.cross : R.drawable.circle);
            crossPlayer = !crossPlayer;
            counter.animate().translationYBy(1000f).rotation(720).setDuration(300);
        }
        GameState state = checkForWin();
        Log.d("state", state.toString());
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private GameState checkForWin() {
        TextView winnerMessage = findViewById(R.id.textView);
        LinearLayout layout = findViewById(R.id.linearLayout);
        for (int[] winningPosition : winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] != 0) {
                gameIsActive = false;
                String player;
                GameState state;
                if (gameState[winningPosition[0]] == 1) {
                    player = "Cross";
                    state = GameState.WIN_CROSS;
                } else {
                    player = "Circle";
                    state = GameState.WIN_CIRCLE;
                }
                winnerMessage.setText(player + " has won!");
                layout.setVisibility(View.VISIBLE);
                return state;
            }
        }
        if (isGameBoardFull()) {
            winnerMessage.setText("It's a draw!");
            layout.setVisibility(View.VISIBLE);
            return GameState.DRAW;
        }
        return GameState.PLAYING;
    }

    public void playAgain(View view) {
        LinearLayout layout = findViewById(R.id.linearLayout);
        layout.setVisibility(View.INVISIBLE);
        crossPlayer = true;
        Arrays.fill(gameState, 0);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView image = (ImageView) gridLayout.getChildAt(i);
            image.setImageResource(0);
        }
        gameIsActive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}