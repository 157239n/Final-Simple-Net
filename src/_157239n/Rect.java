package _157239n;

import processing.core.PApplet;
/**
 * Represents a rectangle.
 * 
 * @author 157239n
 * @author www.157239n.com
 * @version 1.0
 * */
public class Rect {
	/**
	 * The x coordinate of the rectangle
	 * */
	public int x = 0;
	/**
	 * The y coordinate of the rectangle*/
	public int y = 0;
	/**
	 * The width of the rectangle
	 * */
	public int w = 10;
	/**
	 * The height of the rectangle
	 * */
	public int h = 10;

	Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Clones the current object.
	 * 
	 * @return the cloned Rect
	 */
	public Rect clone() {
		return new Rect(x, y, w, h);
	}

	/**
	 * Prints the Rect out onto the console with some additional String at both ends.
	 * 
	 * @param beginningString the string to be concatenated into the Rectangle's output at the beginning
	 * @param endingString the string to be concatenated into the Rectangle's output at the end
	 */
	public void pl(String beginningString, String endingString) {
		System.out.println(beginningString+getCoordinate()+endingString);
	}
	
	/**
	 * Prints the Rectangle out onto the console.
	 */
	public void pl() {
		pl("", "");
	}
	
	/**
	 * Returns a String describing the coordinates of the Rectangle.
	 * 
	 * @return a String describing the coordinates of the Rectangle
	 * */
	public String getCoordinate() {
		return "x: " + PApplet.str(x) + ", y: " + PApplet.str(y) + ", w: " + PApplet.str(w) + ", h: "+ PApplet.str(h);
	}
}