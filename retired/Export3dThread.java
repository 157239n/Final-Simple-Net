/*
import java.io.FileOutputStream;

import processing.core.PApplet;
import processing.core.PVector;

public class Export3dThread extends SimpleProgressThread {
	int[] _3dScales = { 1, 10, 100, 1000, 10000 };
	boolean free = false;
	Network net;
	Matrix lX, lP;
	PVector synNum, p0, p1, shift;
	String path;
	int tag, id, countT;
	PApplet p;
	
	Export3dThread(PApplet proc, Network net, Matrix lX, Matrix lP, PVector synNum, PVector p0, PVector p1, String path, PVector shift, int tag) {
		super(0.0);
		_init(proc, net, lX, lP, synNum, p0, p1, path, shift, tag);
	}

	Export3dThread(PApplet proc, Network net, Matrix lX, Matrix lP, PVector synNum, PVector p0, PVector p1, String path,
			PVector shift) {
		super(0.0);
		_init(proc, net, lX, lP, synNum, p0, p1, path, shift, 0);
	}

	Export3dThread(PApplet proc, Network net, Matrix lX, Matrix lP, PVector synNum, PVector p0, PVector p1, String path) {
		super(0.0);
		_init(proc, net, lX, lP, synNum, p0, p1, path, new PVector(0, 0), 0);
	}

	Export3dThread(PApplet proc, Network net, Matrix lX, Matrix lP, String path, PVector shift) {
		super(0.0);
		_init(proc, net, lX, lP, new PVector(1, 1), new PVector(0, 0), new PVector(0, 1), path, shift, 0);
	}

	Export3dThread(PApplet proc, Network net, Matrix lX, Matrix lP, String path) {
		super(0.0);
		_init(proc, net, lX, lP, new PVector(1, 1), new PVector(0, 0), new PVector(0, 1), path, new PVector(0, 0), 0);
	}

	void _init(PApplet proc, Network net, Matrix lX, Matrix lP, PVector synNum, PVector p0, PVector p1, String path, PVector shift,
			int tag) {
		this.net = net;
		this.lX = lX.clone();
		this.lP = lP.clone();
		this.synNum = synNum.copy();
		this.p0 = p0.copy();
		this.p1 = p1.copy();
		this.path = path.substring(0, path.length());
		this.shift = shift.copy();
		this.tag = tag;
		this.p=proc;
	}
//experimental
	void attachProgress(_Float progress) {
		this.progress = progress;
	}
	Export3dThread free() {
		free = true;
		return this;
	}

	public void run() {
		float space = (float) (1.0 / _3dScales.length);
		FileOutputStream file;
		id = net.id;
		//if(!PApplet.split(path, ".")[-1].equals("3dg")) {path+=".3dg";}
		p.saveStrings(path+".3dg", Env._String(""));
		if (!free) {
			String[] tmp = PApplet.split(path, "/");
			p.saveStrings(Dir.trace(path, tmp[tmp.length - 2]) + "plane data/" + tmp[tmp.length - 1] + ".txt", info());
		} else {
			p.saveStrings(path + ".txt", info());
		}
		try {
			file = new FileOutputStream(path + ".3dg");
			Dir.writeInt(file, 157232);
			Dir.writeInt(file, id);
			Dir.writeInt(file, _3dScales.length);
			for (int i = 0; i < _3dScales.length; i++) {
				Dir.writeInt(file, _3dScales[i]);
			}
			for (int i = 0; i < _3dScales.length; i++) {
				subRun(file, _3dScales[i], i * space, (i + 1) * space);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("something problem with the file. Function run() class Export3dThread");
		}
		try {
			file.close();
		} catch (Exception e) {
			throw new RuntimeException("some prob with closing the file. Function run() class Export3dThread");
		}
		progress.setValue(1.0);
	}

	String[] info() {
		return Env._String("id: " + PApplet.str(id), "shift: (" + Env.format(shift.x) + ", " + Env.format(shift.y) + ")", "synNum: (" + Env.format(synNum.x) + ", " + Env.format(synNum.y) + ")", "p0: (" + Env.format(p0.x) + ", " + Env.format(p0.y) + ")", "p1: (" + Env.format(p1.x) + ", " + Env.format(p1.y) + ")");
	}

	void subRun(FileOutputStream stream, float range, float startProg, float endProg){
		
		float[][] ans=new float[50][50];
		int syn0=(int) (synNum.x);
		int syn1=(int) (synNum.y);
		int p00=(int) (p0.x);
		int p01=(int) (p0.y);
		int p10=(int) (p1.x);
		int p11=(int) (p1.y);
		Network tN=net.clone();
		PVector avg=new PVector((float) (tN.syn[syn0].getValue(p00, p01)+shift.x*range*0.5), (float) (tN.syn[syn1].getValue(p10, p11)+shift.y*range*0.5));
		for(int i=0;i<ans.length;i++){
			for(int j=0;j<ans[0].length;j++){
				tN.syn[syn0].setValue(p00, p01, (avg.x+(i-ans.length/2)*range*0.02));
				tN.syn[syn1].setValue(p10, p11, (avg.y+(j-ans[0].length/2)*range*0.02));
				ans[i][j]=tN.error(lX, lP);
				progress.setValue((i*ans[0].length+j)/(1.0*ans.length*ans[0].length), startProg, endProg);
			}
		}
		Dir.writeMatrix(stream, new Matrix(ans));progress.setValue(1.0, startProg, endProg);
	}
}
*/