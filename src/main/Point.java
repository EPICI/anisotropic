package main;

public class Point {
	public double x;
	public double y;
	public Point(double x,double y){
		this.x=x;
		this.y=y;
	}
	public int hashCode(){
		return Double.hashCode(x)*33+Double.hashCode(y);
	}
	public boolean equals(Object o){
		return hashCode()==o.hashCode();
	}
}
