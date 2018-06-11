
package shnake;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
class SnakeConstants {
    public final static int WIDTH = 20;
    public final static int GRID_WIDTH = 50;
    public final static int GRID_HEIGHT = 30;
    public final static int DELAY = 80;
    public final static int MAX_LEVEL = 10;
}
abstract class Shape{
    
    protected int x;
    protected int y;
    public Shape(int x, int y){
        this.x=x*SnakeConstants.WIDTH;
        this.y=y*SnakeConstants.WIDTH;
    }
    
    public int getX() {
        return x/SnakeConstants.WIDTH;
    }
    
    public int getY(){
        return y/SnakeConstants.WIDTH;
    }
    public abstract void paint(Graphics g);

    }
class Square extends Shape{
    
    public Square(int x,int y){
        super(x,y);
        
    }public void paint(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(x, y, SnakeConstants.WIDTH, SnakeConstants.WIDTH);
    }
   
     
}
class Food extends Shape{

    public Food(int x,int y){
        super(x,y);   
    }
    public void paint(Graphics g){
        g.setColor(Color.RED);
    g.fillOval(x+SnakeConstants.WIDTH/4, y+SnakeConstants.WIDTH/4, SnakeConstants.WIDTH/2, SnakeConstants.WIDTH/2);
}
    
    
}
enum Direction {TOP, BOTTOM, RIGHT, LEFT};
class Snake{
    private ArrayList<Square> sq=new ArrayList();
    private Direction dir =Direction.RIGHT;
    public Snake(){
        Square s =new Square(SnakeConstants.GRID_WIDTH/2,SnakeConstants.GRID_HEIGHT/2);
        sq.add(s);
        for(int i=0;i<3;i++)
            grow();
    }
    public void grow(){
        Square tail=sq.get(0);Square n ;
        switch (dir) {
            case RIGHT:
                if (tail.getX()+1 <= SnakeConstants.GRID_WIDTH) {
                    n = new Square(tail.getX() + 1, tail.getY());
                } else {
                    n = new Square(0, tail.getY());
                }
                if(collidesnake(n)){
                    JOptionPane.showMessageDialog(null,"Game Over");
                    System.exit(0);
                }
                sq.add(0, n);
                break; 
            case LEFT:
                if (tail.getX()-1 < 0) {
                    n = new Square(SnakeConstants.GRID_WIDTH-1, tail.getY());
                } else {
                    n = new Square(tail.getX()-1, tail.getY());
                }
                 if(collidesnake(n)){
                    JOptionPane.showMessageDialog(null,"Game Over");
                    System.exit(0);
                }
                sq.add(0, n);
                break; 
            case TOP:
                if (tail.getY()-1 <0) {
                    n = new Square(tail.getX(), SnakeConstants.GRID_HEIGHT-1);
                } else {
                    n = new Square(tail.getX(), tail.getY()-1);
                }
                 if(collidesnake(n)){
                    JOptionPane.showMessageDialog(null,"Game Over");
                    System.exit(0);
                }
                sq.add(0, n);
                break; 
                case BOTTOM:
                if (tail.getY()+1 >SnakeConstants.GRID_HEIGHT) {
                    n = new Square(tail.getX(), 0);
                } else {
                    n = new Square(tail.getX(), tail.getY()+1);
                }
                 if(collidesnake(n)){
                    JOptionPane.showMessageDialog(null,"Game Over");
                    System.exit(0);
                }
               sq.add(0, n);
                break; 
        }
         
    }
    public boolean hasCollision(int x, int y){
        for(int i=0;i<sq.size();i++){
            Square s = (Square)sq.get(i);
            if(s.getX()==x && s.getY()==y)
                return true;
        }
        return false;
    }
    public void move(){
        grow();
        sq.remove(sq.size()-1);
    }
    public void paint(Graphics g){
        for(int i=0;i<sq.size();i++){
            sq.get(i).paint(g);
        }
    }
    public Direction getOpDir(Direction dir){
        Direction d=Direction.RIGHT;
        switch(dir){
            case TOP:
                d=Direction.BOTTOM;
                break;
            case BOTTOM:
                d=Direction.TOP;
                break;
            case RIGHT:
                d=Direction.LEFT;
                break;
            case LEFT:
                d=Direction.RIGHT;
                break;
        }
        return d;
    }
    public void setDir(Direction d){
        if(d!=getOpDir(this.dir))
        this.dir=d;
    }
  
    
    public boolean collidesnake(Square n){
        for (int i = 0; i < sq.size(); i++) {
                if(n.getX()==sq.get(i).getX()&&n.getY()==sq.get(i).getY())
                    return true;
        }
        return false;
    }
     public int getX(){
        return ((Square)sq.get(0)).getX();
    }
    
    public int getY(){
        return ((Square)sq.get(0)).getY();        
    }
}



class draw extends JPanel{
    private Snake sn;
    private Food f;
    public draw(Snake sn,Food f){
         this.sn=sn;
         this.f=f;
      }
    public void paint(Graphics g){
        super.paint(g);
        f.paint(g);
        sn.paint(g);
    }
    public boolean iscollided(){
        if(sn.getX()==f.getX()&&sn.getY()==f.getY())
            return true;
        else
            return false;
    }
    public void setFood(Food fd){
        this.f = fd;
    }
}
public class Shnake extends JFrame{
    private draw snakepnl ;
    private JPanel labels =new JPanel();
    private JLabel score =new JLabel("Score=");
    private JLabel level =new JLabel("Level=");
    private int Score =0;
    private int Level =0;
    private Snake sk=new Snake();
    private Food fo;
    private Random rand;
    private javax.swing.Timer t;
    private int eatCount;private int delay=SnakeConstants.DELAY;
    public Food generateFood(){
        int x = rand.nextInt(SnakeConstants.GRID_WIDTH);
        int y = rand.nextInt(SnakeConstants.GRID_HEIGHT);
        while(sk.hasCollision(x, y)){
            x = rand.nextInt(SnakeConstants.GRID_WIDTH);
            y = rand.nextInt(SnakeConstants.GRID_HEIGHT);
        }
        Food f = new Food(x,y);
        return f;
    }
    public Shnake(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        score.setPreferredSize(new Dimension(100,30));
        level.setPreferredSize(new Dimension(100,30));
        rand=new Random();
        fo=generateFood();
       
        snakepnl=new draw(sk,fo);
        snakepnl.setPreferredSize(new Dimension(SnakeConstants.GRID_WIDTH*SnakeConstants.WIDTH,SnakeConstants.GRID_HEIGHT*SnakeConstants.WIDTH));
        labels.add(score);
        labels.add(level);
        this.add(labels,BorderLayout.SOUTH);
        snakepnl.setBackground(Color.yellow);
        this.add(snakepnl);
        this.setResizable(false);
        this.pack();
        snakepnl.setFocusable(true);
        t=new javax.swing.Timer(SnakeConstants.DELAY,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sk.move();
                if (snakepnl.iscollided()) {
                    eatFood();
                }
                snakepnl.repaint();
            }
        });
        t.start();
        snakepnl.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode()==KeyEvent.VK_UP)
                    sk.setDir(Direction.TOP);
                if(e.getKeyCode()==KeyEvent.VK_DOWN)
                    sk.setDir(Direction.BOTTOM);
                if(e.getKeyCode()==KeyEvent.VK_LEFT)
                    sk.setDir(Direction.LEFT);
                if(e.getKeyCode()==KeyEvent.VK_RIGHT)
                    sk.setDir(Direction.RIGHT);
            }
        });
    }
    public void eatFood(){
        eatCount++;
        Score+=10;
        sk.grow();
        if(eatCount==5&&Level<SnakeConstants.MAX_LEVEL){
            eatCount=0;
            delay-=10;
            t.setDelay(delay);
            Level++;
        }
        if(Level==SnakeConstants.MAX_LEVEL){
            JOptionPane.showMessageDialog(null, "Congratulations!");
            System.exit(0);
        }
        score.setText("Score="+Score);
        level.setText("Level ="+Level);
        fo=generateFood();
        snakepnl.setFood(fo);
        snakepnl.repaint();
    }
    public static void main(String[] args) {
      Shnake sh=new Shnake();
      sh.setVisible(true);
    }
    
}
