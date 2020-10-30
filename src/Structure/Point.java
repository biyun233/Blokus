package Structures;

public class Point {
	private int x,y;
	
	public Point(int x,int y) {
			this.x = x;
			this.y = y;
	}
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Point left() {
		if(this.getY() ==0) {
			return null;
		}
		return new Point(this.getX(),this.getY()-1);
	}
	public Point right() {
		return new Point(this.getX(),this.getY()+1);
	}
	public Point up() {
		if(this.getX() == 0) {
			return null;
		}
		return new Point(this.getX()-1,this.getY());
	}
	
	public Point down() {
		return new Point(this.getX()+1,this.getY());
	}
}
