import java.awt.*; // Abstract Window Toolkit
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener { 

    private class Tile{ // adjust size of objects
        int x;
        int y;
        
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    Random random;
    int boardWidth;
    int boardHeight;
    int velocityX;
    int velocityY;
    int tileSize = 25; // size of each frame is 25px.

    //Create Snake Head
    Tile snakeHead;
    ArrayList<Tile> snakeBody; //A list to store all the snake's body parts.
    // Create food
    Tile food;
    Timer gameLoop;

    boolean gameOver = false;

    public SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this); 
        setFocusable(true); //Allows focus on keyboard events.

        snakeHead = new Tile(5, 5); // start the game in here.
        snakeBody = new ArrayList<Tile>(); //A list to store all the snake's body parts.

        food = new Tile(10, 10); // place of first food.
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames.
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        //Create Grid
        for (int i = 0; i < boardWidth/tileSize; i++) {
            // int x1, int y1, int x2, int y2 
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        //Create Snake's proporties
        g.setColor(Color.green);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize); //fillRect = Fills the specified rectangle

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
        }

        //Create Food's proporties + change color 
        Random random = new Random();
        Color randomColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
        g.setColor(randomColor);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
	}

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        // eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        // move snake body 
        for (int i = snakeBody.size()-1; i >= 0; i--) {

            Tile snakePart = snakeBody.get(i);
            if(i == 0){ // if it is head of snake.
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else { //Each segment moves to the coordinates of the previous segment.
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
            
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game Over Conditions 
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth ||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

        @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
    }

    // we do not need have these methods.
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
