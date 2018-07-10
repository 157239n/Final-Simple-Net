package _157239n;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Final_Simple_Net extends PApplet {
	/* importing MNIST */
	/**
	 * MNIST training set images
	 */
	public static Matrix trainX = new Matrix();
	/**
	 * MNIST training set labels
	 */
	public static Matrix trainP = new Matrix();
	/**
	 * MNIST test set images
	 */
	public static Matrix testX = new Matrix();
	/**
	 * MNIST test set labels
	 */
	public static Matrix testP = new Matrix();

	/**
	 * The list of networks that we will train one by one
	 */
	private ArrayList<Network> nets;
	// TODO: test that the shutdown function still works
	/**
	 * Specifies whether the program will shut the computer down when everything has
	 * been finished
	 */
	private boolean shutdown = false;
	/**
	 * The absolute path to the outside DropBox folder
	 */
	public static String absPath;
	/**
	 * The absolute parent directory of the sketch
	 */
	public static String parentAbsPath;
	/**
	 * A list of work to be done consecutively
	 */
	private ArrayList<Work> work = new ArrayList<Work>(0);
	private float lowMemoryThreshold = (float) 0.8;
	/**
	 * This is to prevent the graph drawings from happenning too fast
	 */
	private int time = 0;

	private PImage mainImg, secondaryImg;
	private PGraphics confidence = null, prediction = null;

	// work.add(Work(this, new Rect(100, 0+275, 400, 25)).add(new
	// Export3dThread(net, trainX._slash_(0, 1), trainP._slash_(0, 1),
	// "independent").free()));
	/**
	 * Main method to initialize Java.
	 * 
	 * @param args
	 *            the String arguments
	 */
	public static void main(String[] args) {
		PApplet.main("_157239n.Final_Simple_Net");
	}

	public void settings() {
		size(900, 600);
	}

	public void setup() {
		background(Env.eraseColor);
		textSize(15);
		textAlign(CENTER, CENTER);// randomSeed(15);
		parentAbsPath = Dir.initPath(this);
		absPath = Dir.trace(parentAbsPath, "Dropbox");
		int initH = 150 + 75;
		int tmp_iter = 100;
		nets = new ArrayList<Network>(0);
		for (int i = 0; i < 1; i++) {
			nets.add(new Network(this, Env._int(28 * 28 + 1, 50, 10)).id(i + 10).graphSpace(1).setLc(0.03)
					.learningStyle(Network.BP).learningTimes(tmp_iter).activationFamily(Matrix.SIGMOID).cores(8)
			// .importSyns("0.synx")
			// .autoAdjustLc()
			);
		}
		{
			int maxSize = 1000;
			String MNISTPath = "Databases/MNIST/";
			work.add(new Work(this, new Rect(0, initH + 0, 300, 25)).add(new SimpleProgressThread() {
				public void run() {
					Dir.loadMNIST(absPath + MNISTPath + "train-images", maxSize, this.progress, trainX);
				}
			}).add(new SimpleProgressThread() {
				public void run() {
					Dir.loadMNIST(absPath + MNISTPath + "train-labels", maxSize, this.progress, trainP);
				}
			}).add(new SimpleProgressThread() {
				public void run() {
					Dir.loadMNIST(absPath + MNISTPath + "test-images", maxSize, this.progress, testX);
				}
			}).add(new SimpleProgressThread() {
				public void run() {
					Dir.loadMNIST(absPath + MNISTPath + "test-labels", maxSize, this.progress, testP);
				}
			}).multithread().addDescription("load MNIST.."));
			work.add(new Work(this, new Rect(0, initH + 25, 300, 25)).add("work0").addDescription("preparing.."));
			int currentHeight = 50, x = 0;
			for (int i = 0; i < nets.size(); i++) {
				if (initH + currentHeight + 150 > height) {
					currentHeight = 0;
					x = 300;
				}
				work.add(new Work(this, new Rect(x, initH + currentHeight, 300, 25))
						.add("work1", new InfoPacket().add(i)).addDescription("net prep.."));
				currentHeight += 25;
				work.add(new Work(this, new Rect(x, initH + currentHeight, 300, 125)).add(nets.get(i)).processSpecificDisplay().signature(Network.SIGNATURE));
				currentHeight += 125;
			}
			work.add(new Work(this, new Rect(300, 550, 300, 25)).add("wait", new InfoPacket().add(20))
					.addDescription("waiting 20s.."));
			work.add(new Work(this, new Rect(300, 575, 300, 25)).add("shutdown").addDescription("shutdown.."));
		}
		/*
		 * { work.add(new Work(this, new Rect(0, 0, 300, 25)).add("recognizeABunch")); }
		 * /
		 **/
		work.get(0).start();
		time = millis();
		println("setup,", nashorn.ObjectSizeCalculator.getObjectSize(nets.get(0)));
	}

	public void shutdown(InfoPacket data) {
		data.get_Float(0).setValue(0.0);
		if (shutdown) {
			launch(Dir.trace(absPath, "Dropbox") + "Tools/shutdown (3s).lnk");
		}
		data.get_Float(0).setValue(1.0);
	}

	public static String str(double value) {
		return str((float) value);
	}

	private void drawSideBar() {
		fill(Env.eraseColor);
		rect(600, 0, 300, 600);
		fill(Env.textColor);
		textAlign(CENTER);
		textSize(30);
		text("Status", 600 + 150, 40);
		textAlign(LEFT);
		textSize(15);
		int tmpSpace = 30, tmpBegin = 50;
		text("System stats:", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Frame rate: " + str(frameRate), 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Refresh rate: " + str(Env.refreshTime) + " ms", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Allocated memory: " + Env.format(Runtime.getRuntime().maxMemory() / (1024 * 1024.0f)) + " MB", 610,
				tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Used memory: " + Env.format(Runtime.getRuntime().totalMemory() / (1024 * 1024.0f)) + " MB", 610,
				tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Current free memory: " + Env.format(Runtime.getRuntime().freeMemory() / (1024 * 1024.0f)) + " MB", 610,
				tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("  Total free memory: " + Env.format(Runtime.getRuntime().maxMemory() / (1024 * 1024.0f)
				- Runtime.getRuntime().totalMemory() / (1024 * 1024.0f)) + " MB", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		if (Runtime.getRuntime().totalMemory() / Runtime.getRuntime().maxMemory() > lowMemoryThreshold) {
			fill(255, 0, 0);
			text("Warning! System low on memory", 610, tmpBegin + tmpSpace);
			tmpBegin += tmpSpace;
			fill(Env.textColor);
		}
		tmpBegin += tmpSpace;
		text("Shortcuts:", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("e: end this network's turn", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("l: decrease this network's lc", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		text("space: take a screenshot", 610, tmpBegin + tmpSpace);
		tmpBegin += tmpSpace;
		tmpBegin += tmpSpace;
		if (!shutdown) {
			text("press \"s\" to automatically", 610, tmpBegin + tmpSpace);
			tmpBegin += tmpSpace;
			text("shutdown once training is done", 610, tmpBegin + tmpSpace);
			tmpBegin += tmpSpace;
		} else {
			text("computer will shutdown once training", 610, tmpBegin + tmpSpace);
			tmpBegin += tmpSpace;
			text("is done. Press \"s\" to cancel", 610, tmpBegin + tmpSpace);
			tmpBegin += tmpSpace;
		}
	}

	public void work0(InfoPacket data) {
		int num = 3;
		float space = (float) (1.0 / num);
		trainX = Dir.addBias(trainX);
		data.get_Float(0).setValue(space * 1);
		testX = Dir.addBias(testX);
		data.get_Float(0).setValue(space * 2);
		for (int i = 0; i < nets.size(); i++) {
			nets.get(i).miniBatch(trainX, trainP, 150);
			nets.get(i).testSet(testX.slash(0, 100), testP.slash(0, 100));
		}
		data.get_Float(0).setValue(space * 3);
	}

	public void work1(InfoPacket data) {
		if (data.getInt(0) > 0) {
			nets.remove(0);
		}
		nets.get(0).startLearning();
		data.get_Float(0).setValue(1);
	}

	public void wait(InfoPacket data) {
		if (shutdown) {
			println("shutting down...");
		}
		int time = millis(), count = 0;
		int refreshTime = 10;
		double ms = (data.getInt(0) * 1000.0);
		while (count < ms / refreshTime) {
			while (millis() - time < refreshTime) {
			}
			time += refreshTime;
			count++;
			data.get_Float(0).setValue(1.0 * refreshTime * count / (1000 * data.getInt(0)));
		}
		data.get_Float(0).setValue(1.0);
	}

	public void draw() {
		if (work.size() > 0) {
			work.get(0).display();
			if (work.get(0).signature() == Network.SIGNATURE) {
				if (work.get(0).threads.size() > 0) {
					Network net = (Network) work.get(0).threads.get(0);
					if (net.active()) {
						if (net.graphReady && !net.ending) {
							if (millis() - time > Env.refreshTime) {
								time += ((millis() - time) / Env.refreshTime) * Env.refreshTime;
								image(net.quadraticErrorGraph.graph(150, 75, 1, 0.95), 0, 0);
								image(net.realQuadraticErrorGraph.graph(150, 75, 1, 0.95), 150, 0);
								image(net.crossEntropyErrorGraph.graph(150, 75, 1, 0.95), 0, 75);
								image(net.realCrossEntropyErrorGraph.graph(150, 75, 1, 0.95), 150, 75);
								image(net.quadraticErrorGraph.graph(150, 75, 1, 0.05), 0, 150);
								image(net.crossEntropyErrorGraph.graph(150, 75, 1, 0.05), 150, 150);
								if (net.autoAdjustLc) {
									image(net.lcGraph.graph(150, 75, 1, 0.95), 300, 150);
									image(net.lcGraph.graph(150, 75, 1, 0.05), 450, 150);
								}
								image(net.confidenceGraph.graph(150, 75, 1, 0.95), 300, 0);
								image(net.accuracyGraph.graph(150, 75, 1, 0.95), 300, 75);
								image(net.realConfidenceGraph.graph(150, 75, 1, 0.95), 450, 0);
								image(net.realAccuracyGraph.graph(150, 75, 1, 0.95), 450, 75);
							}
						}
					}
				}
			}
			if (work.get(0).done()) {
				work.remove(0);
				if (work.size() > 0) {
					work.get(0).start();
				}
			}
		}
		drawSideBar();
		/*
		 * //experimental if(confidence != null) { image(mainImg, 0, 20);
		 * image(confidence, 300, 20); } /
		 **/
		/*
		 * secondaryImg=mainImg.get(mouseX, mouseY, 18, 18); secondaryImg.resize(300,
		 * 300); fill(0);rect(300, 300, 300, 300); image(Env.makeGrey(secondaryImg),
		 * 300, 300); /
		 **/
	}

	public void recognizeABunch(InfoPacket data) {
		int imgSize = 18, halfImgSize = imgSize / 2, scanningInterval = 1;

		mainImg = loadImage("image with numbers.PNG");
		confidence = createGraphics(mainImg.width, mainImg.height);
		confidence.beginDraw();
		confidence.fill(0, 0);
		confidence.loadPixels();
		image(mainImg, 0, 0);
		for (int i = 0; i < mainImg.width; i += scanningInterval) {
			for (int j = 0; j < mainImg.height; j += scanningInterval) {
				secondaryImg = mainImg.get(i - halfImgSize, j - halfImgSize, imgSize, imgSize);
				secondaryImg.resize(28, 28);
				nets.get(0).feed(Env.imageToMatrix(secondaryImg, 0.0, 1.0, 1));
				float value = constrain((float) (Matrix.getConfidence(nets.get(0).ans.getRow(0)) * 0.5), 0, 1);
				confidence.pixels[j * mainImg.width + i] = Env.mix2(color(255, 0, 0), color(255, 255, 255), value);
				data.get_Float(0).setValue(1.0 * (j + i * mainImg.height) / (mainImg.width * mainImg.height));

			}
		}
		confidence.updatePixels();
		confidence.endDraw();
		confidence.save("frame.png");
		data.get_Float(0).setValue(1.0);
	}

	public void keyTyped() {
		if (str(key).equals(" ")) {
			saveFrame("brains.png");
		}
		if (str(key).equals("s")) {
			shutdown = !shutdown;
		}
		if (str(key).equals("e")) {
			if (work.get(0).signature() == Network.SIGNATURE) {
				Network net = (Network) work.get(0).threads.get(0);
				net.halt();
			}
		}
		if (str(key).equals("l")) {
			if (work.get(0).signature() == Network.SIGNATURE) {
				Network net = (Network) work.get(0).threads.get(0);
				net.lc *= 0.99;
			}
		}
	}
}
