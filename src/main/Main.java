package main;

import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class Main {
	
	public static Random random;
	
	public static BufferedReader in;
	public static BufferedImage img;
	public static int trueImgX;
	public static int trueImgY;
	public static int imgX;
	public static int imgY;
	public static int imgScale;
	
	public static JFrame imageFrame;
	public static JFrame controlFrame;
	public static ImagePanel imagePanel;
	public static JSplitPane controlParent;
	public static JSplitPane graphSplit;
	public static GraphViewerPanel graphPanel;
	public static JTextArea infoArea;
	public static JPanel controlPanel;
	public static JLabel xlabel;
	public static JTextField xfield;
	public static JLabel ylabel;
	public static JTextField yfield;
	public static JButton buttonTranslate;
	public static JButton buttonScale;
	public static JButton buttonBright;
	public static JButton buttonTan;
	public static JButton buttonIntegral;
	public static JButton buttonInverse;
	public static JButton buttonMirror;
	public static JButton buttonSave;
	
	public static Point[] points;
	public static double reference;
	public static double gamma = 2d;
	
	public static int mouseX;
	public static int mouseY;

	public static void main(String[] args) throws IOException {
		//IO
		in = new BufferedReader(new FileReader("config.txt"));
		String imageFileName = in.readLine();
		gamma = Double.valueOf(in.readLine());
		in.close();
		img = ImageIO.read(new File(imageFileName));
		trueImgX = img.getWidth();
		trueImgY = img.getHeight();
		imgX = trueImgX;
		imgY = trueImgY;
		imgScale = 1;
		while(imgX>1900||imgY>1000){
			imgX>>=1;
			imgY>>=1;
			imgScale<<=1;
		}
		
		//Components
		imagePanel = new ImagePanel(img,imgX,imgY);
		imageFrame = new JFrame("Image viewer: "+imageFileName);
		imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imageFrame.add(imagePanel);
		imageFrame.pack();
		imageFrame.setVisible(true);
		controlFrame = new JFrame("Controls");
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlParent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		controlFrame.add(controlParent);
		graphSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		infoArea = new JTextArea();
		infoArea.setText("Congratulations, the image loaded.\nNow, follow the instructions:\nMove the cursor to an unlit area and press R to set it as the reference. Then move it to the same Y as the laser line and press S to sample.\nNow is a good time to do any corrections. Note that x=0 should be where a ray perpendicular to the reflected surface originating at the point of reflection would hit. Also, 1 unit on the x axis should be whatever distance was between the reflective material being tested and the unreflective material.\nOnce all that is done, convert from brightness to distribution. Do more tinkering if you need to make corrections.\nOnce that's done, use tangent to go back to slope form.\nDo integral, then inverse, then mirror, then integral again.\nNow save.");
		graphSplit.setTopComponent(infoArea);
		graphPanel = new GraphViewerPanel(points);
		graphSplit.setBottomComponent(graphPanel);
		controlParent.setTopComponent(graphSplit);
		controlPanel = new JPanel();
		BoxLayout controlLayout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		xlabel = new JLabel("X value");
		xfield = new JTextField(30);
		ylabel = new JLabel("Y value");
		yfield = new JTextField(30);
		controlPanel.add(xlabel);
		controlPanel.add(xfield);
		controlPanel.add(ylabel);
		controlPanel.add(yfield);
		buttonTranslate = controlButton("Translate XY",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.translate(points, doubleFrom(xfield), doubleFrom(yfield));
				}
			}
		});
		buttonScale = controlButton("Scale XY",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.scale(points, doubleFrom(xfield), doubleFrom(yfield));
				}
			}
		});
		buttonBright = controlButton("Brightness to distribution",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.brightnessToDistribution(points);
				}
			}
		});
		buttonTan = controlButton("Tangent",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.function(points, Function.x, Function.tan);
				}
			}
		});
		buttonIntegral = controlButton("Integral",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.integral(points);
				}
			}
		});
		buttonInverse = controlButton("Inverse",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.inverse(points);
				}
			}
		});
		buttonMirror = controlButton("Mirror",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					points = Transforms.mirror(points);
				}
			}
		});
		buttonSave = controlButton("Export to CSV",new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(points!=null){
					PrintWriter pw = null;
					try {
						pw = new PrintWriter("graph-"+Integer.toHexString(random.nextInt())+".csv");
						pw.println("X,Y");
						for(Point p:points){
							pw.println(p.x+","+p.y);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if(pw!=null){
						pw.close();
					}
				}
			}
		});
		controlPanel.setLayout(controlLayout);
		controlParent.setBottomComponent(controlPanel);
		imagePanel.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				int cx = mouseX*imgScale;
				int cy = mouseY*imgScale;
				switch(e.getKeyCode()){
				case KeyEvent.VK_S:{
					//Record brightness data
					ArrayList<Double> al = new ArrayList<>();
					int cap = img.getWidth()-4;
					for(int i=4;i<cap;i+=4){
						al.add(avgSquare(i,cy,3)-reference);
					}
					int size = al.size();
					int half = size>>1;
					double xmult = 1d/half;
					points = new Point[size];
					for(int i=0;i<size;i++){
						points[i] = new Point((i-half)*xmult,al.get(i));
					}
					graphPanel.points=points;
					SwingUtilities.updateComponentTreeUI(controlFrame);
					break;
				}
				case KeyEvent.VK_R:{
					//Set unlit reference point
					reference = avgSquare(cx,cy,9);
					break;
				}
				}
			}
			
		});
		imagePanel.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
			
		});
	}
	
	public static double avgSquare(int x,int y,int r){
		final int d = 2*r+1;
		final int n = d*d;
		double[] values = new double[n];
		int lx = x-r;
		int ly = y-r;
		int k=0;
		for(int i=0;i<d;i++){
			for(int j=0;j<d;j++,k++){
				int argb = img.getRGB(lx+i, ly+j);
				values[k] = gammaBrightness(argb);
			}
		}
		double sum = 0d;
		for(double v:values){
			sum+=v;
		}
		sum /= n;
		return sum;
	}
	
	public static double gammaBrightness(int argb){
		int r = (argb<<8)>>>24;
		int g = (argb<<16)>>>24;
		int b = (argb<<24)>>>24;
		return Math.pow(r, gamma)+Math.pow(g, gamma)+Math.pow(b, gamma);
	}
	
	public static JButton controlButton(String text,ActionListener listener){
		JButton result = new JButton(text);
		result.addActionListener(listener);
		controlPanel.add(result);
		return result;
	}
	
	public static double doubleFrom(JTextField tf){
		return Double.parseDouble(tf.getText());
	}

}
