import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class  Mesh extends JFrame implements ActionListener {


private MeshCanvas mymesh;
private MeshCanvas newMesh;
private MeshCanvas animatedMesh;
private JPanel mesh;
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
// constructor: creates GUI

public Mesh() {
	super("Simple Mesh");

	//setSize(getPreferredSize());
	setSize(5000,1000);
	Container c = getContentPane();         // create container
	c.setBackground(new Color(232, 232, 232));
	c.setForeground(new Color(0, 0, 0));
	c.setLayout(new BorderLayout());
	//c.setSize(5000,1000);
	//animateWindow.setSize(500,500);
	//first mesh
	mesh = new JPanel(new FlowLayout());
	// new canvas
	mymesh = new MeshCanvas();
	mymesh.setImage("test.jpg");
	mymesh.setSize(500,500);
	mesh.add(mymesh,BorderLayout.EAST);
	// second mesh
	newMesh = new MeshCanvas();
	newMesh.setImage("boat2.jpg");
	newMesh.setSize(500,500);
	mesh.add(newMesh,BorderLayout.WEST);
	time = 0;
	t = 50;
	timer = new Timer(100,null);
	timer.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		//	animatedMesh.clear();
			if(t>0&&time/t<=1){
		//		animatedMesh.clear();
				animate();
			}
			else
				timer.stop();
			time++;
			System.out.println(time);
		}
	});
	timer2 = new Timer(10,null);
	timer2.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			paintd();
		}
	});
	timer2.start();
	animatedMesh = new MeshCanvas();
	animatedMesh.setSize(500,500);
	c.add(mesh, BorderLayout.CENTER);
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
			timer.start();

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
	mymesh.addMouseMotionListener(new MouseMotionAdapter() {
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

	newMesh.addMouseMotionListener(new MouseMotionAdapter() {
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
	public void paintd() {
		if(newMesh.isSelected()) {
				mymesh.paintDot(mymesh.getGraphics(), newMesh.getCurrentx(), newMesh.getCurrentpointy());
				mymesh.repaint();
			}
		if(mymesh.isSelected()) {
			newMesh.paintDot(newMesh.getGraphics(), mymesh.getCurrentx(), mymesh.getCurrentpointy());
			newMesh.repaint();
		}

		}

public void animate(){
	int x1[][] =  mymesh.getx();
	int x2[][] =  newMesh.getx();
	int y1[][] = mymesh.gety();
	int y2[][] = newMesh.gety();
	int newx[][] = new int[6][6];
	int newy[][] = new int[6][6];
	//double x = 0;
	//double y = 0;
	//int t = 1;
	///x1 = mymesh.getx();
	animatedP.remove(animatedMesh);

	for (int i = 0; i <= 5; i++) {
		for (int j = 0; j <= 5; j++) {
			newx[i][j] = (int) (x1[i][j] + ((time/t) * (x2[i][j] - x1[i][j])));
			newy[i][j] = (int) (y1[i][j] + ((time/t) * (y2[i][j] - y1[i][j])));
			// t = t+.2;
			//System.out.println(j+";"+i);
			//System.out.println(newx[i][j]+":"+newy[i][j]);

		}
	}
	animatedP.add(animatedMesh);
	animatedMesh.setx(newx);
	animatedMesh.sety(newy);
	animatedMesh.drawMesh();

}

// capture button actions
public void actionPerformed(ActionEvent evt) {
	Object src = evt.getSource();

	// reset the original image and show the mesh
	if(src == clearB) {
		mymesh.clear();
		newMesh.clear();
		animatedMesh.clear();
		timer.stop();
		timer2.stop();
	}
	if(src == animate){
		time = 0;
		animatedMesh.animate(true);
		animate();
		animatedP.add(animatedMesh);
		animatedP.add(start);
		animatedP.add(label);
		animatedP.add(slider);
		animateWindow.add(animatedP);
		animateWindow.setVisible(true);
	}
	if(src == warpB) {
		mymesh.makeWarp();
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
