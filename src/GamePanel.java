import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 200;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel()
    {
        random = new Random();
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        y[0] = SCREEN_HEIGHT / 2;
        startGame();
    }
    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                //g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                //g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            //Draws wire frame

            //drawApple

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //drawSnake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.white);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else if (i % 2 == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.green);
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); //Colorful snake mode
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.yellow);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + Integer.toString(applesEaten), (SCREEN_WIDTH - metrics.stringWidth("Score: " + Integer.toString(applesEaten))) / 2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple()
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

        for(int i = bodyParts; i > 0; i--) //Checks if food is in snake, Do not set food onto snake
        {
            if((appleX == x[i]) && appleY == y[i])
            {
                newApple();
            }
        }
    }
    public void move()
    {
        for(int i = bodyParts; i > 0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction)
        {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple()
    {
        if((x[0] == appleX) && y[0] == appleY)
        {
            newApple();
            applesEaten++;
            bodyParts++;
        }
    }
    public void checkCollisions()
    {
        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--)
        {
            if((x[0] == x[i]) && y[0] == y[i])
            {
                running = false;
            }
        }

        //check if head touches left border
        if(x[0] < 0)
        {
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH)
        {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0)
        {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT)
        {
            running = false;
        }
        if(!running)
        {
            timer.stop();
        }
    }
    public void gameOver(Graphics g)
    {
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ariel", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        //Display Score
        g.setColor(Color.yellow);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + Integer.toString(applesEaten), (SCREEN_WIDTH - metrics.stringWidth("Score: " + Integer.toString(applesEaten))) / 2, g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_A:
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
