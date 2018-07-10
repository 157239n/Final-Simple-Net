package _157239n;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.FloatList;
import processing.data.IntList;

public class Network extends ProgressThread implements java.io.Serializable {
	/**
	 * All forms of identifier for the class Network
	 */
	private static final long serialVersionUID = 157238L;
	public static final long MAGIC_NUMBER = serialVersionUID;
	public static final long SIGNATURE = MAGIC_NUMBER;
	// TODO: see more into what this variable means
	public final static int ES = 0, BP = 1;// learning style
	public final static int GAUS = 0, NOIS = 1, RAND = 2;// random style
	public final static int QUADRATIC = 0, ABSOLUTE = 1, CROSS_ENTROPY = 2;
	// default error function
	// variable initialization: basic characteristics of the network
	/**
	 * The synapses
	 * */
	public Matrix[] syn;
	/**
	 * The answer matrix when the network has done training
	 * */
	public Matrix ans;
	private IntList margins = new IntList(0);
	// additional defining characteristics of the network
	private int iter = 0, startTime, id = -1, nSlice, nGraph, sGraph, learnStyle = BP, costFunction = ABSOLUTE,
			family = 1;
	private float error = PApplet.MAX_INT, ansSD = -1, lastAnsSD = (float) (PApplet.MAX_INT * 1.0);
	public float lc = 0.04f;
	// counters of all kinds
	private int count, _3dGraphCount = 0;
	// state variables
	private boolean exportSlices = false, exportGraph = false, haltSignal = false;
	public boolean graphReady = false, autoAdjustLc = false, ending = false;
	/**
	 * Quadratic-based error graph
	 * */
	public Graph quadraticErrorGraph, realQuadraticErrorGraph;
	/**
	 * Accuracy graph
	 * */
	public Graph accuracyGraph, realAccuracyGraph;
	/**
	 * Confidence graph
	 * */
	public Graph confidenceGraph, realConfidenceGraph;
	/**
	 * Cross entropy-based error graph
	 * */
	public Graph crossEntropyErrorGraph, realCrossEntropyErrorGraph;
	/**
	 * Learning constant graph
	 * */
	public Graph lcGraph;
	// other high-level objects
	// private PGraphics frame;
	private FloatList last10Error = new FloatList(0);
	// multithreading features
	private int cores = 1;
	private ArrayList<Work> _3dWorks = new ArrayList<Work>(0);
	// positioning
	private Rect _3dPos;
	// mini-batches
	private Matrix[] Xs, Ps;
	// test sets
	private Matrix tX, tP;

	/**
	 * Constructor.
	 * 
	 * @param proc
	 *            the processing sketch object
	 * @param margins
	 *            the network's dimensions
	 */
	Network(PApplet proc, IntList margins) {
		super(proc);
		__init__(margins);
	}

	/**
	 * Constructor.
	 * 
	 * @param proc
	 *            the processing sketch object
	 * @param margins
	 *            the network's dimensions
	 */
	Network(PApplet proc, int[] margins) {
		super(proc);
		__init__(Env.IntList(margins));
	}

	/* Property methods----------------------------- */
	/**
	 * Specifies the total number of slices the network will record. <br>
	 * <br>
	 * This is for users that want to have fixed-size information packages as this
	 * operation is really memory intensive.<br>
	 * 
	 * @param numberOfSlice
	 *            the total number of slices the network will record.
	 * @return itself
	 */
	public Network sliceNumber(int numberOfSlice) {
		exportSlices = true;
		this.nSlice = numberOfSlice;
		return this;
	}
	/**
	 * Specifies the total number of graph sampling events the network will do. <br>
	 * <br>
	 * This is for users that want to have fixed-size graph files.<br>
	 * 
	 * @param numberOfGraph
	 *            the total number of graph sampling events
	 * @return itself
	 */
	public Network graphNumber(int numberOfGraph) {
		exportGraph = true;
		sGraph = 0;
		nGraph = numberOfGraph;
		return this;
	}
	/**
	 * Specifies the distance between graph sampling events.
	 * 
	 * @param spaceBetweenGraphs
	 *            the distance between graph sampling events
	 * @return itself
	 */
	public Network graphSpace(int spaceBetweenGraphs) {
		exportGraph = true;
		nGraph = 0;
		sGraph = spaceBetweenGraphs;
		return this;
	}
	/**
	 * Specifies the learning style of the network. <br>
	 * <br>
	 * This is either {@link Network#ES} or {@link Network#BP}.<br>
	 * 
	 * @param learnStyle
	 *            the learning style of the network
	 * @return itself
	 */
	public Network learningStyle(int learnStyle) {
		this.learnStyle = learnStyle;
		return this;
	}
	/**
	 * Specifies the intended learning times of the network.
	 * 
	 * @param learnTimes
	 *            the intended learning times of the network
	 * @return itself
	 */
	public Network learningTimes(int learnTimes) {
		iter = learnTimes;
		return this;
	}
	/**
	 * Specifies the activation family of the network.
	 * 
	 * @param family
	 *            the activation family
	 * @return itself
	 */
	public Network activationFamily(int family) {
		this.family = family;
		return this;
	}
	/**
	 * Specifies whether the network will automatically decrease the learning
	 * constant whenever the error deviates a lot.
	 * 
	 * @return itself
	 */
	public Network autoAdjustLc() {
		autoAdjustLc = true;
		return this;
	}
	/**
	 * Sets the initial learning constant for this network. <br>
	 * <br>
	 * The default learning constant is 0.04<br>
	 * 
	 * @param lc
	 *            the learning constant
	 * @return itself
	 */
	public Network setLc(float lc) {
		this.lc = lc;
		return this;
	}
	/**
	 * Sets the initial learning constant for this network. <br>
	 * <br>
	 * The default learning constant is 0.04<br>
	 * 
	 * @param lc
	 *            the learning constant
	 * @return itself
	 */
	public Network setLc(double lc) {
		return setLc((float) lc);
	}
	/**
	 * Sets this network's id for exporting the training data. <br>
	 * <br>
	 * If this method is not called then the id will be a randomly generated integer
	 * between 0 and 9999.<br>
	 * 
	 * @param id
	 *            the network's intended id
	 * @return itself
	 */
	public Network id(int id) {
		this.id = id;
		return this;
	}
	/**
	 * Tells the network the number of cores we have (default is 2).
	 * 
	 * @param cores
	 *            the number of cores our computer has
	 * @return itself
	 */
	public Network cores(int cores) {
		this.cores = cores;
		return this;
	}

	// training and test set configuration
	/**
	 * Sets up mini-batchs.
	 * 
	 * @param lX
	 *            the input sample matrix
	 * @param lP
	 *            the correct output sample matrix
	 * @param setPerBatch
	 *            the number of sets in one batch. If the numbers don't divide then
	 *            the last batch will have an abnormal size.
	 * @return itself
	 */
	public Network miniBatch(Matrix lX, Matrix lP, int setPerBatch) {
		int numBatches = lX.m / setPerBatch + (1 - Env.kron(lX.m % setPerBatch, 0));
		Xs = new Matrix[numBatches];
		Ps = new Matrix[numBatches];
		for (int i = 0; i < numBatches - 1; i++) {
			Xs[i] = lX.slash(i * setPerBatch, setPerBatch);
			Ps[i] = lP.slash(i * setPerBatch, setPerBatch);
		}
		{
			int tmp = (numBatches - 1) * setPerBatch;
			Xs[numBatches - 1] = lX.slash(tmp, lX.m - tmp);
			Ps[numBatches - 1] = lP.slash(tmp, lP.m - tmp);
		}
		return this;
	}
	/**
	 * Sets up mini-batches with the default batch size of 150.
	 * 
	 * @param lX
	 *            the input sample matrix
	 * @param lP
	 *            the correct output sample matrix
	 * @return itself
	 */
	public Network miniBatch(Matrix lX, Matrix lP) {
		miniBatch(lX, lP, 150);
		return this;
	}
	/**
	 * Sets up batch learning or mini-batch with the default batch size equaling the
	 * sample's length.
	 * 
	 * @param lX
	 *            the input sample matrix
	 * @param lP
	 *            the correct output sample matrix
	 * @return itself
	 */
	public Network batch(Matrix lX, Matrix lP) {
		Xs = new Matrix[1];
		Ps = new Matrix[1];
		Xs[0] = lX.clone();
		Ps[0] = lP.clone();
		return this;
	}
	/**
	 * Sets up stochastic learning or mini-batch with the default batch size of 1.
	 * 
	 * @param lX
	 *            the input sample matrix
	 * @param lP
	 *            the correct output sample matrix
	 * @return itself
	 */
	public Network stochastic(Matrix lX, Matrix lP) {
		miniBatch(lX, lP, 1);
		return this;
	}
	/**
	 * Sets up the test set samples that the network will not train with but will
	 * use to record the performance.
	 * 
	 * @param X
	 *            the input test matrix
	 * @param P
	 *            the correct output test matrix
	 * @return itself
	 */
	public Network testSet(Matrix X, Matrix P) {
		this.tX = X;
		this.tP = P;
		return this;
	}

	/* Mathematical methods ----------------------- */
	// Cost functions
	/**
	 * Splits the matrix into multiple rows and calculates the average quadratic
	 * error of the rows.
	 * 
	 * @param D
	 *            the input matrix or the Delta matrix
	 * @return the average quadratic error
	 */
	public static float quadraticError(Matrix D) {
		float avg = (float) 0.0;
		for (int i = 0; i < D.m; i++) {
			for (int j = 0; j < D.n; j++) {
				avg += Math.pow(D.getValue(i, j), 2);
			}
		}
		avg /= 2 * D.m;
		return avg;
	}
	/**
	 * Splits the matrix into multiple rows and calculates the average over-the-rows
	 * error by summing the absolute value of the values in a row. <br>
	 * 
	 * @param D
	 *            the input matrix or the Delta matrix
	 * @return the average absolute error
	 */
	public static float absoluteError(Matrix D) {
		float avg = (float) 0.0;
		for (int i = 0; i < D.m; i++) {
			for (int j = 0; j < D.n; j++) {
				avg += Math.abs(D.getValue(i, j));
			}
		}
		avg /= D.m;
		return avg;
	}
	/**
	 * Splits the matrix into multiple rows and calculates the average cross-entropy
	 * error of the rows.
	 * 
	 * @param D
	 *            the input matrix or the Delta matrix
	 * @param lowerBound
	 *            the lower bound of the activation function range
	 * @param upperBound
	 *            the upper bound of the activation function range
	 * @return the average cross-entropy error
	 */
	public static float crossEntropyError(Matrix D, float lowerBound, float upperBound) {
		if (upperBound - lowerBound < 0) {
			throw new RuntimeException("state2 is smaller than state1");
		}
		if (Env.kron(Env.kron(lowerBound, upperBound))) {
			throw new RuntimeException("state2 nearly equals state1.");
		}
		float avg = (float) 0.0;
		float diff = upperBound - lowerBound;
		for (int i = 0; i < D.m; i++) {
			for (int j = 0; j < D.n; j++) {
				float v = Math.abs(D.getValue(i, j));
				float a = (v - lowerBound) / diff;
				float b = (upperBound - v) / diff;
				avg += a * Math.log(a) + b * Math.log(b);
			}
		}
		avg /= D.m;
		avg = -avg;
		return avg;
	}
	/**
	 * Splits the matrix into multiple rows and calculates the average cross-entropy
	 * error of the rows.
	 * 
	 * @param D
	 *            the input matrix or the Delta matrix
	 * @return the average cross-entropy error
	 */
	public float crossEntropyError(Matrix D) {
		float[] bounds = Matrix.familyBounds(family);
		return crossEntropyError(D, bounds[0], bounds[1]);
	}
	/**
	 * Calculates the cost function based on one of the existing cost functions:
	 * {@link Network#quadraticError(Matrix)}, {@link Network#absoluteError(Matrix)}
	 * or {@link Network#crossEntropyError(Matrix, float, float)}. <br>
	 * <br>
	 * The default cost function is the quadratic cost function.<br>
	 * If the network is using the cross-entropy cost function then the range of
	 * operation will be chosen automatically.<br>
	 * 
	 * @param D
	 *            the input matrix or the Delta matrix
	 * @return the error of the matrix
	 * @throws RuntimeException
	 *             when there's a cost function it doesn't recognize
	 * @throws RuntimeException
	 *             when there's an activation family it doesn't recognize
	 */
	public float error(Matrix D) {
		if (costFunction == QUADRATIC) {
			return quadraticError(D);
		} else if (costFunction == ABSOLUTE) {
			return absoluteError(D);
		} else if (costFunction == CROSS_ENTROPY) {
			float[] bounds = Matrix.familyBounds(family);
			return crossEntropyError(D, bounds[0], bounds[1]);
		} else {
			throw new RuntimeException("don't recognize that cost function");
		}
	}
	/**
	 * Calculates the quadratic error of a sample containing 2 matrix.
	 * 
	 * @param X
	 *            the input sample
	 * @param P
	 *            the correct answer for that sample
	 * @return the quadratic error of that sample
	 * @see Network#quadraticError(Matrix)
	 */
	public float quadraticError(Matrix X, Matrix P) {
		return quadraticError(Matrix.sub(P, feed(X)));
	}
	/**
	 * Calculates the absolute error of a sample containing 2 matrix.
	 * 
	 * @param X
	 *            the input sample
	 * @param P
	 *            the correct answer for that sample
	 * @return the absolute error of that sample
	 * @see Network#absoluteError(Matrix)
	 */
	public float absoluteError(Matrix X, Matrix P) {
		return absoluteError(Matrix.sub(P, feed(X)));
	}
	/**
	 * Calculates the cross-entropy error of a sample containing 2 matrix.
	 * 
	 * @param X
	 *            the input sample
	 * @param P
	 *            the correct answer for that sample
	 * @param lowerBound
	 *            the lower bound of the activation function range
	 * @param upperBound
	 *            the upper bound of the activation function range
	 * @return the cross-entropy error of that sample
	 * @see Network#crossEntropyError(Matrix, float, float)
	 */
	public float crossEntropyError(Matrix X, Matrix P, float lowerBound, float upperBound) {
		return crossEntropyError(Matrix.sub(P, feed(X)), lowerBound, upperBound);
	}
	/**
	 * Calculates the cross-entropy error of a sample containing 2 matrix.
	 * 
	 * @param X
	 *            the input sample
	 * @param P
	 *            the correct answer for that sample
	 * @return the cross-entropy error of that sample
	 * @see Network#crossEntropyError(Matrix, float, float)
	 */
	public float crossEntropyError(Matrix X, Matrix P) {
		float[] bounds = Matrix.familyBounds(family);
		return crossEntropyError(Matrix.sub(P, feed(X)), bounds[0], bounds[1]);
	}
	/**
	 * Calculates the error of a sample containing 2 matrix.
	 * 
	 * @param X
	 *            the input sample
	 * @param P
	 *            the correct answer for that sample
	 * @return the error of that sample
	 * @see Network#error(Matrix)
	 */
	public float error(Matrix X, Matrix P) {
		return error(Matrix.sub(P, feed(X)));
	}

	// Learning algorithms
	/**
	 * Feeds the input sample matrix into specified synapses.
	 * 
	 * @param matricies
	 *            the synapses
	 * @param X
	 *            the input sample matrix
	 * @return the resulting matrix
	 */
	private static Matrix feed(Matrix[] matricies, Matrix X) {
		Matrix[] l = new Matrix[matricies.length + 1];
		l[0] = X;
		for (int i = 0; i < matricies.length; i++) {
			l[i + 1] = Matrix.acti(Matrix.dotp(l[i], matricies[i]), Matrix.family);
		}
		return l[l.length - 1];
	}
	/**
	 * Feeds the input sample matrix into this network
	 * 
	 * @param X
	 *            the input sample matrix
	 * @return the resulting matrix
	 */
	Matrix feed(Matrix X) {
		ans = feed(syn, X);
		return ans.clone();
	}
	/**
	 * Learns by backpropagation.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 */
	void learnBP(Matrix X, Matrix P) {// gradient descent
		// initializing some variables
		Matrix[] l = new Matrix[syn.length + 1];
		l[0] = X.clone();
		Matrix[] s = new Matrix[syn.length + 1];
		s[0] = null;
		Matrix[] d = new Matrix[syn.length];
		// feed forward
		for (int i = 0; i < syn.length; i++) {
			s[i + 1] = Matrix.dotp(l[i], syn[i]);
			l[i + 1] = Matrix.acti(s[i + 1], family);
		}
		ans = l[l.length - 1];
		// calculate last delta, rule: (Y-l2)*sigmoid'(l2)
		d[syn.length - 1] = Matrix.mul(Matrix.sub(P, l[syn.length]), Matrix.actiDe(s[syn.length], family));
		// calculate deltas, rule: (l2d.syn1T)*sigmoid'(l1)
		for (int i = syn.length - 2; i >= 0; i--) {
			d[i] = Matrix.mul(Matrix.dotp(d[i + 1], syn[i + 1].T()), Matrix.actiDe(s[i + 1], family));
		}
		for (int i = 0; i < syn.length; i++) {
			syn[i] = Matrix.add(syn[i], Matrix.mul(Matrix.dotp(l[i].T(), d[i]), lc));
		} // learn, rule: //syn1+=l1T.l2d
	}
	/**
	 * Learns by backpropagation multiple times.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 * @param times
	 *            the number of times to learn
	 */
	@Deprecated
	private void learn(Matrix X, Matrix P, int times) {
		for (int i = 0; i < times; i++) {
			learn(X, P);
		}
	}
	/**
	 * Learns by backpropagation multiple times while adjusting variables each time.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 * @param times
	 *            the number of times to learn
	 * @param nets
	 *            the number of times to start over and learn again
	 */
	@Deprecated
	private void learn(Matrix X, Matrix P, int times, int nets) {
		Matrix[] out = exportSyns();
		float minError = error(X, P);
		for (int w = 0; w < nets; w++) {
			newSyns();
			learn(X, P, times);
			float error = error(X, P);
			if (minError > error) {
				minError = error;
				out = exportSyns();
			}
		}
		importSyns(out);
	}
	/**
	 * Learns by evolutionary strategy.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 * @return whether the network was updated after one pass through the method
	 */
	private boolean learnESGuts(Matrix X, Matrix P) {
		boolean ans = false;
		Matrix[] guess = Matrix.tweak(syn, lc)/* tweak(syn, min(error/10, 0.01))/ **/;
		float newError = error(Matrix.sub(P, feed(guess, X)));
		if (newError < error) {
			syn = guess;
			error = newError;
			ans = true;
		}
		return ans;
	}
	/**
	 * Learns by evolutionary strategy.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 * @param times
	 *            the number of times to learn
	 */
	private void learnES(Matrix X, Matrix P, int times) {
		for (int i = 0; i < times; i++) {
			learnESGuts(X, P);
		}
	}
	/**
	 * Learns by evolutionary strategy till the network has been updated.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 */
	private void learnESLoop(Matrix X, Matrix P) {
		boolean lc = false;
		while (!lc) {
			lc = learnESGuts(X, P);
		}
	}
	/**
	 * Learns 1 time by evolutionary strategy.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 */
	private void learnES(Matrix X, Matrix P) {
		learnESGuts(X, P);
	}
	/**
	 * Just learn with the defaults.
	 * 
	 * @param X
	 *            the input sample matrix
	 * @param P
	 *            the correct output sample matrix
	 */
	private void learn(Matrix X, Matrix P) {
		if (learnStyle == ES) {
			learnES(X, P);
		} else if (learnStyle == BP) {
			learnBP(X, P);
		}
	}

	/* Program flow methods */
	/**
	 * Initializes very random but crucial things. <br>
	 * 
	 * Should always be called after constructing the object
	 * 
	 * @param margins
	 *            the network's dimensions
	 */
	private void __init__(IntList margins) {
		Xs = new Matrix[0];
		Ps = new Matrix[0];
		syn = new Matrix[margins.size() - 1];
		this.margins = margins;
		newSyns();
		id = (int) Math.round(Math.random() * 1000);
		initGraph();
	}
	/**
	 * Initializes a bunch of graphs. <br>
	 * <br>
	 * This includes:<br>
	 * - {@link Network#quadraticErrorGraph} -
	 * {@link Network#realQuadraticErrorGraph} - {@link Network#lcGraph} -
	 * {@link Network#confidenceGraph} - {@link Network#accuracyGraph} -
	 * {@link Network#realConfidenceGraph} - {@link Network#realAccuracyGraph}
	 */
	private void initGraph() {
		quadraticErrorGraph = new Graph(p).addDesc("quadE");
		realQuadraticErrorGraph = new Graph(p).addDesc("rQuadE");
		lcGraph = new Graph(p).addDesc("lc");
		confidenceGraph = new Graph(p).addDesc("conf");
		accuracyGraph = new Graph(p).addDesc("acc");
		realConfidenceGraph = new Graph(p).addDesc("rConf");
		realAccuracyGraph = new Graph(p).addDesc("rAcc");
		crossEntropyErrorGraph = new Graph(p).addDesc("cEnE");
		realCrossEntropyErrorGraph = new Graph(p).addDesc("rCEnE");
	}
	/**
	 * Move stuff around to make it look nice
	 */
	private Network move() throws RuntimeException {
		if (location.x < 0 || location.y < 0 || location.x + location.w > p.width
				|| location.y + location.h > p.height) {
			throw new RuntimeException("the image produced will be positioned out of screen");
		}
		int commonHeight = location.h / (progressLength() + 1);
		_3dPos = new Rect(location.x, location.y + 2 * commonHeight, location.w, commonHeight);
		return this;
	}
	/**
	 * Gives the network the go ahead to start learning
	 * 
	 * @param learnTimes
	 *            the expected learn iteration
	 * @throws RuntimeException
	 *             whenever there is no training samples inside the network
	 */
	public void startLearning(int learnTimes) throws RuntimeException {
		if (Xs == null) {
			throw new RuntimeException(
					"there haven't been any training samples inside the network yet. To create one, call Network#miniBatch(X, P, size), Network#miniBatch(X, P), Network#stochastic(X, P) or batch(X, P).");
		}
		ending = false;
		error = error(Xs[0], Ps[0]);
		iter = learnTimes;
		if (nGraph > 0) {
			sGraph = Math.max(iter, nGraph) / nGraph;
			nGraph = 0;
		}
		startTime = p.millis();
		// 0 for main function, 1 for endTurn, 2 for export3d, 3 for cleaning up
		initProgresses(4);
		for (int i = 0; i < progressLength(); i++) {
			attachProgress(i, new _Float(0.0));
		}
		move();
	}
	/**
	 * Starts learning with a preset learn iteration.
	 * 
	 * @throws RuntimeException
	 *             whenever the user has not specified the number of iterations to
	 *             learn
	 * @see Network#learningTimes(int)
	 * @see Network#startLearning(int)
	 */
	public void startLearning() {
		if (iter <= 0) {
			throw new RuntimeException(
					"learning times has the value of " + Env.format(iter) + ". Please specify this.");
		}
		startLearning(iter);
	}
	/**
	 * The main function to run as {@link Network} extends from {@link Thread}
	 */
	public void run() {
		for (int i = 0; i < iter; i++) {
			count = i;
			learn(Xs[i % Xs.length], Ps[i % Ps.length]);
			update(i);
			setTrainingProgress(1.0 * i / iter);
			if (haltSignal) {
				break;
			}
		}
		setTrainingProgress(1.0);
		endTurn(getProgressAttachment(1));
		handle3dThreads(getProgressAttachment(2));
		freeMem(getProgressAttachment(3));
		Env.threadDelay(3*Env.refreshTime);
	}
	/**
	 * This is called so that we can collect information about the network such as
	 * the accuracy graphs after one training iteration.
	 * 
	 * @param count
	 *            the current learn iteration (so that we know when to sample the
	 *            graphs).
	 */
	private void update(int count) {
		feed(Xs[0]);
		Matrix lD = Matrix.sub(Ps[0], ans);
		float error = quadraticError(lD);
		if (autoAdjustLc) {
			if (last10Error.size() >= 10) {
				last10Error.remove(0);
			}
			last10Error.push(error(lD));
			ansSD = Env.SD(last10Error);
			if (learnStyle == BP) {
				if (1.0 * Math.abs(lastAnsSD - ansSD) / ansSD > 0.2) {
					// TODO: figure a better way to decrease the learning constant
					lc *= 0.99;
				}
				lastAnsSD = ansSD;
			} else if (learnStyle == ES) {
				if (ansSD < Env.epsilon) {
					lc *= 0.99;
				}
			}
		}
		if (count < iter) {
			if (exportSlices) {
				if (count % (iter / nSlice) == 0) {
					for (int i = 0; i < syn.length; i++) {
						getSlice(i).save("id " + PApplet.str(id) + "/layer " + PApplet.str(i) + "/times "
								+ PApplet.str(count / (iter / nSlice)) + ".png");
					}
				}
			}
			if (exportGraph) {
				int sp = 0;
				if (sGraph != 0) {
					sp = sGraph;
				} else if (nGraph != 0) {
					sp = iter / nGraph;
				} else {
					throw new RuntimeException(
							"no graph interval information. Function: update(), Network class, Network tab");
				}
				if (count % sp == 0) {
					if (sp > 1) {
						quadraticErrorGraph.total(count);
						lcGraph.total(count);
						confidenceGraph.total(count);
						accuracyGraph.total(count);
						crossEntropyErrorGraph.total(count);
						realQuadraticErrorGraph.total(count);
						realConfidenceGraph.total(count);
						realAccuracyGraph.total(count);
						realCrossEntropyErrorGraph.total(count);
					}
					lcGraph.add(lc);
					quadraticErrorGraph.add(error);
					confidenceGraph.add(Matrix.calcConfidence(ans));
					accuracyGraph.add(Matrix.calcAccuracy(ans, Ps[0]));
					crossEntropyErrorGraph.add(crossEntropyError(lD));
					feed(tX);
					lD = Matrix.sub(tP, ans);
					realQuadraticErrorGraph.add(quadraticError(lD));
					realConfidenceGraph.add(Matrix.calcConfidence(ans));
					realAccuracyGraph.add(Matrix.calcAccuracy(ans, tP));
					realCrossEntropyErrorGraph.add(crossEntropyError(lD));
					graphReady = true;
				} else {
					graphReady = false;
				}
			}
		}
	}
	/**
	 * Prepares to end the network's term.
	 * 
	 * @param progress
	 *            the progress to keep track of the function
	 */
	private void endTurn(_Float progress) {
		ending = true;
		progress.setValue(0.0);
		int num = 23;
		float space = (float) (1.0 / num);
		String commonDir = "id " + PApplet.str(id) + "/";
		System.out.println("quadratic error: " + PApplet.str(quadraticErrorGraph.getValue(-1)));
		progress.setValue(space * 1);
		exportSyns(commonDir + "0.synx");
		progress.setValue(space * 2);
		ans.print(commonDir + "ans.txt");
		progress.setValue(space * 3);

		p.saveStrings(commonDir + "data.txt", info());
		progress.setValue(space * 4);
		/* exporting graph images */
		if (sGraph > 1) {
			quadraticErrorGraph.total(count);
			realQuadraticErrorGraph.total(count);
			accuracyGraph.total(count);
			realAccuracyGraph.total(count);
			confidenceGraph.total(count);
			realConfidenceGraph.total(count);
			crossEntropyErrorGraph.total(count);
			realCrossEntropyErrorGraph.total(count);
			if (autoAdjustLc) {
				lcGraph.total(count);
			}
		}
		{
			quadraticErrorGraph.addDesc("");
			realQuadraticErrorGraph.addDesc("");
			accuracyGraph.addDesc("");
			realAccuracyGraph.addDesc("");
			confidenceGraph.addDesc("");
			realConfidenceGraph.addDesc("");
			crossEntropyErrorGraph.addDesc("");
			realCrossEntropyErrorGraph.addDesc("");
			if (autoAdjustLc) {
				lcGraph.addDesc("");
			}
		}
		quadraticErrorGraph.graph(900, 450, 1, 0.95)
				.save("id " + PApplet.str(id) + "/graph images/1 quadratic error.png");
		progress.setValue(space * 5);
		realQuadraticErrorGraph.graph(900, 450, 1, 0.95)
				.save("id " + PApplet.str(id) + "/graph images/2 real quadratic error.png");
		progress.setValue(space * 6);
		accuracyGraph.graph(900, 450, 1, 0.95).save("id " + PApplet.str(id) + "/graph images/3 accuracy.png");
		progress.setValue(space * 7);
		realAccuracyGraph.graph(900, 450, 1, 0.95).save("id " + PApplet.str(id) + "/graph images/4 real accuracy.png");
		progress.setValue(space * 8);
		confidenceGraph.graph(900, 450, 1, 0.95).save("id " + PApplet.str(id) + "/graph images/5 confidence.png");
		progress.setValue(space * 9);
		realConfidenceGraph.graph(900, 450, 1, 0.95)
				.save("id " + PApplet.str(id) + "/graph images/6 real confidence.png");
		progress.setValue(space * 10);
		crossEntropyErrorGraph.graph(900, 450, 1, 0.95)
				.save("id " + PApplet.str(id) + "/graph images/7 cross entropy error.png");
		progress.setValue(space * 11);
		realCrossEntropyErrorGraph.graph(900, 450, 1, 0.95)
				.save("id " + PApplet.str(id) + "/graph images/8 real cross entropy error.png");
		progress.setValue(space * 12);
		if (autoAdjustLc) {
			lcGraph.graph(900, 450, 1, 0.95).save("id " + PApplet.str(id) + "/graph images/9 lc.png");
		}
		progress.setValue(space * 13);
		/* exporting graphs */
		quadraticErrorGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/1 error.graph");
		progress.setValue(space * 14);
		realQuadraticErrorGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/2 real error.graph");
		progress.setValue(space * 15);
		accuracyGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/3 accuracy.graph");
		progress.setValue(space * 16);
		realAccuracyGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/4 real accuracy.graph");
		progress.setValue(space * 17);
		confidenceGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/5 confidence.graph");
		progress.setValue(space * 18);
		realConfidenceGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/6 real confidence.graph");
		progress.setValue(space * 19);
		crossEntropyErrorGraph
				.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/7 cross entropy error.graph");
		progress.setValue(space * 20);
		realCrossEntropyErrorGraph
				.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/8 real cross entropy error.graph");
		progress.setValue(space * 21);
		if (autoAdjustLc) {
			lcGraph.exportGraph(Final_Simple_Net.parentAbsPath + commonDir + "graphs/9 lc.graph");
		}
		progress.setValue(space * 22);
		{
			Matrix llX = tX.slash(0, 3), llP = tP.slash(0, 3);
			ArrayList<SimpleProgressThread> lThreads = new ArrayList<SimpleProgressThread>(0);
			// add new threads to your will
			Network tmpNet = this;
			_Float[] tFloat = new _Float[10];
			for (int i = 0; i < tFloat.length; i++) {
				tFloat[i] = new _Float(0.0);
			}
			lThreads.add(new SimpleProgressThread(tFloat[0]) {
				public void run() {
					Dir.export3d(p, tFloat[0], tmpNet, llX, llP, "id " + PApplet.str(id) + "/3d planes/0",
							new PVector((float) 0.0, (float) 0.0));
				}
			});
			lThreads.add(new SimpleProgressThread(tFloat[1]) {
				public void run() {
					Dir.export3d(p, tFloat[1], tmpNet, llX, llP, "id " + PApplet.str(id) + "/3d planes/1",
							new PVector((float) 0.5, (float) 0.0));
				}
			});
			lThreads.add(new SimpleProgressThread(tFloat[2]) {
				public void run() {
					Dir.export3d(p, tFloat[2], tmpNet, llX, llP, "id " + PApplet.str(id) + "/3d planes/2",
							new PVector((float) 0.5, (float) 5.0));
				}
			});
			lThreads.add(new SimpleProgressThread(tFloat[3]) {
				public void run() {
					Dir.export3d(p, tFloat[3], tmpNet, llX, llP, "id " + PApplet.str(id) + "/3d planes/3",
							new PVector((float) 1.0, (float) 0.0));
				}
			});
			lThreads.add(new SimpleProgressThread(tFloat[4]) {
				public void run() {
					Dir.export3d(p, tFloat[4], tmpNet, llX, llP, "id " + PApplet.str(id) + "/3d planes/4",
							new PVector((float) 1.0, (float) 1.0));
				}
			});
			// transfer the list of threads into a work form
			while (lThreads.size() > 0) {
				Work newWork = new Work(p, _3dPos);
				for (int i = 0; i < cores; i++) {
					if (lThreads.size() > 0) {
						newWork.add(lThreads.get(0));
						lThreads.remove(0);
					}
				}
				newWork.initProgresses(newWork.numWork());
				_3dWorks.add(newWork.multithread());
			}
			_3dGraphCount = _3dWorks.size();
			_3dWorks.get(0).start();
		}
		progress.setValue(space * 23);
	}
	/**
	 * Does all the job of maintaining the 3d exporting process.
	 * 
	 * @param progress
	 *            the progress to report back
	 */
	private void handle3dThreads(_Float progress) {
		int time = p.millis();
		while (_3dWorks.size() > 0) {
			if (p.millis() - time > Env.refreshTime) {
				time += Env.refreshTime;
				update3dThreads(progress);
			}
		}
	}
	/**
	 * Does all the job of maintaining 1 snapshot of the 3d threads.
	 * 
	 * @param progress
	 *            the progress to report back
	 */
	private void update3dThreads(_Float progress) {
		if (_3dWorks.size() > 0) {
			if (_3dWorks.get(0).getProgress() >= 1 - Env.epsilon) {
				_3dWorks.remove(0);
				if (_3dWorks.size() > 0) {
					_3dWorks.get(0).start();
				}
			}
		}
		if (_3dWorks.size() > 0) {
			progress.setValue(1.0 * (_3dGraphCount - _3dWorks.size()) / _3dGraphCount
					+ _3dWorks.get(0).getProgress() / _3dGraphCount);
		} else {
			progress.setValue((_3dGraphCount - _3dWorks.size()) / _3dGraphCount);
		}
	}
	/**
	 * Frees graphs' memory.
	 * 
	 * @param progress
	 *            the progress to keep track of the function
	 */
	private void freeMem(_Float progress) {
		PApplet.println("in freeMem");
		ArrayList<WeakReference<Graph>> graphRefs = new ArrayList<WeakReference<Graph>>(0);
		graphRefs.add(new WeakReference<Graph>(quadraticErrorGraph));
		quadraticErrorGraph = null;
		graphRefs.add(new WeakReference<Graph>(realQuadraticErrorGraph));
		realQuadraticErrorGraph = null;
		graphRefs.add(new WeakReference<Graph>(lcGraph));
		lcGraph = null;
		graphRefs.add(new WeakReference<Graph>(accuracyGraph));
		accuracyGraph = null;
		graphRefs.add(new WeakReference<Graph>(realAccuracyGraph));
		realAccuracyGraph = null;
		graphRefs.add(new WeakReference<Graph>(confidenceGraph));
		confidenceGraph = null;
		graphRefs.add(new WeakReference<Graph>(realConfidenceGraph));
		realConfidenceGraph = null;
		graphRefs.add(new WeakReference<Graph>(crossEntropyErrorGraph));
		crossEntropyErrorGraph = null;
		graphRefs.add(new WeakReference<Graph>(realCrossEntropyErrorGraph));
		realCrossEntropyErrorGraph = null;
		int initialSize = graphRefs.size();
		System.gc();
		while (graphRefs.size() > 0) {
			Env.threadDelay(Env.refreshTime);
			System.gc();
			for (int i = 0; i < graphRefs.size(); i++) {
				if (graphRefs.get(i).get() == null) {
					graphRefs.remove(i);
				}
			}
			progress.setValue(1 - graphRefs.size() / initialSize);
		}
		progress.setValue(1.0);
	}

	/* Program state checking methods */
	/**
	 * Provide a summary of the network's characteristics
	 * 
	 * @error the new error to export
	 */
	private String[] info() {
		String st = "";
		for (int i = 0; i < margins.size(); i++) {
			st += PApplet.str(margins.get(i)) + " ";
		}
		feed(Xs[0]);
		int decimalAccuracy = 9;
		String[] tmp = { "Network's basic characteristics:", "  task: recognizing digits",
				"  architecture: simple feed forward network", "  dimension: " + st,
				"  activation function: " + Matrix.famDesc[family], "  learning constant: " + PApplet.str(lc),
				"  learning paradigm {ES, BP}: " + PApplet.str(learnStyle), "Learning details:",
				"  expected iterations: " + PApplet.str(iter), "  expected epochs:" + Env.format(iter / Xs.length),
				"  iterations: " + PApplet.str(count), "  epochs: " + Env.format(count / Xs.length),
				"  number of test samples: " + PApplet.str(Xs[0].m * (Xs.length - 1) + Xs[Xs.length - 1].m),
				"  number of batches: " + PApplet.str(Xs.length), "  average batch size: " + PApplet.str(Xs[0].m),
				"  last batch size: " + PApplet.str(Xs[Xs.length - 1].m), "Results:",
				"  quadratic error: " + Env.format(quadraticErrorGraph.getValue(-1), decimalAccuracy),
				"  cross entropy error: " + Env.format(crossEntropyErrorGraph.getValue(-1), decimalAccuracy),
				"  accuracy: " + Env.format(accuracyGraph.getValue(-1), decimalAccuracy),
				"  confidence: " + Env.format(confidenceGraph.getValue(-1), decimalAccuracy),
				"  real quadratic error: " + Env.format(realQuadraticErrorGraph.getValue(-1), decimalAccuracy),
				"  real cross entropy error: " + Env.format(realCrossEntropyErrorGraph.getValue(-1), decimalAccuracy),
				"  real accuracy: " + Env.format(realAccuracyGraph.getValue(-1), decimalAccuracy),
				"  real confidence: " + Env.format(realConfidenceGraph.getValue(-1), decimalAccuracy) };
		return tmp;
	}
	/**
	 * Returns whether the network is actively running (training and exporting).
	 * 
	 * @return whether the network is running
	 */
	public boolean active() {
		return !done();
	}
	/**
	 * Returns whether the network is done training and is preparing to end.
	 * 
	 * @return whether the network is done training
	 */
	public boolean isEnding() {
		return ending;
	}
	/**
	 * Calculates the estimated training time remaining.
	 * 
	 * @return the string that describes the remaining time
	 */
	private String timeLeft() {
		String ans = "";
		if (!haltSignal) {
			int dT = p.millis() - startTime;
			int timeLeft = (int) Math.round(1.0 * dT / 1000 * (iter - count) / count);
			if (timeLeft / 3600 > 0) {
				ans += PApplet.str(timeLeft / 3600) + "h ";
			}
			if (timeLeft % 3600 / 60 > 0) {
				ans += PApplet.str(timeLeft % 3600 / 60) + "' ";
			}
			if (timeLeft % 60 > 0) {
				ans += PApplet.str(timeLeft % 60) + "s";
			}
			if (ans.equals("")) {
				ans = "done";
			}
		} else {
			ans = "0s";
		}
		return ans;
	}
	/**
	 * Gets a specified layer after being activated and scaled to the desired color
	 * range.
	 * 
	 * This method is activation family dependent so don't use this on activation
	 * functions that have range other than (0, 1)
	 * 
	 * @param layer
	 *            the layer to sample
	 * @return an image of the layer
	 * @throws RuntimeException
	 *             when trying to sample a layer that doesn't exist
	 */
	@Deprecated
	public PImage getSlice(int layer) throws RuntimeException {
		if (layer >= margins.size() - 1) {
			throw new RuntimeException("can't sample a layer that doesn't exist");
		} else {
			PImage ans = p.createImage(syn[layer].n, syn[layer].m, PApplet.RGB);
			ans.loadPixels();
			for (int i = 0; i < syn[layer].m; i++) {
				for (int j = 0; j < syn[layer].n; j++) {
					ans.set(j, i, Env.mix3(p.color(0, 0, 255), p.color(255), p.color(255, 0, 0),
							2 * Matrix.acti(syn[layer].getValue(i, j), 0) - 1));
				}
			}
			ans.updatePixels();
			return ans;
		}
	}
	/**
	 * Returns the id of the network.
	 * 
	 * @return the id of the network
	 */
	public int id() {
		return this.id;
	}

	/* Graphics methods */
	/**
	 * Displays the frame onto the main sketch. <br>
	 * <br>
	 * Due to complex multithreading features, this method should only be called by
	 * the main PApplet thread or it will cause weird graphical disturbances.<br>
	 */
	public void display() {
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		p.textSize(15);
		p.fill(Env.eraseColor);
		p.rect(location.x, location.y, location.w, location.h);
		drawProgress(p.g, Env._String(PApplet.str(id) + " tr " + timeLeft(), PApplet.str(id) + " endTurn",
				PApplet.str(id) + " 3d, not yet", PApplet.str(id) + " cleaning", PApplet.str(id) + " sum"));
		// overdrawing special elements
		if (_3dWorks.size() > 0) {
			_3dWorks.get(0).display(p.g,
					PApplet.str(_3dWorks.size()) + "x" + PApplet.str(_3dWorks.get(0).threadsLength()) + " threads, ");
		}
	}

	/* Out of flow methods that affect the internals */
	/**
	 * Sends a request to the network to stop training. <br>
	 * <br>
	 * Please note that the network will not immediately stop training but will stop
	 * in at most a couple of minutes.<br>
	 */
	public void halt() {
		haltSignal = true;
	}
	/**
	 * Resets stuff to prepare to train another time
	 */
	public void reset() {
		count = 0;
		newSyns();
		resetGraphs();
	}/**/
	/**
	 * Resets all the graphs
	 */
	public void resetGraphs() {
		quadraticErrorGraph.clear();
		realQuadraticErrorGraph.clear();
		lcGraph.clear();
		confidenceGraph.clear();
		realConfidenceGraph.clear();
		accuracyGraph.clear();
		realAccuracyGraph.clear();
		crossEntropyErrorGraph.clear();
		realCrossEntropyErrorGraph.clear();
	}

	// Reset methods
	// TODO: document the network's signature
	// TODO: develop the Network#NOIS mode
	/**
	 * Randomize the network's synapses with specified values. <br>
	 * <br>
	 * For {@link Network#GAUS} mode, the first argument is the mean while the
	 * second is the deviation. See more at
	 * {@link Matrix#gaussianMatrix(int, int, float, float)}.<br>
	 * For {@link Network#RAND} mode, the first argument is the lower bound while
	 * the second is the upper bound. See more at
	 * {@link Matrix#randomMatrix(int, int, float, float)}.<br>
	 * The {@link Network#NOIS} mode hasn't been developed yet.<br>
	 * 
	 * @param mode
	 *            the randomization method
	 * @param arg1
	 *            the first argument
	 * @param arg2
	 *            the second argument
	 * @return itself
	 */
	public Network newSyns(int mode, float arg1, float arg2) throws RuntimeException {
		if (mode == GAUS) {
			for (int i = 0; i < syn.length; i++) {
				syn[i] = Matrix.gaussianMatrix(margins.get(i), margins.get(i + 1), arg1, arg2);
			}
		} else if (mode == NOIS) {
			throw new RuntimeException("haven't have noise mode just yet");
		} else if (mode == RAND) {
			for (int i = 0; i < syn.length; i++) {
				syn[i] = Matrix.randomMatrix(margins.get(i), margins.get(i + 1), arg1, arg2);
			}
		} else {
			throw new RuntimeException("couldn't recognize this random mode");
		}
		return this;
	}
	/**
	 * Randomize the network's synapses with some default values. <br>
	 * <br>
	 * For {@link Network#GAUS} mode, the mean is 0 while the deviation is 1.<br>
	 * For {@link Network#RAND} mode, the range is from -1 to 1.<br>
	 * The {@link Network#NOIS} mode hasn't been developed yet.<br>
	 * 
	 * @param mode
	 *            the randomization method
	 * @return itself
	 */
	public Network newSyns(int mode) {
		if (mode == GAUS) {
			newSyns(mode, 0, 1);
		} else if (mode == RAND) {
			newSyns(mode, -1, 1);
		} else if (mode == NOIS) {
			throw new RuntimeException("haven't have that noise mode just yet");
		}
		return this;
	}
	/**
	 * Randomize the network's synapses using the
	 * {@link Matrix#gaussianMatrix(int, int, float, float)} method with a mean of 0
	 * and a deviation of 0.2.
	 * 
	 * @return itself
	 */
	public Network newSyns() {
		return newSyns(GAUS, 0, (float) 0.2);
	}

	// Cloning methods
	/**
	 * Makes a clone of the network. <br>
	 * <br>
	 * The new network will receive the same synapses and the same id.<br>
	 * 
	 * @return the cloned network
	 */
	public Network clone() {
		Network N = new Network(p, margins.copy());
		for (int i = 0; i < syn.length; i++) {
			N.syn[i] = this.syn[i].clone();
		}
		N.id = id;
		return N;
	}
	/**
	 * Creates a clone of the array of synapses from a given network.
	 * 
	 * @param net
	 *            the network that will have its synapses cloned
	 * @return the cloned array of matrix that describe the synapses
	 */
	public static Matrix[] cloneSynapses(Network net) {
		return Matrix.clone(net.syn);
	}
	/**
	 * Creates a clone of this network's synapses.
	 * 
	 * @return the cloned array of matrix that describe the synapses
	 */
	public Matrix[] cloneSynapses() {
		return cloneSynapses(this);
	}

	// Export and import methods
	/**
	 * Export the synapses as an array of matrix.
	 * 
	 * @return an array of matrix depicting the synapses
	 */
	public Matrix[] exportSyns() {
		return Matrix.clone(syn);
	}
	/**
	 * Import an array of matrix into the network's synapses.
	 * 
	 * @param M
	 *            an array of matrix to be imported
	 * @return itself
	 * @throws RuntimeException
	 *             when the synapses length does not equal the array length
	 * @throws RuntimeException
	 *             when some of the synapses have a dimension different from the
	 *             array
	 */
	public Network importSyns(Matrix[] M) throws RuntimeException {
		if (M.length != syn.length) {
			throw new RuntimeException(
					"mismatch length while importing synapses. Function importSyns(Matrix[] M) in tab Network");
		} else {
			for (int i = 0; i < M.length; i++) {
				if (M[i].m != syn[i].m || M[i].n != syn[i].n) {
					throw new RuntimeException(
							"mismatch dimension while importing synapses. Function importSyns(Matrix[] M) in tab Network. Additional info: ("
									+ PApplet.str(M[i].m) + ", " + PApplet.str(M[i].n) + "), (" + PApplet.str(syn[i].m)
									+ ", " + PApplet.str(syn[i].n) + ")");
				} else {
					syn[i] = M[i].clone();
				}
			}
		}
		return this;
	}
	/**
	 * Force-import an array of matrix into the network's synapses.
	 * 
	 * @param M
	 *            an array of matrix to be imported
	 * @return itself
	 */
	public Network forcedImportSyns(Matrix[] M) {
		boolean tmp = false;
		for (int i = 0; i < M.length - 1; i++) {
			tmp = tmp || (M[i].n != M[i + 1].m);
		}
		if (tmp) {
			System.out.println("dimension warning while forced importing synapses");
		} else {
			IntList m = new IntList(0);
			for (int i = 0; i < M.length; i++) {
				m.append(M[i].m);
			}
			m.append(M[M.length - 1].n);
			this.margins = m;
			this.syn = Matrix.clone(M);
		}
		return this;
	}
	/**
	 * Export the network's synapses as either a .synapses or a .synx file. Please
	 * visit <a href="http://157239n.com/documentation/file%20types/">this
	 * documentation</a> for more information.
	 * 
	 * @param file
	 *            the file name to export
	 * @throws RuntimeException
	 *             when it can't recognize the extension
	 */
	public void exportSyns(String file) {
		if (PApplet.getExtension(file).equals("synapses")) {
			Dir.exportSynapses(file, exportSyns());
		} else if (PApplet.getExtension(file).equals("synx")) {
			if (file.charAt(1) == ':') {
				Dir.exportSynx(file, exportSyns());
			} else {
				Dir.exportSynx(Final_Simple_Net.parentAbsPath + file, exportSyns());
			}
		} else {
			throw new RuntimeException("can't recognize the file extension. Path: " + file);
		}
	}
	/**
	 * Import the network's synapses as either a .synapses or a .synx file. Please
	 * visit <a href="http://157239n.com/documentation/file%20types/">this
	 * documentation</a> for more information.
	 * 
	 * @param file
	 *            the file name to import
	 * @return itself
	 * @throws RuntimeException
	 *             when it can't recognize the extension
	 */
	public Network importSyns(String file) {
		if (PApplet.getExtension(file).equals("synapses")) {
			importSyns(Dir.importSynapses(file));
		} else if (PApplet.getExtension(file).equals("synx")) {
			if (file.charAt(1) == ':') {
				importSyns(Dir.importSynx(file));
			} else {
				importSyns(Dir.importSynx(Final_Simple_Net.parentAbsPath + file));
			}
		} else {
			throw new RuntimeException("can't recognize the file extension. Path: " + file);
		}
		return this;
	}
	/**
	 * Forced-import the network's synapses as either a .synapses or a .synx file.
	 * Please visit <a href="http://157239n.com/documentation/file%20types/">this
	 * documentation</a> for more information.
	 * 
	 * @param file
	 *            the file name to import
	 * @throws RuntimeException
	 *             when it can't recognize the extension
	 */
	public void forcedImportSyns(String file) {
		if (PApplet.getExtension(file).equals("synapses")) {
			forcedImportSyns(Dir.importSynapses(file));
		} else if (PApplet.getExtension(file).equals(".synx")) {
			if (file.charAt(1) == ':') {
				forcedImportSyns(Dir.importSynx(file));
			} else {
				forcedImportSyns(Final_Simple_Net.parentAbsPath + Dir.importSynx(file));
			}
		} else {
			throw new RuntimeException("can't recognize the file extension. Path: " + file);
		}

	}

	// Convenient shorthands
	/**
	 * Sets the training progress to a particular value
	 * 
	 * @param value
	 *            the value to set
	 */
	@SuppressWarnings("unused")
	private void setTrainingProgress(float value) {
		setProgress(0, value);
	}
	/**
	 * Sets the training progress to a particular value
	 * 
	 * @param value
	 *            the value to set
	 */
	private void setTrainingProgress(double value) {
		setProgress(0, value);
	}
	/**
	 * Sets the training progress to a particular value inside a range
	 * 
	 * @param value
	 *            the value to set
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the ending value
	 * @see _Float#setValue(float, float, float)
	 */
	@SuppressWarnings("unused")
	private void setTrainingProgress(float value, float begin, float end) {
		setProgress(0, value, begin, end);
	}
	/**
	 * Sets the training progress to a particular value inside a range
	 * 
	 * @param value
	 *            the value to set
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the ending value
	 * @see _Float#setValue(double, double, double)
	 */
	@SuppressWarnings("unused")
	private void setTrainingProgress(double value, double begin, double end) {
		setProgress(0, value, begin, end);
	}

}