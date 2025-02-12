/*
* Author:  Donoven Nicolas, dnicolas2021@fit.edu
* Course:  CSE 2010, Section 03, Spring 2024
* Project: Proj 22, Snake
*/

import java.util.ArrayList;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Snake {
   private static class Tile {
      int x;
      int y;

      Tile(final int x, final int y) {
         this.x = x;
         this.y = y;
      }
   }
   // Simple and easy implementation of the class as a data structure

   static final int WIDTH = 1200;
   static final int HEIGHT = 600;
   // Windows pixel size
   static final int STUD_X = 25;
   static final int STUD_Y = 20;
   // Dimensions of a tile to multiply x and y by

   static Tile snakeHead;
   static ArrayList<Tile> snakeBody;

   static Tile food;
   static Random random;

   static int velocityX;
   static int velocityY;

   static Boolean gameOver = false;

   static final int UPPER_BOUND_X = 43;
   static final int LOWER_BOUND_X = 5;
   static final int UPPER_BOUND_Y = 26;
   static final int LOWER_BOUND_Y = 4;

   public static void drawBoard () {
      StdDraw.clear(StdDraw.BLACK);
      StdDraw.setPenColor(StdDraw.WHITE);
      final double penr = 0.02;
      StdDraw.setPenRadius(penr);
      final int bariX = 500;
      final int bariY = 250;
      StdDraw.rectangle(WIDTH/2, HEIGHT/2, bariX, bariY);
      // Draws our gameboard 
   }

   public static void snakeGame () {
      snakeHead = new Tile(10, 10);
      snakeBody = new ArrayList<Tile>();
      final int ranX = 12;
      final int ranY = 13;
      food = new Tile(ranX, ranY);
      random = new Random();
      placeFood();
      // Makes a bunch of objects and then generates the foods first placement
   }
   
   public static void drawSnakeStart () {
      // Food Being Drawn 
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.filledRectangle(food.x * STUD_X, food.y * STUD_Y, STUD_X/2, STUD_Y/2);

      // Snakes Head Being Drawn
      StdDraw.setPenColor(StdDraw.GREEN);
      StdDraw.filledRectangle(snakeHead.x * STUD_X, snakeHead.y * STUD_Y, STUD_X/2, STUD_Y/2);
                     
      // Snakes Body Being Drawn
      for (int i = 0; i < snakeBody.size(); i++) {
         final Tile snakePart = snakeBody.get(i);
         StdDraw.filledRectangle(snakePart.x * STUD_X, snakePart.y * STUD_Y, 
                                                      STUD_X/2, STUD_Y/2);
      }
   }

   public static void placeFood () {
      food.x = random.nextInt(UPPER_BOUND_X - LOWER_BOUND_X) + LOWER_BOUND_X;
      food.y = random.nextInt(UPPER_BOUND_Y - LOWER_BOUND_Y) + LOWER_BOUND_Y;
      // Generates the food within the playing field
   }

   public static boolean collision (final Tile t1, final Tile t2) {
      return t1.x == t2.x && t1.y == t2.y;
      // Tests for tile overlaps
   }

   public static void move () {
      if (collision(snakeHead, food)) {
         snakeBody.add(new Tile(food.x, food.y));
         placeFood();
         // Eating the food and replacing it
      }

      // Snake Body Movement
      for (int i = snakeBody.size()-1; i >= 0; i--) {
         final Tile snakePart = snakeBody.get(i);
         if (i == 0) {
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
         }  else {
            final Tile prevSnakePart = snakeBody.get(i-1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
         }
      }

      for (int i = 1; i < snakeBody.size(); i++) {
         final Tile snakePart = snakeBody.get(i);
         if (collision(snakeHead, snakePart)) {
            gameOver = true;
            break;
            // Head to body game end condition
         }
      }

      if (snakeHead.x == LOWER_BOUND_X - 1 || snakeHead.x == UPPER_BOUND_X + 1 
            || snakeHead.y == LOWER_BOUND_Y - 1 || snakeHead.y == UPPER_BOUND_Y + 1) {
         gameOver = true;
         // Wall boundaries
      }

      if (StdDraw.isKeyPressed(KeyEvent.VK_UP) && velocityY != -1) {
         velocityX = 0;
         velocityY = 1;  
         // Move up
      }  else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && velocityY != 1) {
         velocityX = 0;
         velocityY = -1; 
         // Move down
      }  else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && velocityX != 1) {
         velocityX = -1; 
         velocityY = 0;
         // Move left
      }  else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && velocityX != -1) {
         velocityX = 1;
         velocityY = 0;
         // Move right
      }
   }

   public static void gameStart () {
      StdDraw.setPenColor(StdDraw.WHITE);
      // For the Score
      StdDraw.enableDoubleBuffering();
      final int x = 3;
      final int y = 29;
      // For the magic numbers
      while (!gameOver) {
         drawBoard();
         move();
         // This is really laggy and I dont really know why
         // outside it being a massive method that holds literally everything
         StdDraw.text(x * STUD_X, y * STUD_Y, "Score: " + snakeBody.size());
         snakeHead.x += velocityX;
         snakeHead.y += velocityY;
         drawSnakeStart();
         StdDraw.show();
         StdDraw.pause(100);
      }
   }

   public static void main (final String[] args) {
      StdDraw.setCanvasSize(WIDTH, HEIGHT);
      StdDraw.setXscale(0, WIDTH);
      StdDraw.setYscale(0, HEIGHT);

      drawBoard();
      snakeGame();
      drawSnakeStart();
      gameStart();

      if (gameOver) {
         StdDraw.clear(StdDraw.BLACK);
         StdDraw.setPenColor(StdDraw.RED);
         StdDraw.text(WIDTH/2, HEIGHT/2, "Game Over: " + snakeBody.size());
         StdDraw.show();
      }
   }
}
