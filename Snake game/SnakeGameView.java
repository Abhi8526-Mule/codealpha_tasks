package com.example.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGameView extends View {

    private class Tile {
        int x, y;
        Tile(int x, int y) { this.x = x; this.y = y; }
    }

    private int tileSize = 50;
    private int numTilesX, numTilesY;

    private Tile snakeHead;
    private ArrayList<Tile> snakeBody = new ArrayList<>();
    private Tile food;
    private Random random = new Random();

    private int velocityX = 1, velocityY = 0;
    private boolean gameOver = false;

    private Paint paint = new Paint();

    private Handler handler = new Handler();
    private final int updateDelay = 200; // milliseconds
    private Runnable gameRunnable = new Runnable() {
       
        public void run() {
            if (!gameOver) {
                move();
                invalidate();
                handler.postDelayed(this, updateDelay);
            }
        }
    };

    // For swipe detection
    private float touchX, touchY;

    public SnakeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
      
        snakeHead = new Tile(5, 5);
        snakeBody.clear();
        placeFood();

        velocityX = 1;
        velocityY = 0;
        gameOver = false;

        handler.postDelayed(gameRunnable, updateDelay);
    }

    private void placeFood() {
        numTilesX = getWidth() / tileSize;
        numTilesY = getHeight() / tileSize;
        if (numTilesX == 0 || numTilesY == 0) {
            // View not measured yet, try later
            postDelayed(() -> {
                placeFood();
            }, 100);
            return;
        }
        food = new Tile(random.nextInt(numTilesX), random.nextInt(numTilesY));
    }

  
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (numTilesX == 0 || numTilesY == 0) {
            numTilesX = getWidth() / tileSize;
            numTilesY = getHeight() / tileSize;
        }

              canvas.drawColor(Color.BLACK);

        paint.setColor(Color.RED);
        canvas.drawRect(food.x * tileSize, food.y * tileSize,
                (food.x + 1) * tileSize, (food.y + 1) * tileSize, paint);

        paint.setColor(Color.GREEN);
        canvas.drawRect(snakeHead.x * tileSize, snakeHead.y * tileSize,
                (snakeHead.x + 1) * tileSize, (snakeHead.y + 1) * tileSize, paint);

     
        for (Tile t : snakeBody) {
            canvas.drawRect(t.x * tileSize, t.y * tileSize,
                    (t.x + 1) * tileSize, (t.y + 1) * tileSize, paint);
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(60);

        if (gameOver) {
            canvas.drawText("Game Over! Tap to Restart", 50, getHeight() / 2, paint);
            canvas.drawText("Score: " + snakeBody.size(), 50, getHeight() / 2 + 80, paint);
        } else {
            canvas.drawText("Score: " + snakeBody.size(), 50, 80, paint);
        }
    }

    private void move() {
      
        if (snakeHead.x == food.x && snakeHead.y == food.y) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

    
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }
        if (!snakeBody.isEmpty()) {
            snakeBody.get(0).x = snakeHead.x;
            snakeBody.get(0).y = snakeHead.y;
        }

      
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

      
        for (Tile t : snakeBody) {
            if (t.x == snakeHead.x && t.y == snakeHead.y) {
                gameOver = true;
                return;
            }
        }

      
        if (snakeHead.x < 0 || snakeHead.x >= numTilesX ||
                snakeHead.y < 0 || snakeHead.y >= numTilesY) {
            gameOver = true;
        }
    }

    
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            init();  
            invalidate();
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float deltaX = event.getX() - touchX;
                float deltaY = event.getY() - touchY;

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    
                    if (deltaX > 0 && velocityX != -1) {
                        velocityX = 1; velocityY = 0;
                    } else if (deltaX < 0 && velocityX != 1) {
                        velocityX = -1; velocityY = 0;
                    }
                } else {
                    // Vertical swipe
                    if (deltaY > 0 && velocityY != -1) {
                        velocityX = 0; velocityY = 1;
                    } else if (deltaY < 0 && velocityY != 1) {
                        velocityX = 0; velocityY = -1;
                    }
                }
                break;
        }
        return true;
    }
}
