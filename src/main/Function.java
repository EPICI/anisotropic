package main;

public interface Function {
	public double f(double x);
	
	public static Function x = new Function(){
		public double f(double x){
			return x;
		}
	};
	public static Function tan = new Function(){
		public double f(double x){
			return Math.tan(x);
		}
	};
	public static Function atan = new Function(){
		public double f(double x){
			return Math.atan(x);
		}
	};
}
