package main;

public final class Transforms {
	private Transforms(){}
	
	public static Point[] translate(Point[] original,double dx,double dy){
		int n = original.length;
		Point[] result = new Point[n];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(o.x+dx,o.y+dy);
		}
		return result;
	}
	
	public static Point[] scale(Point[] original,double a,double b){
		int n = original.length;
		Point[] result = new Point[n];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(o.x+a,o.y+b);
		}
		return result;
	}
	
	public static Point[] inverse(Point[] original){
		int n = original.length;
		Point[] result = new Point[n];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(o.y,o.x);
		}
		return result;
	}
	
	public static Point[] derivative(Point[] original){
		int n = original.length;
		int m = n-1;
		Point[] result = new Point[m];
		for(int i=0;i<m;i++){
			Point a = original[i];
			Point b = original[i+1];
			result[i]=new Point(0.5d*(a.x+b.x),(b.y-a.y)/(b.x-a.x));
		}
		return result;
	}
	
	public static Point[] integral(Point[] original){
		int n = original.length;
		Point[] result = new Point[n];
		double x = original[0].x;
		double y = 0;
		result[0] = new Point(x,y);
		for(int i=1;i<n;i++){
			Point o = original[i];
			y += o.y/(o.x-x);
			result[i]=new Point(o.x,y);
			x = o.x;
		}
		return result;
	}
	
	public static Point[] brightnessToDistribution(Point[] original){
		int n = original.length;
		Point[] result = new Point[n];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(Math.atan(o.x),o.y*(o.x*o.x-1d));
		}
		return result;
	}
	
	public static Point[] function(Point[] original,Function f,Function g){
		int n = original.length;
		Point[] result = new Point[n];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(f.f(o.x),g.f(o.y));
		}
		return result;
	}
	
	public static Point[] mirror(Point[] original){
		int n = original.length;
		int m = n<<1;
		int m1 = m-1;
		Point[] result = new Point[m];
		for(int i=0;i<n;i++){
			Point o = original[i];
			result[i]=new Point(o.x,o.y);
		}
		double cx = original[n-1].x;
		for(int i=n;i<m;i++){
			Point o = original[m1-i];
			result[i]=new Point(cx-o.x,o.y);
		}
		return result;
	}
}
