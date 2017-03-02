package main;

import java.awt.*;
import java.util.*;
import java.util.function.*;
import javax.swing.*;

public class GraphViewerPanel extends JPanel {
	
	public static final double margin = 0.1d;
	public static final double increment = 1d;
	
	public Point[] points;
	
	public GraphViewerPanel(Point[] points){
		this.points = points;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Get object boundaries
		//Rectangle clipBounds = g.getClipBounds();
		Dimension dimensions = getSize();
		double dimx = dimensions.width;
		double dimy = dimensions.height;
		double dimratio = dimy/dimx;
		Rectangle clipBounds = new Rectangle(0,0,dimensions.width,dimensions.height);
		//Get curve boundaries
		int size;
		if(points==null){
			size = 0;
		}else{
			size = points.length;
		}
		double multiplier = 1.3;
		double leftt = 0.5-0.5*multiplier;
		double rightt = 0.5+0.5*multiplier;
		double margin = 0.01;
		double left;
		double right;
		double bottom;
		double top;
		if(size>0){
			double fl = Arrays.stream(points).mapToDouble(new ToDoubleFunction<Point>(){
				public double applyAsDouble(Point v) {
					return v.x;
				}
			}).min().getAsDouble()-margin;
			double fr = Arrays.stream(points).mapToDouble(new ToDoubleFunction<Point>(){
				public double applyAsDouble(Point v) {
					return v.x;
				}
			}).max().getAsDouble()+margin;
			double fb = Arrays.stream(points).mapToDouble(new ToDoubleFunction<Point>(){
				public double applyAsDouble(Point v) {
					return v.y;
				}
			}).min().getAsDouble()-margin;
			double ft = Arrays.stream(points).mapToDouble(new ToDoubleFunction<Point>(){
				public double applyAsDouble(Point v) {
					return v.y;
				}
			}).max().getAsDouble()+margin;
			left = Draw.lerp(fl,fr,leftt);
			right = Draw.lerp(fl,fr,rightt);
			bottom = Draw.lerp(fb,ft,leftt);
			top = Draw.lerp(fb,ft,rightt);
		}else{
			left=-1;
			right=1;
			top=-1;
			bottom=1;
		}
		double dlr = right-left;
		double dbt = top-bottom;
		double bratio = dbt/dlr;
		if(bratio<dimratio){
			double mult = dimratio/bratio;
			top*=mult;
			bottom*=mult;
		}else{
			double mult = bratio/dimratio;
			left*=mult;
			right*=mult;
		}
		//Create colour object from theme colours
		Color colOuter = new Color(0.7f,0.7f,0.7f);
		Color colInner = new Color(0.9f,0.9f,0.9f);
		Color colGridLine = new Color(0.5f,0.5f,0.5f);
		Color colHighlight = new Color(0.8f,0.6f,0.6f);
		//Blank background
		g.setColor(colOuter);
		g.fillRect(0,0,clipBounds.width,clipBounds.height);
		g.setColor(colInner);
		g.fillRect(2,2,clipBounds.width-4,clipBounds.height-4);
		//Grid lines setup
		g.setColor(colGridLine);
		double gridPos;
		double gridInc;
		//Horizontal
		gridInc = Math.pow(10, Math.floor(Math.log10(top-bottom)-0.4));
		gridPos = Math.ceil(top-gridInc);
		while(gridPos>bottom){
			int mapping = (int) Math.floor((top-gridPos)/(top-bottom)*clipBounds.height);
			if(mapping>0 && mapping<clipBounds.height){
				g.fillRect(2, mapping, clipBounds.width-4, 1);
			}
			gridPos-=gridInc;
		}
		//Vertical
		while(gridPos<right){
			int mapping = (int) Math.floor((gridPos-left)/(right-left)*clipBounds.width);
			if(mapping>0 && mapping<clipBounds.width){
				g.fillRect(mapping, 2, 1, clipBounds.height-4);
			}
			gridPos+=gridInc;
		}
		if(size>0 && clipBounds.width>4){
			//Map and round
			g.setColor(colHighlight);
			int[] px = new int[size];
			int[] py = new int[size];
			for(int i=0;i<size;i++){
				px[i] = Draw.mapToRound(left, right, 0, clipBounds.width, points[i].x);
				py[i] = Draw.mapToRound(bottom, top, 0, clipBounds.height, points[i].y);
			}
			//Show points
			for(int i=0,j=1;j<size;i++,j++){
				g.drawLine(px[i], py[i], px[j], py[j]);
			}
		}
	}

}
