import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.*;


public class GUI extends JFrame {

    final int spacing =1;
    final int sqd=60;
    public int X=-100;
    public int Y=-100;

    int nNeighbors=0;
    Random rand = new Random();
    int mines[][] = new int[12][6]; //=0 if does not have a mine, 1 if has a mine
    int neighbors[][]=new int[12][6];
    boolean revealed [][]= new boolean[12][6];
    boolean flagged[][]= new boolean[12][6];



    public GUI(){




        this.setTitle("Minesweeper");
        this.setSize(12*sqd +16 ,7*sqd+34);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        for(int i=0;i<12;i++){
            for(int j=0;j<6;j++){
                if(rand.nextInt(100)<20){ //~20% chance of being a bomb
                    mines[i][j]=1;
                }else{
                    mines[i][j]=0;
                }
                revealed[i][j]=false;
            }

        }

        for(int i=0;i<12;i++){
            for(int j=0;j<6;j++){
                nNeighbors=0;
                for(int m=0;m<12;m++){
                    for(int n=0;n<6;n++){
                        if(isNeighbor(i,j,m,n)==true){
                            nNeighbors++;
                        }
                    }
                    neighbors[i][j]=nNeighbors;
                }

            }

        }

        Board board = new Board();
        this.setContentPane(board);


        //add mouse eventlisetener
        Move move = new Move();
        this.addMouseMotionListener(move);
        Click click = new Click();
        this.addMouseListener(click);

    }

    public class Board extends JPanel{
        public void paintComponent(Graphics g){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0,0,12*sqd,7*sqd);

            for(int i=0;i< 12;i++){
                for(int j=0;j<6;j++){
                    g.setColor(Color.GRAY);
                    if(mines[i][j]==1){
                       g.setColor(Color.YELLOW);
                    }
                    if(revealed[i][j]==true){
                        g.setColor(Color.WHITE);
                        if(mines[i][j]==1){
                            g.setColor(Color.RED);
                        }
                    }
                    if(X>=spacing+i*sqd +3
                            && X<i*sqd+sqd-spacing +3 &&
                            Y<j*sqd+26+sqd+sqd-2*spacing &&
                            Y>=spacing+j*sqd+sqd+26){
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(spacing+i*sqd, spacing+j*sqd+sqd, sqd-2*spacing, sqd-2*spacing);
                    if(revealed[i][j] == true){
                        g.setColor(Color.BLACK);
                        if(mines[i][j]==0 && neighbors[i][j]!=0){
                            switch (neighbors[i][j]){
                                case 1:
                                    g.setColor(Color.BLUE);
                                    break;
                                case 2:
                                    g.setColor(Color.green);
                                    break;
                                case 3:
                                    g.setColor(Color.red);
                                    break;
                                case 4:
                                    g.setColor(new Color(0,0,128));
                                    break;
                                case 5:
                                    g.setColor(new Color(178,34,34));
                                    break;
                                case 6:
                                    g.setColor(new Color(72, 209,204));
                                    break;
                                case 7:
                                    g.setColor(Color.DARK_GRAY);
                                    break;
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD,sqd/2));
                            g.drawString(Integer.toString(neighbors[i][j]),i*sqd+22,j*sqd+sqd+40);
                        }else{

                            g.fillRect(i*sqd + 10 + 10, j*sqd+sqd +10 , 20,40);
                            g.fillRect(i*sqd +10, j*sqd+sqd+10+10, 40,20);
                            g.fillRect(i*sqd+10+5, j*sqd+sqd+10+5, 30,30);
                            for(int a=0;a<12;a++){
                                for(int b=0;b<6;b++){
                                    if(mines[a][b]==1){
                                        revealed[a][b]=true;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public class Move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            X=e.getX();
            Y=e.getY();
//            System.out.println("Mouse Moved");
//
//            System.out.println(X+","+Y);
        }
    }

    public class Click implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(inBoxX()!=-1 && inBoxY()!=-1){
                revealed[inBoxX()][inBoxY()]=true;
                System.out.println("Mouse Clicked in a box ["+inBoxX()+","+inBoxY()+"], numeber of mines are "+neighbors[inBoxX()][inBoxY()]);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public int inBoxX(){
        for(int i=0;i< 12;i++) {
            for (int j = 0; j < 6; j++) {
                if(X>=spacing+i*sqd+3
                        && X<i*sqd+sqd-spacing+3 &&
                        Y<+j*sqd+26+sqd+sqd-2*spacing &&
                        Y>=spacing+j*sqd+sqd+26){
                    return i;
                }
            }
        }
        return -1;
    }

    public int inBoxY(){
        for(int i=0;i< 12;i++) {
            for (int j = 0; j < 6; j++) {
                if(X>=spacing+i*sqd+3
                        && X<i*sqd+sqd-spacing+3 &&
                        Y<j*sqd+26+sqd+sqd-2*spacing &&
                        Y>=spacing+j*sqd+sqd+26){
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isNeighbor(int mx, int my, int cx, int cy){
        if(mx-cx<2 && mx -cx>-2 && my-cy<2 && my-cy>-2 && mines[cx][cy]==1){
            return true;
        }
        return false;
    }
}
