package _157239n;

import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PVector;
/**
 * Represents a matrix.
 * 
 * @author www.157239n.com
 * @version 1.0
 * */
public class Matrix implements Serializable {
	private static final long serialVersionUID = 157230L;
	public static final long SIGNATURE=serialVersionUID;
	public static final long MAGIC_NUMBER=SIGNATURE;
	private PApplet p = new PApplet();
	public final static int family = 1;
	public final static String[] famDesc = { "Identity", "sigmoid", "tanh", "arctan", "softsign",
			"inverse square root unit (ISRU)", "Rectified linear unit (ReLU)", "Leaky ReLU",
			"Exponential linear unit (ELU)", "Inverse square root linear unit (ISRLU)", "SoftPlus", "Bent identity",
			"Soft Exponential", "Sinusoid", "Sinc", "Gaussian" };
	public final static int IDENTITY = 0, SIGMOID = 1, TANH = 2, ARCTAN = 3, SOFTSIGN = 4, ISRU = 5, RELU = 6,
			LRELU = 7, ELU = 8, ISRLU = 9, SOFTPLUS = 10, BENT_IDENTITY = 11, SOFT_EXP = 12, SINUSOID = 13, SINC = 14,
			GAUSSIAN = 15;
	/**
	 * The number of rows of the matrix.
	 */
	public int m;
	/**
	 * The number of columns of the matrix.
	 * */
	public int n;
	/**
	 * The matrix's 2d floating point array
	 */
	private float[][] ea;/* event args */

	/**
	 * Constructor with no arguments. <br>
	 * <br>
	 * This should be used only to give the object a location in memory for linking
	 * applications.<br>
	 */
	Matrix() {
		init(Env._float(Env._float(0, 0), Env._float(0, 0)));
	}

	/**
	 * Constructor with only a 2d floating point array. <br>
	 * <br>
	 * Use constructor {@link Matrix#Matrix(float[][], int, int)} if one of the
	 * array's length is 0.<br>
	 * 
	 * @param ea
	 *            the 2d floating point array
	 */
	Matrix(float[][] ea) {
		init(ea);
	}

	/**
	 * Constructor with a 2d floating point array and dimensions
	 * 
	 * @param ea
	 *            the 2d floating point array
	 * @param m
	 *            the number of rows the matrix has
	 * @param n
	 *            the number of columns the matrix has
	 */
	Matrix(float[][] ea, int m, int n) {
		init(ea, m, n);
	}

	/**
	 * Initializes the matrix with a 2d floating point array. <br>
	 * <br>
	 * This matches exactly the constructor Matrix(float[][] ea). This is to provide
	 * methods inside this class to initialize the entire matrix
	 * 
	 * @param ea
	 *            a 2 dimensional floating point array
	 */
	public void init(float[][] ea) {
		this.m = ea.length;
		this.n = ea[0].length;
		this.ea = ea;
	}

	/**
	 * Initializes the matrix with a 2d floating point array and specified
	 * dimensions This differs from __init__(float[][] ea) as the 2d floating point
	 * array can be of zero length initially
	 * 
	 * @param ea
	 *            the 2d floating point array
	 * @param m
	 *            number of rows of the matrix
	 * @param n
	 *            number of columns of the matrix
	 */
	public void init(float[][] ea, int m, int n) {
		this.m = m;
		this.n = n;
		this.ea = ea;
	}

	/**
	 * Gets the transpose of the matrix
	 * 
	 * @return the transposed matrix
	 */
	public Matrix T() {
		float[][] es = new float[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				es[j][i] = ea[i][j];
			}
		}
		Matrix M = new Matrix(es);
		return M;
	}

	/**
	 * Print the matrix out into the console
	 */
	public void print() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				PApplet.print(Env.format(ea[i][j]), "  ");
			}
			PApplet.println();
		}
	}

	/**
	 * Print the matrix out into a file
	 * 
	 * @param file
	 *            the file name of the file you want to write on
	 */
	public void print(String file) {
		String[] st = new String[m + 1];
		for (int i = 0; i < m; i++) {
			st[i] = "";
			for (int j = 0; j < n; j++) {
				st[i] += Env.format(ea[i][j]) + " ";
			}
		}
		st[m] = "***";
		p.saveStrings(file, st);
	}

	/**
	 * Calculates the sum of all the elements in the matrix.
	 * 
	 * @return the sum
	 */
	public float sum() {
		float s = (float) 0.0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				s += ea[i][j];
			}
		}
		return s;
	}

	/**
	 * Calculates the sum of the absolute value of all the elements in the matrix.
	 * 
	 * @return the absolute-d sum
	 */
	public float absSum() {
		float s = (float) 0.0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				s += Math.abs(ea[i][j]);
			}
		}
		return s;
	}

	/**
	 * Multiplies this matrix with a constant and return itself.
	 * 
	 * @param a
	 *            the constant
	 * @return the multiplied matrix
	 */
	public Matrix mul(float a) {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				ea[i][j] *= a;
			}
		}
		return this;
	}

	/**
	 * Stacks another matrix onto the existing one.
	 * 
	 * @param a
	 *            the matrix to stack upon the original one
	 * @throws RuntimeException
	 *             when the old and new matrix's width is different from each other
	 */
	public void stack(Matrix a) throws RuntimeException {
		if (n != a.n) {
			throw new RuntimeException(
					"mismatch dimension while stacking matrices. Function stackMatrix(), tab Matlab. Additional info: ("
							+ PApplet.str(m) + ", " + PApplet.str(n) + "), (" + PApplet.str(a.m) + ", "
							+ PApplet.str(a.n) + ")");
		}
		int commonWidth = n;
		float[][] ans = new float[m + a.m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < commonWidth; j++) {
				ans[i][j] = ea[i][j];
			}
		}
		for (int i = 0; i < a.m; i++) {
			for (int j = 0; j < commonWidth; j++) {
				ans[i + m][j] = a.ea[i][j];
			}
		}
		m += a.m;
		ea = ans;
	}

	/**
	 * Clones the existing matrix
	 * 
	 * @return the cloned Matrix
	 */
	public Matrix clone() {
		return new Matrix(clone(ea));
	}

	/**
	 * Slashes the matrix using relative location and length. <br>
	 * <br>
	 * Cuts out a portion of the matrix with specified location and length.<br>
	 * Does not affect the current matrix<br>
	 * 
	 * @param location
	 *            location of the output matrix ranging from 0 to 1
	 * @param length
	 *            length of the output matrix ranging from 0 to 1 (the original
	 *            matrix has a length of 1)
	 * @return the slashed matrix
	 * @throws RuntimeException
	 *             when either variable is outside the range of [0, 1]
	 */
	public Matrix slash(float location, float length) throws RuntimeException {
		return new Matrix(slash(this.ea, location, length));
	}

	/**
	 * Slashes the matrix using absolute location and length. <br>
	 * <br>
	 * Cuts out a portion of the matrix with specified start row and length<br>
	 * Does not affect the current matrix<br>
	 * 
	 * @param start
	 *            start row of the output matrix
	 * @param length
	 *            length of the output matrix
	 * @return the slashed matrix
	 * @throws RuntimeException
	 *             whenever the location of the output matrix lie outside the
	 *             original matrix
	 */
	public Matrix slash(int start, int length) throws RuntimeException {
		if (start < 0 || start + length > m || length <= 0) {
			throw new RuntimeException(
					"can't slash this array. Function Matrix slash(int start, int length), class Matrix. Additional info: start: "
							+ PApplet.str(start) + ", len: " + PApplet.str(length) + ", actual lengh: "
							+ PApplet.str(m));
		}
		return new Matrix(slash(ea, start, length));
	}

	/**
	 * Gets the value at a particular index.
	 * 
	 * @param index
	 *            the index of the value
	 * @return the value at that particular index
	 */
	public float getValue(int index) {
		return ea[index / n][index % n];
	}

	/**
	 * Gets the value at a particular row and column.
	 * 
	 * @param row
	 *            the row count
	 * @param column
	 *            the column count
	 * @return the value at that particular row and column
	 */
	public float getValue(int row, int column) {
		return ea[row][column];
	}

	/**
	 * Sets the value at a particular index.
	 * 
	 * @param index
	 *            the index of the value
	 * @param value
	 *            the value to set
	 */
	public void setValue(int index, float value) {
		ea[index / n][index % n] = value;
	}

	/**
	 * Sets the value at a particular row and column.
	 * 
	 * @param row
	 *            the row count
	 * @param column
	 *            the column count
	 * @param value
	 *            the value to set
	 */
	public void setValue(int row, int column, double value) {
		ea[row][column] = (float) value;
	}

	/**
	 * Sets the value at a particular index.
	 * 
	 * @param index
	 *            the index of the value
	 * @param value
	 *            the value to set
	 */
	public void setValue(int index, double value) {
		ea[index / n][index % n] = (float) value;
	}

	/**
	 * Sets the value at a particular row and column.
	 * 
	 * @param row
	 *            the row count
	 * @param column
	 *            the column count
	 * @param value
	 *            the value to set
	 */
	public void setValue(int row, int column, float value) {
		ea[row][column] = value;
	}

	/**
	 * Sets an entire row of this matrix to 1 particular value.
	 * 
	 * @param index
	 *            the row count to set
	 * @param value
	 *            the value to set
	 * @return the set matrix
	 */
	public Matrix setRow(int index, float value) throws RuntimeException {
		if (m <= index) {
			throw new RuntimeException("can't set a row too far away");
		}
		for (int i = 0; i < n; i++) {
			ea[index][i] = (float) (value * 1.0);
		}
		return this;
	}

	/**
	 * Sets an entire column of this matrix to 1 particular value.
	 * 
	 * @param index
	 *            the column count to set
	 * @param value
	 *            the value to set
	 * @return the set matrix
	 */
	public Matrix setColumn(int index, float value) throws RuntimeException {
		if (n <= index) {
			throw new RuntimeException("can't set a column too far away");
		}
		for (int i = 0; i < m; i++) {
			ea[i][index] = (float) (value * 1.0);
		}
		return this;
	}

	/**
	 * Gets the entire matrix and outputs as a 2 dimensional floating point value
	 * array.
	 * 
	 * @return the 2d float array
	 */
	public float[][] getArray() {
		return clone(ea);
	}

	/**
	 * Gets a row at a particular index.
	 * 
	 * @param index
	 *            the index of the value
	 * @return the row
	 */
	public float[] getRow(int index) {
		return clone(ea[index]);
	}

	/**
	 * Compresses the matrix into a single column.
	 */
	public void compressColumns() {
		float[][] ans = blankMatrixArray(m, 1);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				ans[i][0] += ea[i][j];
			}
		}
		init(ans);
	}

	/**
	 * Compresses the matrix into a single row.
	 */
	public void compressRows() {
		float[][] ans = blankMatrixArray(1, n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				ans[0][i] += ea[j][i];
			}
		}
		init(ans);
	}

	/**
	 * Slashes a 2d floating point array using relative location and length. <br>
	 * <br>
	 * Cuts out a portion of the array with specified location and length.<br>
	 * 
	 * @param ea
	 *            the 2d floating point array
	 * @param location
	 *            location of the output matrix ranging from 0 to 1
	 * @param length
	 *            length of the output matrix ranging from 0 to 1 (the original
	 *            matrix has a length of 1)
	 * @return the slashed array
	 * @throws RuntimeException
	 *             when either location or length is outside the range of [0, 1]
	 * @throws RuntimeException
	 *             when the slashed array have a length of 0
	 */
	public float[][] slash(float[][] ea, float location, float length) throws RuntimeException {
		if (length < 0 || length > 1 || location < 0 || location > 1) {
			throw new RuntimeException("invalid length and location");
		}
		float realLocation = PApplet.map(location, 0, 1, length / 2, 1 - length / 2);
		PVector range = new PVector(realLocation - length / 2, realLocation + length / 2);
		int beg = (int) (range.x * ea.length);
		int end = (int) (range.y * ea.length);
		float[][] ans = new float[end - beg + 1][n];
		for (int i = beg; i < end; i++) {
			ans[i - beg] = clone(ea[i]);
		}
		if (ans.length == 0) {
			throw new RuntimeException("slashed array is of length 0");
		}
		return ans;
	}

	/**
	 * Slashes a 1d floating point array using relative location and length. <br>
	 * <br>
	 * Cuts out a portion of the array with specified location and length.<br>
	 * 
	 * @param ea
	 *            the 1d floating point array
	 * @param location
	 *            the location of the output array
	 * @param length
	 *            the length of the output array
	 * @return the slashed array
	 */
	public static float[] slash(float[] ea, float location, float length) throws RuntimeException {
		if (length < 0 || length > 1 || location < 0 || location > 1) {
			throw new RuntimeException(
					"invalid len and location. Function: slash(float location, float length), class Matrix.");
		}
		float realLocation = PApplet.map(location, 0, 1, length / 2, 1 - length / 2);
		PVector range = new PVector(realLocation - length / 2, realLocation + length / 2);
		int beg = PApplet.parseInt(range.x * ea.length);
		int end = PApplet.parseInt(range.y * ea.length);
		float[] ans = new float[end - beg];
		for (int i = beg; i < end; i++) {
			ans[i - beg] = ea[i];
		}
		if (ans.length == 0) {
			throw new RuntimeException(
					"slashed array is of length 0. Function: slash(float len, float location), class Matrix");
		}
		return ans;
	}

	/**
	 * Slashes a 2d floating point array using absolute location and length. <br>
	 * <br>
	 * Cuts out a portion of the matrix with specified start row and length.<br>
	 * 
	 * @param ea
	 *            the 2d floating point array
	 * @param start
	 *            start row of the output array
	 * @param length
	 *            length of the output array
	 * @return the slashed array
	 * @throws RuntimeException
	 *             whenever the location of the output array lie outside the
	 *             original array
	 */
	public static float[][] slash(float[][] ea, int start, int length) throws RuntimeException {
		if (start < 0 || start + length > ea.length || length <= 0) {
			throw new RuntimeException("can't slash this array, index out of bounds");
		}
		float[][] ans = new float[length][ea[0].length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = clone(ea[i + start]);
		}
		return ans;
	}

	// TODO: test out the function below
	/**
	 * Slashes a floating point array using absolute location and length. <br>
	 * <br>
	 * Cuts out a portion of the matrix with specified start location and
	 * length.<br>
	 * 
	 * @param ea
	 *            the floating point array
	 * @param start
	 *            start location of the output array
	 * @param length
	 *            length of the output array
	 * @return the slashed array
	 * @throws RuntimeException
	 *             whenever the location of the output array lie outside the
	 *             original array
	 */
	public static float[] slash(float[] ea, int start, int length) throws RuntimeException {
		if (start < 0 || start + length > ea.length || length <= 0) {
			throw new RuntimeException("can't slash this array, index out of bounds");
		}
		float[] ans = new float[length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = ea[i + start];
		}
		return ans;
	}

	/**
	 * Stacks the 2nd matrix to the 1st matrix.
	 * 
	 * @param a
	 *            the first matrix
	 * @param b
	 *            the second matrix
	 * @return the stacked matrix
	 */
	public static Matrix stackMatrix(Matrix a, Matrix b) {
		Matrix ans = a.clone();
		ans.stack(b);
		return ans;
	}

	/**
	 * Creates a blank 2d floating point array filled with 0.0 values.
	 * 
	 * @param row
	 *            the number of rows
	 * @param column
	 *            the number of columns
	 * @return the blank 2d floating point array
	 */
	public static float[][] blankMatrixArray(int row, int column) {
		float[][] ans = new float[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				ans[i][j] = (float) 0.0;
			}
		}
		return ans;
	}

	/**
	 * Creates a blank matrix filled with 0.0 values
	 * 
	 * @param row
	 *            the number of rows
	 * @param column
	 *            the number of columns
	 * @return the blank matrix
	 */
	public static Matrix blankMatrix(int row, int column) {
		return new Matrix(blankMatrixArray(row, column));
	}

	/**
	 * Clones a 2d floating point array.
	 * 
	 * @param array
	 *            the array to clone
	 * @return the cloned array
	 */
	public static float[][] clone(float[][] array) {
		float[][] ans = new float[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			ans[i] = clone(array[i]);
		}
		return ans;
	}

	/**
	 * Clones a 1d floating point array
	 * 
	 * @param array
	 *            the array to clone
	 * @return the cloned array
	 */
	public static float[] clone(float[] array) {
		float ans[] = new float[array.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = array[i];
		}
		return ans;
	}

	/**
	 * Clones a matrix.
	 * 
	 * @param matrix
	 *            the matrix to clone
	 * @return the cloned matrix
	 */
	public static Matrix clone(Matrix matrix) {
		return matrix.clone();
	}

	/**
	 * Clones an array of matrix.
	 * 
	 * @param matricies
	 *            the matrix array to clone
	 * @return the cloned array of matrix
	 */
	public static Matrix[] clone(Matrix[] matricies) {
		Matrix[] ans = new Matrix[matricies.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = matricies[i].clone();
		}
		return ans;
	}

	/**
	 * Sets an entire row of a matrix to 1 particular value.
	 * 
	 * @param matrix
	 *            the matrix to set
	 * @param index
	 *            the row count to set
	 * @param value
	 *            the value to set
	 * @return the cloned, set matrix
	 */
	public static Matrix setRow(Matrix matrix, int index, float value) throws RuntimeException {
		return matrix.clone().setRow(index, value);
	}

	/**
	 * Sets an entire column of a matrix to 1 particular value.
	 * 
	 * @param matrix
	 *            the matrix to set
	 * @param index
	 *            the column count to set
	 * @param value
	 *            the value to set
	 * @return the cloned, set matrix
	 */
	public static Matrix setColumn(Matrix matrix, int index, float value) throws RuntimeException {
		return matrix.clone().setColumn(index, value);
	}

	/**
	 * Calculates the dot product of two matrix.
	 * 
	 * @param M
	 *            the first matrix
	 * @param N
	 *            the second matrix
	 * @return the dot product of the two matrix
	 * @throws RuntimeException
	 *             when the dimensions don't match up
	 */
	public static Matrix dotp(Matrix M, Matrix N) throws RuntimeException {
		if (M.n != N.m) {
			throw new RuntimeException("mismatch dimension while doing inner product (dot product). Additional info: ("
					+ M.m + ", " + M.n + "), (" + N.m + ", " + N.n + ")");
		}
		float[][] ea = new float[M.m][N.n];
		for (int i = 0; i < M.m; i++) {
			for (int j = 0; j < N.n; j++) {
				float s = 0;
				for (int k = 0; k < M.n; k++) {
					s += M.ea[i][k] * N.ea[k][j];
				}
				ea[i][j] = s;
			}
		}
		Matrix P = new Matrix(ea);
		return P;
	}

	/**
	 * Multiplies a matrix with a constant.
	 * 
	 * @param M
	 *            the matrix to be multiplied
	 * @param value
	 *            the value to multiply the matrix to
	 * @return the cloned multiplied matrix
	 */
	public static Matrix mul(Matrix M, float value) {
		return M.clone().mul(value);
	}

	/**
	 * Performs an operation on the 2 matrix passed in.
	 * 
	 * @param index
	 *            the type of operation to perform
	 * @param M
	 *            the first matrix
	 * @param N
	 *            the second matrix
	 * @return the resulting matrix
	 * @see Matrix#matxF(int, float, float) for the list of possible operations
	 */
	public static Matrix fc(int index, Matrix M, Matrix N) {
		float[][] ea = new float[M.m][M.n];
		for (int i = 0; i < M.m; i++) {
			for (int j = 0; j < M.n; j++) {
				ea[i][j] = matxF(index, M.ea[i][j], N.ea[i][j]);
			}
		}
		Matrix P = new Matrix(ea);
		return P;
	}

	/**
	 * Performs an operation on 1 matrix passed in. <br>
	 * <br>
	 * All other information passed in must go through the data variable.<br>
	 * This variable will usually contain 2 integers: the first one for the type of
	 * operation and if the operation is 0 (activation function) or 1 (derivative of
	 * activation function) then the second integer specifies the activation
	 * family.<br>
	 * 
	 * @param data
	 *            the data
	 * @param M
	 *            the matrix to be operated
	 * @return the resulting matrix
	 * @see Matrix#matxF(InfoPacket, float) List of possible operations
	 */
	public static Matrix fs(InfoPacket data, Matrix M) {
		float[][] ea = new float[M.m][M.n];
		for (int i = 0; i < M.m; i++) {
			for (int j = 0; j < M.n; j++) {
				ea[i][j] = matxF(data, M.ea[i][j]);
			}
		}
		Matrix P = new Matrix(ea);
		return P;
	}

	/**
	 * Performs an operation on 1 matrix passed in.
	 * 
	 * This functions is like {@link Matrix#fs(InfoPacket, Matrix)} but is easier to
	 * use.
	 * 
	 * @param index
	 *            the type of operation to perform on the matrix
	 * @param M
	 *            the matrix to be operated
	 * @return the resulting matrix
	 */
	public static Matrix fs(int index, Matrix M) {
		if (index == 0 || index == 1) {
			return fs(new InfoPacket().add(index).add(family), M);
		} else {
			return fs(new InfoPacket().add(index), M);
		}
	}

	/**
	 * Performs an operation on 1 floating point value passed in. <br>
	 * <br>
	 * All other information passed in must go through the data variable.<br>
	 * This variable will specify what operations to do to the floating point value.
	 * It will always contain an integer describing the operations:<br>
	 * 0 - use an activation function<br>
	 * 1 - use the derivative of an activation function<br>
	 * 2 - negates the value<br>
	 * 3 (deprecated) - generate random values for the matrix<br>
	 * 4 - returns the sign of the value<br>
	 * Other - returns the value itself<br>
	 * <br>
	 * For operations 0 and 1, there will be a family of activation family
	 * associated and this will be specified in the second integer inside the "data"
	 * variable. See {@link Matrix#famDesc} for a list of possible activation
	 * families<br>
	 * 
	 * @param data
	 *            the data packet
	 * @param x
	 *            the value to be operated
	 * @return the transformed value
	 */
	public static float matxF(InfoPacket data, float x) {
		int index = data.getInt(0);
		int family = data.getInt(1);
		if (family == 0) {
			if (index == 0) {
				return x;
			}
			if (index == 1) {
				return 1;
			} // identity
		} else if (family == 1) {
			if (index == 0) {
				return 1 / (1 + PApplet.exp(-x));
			}
			if (index == 1) {
				float e = PApplet.exp(x);
				return e / PApplet.sq(e + 1);
			} // sigmoid
		} else if (family == 2) {
			if (index == 0) {
				return (float) Math.tanh(x);
			}
			if (index == 1) {
				return (1 - PApplet.sq((float) Math.tanh(x)));
			} // tanh
		} else if (family == 3) {
			if (index == 0) {
				return PApplet.atan(x);
			}
			if (index == 1) {
				return 1 / (PApplet.sq(x) + 1);
			} // arctan
		} else if (family == 4) {
			if (index == 0) {
				return x / (1 + Math.abs(x));
			}
			if (index == 1) {
				return 1 / PApplet.sq(1 + Math.abs(x));
			} // soft sign
		} else if (family == 5) {
			if (index == 0) {
				return (float) (x / Math.sqrt(1 + data.getFloat(0) * PApplet.sq(x)));
			}
			if (index == 1) {
				return (float) Math.pow(1 + data.getFloat(0) * PApplet.sq(x), -3.0 / 2);
			} // ISRU
		} else if (family == 6) {
			if (index == 0) {
				if (x >= 0) {
					return x;
				} else {
					return 0;
				}
			}
			if (index == 1) {
				if (x >= 0) {
					return 1;
				} else {
					return 0;
				}
			} // ReLU
		} else if (family == 7) {
			if (index == 0) {
				if (x >= 0) {
					return x;
				} else {
					return (float) (0.01 * x);
				}
			}
			if (index == 1) {
				if (x >= 0) {
					return 1;
				} else {
					return (float) 0.01;
				}
			} // Leaky ReLU
		} else if (family == 8) {
			if (index == 0) {
				if (x >= 0) {
					return x;
				} else {
					return data.getFloat(0) * (PApplet.exp(x) - 1);
				}
			}
			if (index == 1) {
				if (x >= 0) {
					return 1;
				} else {
					return data.getFloat(0) * (PApplet.exp(x) - 1) + data.getFloat(0);
				}
			} // ELU
		} else if (family == 9) {
			if (index == 0) {
				if (x >= 0) {
					return x;
				} else {
					return (float) (x / Math.sqrt(1 + data.getFloat(0) * PApplet.sq(x)));
				}
			}
			if (index == 1) {
				if (x >= 0) {
					return 1;
				} else {
					return (float) Math.pow(1 + data.getFloat(0) * PApplet.sq(x), -3.0 / 2);
				}
			} // ISRLU
		} else if (family == 10) {
			if (index == 0) {
				return (float) Math.log(1 + PApplet.exp(x));
			}
			if (index == 1) {
				return 1 / (1 + PApplet.exp(-x));
			} // SoftPlus
		} else if (family == 11) {
			if (index == 0) {
				return (float) ((Math.sqrt(PApplet.sq(x) + 1) - 1) / 2 + x);
			}
			if (index == 1) {
				return (float) (0.5 * x / Math.sqrt(PApplet.sq(x) + 1) + 1);
			} // bent identity
		} else if (family == 12) {
			if (index == 0) {
				float a = data.getFloat(0);
				if (a > 0) {
					return (PApplet.exp(a * x) - 1) / a + a;
				} else if (a == 0) {
					return x;
				} else {
					return (float) (-Math.log(1 - a * (x + a)) / a);
				}
			}
			if (index == 1) {
				float a = data.getFloat(0);
				if (a >= 0) {
					return (float) Math.exp(a * x);
				} else {
					return 1 / (1 - a * (a + x));
				}
			} // TODO: soft exponential
		} else if (family == 13) {
			if (index == 0) {
				return (float) Math.sin(x);
			}
			if (index == 1) {
				return (float) Math.cos(x);
			} // sinusoid
		} else if (family == 14) {
			if (index == 0) {
				if (Math.abs(x) < 0.001) {
					return 1;
				} else {
					return (float) Math.sin(x) / x;
				}
			}
			if (index == 1) {
				if (Math.abs(x) < 0.001) {
					return 0;
				} else {
					return (float) ((float) Math.cos(x) / x - Math.sin(x) / PApplet.sq(x));
				}
			} // sinc
		} else if (family == 15) {
			if (index == 0) {
				return (float) Math.exp(-PApplet.sq(x));
			}
			if (index == 1) {
				return (float) (-2.0 * x * Math.exp(-PApplet.sq(x)));
			} // gaussian
		}
		// other functions
		if (index == 2) {
			return -x;
		} // negation
		if (index == 3) {
			throw new RuntimeException("the randomizing function is deprecated. Use Matrix#randomMatrix instead.");
		} // index==3 random values, deprecated
		if (index == 4) {
			return Env.sign(x);
		} // sign with values {-1, 0, 1}
		else {
			return x;
		} // just to satisfy js
	}

	/**
	 * Performs an operation on 1 floating point value passed in. <br>
	 * <br>
	 * This function is like {@link Matrix#matxF(InfoPacket, float)} but is easier
	 * to use.<br>
	 * 
	 * @param index
	 *            the type of operation to perform on the matrix
	 * @param x
	 *            the value to be operated
	 * @return the transformed value
	 */
	public static float matxF(int index, float x) {
		if (index == 0 || index == 1) {
			return matxF(new InfoPacket().add(index).add(family), x);
		} else {
			return matxF(new InfoPacket().add(index), x);
		}
	}

	/**
	 * Performs an operation on 2 floating point value passed in.
	 * 
	 * @param index
	 *            the type of operation to perform
	 * @param x
	 *            the first value
	 * @param y
	 *            the second value
	 * @return the transformed value
	 */
	public static float matxF(int index, float x, float y) {
		if (index == 0) {
			return x + y;
		}
		if (index == 1) {
			return x - y;
		}
		if (index == 2) {
			return x * y;
		} else {
			return x;
		}
	}

	/**
	 * Returns the boundaries of a particular activation function family. <br>
	 * <br>
	 * For example, sigmoid range/boundary is (0, 1) while hyperbolic tangent
	 * range/boundary is (-1, 1). <br>
	 * 
	 * @param family
	 *            the family number
	 * @return the range/boundaries of the specified activation function family
	 */
	public static float[] familyBounds(int family) {
		if (family == Matrix.SIGMOID || family == Matrix.RELU || family == Matrix.SOFTPLUS
				|| family == Matrix.GAUSSIAN) {
			return Env._float(0.0, 1.0);
		} else if (family == Matrix.IDENTITY || family == Matrix.TANH || family == Matrix.ARCTAN
				|| family == Matrix.SOFTSIGN || family == Matrix.ISRU || family == Matrix.LRELU || family == Matrix.ELU
				|| family == Matrix.ISRLU || family == Matrix.BENT_IDENTITY || family == Matrix.SOFT_EXP
				|| family == Matrix.SINUSOID || family == Matrix.SINC) {
			return Env._float(-1.0, 1.0);
		} else {
			throw new RuntimeException(
					"don't recognize that activation family while evaluationg the cross-entropy cost function.");
		}
	}

	/**
	 * Adds 2 matrix together and returns the resulting matrix.
	 * 
	 * @param M
	 *            the first matrix
	 * @param N
	 *            the second matrix
	 * @return the resulting matrix
	 * @throws RuntimeException
	 *             whenever the dimensions do not match up
	 */
	public static Matrix add(Matrix M, Matrix N) throws RuntimeException {
		try {
			return fc(0, M, N);
		} catch (RuntimeException e) {
			throw new RuntimeException(
					"mismatch dimention while adding two matricies. Function Matrix add(Matrix M, Matrix N), tab MatLab. Additional info: ("
							+ PApplet.str(M.m) + ", " + PApplet.str(M.n) + "), (" + PApplet.str(N.m) + ", "
							+ PApplet.str(N.n));
		}
	}

	/**
	 * Subtracts 2 matrix and returns the resulting matrix.
	 * 
	 * @param M
	 *            the first matrix
	 * @param N
	 *            the second matrix
	 * @return the resulting matrix
	 * @throws RuntimeException
	 *             whenever the dimensions do not match up
	 */
	public static Matrix sub(Matrix M, Matrix N) throws RuntimeException {
		try {
			return fc(1, M, N);
		} catch (RuntimeException e) {
			throw new RuntimeException(
					"mismatch dimention while subtracting two matricies. Function Matrix sub(Matrix M, Matrix N), tab MatLab. Additional info: ("
							+ PApplet.str(M.m) + ", " + PApplet.str(M.n) + "), (" + PApplet.str(N.m) + ", "
							+ PApplet.str(N.n));
		}
	}

	/**
	 * Multiply 2 matrix pair-wise and returns the resulting matrix.
	 * 
	 * @param M
	 *            the first matrix
	 * @param N
	 *            the second matrix
	 * @return the resulting matrix
	 * @throws RuntimeException
	 *             whenever the dimensions do not match up
	 */
	public static Matrix mul(Matrix M, Matrix N) throws RuntimeException {
		try {
			return fc(2, M, N);
		} catch (RuntimeException e) {
			throw new RuntimeException(
					"mismatch dimention while multiplying two matricies. Function Matrix sub(Matrix M, Matrix N), tab MatLab. Additional info: ("
							+ PApplet.str(M.m) + ", " + PApplet.str(M.n) + "), (" + PApplet.str(N.m) + ", "
							+ PApplet.str(N.n));
		}
	}

	/**
	 * Use the activation function on a single matrix.
	 * 
	 * The family will be the default family. See {@link Matrix#family} variable for
	 * the default value.
	 * 
	 * @param M
	 *            the matrix to pass to the activation function
	 * @return the transformed matrix
	 */
	public static Matrix acti(Matrix M) {
		return fs(0, M);
	}

	/**
	 * Use the activation function on a single matrix.
	 * 
	 * @param M
	 *            the matrix to pass to the activation function
	 * @param family
	 *            the activation function family. See possible options at
	 *            {@link Matrix#famDesc}.
	 * @return the transformed matrix
	 */
	public static Matrix acti(Matrix M, int family) {
		return fs(new InfoPacket().add(0).add(family), M);
	}

	/**
	 * Use the activation function on a single value.
	 * 
	 * The family will be the default family. See {@link Matrix#family} variable for
	 * the default value.
	 * 
	 * @param value
	 *            the value to pass to the activation function
	 * @return the transformed value
	 */
	public static float acti(float value) {
		return matxF(new InfoPacket().add(0).add(family), value);
	}

	/**
	 * Use the activation function on a single value.
	 * 
	 * @param value
	 *            the value to pass to the activation function
	 * @param family
	 *            the activation function family. See possible options at
	 *            {@link Matrix#famDesc}.
	 * @return the transformed value
	 */
	public static float acti(float value, int family) {
		return matxF(new InfoPacket().add(0).add(family), value);
	}

	/**
	 * Use the activation function derivative on a single matrix.
	 * 
	 * The family will be the default family. See {@link Matrix#family} variable for
	 * the default value.
	 * 
	 * @param M
	 *            the matrix to pass to the activation function derivative
	 * @return the transformed matrix
	 */
	public static Matrix actiDe(Matrix M) {
		return fs(1, M);
	}

	/**
	 * Use the activation function derivative on a single matrix.
	 * 
	 * @param M
	 *            the matrix to pass to the activation function derivative
	 * @param family
	 *            the activation function family. See possible options at
	 *            {@link Matrix#famDesc}.
	 * @return the transformed matrix
	 */
	public static Matrix actiDe(Matrix M, int family) {
		return fs(new InfoPacket().add(1).add(family), M);
	}

	/**
	 * Use the activation function derivative on a single value.
	 * 
	 * The family will be the default family. See {@link Matrix#family} variable for
	 * the default value.
	 * 
	 * @param value
	 *            the value to pass to the activation function derivative
	 * @return the transformed value
	 */
	public static float actiDe(float value) {
		return matxF(new InfoPacket().add(1).add(family), value);
	}

	/**
	 * Use the activation function derivative on a single value.
	 * 
	 * @param value
	 *            the value to pass to the activation function derivative
	 * @param family
	 *            the activation function family. See possible options at
	 *            {@link Matrix#famDesc}.
	 * @return the transformed value
	 */
	public static float actiDe(float value, int family) {
		return matxF(new InfoPacket().add(1).add(family), value);
	}

	/**
	 * Negates all values inside the matrix.
	 * 
	 * @param M
	 *            the matrix to negate
	 * @return the negated matrix
	 */
	public static Matrix minus(Matrix M) {
		return fs(2, M);
	}

	/**
	 * Negates all values inside the matrix and plus one to all values
	 * 
	 * @param M
	 *            the matrix to operate
	 * @return the transformed matrix
	 */
	public static Matrix oneMinus(Matrix M) {
		return fs(4, M);
	}

	/**
	 * Replaces every value in the matrix with the sign of that value.
	 * 
	 * If the absolute value of the matrix's element is less than
	 * {@link Env#epsilon} then it will return 0.
	 * 
	 * @param matrix
	 *            the matrix to be transformed
	 * @return the transformed matrix
	 */
	public static Matrix sign(Matrix matrix) {
		return fs(4, matrix);
	}

	/**
	 * Generates a matrix randomized according to the Gaussian curve.
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @param expectedValue
	 *            the mean (mu) of the Gaussian curve
	 * @param deviation
	 *            the deviation (sigma) of the Gaussian curve
	 * @return the resulting randomized matrix
	 */
	public static Matrix gaussianMatrix(int row, int column, float expectedValue, float deviation) {
		float[][] ans = new float[row][column];
		float maxValue = Env.gaussian(0, expectedValue, deviation);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				ans[i][j] = Env.gaussianReverse((float) Math.random() * maxValue, expectedValue, deviation)
						* Env.sign((float) Math.random() * (1 - (-1)) + (-1));
			}
		}
		return new Matrix(ans);
	}

	/**
	 * Generates a matrix randomized according to the Gaussian curve.
	 * 
	 * Similar to {@link Matrix#gaussianMatrix(int, int, float, float)} but with a
	 * default mean of 0.
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @param deviation
	 *            the deviation (sigma) of the Gaussian curve
	 * @return the resulting randomized matrix
	 */
	public static Matrix gaussianMatrix(int row, int column, float deviation) {
		return gaussianMatrix(row, column, 0, deviation);
	}

	/**
	 * Generates a matrix randomized according to the Gaussian curve.
	 * 
	 * Similar to {@link Matrix#gaussianMatrix(int, int, float)} but with a default
	 * deviation of 1
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @return the resulting randomized matrix
	 */
	public static Matrix gaussianMatrix(int row, int column) {
		return gaussianMatrix(row, column, 1);
	}

	/**
	 * Generates a matrix randomized evenly between some boundaries.
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @param lowerBound
	 *            lower boundary of the randomized values
	 * @param upperBound
	 *            upper boundary of the randomized values
	 * @return the randomized matrix
	 */
	public static Matrix randomMatrix(int row, int column, float lowerBound, float upperBound) {
		float[][] ans = new float[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				ans[i][j] = (float) (Math.random() * (upperBound - lowerBound) + lowerBound);
			}
		}
		return new Matrix(ans);
	}

	/**
	 * Generates a matrix randomized evenly between some boundaries.
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @param bound
	 *            the upper and lower boundary of the randomized values. In other
	 *            words, the value range is (-bound, bound).
	 * @return the randomized matrix
	 */
	public static Matrix randomMatrix(int row, int column, float bound) {
		return randomMatrix(row, column, -Math.abs(bound), Math.abs(bound));
	}

	/**
	 * Generates a matrix randomized evenly in range (-1, 1).
	 * 
	 * @param row
	 *            the number of rows the resulting matrix has
	 * @param column
	 *            the number of columns the resulting matrix has
	 * @return the randomized matrix
	 */
	public static Matrix randomMatrix(int row, int column) {
		return randomMatrix(row, column, 1);
	}

	/**
	 * Introduces Gaussian noise into the matrix.
	 * 
	 * @param matrix
	 *            the matrix to tweak
	 * @param deviation
	 *            the deviation of the tweaks
	 * @return the tweaked matrix
	 */
	public static Matrix tweak(Matrix matrix, float deviation) {
		return add(matrix, gaussianMatrix(matrix.m, matrix.n, (float) 0.0, deviation));
	}

	/**
	 * Introduces Gaussian noise with a deviation of 1 into the matrix.
	 * 
	 * @param matrix
	 *            the matrix to tweak
	 * @return the tweaked matrix
	 */
	public static Matrix tweak(Matrix matrix) {
		return tweak(matrix, 1);
	}

	/**
	 * Introduces Gaussian noise into an array of matrix.
	 * 
	 * @param matricies
	 *            the array of matrix to tweak
	 * @param deviation
	 *            the deviation of the tweaks
	 * @return the tweaked array of matrix
	 */
	public static Matrix[] tweak(Matrix[] matricies, float deviation) {
		Matrix[] ans = clone(matricies);
		for (int i = 0; i < ans.length; i++) {
			ans[i] = tweak(ans[i], deviation);
		}
		return ans;
	}

	/**
	 * Introduces Gaussian noise with a deviation of 1 into the matrix
	 * 
	 * @param matricies
	 *            the array of matrix to tweak
	 * @return the tweaked array of matrix
	 */
	public static Matrix[] tweak(Matrix[] matricies) {
		return tweak(matricies, 1);
	}

	/* evaluation functions, requested by Network class */
	/**
	 * Gets the index of the highest value inside a 1d floating point array.
	 * 
	 * @param Y
	 *            the input floating point array
	 * @return the index with the highest value
	 */
	public static int getHighestIndex(float[] Y) {
		int ans = 0;
		float cur = Y[0];
		for (int i = 0; i < Y.length; i++) {
			if (Y[i] > cur) {
				ans = i;
				cur = Y[i];
			}
		}
		return ans;
	}

	/**
	 * Gets the index of the lowest value inside a 1d floating point array.
	 * 
	 * @param Y
	 *            the input floating point array
	 * @return the index with the highest value
	 */
	public static int getLowestIndex(float[] Y) {
		int ans = 0;
		float cur = Y[0];
		for (int i = 0; i < Y.length; i++) {
			if (Y[i] < cur) {
				ans = i;
				cur = Y[i];
			}
		}
		return ans;
	}

	/**
	 * Gets the confidence of a 1d floating point array. <br>
	 * <br>
	 * For each element a inside the array, a value b=min(abs(a-lowerBound),
	 * abs(a-upperBound)) is calculated.<br>
	 * The confidence is the sum of all values of b<br>
	 * 
	 * @param Y
	 *            the input floating point array
	 * @param lowerBound
	 *            the lower boundary of the activation function
	 * @param upperBound
	 *            the upperBound of the activation function
	 * @return the confidence of the array
	 */
	public static float getConfidence(float[] Y, float lowerBound, float upperBound) {
		float ans = (float) 0.0;
		for (int i = 0; i < Y.length; i++) {
			ans += PApplet.min(Math.abs(Y[i] - lowerBound), Math.abs(Y[i] - upperBound));
		}
		return ans;
	}

	/**
	 * Gets the confidence of a 1d floating point array with a specified activation
	 * function family. <br>
	 * 
	 * @param Y
	 *            the input floating point array
	 * @param family
	 *            the activation function family we are considering.
	 * @return the confidence of the array
	 */
	public static float getConfidence(float[] Y, int family) {
		float[] bounds = familyBounds(family);
		return getConfidence(Y, bounds[0], bounds[1]);
	}

	/**
	 * Gets the confidence of a 1d floating point array of the sigmoid activation
	 * function.
	 * 
	 * @param Y
	 *            the input floating point array
	 * @return the confidence of the array
	 */
	public static float getConfidence(float[] Y) {
		return getConfidence(Y, Matrix.SIGMOID);
	}

	/**
	 * Gets the similarity of the two matrix. <br>
	 * <br>
	 * For each row of the 2 matrix, a value b is recorded which equals 1 if the
	 * highest index of both rows are the same and 0 if otherwise.<br>
	 * The similarity of the two matrix is calculated by averaging all values of
	 * b.<br>
	 * 
	 * @param Y
	 *            the first matrix
	 * @param P
	 *            the second matrix
	 * @param highestIndex
	 *            the mode of the method, whether to get the highest or the lowest
	 *            element
	 * @return the similarity of the two matrix
	 * @see Matrix#getHighestIndex(float[])
	 * @see Matrix#getLowestIndex(float[])
	 */
	public static float calcAccuracy(Matrix Y, Matrix P, boolean highestIndex) {
		if (highestIndex) {
			int numAccurate = 0;
			for (int i = 0; i < Y.m; i++) {
				numAccurate += Env.kron(getHighestIndex(Y.ea[i]) == getHighestIndex(P.ea[i]));
			}
			return (float) (1.0 * numAccurate / Y.m);
		} else {
			int numAccurate = 0;
			for (int i = 0; i < Y.m; i++) {
				numAccurate += Env.kron(getLowestIndex(Y.ea[i]) == getLowestIndex(P.ea[i]));
			}
			return (float) (1.0 * numAccurate / Y.m);
		}
	}

	/**
	 * Gets the similarity of the two matrix. <br>
	 * <br>
	 * For each row of the 2 matrix, a value b is recorded which equals 1 if the
	 * highest index of both rows are the same and 0 if otherwise.<br>
	 * The similarity of the two matrix is calculated by averaging all values of
	 * b.<br>
	 * The default highestValue mode is set to true in the original
	 * {@link Matrix#calcAccuracy(Matrix, Matrix, boolean)} method<br>
	 * 
	 * @param Y
	 *            the first matrix
	 * @param P
	 *            the second matrix
	 * @return the similarity of the two matrix
	 */
	public static float calcAccuracy(Matrix Y, Matrix P) {
		return calcAccuracy(Y, P, true);
	}

	/**
	 * Splits the matrix into individual rows and averages the confidence on all
	 * possible rows.
	 * 
	 * @param Y
	 *            the input matrix
	 * @param family
	 *            the activation family under consideration
	 * @return the average confidence of the matrix
	 */
	public static float calcConfidence(Matrix Y, int family) {
		float avg = (float) 0.0;
		for (int i = 0; i < Y.m; i++) {
			avg += getConfidence(Y.ea[i], family);
		}
		return avg / Y.m;
	}

	/**
	 * Splits the matrix into individual rows and averages the confidence on all
	 * possible rows. <br>
	 * <br>
	 * This method assumes the activation value is sigmoid.<br>
	 * 
	 * @param Y
	 *            the input matrix
	 * @return the average confidence of the matrix
	 */
	public static float calcConfidence(Matrix Y) {
		return calcConfidence(Y, Matrix.SIGMOID);
	}

	/**
	 * Calculates the cost when compress the matrix using the 2nd schema according
	 * to
	 * <a href="http://157239n.com/documentation/file%20types/" target="_blank">this
	 * documentation</a>.
	 * 
	 * @param M
	 *            the matrix under consideration
	 * @return the cost of the matrix
	 */
	public static int matrixCost(Matrix M) {
		int count = 0;
		int cost = 0;
		int dim = M.m * M.n;
		while (count < dim) {
			int i = count + 1;
			float value = M.getValue(count);
			while (i < dim && (i - count) < 255) {
				if (Env.kron(Env.kron(M.getValue(i), value))) {
					i++;
				} else {
					break;
				}
			}
			cost += 5;
			if (i < dim) {
				if (count == i - 1) {
					count++;
				} else {
					count = i;
				}
			} else {
				break;
			}
		}
		return cost;
	}
}
