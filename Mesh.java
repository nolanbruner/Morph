import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.image.RescaleOp;
//import javax.media.jai.PlanarImage;

public class  Mesh extends JFrame implements ActionListener{


private MeshCanvas firstMesh;
private MeshCanvas secondMesh;
private MeshCanvas animatedMesh;
private JPanel mesh1;
private JPanel mesh2;
private JPanel controlP;
private JPanel animatedP;
private JButton clearB;
private JButton warpB;
private JButton animate;
private JPanel positionP;
private JLabel xposL;
private JLabel yposL;
private JFrame animateWindow;

private JSlider slider;
private Timer timer;
private Timer timer2;
private double t;
private double time;
private JButton start;
private JLabel label;

private String[] images;
private String FirstImage;
private String SecondImage;
private int Size = 8;
private BufferedImage newImg;
private ArrayList<BufferedImage> bImgs ;
private int k =1;
// keeps up with old values for meshing
public 	int oldx[][];
public  int oldy[][];
// constructor: creates GUI

public Mesh() {
	super("Simple Mesh");
	Container c = getContentPane();         // create container
	c.setBackground(new Color(232, 232, 232));
	c.setForeground(new Color(0, 0, 0));
	c.setLayout(new BorderLayout());

	//Start menu items
	images = new String[]{"Tony4.JPG","Tony3.JPG", "Tony2.jpg","Tony.PNG","ironman.PNG", "GoT1.jpg","GoT2.jpg","test.jpg", "boat2.jpg"};
	FirstImage = images[0];
	SecondImage = images[1];
	JMenuItem firstImages[] = new JMenuItem[images.length];
	JMenuItem secondImages[] = new JMenuItem[images.length];
	for(int i = 0; i< images.length;i++){
		firstImages[i] = new JMenuItem(images[i]);
		secondImages[i] = new JMenuItem(images[i]);
	}
	//JMenu menu = new JMenu("Menu");
	JMenuBar bar = new JMenuBar();
	JMenu select = new JMenu("Select");
	JMenu firstImage = new JMenu("First Image");
	JMenu secondImage = new JMenu("Second Image");
	JTextField sizeS = new JTextField("Type a size");
	JMenu size = new JMenu("Size");
	JMenuItem five = new JMenuItem("5");
	JMenuItem eight = new JMenuItem("8");
	sizeS.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		//	sizeS.setText(e.getActionCommand());
			int stringIn = Integer.parseInt(e.getActionCommand());
		//	System.out.println(e.getActionCommand());
			if(stringIn>5&&stringIn<20) {
				firstMesh.clear();
				secondMesh.clear();
				animatedMesh.clear();
				Size = stringIn;
				firstMesh.setSize(Size);
				firstMesh.paint(firstMesh.getGraphics());
				secondMesh.setSize(Size);
				secondMesh.paint(secondMesh.getGraphics());
//				if(animatedP.isVisible())
//				animatedMesh.setSize(Size);
			}
		}
	});
	size.add(sizeS);
	five.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//mesh.removeAll();
			firstMesh.clear();
			secondMesh.clear();
			animatedMesh.clear();
			Size = 5;
			firstMesh.setSize(5);
			firstMesh.paint(firstMesh.getGraphics());
			secondMesh.setSize(5);
			secondMesh.paint(secondMesh.getGraphics());
			animatedMesh.setSize(5);
		//	firstMesh.
			//createMesh();
		}
	});
	eight.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//mesh.removeAll();
			firstMesh.clear();
			secondMesh.clear();
			animatedMesh.clear();
			Size = 8;
			firstMesh.setSize(8);
			firstMesh.paint(firstMesh.getGraphics());
			secondMesh.setSize(8);
			secondMesh.paint(secondMesh.getGraphics());
			animatedMesh.setSize(8);
		//	firstMesh.setSize(8);
		//	secondMesh.setSize(8);
			createMesh();
		}
	});
	// creates an avtion listener for all first images
	for(int i = 0; i <firstImages.length;i++){
		firstImage.add(firstImages[i]);
		int finalI = i;
		firstImages[i].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FirstImage = images[finalI];
				firstMesh.clear();
				firstMesh.setImage(FirstImage);
				firstMesh.paint(firstMesh.getGraphics());
				repaint();
				System.out.println(FirstImage);
				//repaint();
			}
		});
	}
	// creates an action listener for all second images
	for(int i = 0; i <secondImages.length;i++){
		secondImage.add(secondImages[i]);
		int finalI = i;
		secondImages[i].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SecondImage = images[finalI];
				secondMesh.clear();
				secondMesh.setImage(SecondImage);
				repaint();
				System.out.println(SecondImage);
			}
		});
	}
	select.add(firstImage);
	select.add(secondImage);
	size.add(five);
	size.add(eight);
	bar.add(select);
	bar.add(size);
	setJMenuBar(bar);
	// end menu items
	JSlider brightness1 = new JSlider();
	brightness1.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			//createImage(fi)
			BufferedImage brightenedBuff= new BufferedImage(firstMesh.getWidth(),firstMesh.getHeight(),BufferedImage.TYPE_INT_RGB);
			//brightenedBuff.se
			float value = (float) slider.getValue();
			float scaleFactor = 2 * value / slider.getMaximum();
			RescaleOp op = new RescaleOp(scaleFactor, 0, null);
			firstMesh.setBim(op.filter(firstMesh.getBim(), firstMesh.getBim()));
			//firstMesh.setBim(brightenedBuff);
			repaint();
		}
	});
	JSlider brightness2 = new JSlider();
  	// Mesh Items
	mesh1 = new JPanel(new FlowLayout());
	firstMesh = new MeshCanvas(Size);
	firstMesh.setImage(FirstImage);
	mesh1.add(firstMesh, BorderLayout.EAST);
	mesh1.add(brightness1,BorderLayout.SOUTH);
	secondMesh = new MeshCanvas(Size);
	secondMesh.setImage(SecondImage);
	mesh2 = new JPanel(new FlowLayout());
	mesh2.add(secondMesh, BorderLayout.WEST);
	mesh2.add(brightness2,BorderLayout.SOUTH);
	//mesh.updateUI();
	bImgs = new ArrayList<>();
//	int oldx[][] = firstMesh.getx();
//	int oldy[][] = firstMesh.gety();

	time = 0;
	t = 50;
	timer = new Timer(100,null);
	timer.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		//	animatedMesh.clear();
			if(t>0&&time/t<=1){
		//		animatedMesh.clear();
				try {
					System.out.println(time/t);
					if (time < 2)
					composite((float) (time/t),firstMesh.getBim(),secondMesh.getBim());
					else
					composite((float) (time/t),bImgs.get(k-2),bImgs.get(k-1));


			} catch (IOException ex) {
					ex.printStackTrace();
				}
				try {
					animate();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				k++;
			}
			else
				timer.stop();
			time++;
			System.out.println(time);
		}
	});
	// repaint every 10 milliseconds
	timer2 = new Timer(10,null);
	timer2.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			paintd();
		}
	});
	timer2.start();
	animatedMesh = new MeshCanvas(Size);
	animatedMesh.setSize(500,500);
	c.add(mesh1, BorderLayout.WEST);
	c.add(mesh2,BorderLayout.EAST);
	//c.add(newMesh, BorderLayout.EAST);
	// add control buttons
	clearB = new JButton("Clear");
	clearB.addActionListener(this);
	warpB = new JButton("Warp");
	warpB.addActionListener(this);

	animate = new JButton("Animate");
	animate.addActionListener(this);
	start = new JButton("Start");
	start.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			time = 0;
			timer.restart();
			oldx = firstMesh.getx();
			oldy = firstMesh.gety();
			k = 0;
			//timer.start();

//			try {
//				composite((float) (time/t),firstMesh.getBim());
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}

		}
	});

	animateWindow = new JFrame();
	animatedP = new JPanel(new FlowLayout());

	animateWindow.setSize(600,700);
	animateWindow.setBackground(Color.white);

	slider = new JSlider();
	label = new JLabel("Frames per second");
	slider.add(label);
	slider.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			t = (double) slider.getValue();
			System.out.println(t);
		}
	});
	// add position labels
	positionP = new JPanel(new FlowLayout());

	controlP = new JPanel(new FlowLayout());
	controlP.setForeground(new Color(0, 0, 0));
	controlP.add(clearB);
	controlP.add(warpB);
	controlP.add(animate);
	//controlP.add(slider);
	c.add(controlP, BorderLayout.SOUTH);



	xposL = new JLabel("X: ");		// xposL used temporarily
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);
	xposL = new JLabel("000");		// real xposL
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);

	yposL = new JLabel("  Y: ");	// yposL used temporarily
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);
	yposL = new JLabel("000");		// real yposL
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);

	c.add(positionP, BorderLayout.NORTH);

	// keep track of mouse position
	firstMesh.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));

		}

		public void mouseDragged(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
			timer2.start();
			//paintd();
		//
		//
		}

	} );

	secondMesh.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));


		}

		public void mouseDragged(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
			timer2.start();
			//
			//
		}

	} );

	// allow use of "X" button to exit
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent winEvt) {
			setVisible(false);

			System.exit(0);
		}
	} );

} // Mesh ()
	public void createMesh() {
		//first mesh


		//morphMesh=secondMesh;
		//morphFrame.setVisible(true);
	}
	public void paintd() {
		if(secondMesh.isSelected()) {
				firstMesh.paintDot(firstMesh.getGraphics(), secondMesh.getCurrentx(), secondMesh.getCurrentpointy());
				firstMesh.repaint();
			}
		if(firstMesh.isSelected()) {
			secondMesh.paintDot(secondMesh.getGraphics(), firstMesh.getCurrentx(), firstMesh.getCurrentpointy());
			secondMesh.repaint();
		}

		}
		// need to find a way to create a new buffered image after every tweener
		// need a way to display the morph
// oldTs are initialized to image 1, then set to the newT's
private void writexy() throws FileNotFoundException {
	new FileOutputStream("old:"+firstMesh.getx() + ":" + firstMesh.gety() + "new:"+secondMesh.getx()+":"+secondMesh.gety());

}

public void morph(int newy[][],int newx[][],int k) throws IOException {

	//ArrayList<Triangle> oldT;
	BufferedImage NewImg;
	ArrayList<Triangle> newT = new ArrayList<>();
	if(k>1) {
		// create triangles for old frame and new frame
		for (int i = 0; i < newx.length - 1; i++) {
			for (int j = 0; j < newy.length - 1; j++) {
				Triangle D1 = new Triangle(newx[i][j], newy[i][j], newx[i][j + 1], newy[i][j + 1], newx[i + 1][j], newy[i + 1][j]);
				Triangle S1 = new Triangle(oldx[i][j], oldy[i][j], oldx[i][j + 1], oldy[i][j + 1], oldx[i + 1][j], oldy[i + 1][j]);
				MorphTools.warpTriangle(bImgs.get(k - 1), bImgs.get(k), S1, D1, null, null, false);

				Triangle D2 = new Triangle(newx[i + 1][j], newy[i + 1][j], newx[i][j + 1], newy[i][j + 1], newx[i + 1][j + 1], newy[i + 1][j + 1]);
				Triangle S2 = new Triangle(oldx[i + 1][j], oldy[i + 1][j], oldx[i][j + 1], oldy[i][j + 1], oldx[i + 1][j + 1], oldy[i + 1][j + 1]);
				MorphTools.warpTriangle(bImgs.get(k - 1), bImgs.get(k), S2, D2, null, null, false);
				System.out.println(newx[i][j] + ":" + newy[i][j]);

			}
		}
		System.out.println(k);
		NewImg = composite2((float) (time / t), bImgs.get(k));
		File f = new File("/Users/Nolan/Documents/CS335/frames/hh"+k+".jpeg");
		if(bImgs.size()>0)
			ImageIO.write(NewImg,"jpeg",f);
		//ffmeg -i image%d.jpg exercise10.mp4
	}
	else{
		File f = new File("/Users/Nolan/Documents/CS335/frames/hh"+k+".jpeg");
		if(bImgs.size()>0)
			ImageIO.write(bImgs.get(k),"jpeg",f);
	}
	// change new values to old values
	oldx = newx;
	oldy = newy;
}
// for creating frames
	public void composite(float alpha, BufferedImage b1,BufferedImage b2) throws IOException {
		newImg = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		BufferedImage img1 = firstMesh.getBim();
		BufferedImage img2 = secondMesh.getBim();
		System.out.println(alpha);
		Graphics2D big = newImg.createGraphics();

		big.drawImage(b1, 0, 0, this);
		AlphaComposite ac =
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		big.setComposite(ac);

		// Now draw over the already rendered compositing area
		// This last draw will be augmented by the alpha composite object
		big.drawImage(b2, 0, 0, this);
		bImgs.add(newImg);

	}
	// for compositing the morph and second image
	public BufferedImage composite2(float alpha, BufferedImage b1) throws IOException {
	newImg = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
	BufferedImage img2 = secondMesh.getBim();
	System.out.println(alpha);
	Graphics2D big = newImg.createGraphics();
	big.drawImage(b1, 0, 0, this);
	AlphaComposite ac =
			AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	big.setComposite(ac);
	// Now draw over the already rendered compositing area
	// This last draw will be augmented by the alpha composite object
	big.drawImage(img2, 0, 0, this);
	return newImg;
}



public void animate() throws IOException {

	int x1[][] =  firstMesh.getx();
	int x2[][] =  secondMesh.getx();
	int y1[][] = firstMesh.gety();
	int y2[][] = secondMesh.gety();
	int newx[][] = new int[Size+1][Size+1];
	int newy[][] = new int[Size+1][Size+1];
	animatedP.remove(animatedMesh);
	// calculate new grid
	for (int i = 0; i <= Size; i++) {
		for (int j = 0; j <= Size; j++) {
			newx[i][j] = (int) (x1[i][j] + ((time/t) * (x2[i][j] - x1[i][j])));
			newy[i][j] = (int) (y1[i][j] + ((time/t) * (y2[i][j] - y1[i][j])));
		}
	}

	morph(newy,newx,k);

	animatedP.add(animatedMesh);
	animatedMesh.setx(newx);
	animatedMesh.sety(newy);
	//animatedMesh.setSize(Size);
	animatedMesh.drawMesh();

}

// capture button actions
public void actionPerformed(ActionEvent evt) {
	Object src = evt.getSource();

	// reset the original image and show the mesh
	if(src == clearB) {
		firstMesh.clear();
		secondMesh.clear();
		animatedMesh.clear();
		timer.stop();
		timer2.stop();
		//createMesh();
	}
	if(src == animate){
		time = 0;
		k = 0;
		animatedMesh.setSize(Size);
		//animatedMesh.setSize();

		animateWindow.setVisible(true);
		animatedMesh.animate(true);


		//animatedMesh.paint(animatedMesh.getGraphics());

		try {
			animate();
		} catch (IOException e) {
			e.printStackTrace();
		}

		animatedP.add(animatedMesh);
		animatedP.add(start);
		animatedP.add(label);
		animatedP.add(slider);
		animateWindow.add(animatedP);
		animatedMesh.setSize(Size);
		animatedP.paint(animatedMesh.getGraphics());

	}
	if(src == warpB) {
		//secondMesh.makeWarp();
		try {
			writexy();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

} // actionPerformed()


// main: creates new instance of Mesh object
public static void main(String args[]) {
	Mesh pl;
	pl = new Mesh();
	pl.setSize(pl.getPreferredSize().width*5/4, pl.getPreferredSize().height*5/4);
	pl.setVisible(true);
} // main()


}
