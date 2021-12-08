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
	private int size = 5;
	private int x[][] = new int[size+1][size+1];
	private int y[][] = new int[size+1][size+1];
	private int currentpointx;
	private int currentpointy;
	//private int controlpoints[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	private boolean selected;
	private boolean firsttime;
	private boolean showwarp = false;
	private boolean showSelected = false;
	private BufferedImage bim = null;
	private BufferedImage bim2 = null;
	private BufferedImage bimwarp = null;
	private Triangle S, T;
	private int xsize, ysize;
	private int imgStart = 0;
	private boolean isAnimate = false;

	private final int THRESHOLD_DISTANCE = 4;

	// constructor: creates an empty mesh point
	public MeshCanvas() {
		setSize(getPreferredSize());
		addMouseListener(this);
		addMouseMotionListener(this);
		selected = false;
		firsttime = true;
		isAnimate = false;
		System.out.println("Mesh");
	}


	// resets mesh point to center
	public void clear() {
		selected = false;
		firsttime = true;
		showwarp = false;
		drawMesh();
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
		showSelected = false;
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

private BufferedImage readImage(String file) {

   Image image = Toolkit.getDefaultToolkit().getImage(file);
	//Image image2 = Toolkit.getDefaultToolkit().getImage(file2);
   MediaTracker tracker = new MediaTracker (new Component () {});
   tracker.addImage(image, 0);
//	tracker.addImage(image, 1);
   try { tracker.waitForID (0); }
   catch (InterruptedException e) {}
      BufferedImage bim = new BufferedImage 
          (image.getWidth(this), image.getHeight(this), 
          BufferedImage.TYPE_INT_RGB);
   Graphics2D big = bim.createGraphics();
   big.drawImage (image, imgStart, 0, this);
   //big.drawImage (image2, image.getWidth(this), 0, this);
   return bim;
}

public void setImage(String file) {
   bim = readImage(file);
//   setSize(new Dimension(bim.getWidth(), bim.getHeight()));
   imgStart = bim.getWidth();
	//bim2 = readImage(file2);
	setSize(new Dimension(bim.getWidth()/2, bim.getHeight()/2));
   this.repaint();
}/*
public MeshCanvas animate(){
		for(int i = 0; i<size;i++){
			for(int j = 0; j<size;j++){

			}
		}
}*/
	public void setx(int xIn[][]){
		for(int i = 0; i <=5; i++){
			for(int j =0; j<=5;j++){
				x[i][j] = xIn[i][j];
			}
		}
	}
	public void sety(int yIn[][]){
		for(int i = 0; i <=5; i++){
			for(int j =0; j<=5;j++){
				y[i][j] = yIn[i][j];
			}
		}
	}
public void makeWarp() {

	if (bimwarp == null)
		bimwarp = new BufferedImage(bim.getWidth(this),
				bim.getHeight(this),
				BufferedImage.TYPE_INT_RGB);


// Divide the image into 4 triangles defined by the one point
// out in the image somewhere.
// MorphTools has to be set up NOT to clear the destination image
// each time it is called.
	// public Triangle (int x1, int y1, int x2, int y2, int x3, int y3)
	// top triangle
	/*
	S = new Triangle (0, 0, xsize/2, ysize/2, xsize, 0);
	T = new Triangle (0, 0, x, y, xsize, 0);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);
	// left triangle
	S = new Triangle (0, 0, 0, ysize, xsize/2, ysize/2);
	T = new Triangle (0, 0, 0, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);
	//right triangle
	S = new Triangle (0, ysize, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (0, ysize, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (xsize, 0, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (xsize, 0, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);
	*/


	for (int i = 0; i < 5; i++) {
		for(int j = 0; j<5;j++) {
			S = new Triangle((xsize/5)*j, (ysize/5)*i, (xsize/5)*(j+1), (xsize/5)*i, (xsize/5)*j, (ysize/5)*(i+1));
			T = new Triangle((xsize/5)*j, (ysize/5)*i, (xsize/5)*currentpointx, (ysize/5)*currentpointy, (xsize/5)*j, (ysize/5)*(i+1));
			MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

			S = new Triangle((xsize/5)*(j+1), (ysize/5)*i,(xsize/5)*j, (ysize/5)*(i+1), (xsize/5)*(j+1), (ysize/5)*(i+1));
			T = new Triangle((xsize/5)*(j+1), (ysize/5)*i, (xsize/5)*currentpointx, (ysize/5)*currentpointy, (xsize/5)*(j+1), (ysize/5)*(i+1));
			MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);
		//	this.repaint();
		}
	}
	showwarp=true;
	this.repaint();
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
				x[i][j]= (xsize/size)*j;  y[i][j] = (ysize/size)*i; firsttime=false;
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
				//x[i][j] = (xsize/5)*j;
			//	y[i][j] = (ysize/5)*i;
			//	g.drawLine((xsize/5)*j,(ysize/5)*i,x,y);
				/*g.drawLine((xsize/5)*j,(ysize/5)*i,(xsize/5)*j,(ysize/5)*(i+1));
				g.drawLine((xsize/5)*j,(ysize/5)*i,(xsize/5)*(j+1),(ysize/5)*i);
				g.drawLine((xsize/5)*j,(ysize/5)*i,(xsize/5)*(j+1),(ysize/5)*(i+1));
				g.fillOval((xsize/5)*j-5, (ysize/5)*i-6, 12, 12);*/
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
					g.setColor(Color.BLACK);
					g.fillOval(x[i][j] - 5, y[i][j] - 6, 12, 12);
				}
				//g.fillOval(x[i][5]-5, y[i][5]-6, 12, 12);
				//g.fillOval(x[5][j]-5, y[5][j]-6, 12, 12);
			}

		}
	  /*
	   g.drawLine(0,0,x,y);
	   g.drawLine(0,ysize,x,y);
	   g.drawLine(xsize,0,x,y);
	   g.drawLine(xsize,ysize,x,y);
	   */

	   // circle at point
	  // g.fillOval(x-6, y-6, 12, 12);
	}

}  //paint();


}
