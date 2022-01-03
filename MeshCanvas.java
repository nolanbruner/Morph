// Mesh and Warp Canvas


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
//import com.sun.image.codec.jpeg.*;


public class MeshCanvas extends Canvas
             implements MouseListener, MouseMotionListener {
	//to change the number of control points, change size
	private int size =21 ;
	private int x[][] = new int[size+1][size+1];
	private int y[][] = new int[size+1][size+1];
	private int currentpointx;
	private int currentpointy;
	private boolean selected;
	private boolean firsttime;
	private boolean showwarp = false;
	private String File;

	private BufferedImage bim = null;

	private BufferedImage bimwarp = null;


	private int xsize, ysize;
	private int imgStart = 0;
	private boolean isAnimate = false;

	private final int THRESHOLD_DISTANCE = 4;

	// constructor: creates an empty mesh point
	public MeshCanvas(int Size) {
		setSize(getPreferredSize());
		addMouseListener(this);
		addMouseMotionListener(this);
		selected = false;
		firsttime = true;
		isAnimate = false;
		size = Size;

	}
	public void setSize(int Size){
		size = Size;
	//	paint(getGraphics());
	}

	// resets mesh point to center
	public void clear() {
		selected = false;
		firsttime = true;
		showwarp = false;
		drawMesh();

	}
	public void setBim(BufferedImage b){
		bim = b;
	}
	public int[][] getx(){
		//System.out.println(x);
		return x;
	}
	public int[][] gety(){
		//System.out.println(y);
		return y;
	}
	public void mouseClicked(MouseEvent mevt) {

	}

	public void mouseEntered(MouseEvent mevt) {
	}

	public void mouseExited(MouseEvent mevt) {
	}
	public boolean isSelected(){
		return selected;
	}
	public void setSelected(boolean isS){
		selected = isS;
	}
// checks for user selecting the mesh control point within a threshold
// distance of the point

	public void mousePressed(MouseEvent E) {
		int curx, cury;

		curx = E.getX();
		cury = E.getY();
			for(int i = 0; i<=size;i++) {
				for (int j = 0; j <= size; j++) {
					double distance = Math.sqrt((curx - x[i][j]) * (curx - x[i][j]) + (cury - y[i][j]) * (cury - y[i][j]));
						if (distance < THRESHOLD_DISTANCE) {
						selected = true;
						//showSelected = true;
						//isSelected(true);
						firsttime = false;
						currentpointx = j;
						currentpointy = i;
						System.out.println(i+":"+j);
					 	}
				}
			}

	}

// if a point is being dragged, the point is released
// otherwise, adds/removes a point at current position
public void mouseReleased(MouseEvent E) {

	// if a point was selected, it was just released
	if(selected) {
		selected = false;

	}
	//setSelected(false);
	update(getGraphics());
	//makeWarp();
}


// if a point is selected, drag it
public void mouseDragged(MouseEvent E) {

	// if a point is selected, it's being moved
	// redraw (rubberbanding)
	if(selected &&!isAnimate) {
		//repaint();
		x[currentpointy][currentpointx]=E.getX();
		y[currentpointy][currentpointx]=E.getY();

                // Can show warp dynamically by unstubbing here
                // This will apply the warp during rubberbanding
	        //makeWarp();
		drawMesh();
	}
}

public void mouseMoved(MouseEvent mevt) {
}


// draws the mesh on top of the background image
public void drawMesh() {
	repaint();
}

public BufferedImage readImage(String file) {

   Image image = Toolkit.getDefaultToolkit().getImage(file);
	image = image.getScaledInstance(500,500,Image.SCALE_DEFAULT);
   MediaTracker tracker = new MediaTracker (new Component () {});
   tracker.addImage(image, 0);

   try { tracker.waitForID (0); }
   catch (InterruptedException e) {}
      BufferedImage bim = new BufferedImage
          (image.getWidth(this), image.getHeight(this), 
          BufferedImage.TYPE_INT_RGB);
   Graphics2D big = bim.createGraphics();
   big.drawImage (image, imgStart, 0, this);
   return bim;
}

public void setImage(String file) {
		File = file;
   bim = readImage(file);
//   setSize(new Dimension(bim.getWidth(), bim.getHeight()));
  // imgStart = bim.getWidth();
	setSize(new Dimension(500,500));
	//bim2 = readImage(file2);
//	setSize(new Dimension(bim.getWidth()/2, bim.getHeight()/2));
   this.repaint();
}
	public BufferedImage getBim(){
		return bim;
	}
	public void setx(int xIn[][]){
		for(int i = 0; i <=size; i++){
			for(int j =0; j<=size;j++){
				x[i][j] = xIn[i][j];
			}
		}
	}
	public void sety(int yIn[][]){
		for(int i = 0; i <=size; i++){
			for(int j =0; j<=size;j++){
				y[i][j] = yIn[i][j];
			}
		}
	}

// Over-ride update method 
public void update(Graphics g) {
	paint(g);
}

public int getCurrentx(){
	return currentpointx;
	}
public int getCurrentpointy(){
	return currentpointy;
	}
public void paintDot(Graphics g,int cx,int cy){

		g.setColor(Color.RED);
		g.fillOval(x[cy][cx]-5, y[cy][cx]-6, 12, 12);
		//repaint();

}
public void animate(boolean b){
		isAnimate = b;
}

// paints the polyline
public void paint (Graphics g) {

	// draw lines from each corner of canvas to the mesh point
	// with a circle at the mesh point

	xsize=getWidth();
	ysize=getHeight();

	if (firsttime&&!isAnimate) {
		for(int i = 0; i<=size;i++){
			for(int j=0;j<=size;j++){
				x[i][j]= (xsize/size)*j;  y[i][j] = (ysize/size)*i; //firsttime=false;
			}
		}
	}

	Graphics2D big = (Graphics2D) g;

	if (showwarp)
           big.drawImage(bimwarp, 0, 0, this);
	else {
           big.drawImage(bim, 0, 0, this);
		for(int i =0;i<=size;i++){
			for(int j = 0; j<=size;j++){
				g.setColor(Color.BLACK);

				if(i!=size&&j!=size) {
					g.drawLine(x[i][j], y[i][j], x[i + 1][j], y[i + 1][j]);
				//	System.out.println("From ("+x[i][j]+":"+y[i][j]+") to ("+x[i+1][j]+":"+y[i+1][j]+")");
					g.drawLine(x[i][j], y[i][j], x[i][j + 1], y[i][j + 1]);
					g.drawLine(x[i][j], y[i][j], x[i + 1][j + 1], y[i + 1][j + 1]);
				}
				else if(i==size && j<size){
					g.drawLine(x[i][j], y[i][j], x[i][j + 1], y[i][j + 1]);
				}
				else if(j==size && i<size){
					g.drawLine(x[i][j], y[i][j], x[i + 1][j], y[i + 1][j]);
				}
				if(selected&&currentpointy==i&&currentpointx==j) {
					paintDot(g, currentpointx,currentpointy);
				}
				else{
					g.setColor(Color.GREEN);
					g.fillOval(x[i][j] - 5, y[i][j] - 6, 12, 12);
					g.setColor(Color.BLACK);
				}

			}

		}

	}

}  //paint();


}
