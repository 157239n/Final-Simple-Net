package _157239n;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.FloatList;

public class Graph {
	private FloatList fl = new FloatList(0);
	private float total = 0;
	private float dx, dy;
	private boolean importing = false, exporting = false, graphing = false, auto = true;
	private float[] r = { (float) 0.05, (float) 0.95, (float) 0.1, (float) 0.95 };
	private PApplet p;
	private String desc = "";

	Graph(PApplet proc) {
		this.p = proc;
	}

	/**
	 * Adds a floating point value to the graph. <br>
	 * <br>
	 * Normally, this will automatically increase the internal count by 1.<br>
	 * If the {@link Graph#total(int)} method is called, automatic increases are
	 * immediately disabled<br>
	 * 
	 * @param value
	 *            the value to add to the graph
	 * @return itself
	 */
	public Graph add(float value) {
		fl.append(value);
		if (auto) {
			total++;
		}
		return this;
	}

	/**
	 * Adds a description to the graph which will be displayed in the middle of the
	 * screen. <br>
	 * <br>
	 * This feature will not be exported.
	 * 
	 * @param description
	 *            the description to add
	 * @return itself
	 */
	public Graph addDesc(String description) {
		this.desc = description;
		return this;
	}

	/**
	 * Specifies the total amount of samples the graph is recording. <br>
	 * <br>
	 * For example, we want to graph the population over 100 years from 1900 to
	 * 2000. However, we only have values of the population at the year 1910, 1920,
	 * ..., 2000. We can add elements and the internal count rise to 10 when we add
	 * the year 2000 value. If we want to specify that we have reach the year 2000
	 * when we add the value, we can use total(2000) to specify that.<br>
	 * 
	 * @param times
	 *            the times we want to specify
	 */
	public void total(int times) {
		auto = false;
		total = times;
	}

	/**
	 * Gets the floating point value at a particular index, out of range values are
	 * warped back in.
	 * 
	 * @param index
	 *            the index of the value we want to get
	 * @return the value at that particular index
	 */
	public float getValue(int index) {
		int n = fl.size();
		if (index < 0) {
			index += (Math.abs(index) / n) * n;
		}
		while (index < 0) {
			index += n;
		}
		index %= n;
		return fl.get(index);
	}

	// TODO: document the slash model
	/**
	 * Graph the current graph.
	 * 
	 * @param m
	 *            the width of the output image
	 * @param n
	 *            the height of the output image
	 * @param length
	 *            the length of the graph
	 * @param location
	 *            the location or position of the graph
	 * @return the PGraphics element that contains the image
	 */
	public PGraphics graph(int m, int n, float location, float length) {
		if (graphing) {
			throw new RuntimeException(
					"another thread has already request drawing already. Graph will be damaged if start drawing now");
		}
		graphing = true;
		// mathematical calculations, truncate fl so that it fits our params of (len,
		// loc) and reduce the size of fl
		FloatList oldFl = fl.copy();
		float[] vals = Env._float(fl);
		vals = Matrix.slash(vals, location, length);
		/* reduce the size of the arrray */float multiple = (float) (1.0 * vals.length / m);
		if (multiple > 1.5) {
			float[] newvals = new float[m];
			for (int i = 0; i < m; i++) {
				float midway = (float) (1.0 * i / m * vals.length);
				float alpha = (float) (midway - (int) midway);
				int intMidway = (int) midway;
				newvals[i] = alpha * vals[intMidway] + vals[intMidway + 1] * (1 - alpha);
			}
			fl = Env.FloatList(newvals);
		} else {
			fl = Env.FloatList(vals);
		}
		location = PApplet.map(location, 0, 1, (float) length / 2, (float) 1 - length / 2);
		float startLearn = location - length / 2, endLearn = location + length / 2;
		float[] range = { r[0] * m, r[1] * m, r[2] * n, r[3] * n };
		// this defines a box that the graph will touch and that is smaller than the (m,
		// n) graph requested

		// find the maximum value in fl
		float top = 0;
		for (int i = 0; i < fl.size(); i++) {
			top = PApplet.max(top, fl.get(i));
		}
		configureFunction(fl.size() - 1, top, range[1] - range[0], range[3] - range[2]);
		// initialization and text management
		PGraphics ans = p.createGraphics(m, n);
		ans.beginDraw();
		ans.textAlign(PApplet.LEFT, PApplet.CENTER);
		ans.background(255);
		ans.fill(255, 0, 0);
		ans.text(PApplet.str(top), (float) 1.5 * range[0], -range[2] + ans.height - dy * top);
		// markers and text
		ans.pushMatrix();
		ans.translate(0, -range[2]);
		ans.textAlign(PApplet.CENTER, PApplet.CENTER);
		ans.stroke(0);
		{
			int space = 30;
			int last = -space - 1;
			for (int i = 0; i < fl.size(); i++) {
				int now = (int) (dx * i);
				if (now - last > space) {
					ans.text(PApplet.str((int) (PApplet.map(i, 0, fl.size(), startLearn * total, endLearn * total))),
							now + range[0], range[2] / 2 + ans.height);
					ans.line(now + range[0], ans.height + 5, now + range[0], ans.height - 5);
					last = now;
				}
			}
		}
		ans.popMatrix();
		// prior translation
		ans.pushMatrix();
		ans.translate(0, (float) 0.5 * ans.height);
		ans.scale(1, -1);
		ans.translate(0, (float) (-0.5 * ans.height));
		ans.translate(range[0], range[2]);
		ans.fill(255, 0, 0);
		// horizontal line
		ans.line(0, 0, range[1] - range[0], 0);
		ans.line(range[1] - range[0] - 5, 5, range[1] - range[0], 0);
		ans.line(range[1] - range[0] - 5, -5, range[1] - range[0], 0);
		// vertical line
		ans.line(0, 0, 0, range[3] - range[2]);
		ans.line(-5, range[3] - range[2] - 5, 0, range[3] - range[2]);
		ans.line(5, range[3] - range[2] - 5, 0, range[3] - range[2]);
		// markers
		ans.line(-5, dy * top, +5, dy * top);
		// graph
		for (int i = 0; i < fl.size() - 1; i++) {
			ans.line(f(i).x, f(i).y, f(i + 1).x, f(i + 1).y);
		}
		ans.popMatrix();
		ans.fill(0);
		ans.text(desc, ans.width / 2, ans.height / 2);
		ans.endDraw();
		fl = oldFl;
		graphing = false;
		return ans;
	}

	/**
	 * Graph the current graph.
	 * 
	 * @param m
	 *            the width of the output image
	 * @param n
	 *            the height of the output image
	 * @param location
	 *            the location or position of the graph
	 * @param length
	 *            the length of the graph
	 * @return the PGraphics element that contains the image
	 */
	public PGraphics graph(int m, int n, double location, double length) {
		return graph(m, n, (float) location, (float) length);
	}

	/**
	 * Clears/resets the current graph.
	 */
	public void clear() {
		fl = new FloatList(0);
	}

	/**
	 * Exports the graph to a file.
	 * 
	 * @param path
	 *            the path of the intended file
	 */
	public void exportGraph(String path) throws RuntimeException {
		if (exporting) {
			throw new RuntimeException("can't export graph. Another thread is exporting it already.");
		}
		if (importing) {
			throw new RuntimeException("can't export graph. Another thread is importing it.");
		}
		exporting = true;
		if (!PApplet.getExtension(path).equals("graph")) {
			throw new RuntimeException("can't write file of type " + PApplet.getExtension(path));
		}
		if (path.charAt(1) != ':') {
			path = Final_Simple_Net.parentAbsPath + path;
		}
		p.saveStrings(path, Env.emptyStringArray);
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(path);
			Dir.writeInt(file, 157239);
			Dir.writeInt(file, PApplet.parseInt(total));
			Dir.writeInt(file, fl.size());
			for (int i = 0; i < fl.size(); i++) {
				Dir.writeFloat(file, fl.get(i));
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("can't export graph. Something went wrong while writing to stream.");
		}
		exporting = false;
	}

	/**
	 * Imports the graph from a file.
	 * 
	 * @param path
	 *            the path of the intended file
	 */
	public void importGraph(String path) throws RuntimeException {
		if (exporting) {
			throw new RuntimeException("can't export graph. Another thread is exporting it.");
		}
		if (importing) {
			throw new RuntimeException("can't export graph. Another thread is importing it already.");
		}
		importing = true;
		if (!PApplet.getExtension(path).equals("graph")) {
			throw new RuntimeException("can't read file of type " + PApplet.getExtension(path)
					+ ". Function: import(String path), class Graph");
		}
		if (path.charAt(1) != ':') {
			path = Final_Simple_Net.parentAbsPath + path;
		}
		FileInputStream file = null;
		try {
			file = new FileInputStream(path);
			int magicNumber = Dir.readInt(file);
			if (magicNumber != 157239) {
				throw new RuntimeException("can't read file with magic number " + PApplet.str(magicNumber)
						+ ". Function: import(String path), class Graph");
			}
			total = Dir.readInt(file);
			int len = Dir.readInt(file);
			fl = new FloatList(0);
			for (int i = 0; i < len; i++) {
				fl.append(Dir.readFloat(file));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("can't import graph. Function: import(String path), class Graph");
		}
		importing = false;
	}

	private void configureFunction(float a, float b, float m, float n) {
		dx = (float) (0.95 * m / a);
		dy = (float) (0.9 * n / b);
	}

	private PVector f(int index) {
		return new PVector(dx * index, dy * fl.get(index));
	}
}