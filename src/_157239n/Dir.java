package _157239n;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Dir {
	private static PApplet p = new PApplet();

	Dir() {
	}

	/*
	 * main directory tools (3), String[] getSplit(String st, String ch),
	 * initPath(), String trace(String path, String name)
	 */
	/**
	 * Splits a String into many parts using a specified token then returns a String
	 * array containing the first part and all the other parts combine. <br>
	 * <br>
	 * So getSplit("java.io.IOException", ".") will return {"java",
	 * "io.IOException"}<br>
	 * 
	 * @param st
	 *            the initial String
	 * @param ch
	 *            the token to split
	 * @return a String array with length 2 containing the answer
	 */
	public static String[] getSplit(String st, String ch) {
		String[] tmp = PApplet.split(st, ch);
		String[] ans = { tmp[0], "" };
		for (int i = 1; i < tmp.length - 1; i++) {
			ans[1] += tmp[i] + ch;
		}
		ans[1] += tmp[tmp.length - 1];
		return ans;
	}

	/**
	 * Gets the sketch path, change all backward slashes into forward slashes and
	 * outputs the result.
	 * 
	 * @param prog
	 *            the PApplet main sketch
	 * @return the sketch path with forward slashes
	 */
	public static String initPath(PApplet prog) {
		String ans = prog.sketchPath();
		String[] tmpSt = PApplet.split(ans, "\\");
		ans = "";
		for (int i = 0; i < tmpSt.length; i++) {
			ans += tmpSt[i] + "/";
		}
		return ans;
	}

	/**
	 * Gets the closest parent directory that has the specified name. <br>
	 * <br>
	 * If it can't find the directory then it will return the drive path, like
	 * "C:/".<br>
	 * 
	 * @param path
	 *            the directory
	 * @param name
	 *            the parent directory's name we want to find
	 * @return the parent directory with the specified name
	 */
	public static String trace(String path, String name) {
		String[] tmp = PApplet.split(path, "/");
		int w = tmp.length - 1;
		while (!tmp[w].equals(name) && w > 0) {
			w--;
		}
		String ans = "";
		for (int i = 0; i < w + 1; i++) {
			ans += tmp[i] + "/";
		}
		return ans;
	}

	// export and import .synapses files (2)
	/**
	 * Exports multiple matrix into a plain text file with a .synapses extension.
	 * <br>
	 * <br>
	 * For every matrix, the method will write the dimensions for the matrix in 1
	 * line and then start to write out lines of the matrix.<br>
	 * This method should only be used to export .synapses files<br>
	 * 
	 * @param file
	 *            the file name to export into
	 * @param M
	 *            the array of matrix to export
	 */
	public static void exportSynapses(String file, Matrix[] M) {
		int sum = M.length, count = 0;
		for (int i = 0; i < M.length; i++) {
			sum += M[i].m;
		}
		String[] s = new String[sum];
		// calculates String[] length and initialize the variable
		for (int w = 0; w < M.length; w++) {
			s[count] = PApplet.str(M[w].m) + " " + PApplet.str(M[w].n);
			count++;
			// exports the dimension
			for (int i = 0; i < M[w].m; i++) {
				s[count] = "";
				for (int j = 0; j < M[w].n; j++) {
					s[count] += PApplet.str(M[w].getValue(i, j)) + " ";
				}
				count++;
			}
			// exporting the content
		}
		p.saveStrings(file, s);
	}

	/**
	 * Imports multiple matrix into a plain text file with a .synapses extension.
	 * <br>
	 * <br>
	 * This method will keep on importing until an Exception occurs or the file has
	 * ended. <br>
	 * Thus the matricies must be located at the end of the file and should only be
	 * used to open .synapses files. <br>
	 * 
	 * @param file
	 *            the file to import the matricies
	 * @return the imported array of matrix
	 */
	public static Matrix[] importSynapses(String file) {
		// open file
		String[] st = p.loadStrings(file);
		// figures out the number of matrices contained within that file
		int numMatrix = 0, index = 0;
		while (index < st.length) {
			int[] dim = PApplet.parseInt(PApplet.split(st[index], " "));
			index += dim[0] + 1;
			numMatrix++;
		}
		// initializes some variables
		Matrix[] ans = new Matrix[numMatrix];
		index = 0;
		// dimension handling
		for (int w = 0; w < numMatrix; w++) {
			int[] dim = PApplet.parseInt(PApplet.split(st[index], " "));
			index++;
			float[][] tA = new float[dim[0]][dim[1]];
			// importing the content
			for (int i = 0; i < tA.length; i++) {
				st[index] = PApplet.trim(st[index]);
				float[] values = PApplet.parseFloat(PApplet.split(st[index], " "));
				tA[i] = values;
				index++;
			}
			ans[w] = new Matrix(tA);
		}
		return ans;
	}

	// stream writing functions
	/**
	 * Writes a matrix onto a stream. <br>
	 * <br>
	 * Guidelines are available on
	 * <a href="http://157239n.com/documentation/file%20types/#matx">this
	 * documentation</a><br>
	 * 
	 * @param stream
	 *            the stream to write onto
	 * @param M
	 *            the matrix to write onto the stream
	 */
	public static void writeMatrix(FileOutputStream stream, Matrix M) {
		int dim = M.n * M.m, count = 0;
		boolean encoding = Matrix.matrixCost(M) < dim * 4;
		Dir.writeInt(stream, M.m);
		Dir.writeInt(stream, M.n);
		if (encoding) {
			Dir.writeByte(stream, PApplet.parseByte(1));
		} else {
			Dir.writeByte(stream, PApplet.parseByte(0));
		}
		if (encoding) {
			count = 0;
			int i;
			float value;
			while (count < dim) {
				i = count + 1;
				value = M.getValue(count);
				while (i < dim && (i - count) < 255) {
					if (Env.kron(Env.kron(M.getValue(i), value))) {
						i++;
					} else {
						break;
					}
				}
				Dir.writeByte(stream, PApplet.parseByte(i - count));
				Dir.writeFloat(stream, M.getValue(count));
				count = i;
			}
		} else {
			for (int i = 0; i < dim; i++) {
				Dir.writeFloat(stream, M.getValue(i));
			}
		}
	}

	/**
	 * Writes an array of matrix onto a stream. <br>
	 * <br>
	 * Guidelines are available on
	 * <a href="http://157239n.com/documentation/file%20types/#matx">this
	 * documentation</a><br>
	 * 
	 * @param stream
	 *            the stream to write onto
	 * @param M
	 *            the array of matrix to write onto the stream
	 */
	public static void writeMatricies(FileOutputStream stream, Matrix[] M) {
		Dir.writeInt(stream, M.length);
		for (int i = 0; i < M.length; i++) {
			writeMatrix(stream, M[i]);
		}
	}

	/**
	 * Writes an array of matrix onto a stream. <br>
	 * <br>
	 * Guidelines are available on
	 * <a href="http://157239n.com/documentation/file%20types/#matx">this
	 * documentation</a><br>
	 * 
	 * @param stream
	 *            the stream to write onto
	 * @param M
	 *            the array of matrix to write onto the stream
	 */
	public static void writeMatrix(FileOutputStream stream, Matrix[] M) {
		writeMatricies(stream, M);
	}

	/**
	 * Reads a matrix from a stream. <br>
	 * <br>
	 * Guidelines are available on
	 * <a href="http://157239n.com/documentation/file%20types/#matx">this
	 * documentation</a><br>
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the matrix read from the stream
	 */
	public static Matrix readMatrix(FileInputStream stream) {
		int count = 0;
		int m = Dir.readInt(stream), n = Dir.readInt(stream);
		int dim = m * n;
		float[][] ea = new float[m][n];
		Matrix ans = new Matrix(ea, m, n);
		boolean encoding = Env.kron(Dir.readByte(stream));
		if (encoding) {
			do {
				int times = Dir.readByte(stream);
				float value = Dir.readFloat(stream);
				for (int i = count; i < count + times; i++) {
					ans.setValue(i, value);
				}
				count += times;
			} while (count < dim);
		} else {
			for (int i = 0; i < dim; i++) {
				ans.setValue(i, Dir.readFloat(stream));
			}
		}
		return ans;
	}

	/**
	 * Reads an array of matrix from a stream. <br>
	 * <br>
	 * Guidelines are available on
	 * <a href="http://157239n.com/documentation/file%20types/#matx">this
	 * documentation</a><br>
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the array of matrix read from the stream
	 */
	public static Matrix[] readMatricies(FileInputStream stream) {
		int num = Dir.readInt(stream);
		Matrix[] M = new Matrix[num];
		for (int i = 0; i < num; i++) {
			M[i] = readMatrix(stream);
		}
		return M;
	}

	// export and import .synx files (2)
	/**
	 * Exports an array of matrix onto a .synx file. <br>
	 * <br>
	 * Guidelines for the .synx file can be found on
	 * <a href="http://157239n.com/documentation/file%20types/#synx">this
	 * documentation</a><br>
	 * 
	 * @param path
	 *            the directory of the file and the file name itself
	 * @param M
	 *            the array of matrix to export
	 * @throws RuntimeException
	 *             whenever it detected the path don't have a recognizable extension
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing function
	 */
	public static void exportSynx(String path, Matrix[] M) throws RuntimeException {
		String[] tmp = { "" };
		try {
			if (!PApplet.getExtension(path).equals("synx")) {
				throw new RuntimeException("can't write file of type " + PApplet.getExtension(path));
			}
			if (path.charAt(1) != ':') {
				path = Final_Simple_Net.parentAbsPath + path;
			}
			p.saveStrings(path, tmp);
			FileOutputStream file;
			try {
				file = new FileOutputStream(path);
				writeInt(file, 157231);
				writeMatricies(file, M);
				file.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("can't write to stream");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't write to stream");
		}
	}

	/**
	 * Imports an array of matrix from a .synx file. <br>
	 * <br>
	 * Guidelines for the .synx file can be found on
	 * <a href="http://157239n.com/documentation/file%20types/#synx">this
	 * documentation</a>
	 * 
	 * @param path
	 *            the .synx directory and file name to import from
	 * @return an array of matrix received
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream reading function
	 */
	public static Matrix[] importSynx(String path) {
		FileInputStream file;
		Matrix[] M;
		try {
			if (!PApplet.getExtension(path).equals("synx")) {
				throw new RuntimeException("can't write file of type " + PApplet.getExtension(path));
			}
			if (path.charAt(1) != ':') {
				path = Final_Simple_Net.parentAbsPath + path;
			}
			file = new FileInputStream(path);
			int magicNumber = readInt(file);
			if (magicNumber != 157231) {
				throw new RuntimeException(
						"magic number " + PApplet.str(magicNumber) + " not recognized while inputing .synx file");
			}
			M = readMatricies(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't read from stream");
		}
		return M;
	}

	@Deprecated
	public float[][] importDigits(int set, int dimension, String character) {
		String[] st = p
				.loadStrings("tests/" + PApplet.str(set) + "." + PApplet.str(dimension) + "." + character + ".txt");
		float[][] ans = new float[st.length / (dimension * dimension)][dimension * dimension];
		for (int i = 0; i < st.length / (dimension * dimension); i++) {
			for (int j = 0; j < dimension * dimension; j++) {
				ans[i][j] = PApplet.parseFloat(st[i * dimension * dimension + j]);
			}
		}
		return ans;
	}

	@Deprecated
	public float[][] importDigits(int set, int dimension, int number) {
		return importDigits(set, dimension, PApplet.str(number));
	}

	@Deprecated
	public float[] importDigits(int set, int dimension, int number, int position) {
		return importDigits(set, dimension, number)[position];
	}

	@Deprecated
	Matrix[] importTestSet(int[] sets, int dimension, boolean isNumbers, boolean addBias) {
		float[][] tX, tP;
		Matrix X, P;
		tX = Matrix.blankMatrixArray(0, dimension * dimension + Env.kron(addBias));
		X = new Matrix(tX, 0, dimension * dimension + Env.kron(addBias));
		if (isNumbers) {
			tP = Matrix.blankMatrixArray(0, 10);
			P = new Matrix(tP, 0, 10);
		} else {
			tP = Matrix.blankMatrixArray(0, 26);
			P = new Matrix(tP, 0, 26);
		}
		if (isNumbers) {
			for (int w = 0; w < sets.length; w++) {
				for (int i = 0; i < 10; i++) {
					/* 10 numbers */
					Matrix tempX = new Matrix(importDigits(sets[w], dimension, i));
					/* import digit i from sets[w] with dimension dimension */
					Matrix tempP = Matrix.setColumn(Matrix.blankMatrix(tempX.m, 10), i, 1);
					if (addBias) {
						X.stack(addBias(tempX));
					} else {
						X.stack(tempX);
					}
					P.stack(tempP);
				}
			}
		} else {
			for (int w = 0; w < sets.length; w++) {
				for (int i = 0; i < 26; i++) {
					/* 26 letters */
					Matrix tempX = new Matrix(importDigits(sets[w], dimension, PApplet.str(PApplet.parseChar(i + 97))));
					/* import letter i+97 from sets[w] with dimension dimension */
					Matrix tempP = Matrix.setColumn(Matrix.blankMatrix(tempX.m, 26), i, 1);
					if (addBias) {
						X.stack(addBias(tempX));
					} else {
						X.stack(tempX);
					}
					P.stack(tempP);
				}
			}
		}
		Matrix[] ans = new Matrix[2];
		ans[0] = X;
		ans[1] = P;
		return ans;
	}

	@Deprecated
	Matrix[] importTestSet(int[] sets, int dimension, boolean isNumbers) {
		return importTestSet(sets, dimension, isNumbers, true);
	}

	// byte reading and writing (2)
	/**
	 * Reads from stream an integer (32 bit).
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the integer value read
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream reading function
	 */
	@Deprecated
	public int readInt32(FileInputStream stream) throws RuntimeException {
		try {
			return (stream.read() << 24) | (stream.read() << 16) | (stream.read() << 8) | (stream.read());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Reads from stream a byte (8 bit).
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the byte value read
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream reading function
	 */
	public static int readByte(FileInputStream stream) throws RuntimeException {
		try {
			return stream.read();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Writes to stream a byte (8 bit).
	 * 
	 * @param stream
	 *            the stream to write to
	 * @param value
	 *            the value to write
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing
	 *             function.
	 */
	public static void writeByte(FileOutputStream stream, byte value) throws RuntimeException {
		try {
			stream.write(value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't write to stream");
		}
	}

	// stream primitive operations (16)
	// byte[] _byte(<pdt> value) (4)
	/**
	 * Converts a floating point value into an array of bytes.
	 * 
	 * @param value
	 *            the floating point value
	 * @return an array of bytes
	 */
	public static byte[] _byte(float value) {
		return ByteBuffer.allocate(4).putFloat(value).array();
	}

	/**
	 * Converts a double-precision floating point value into an array of bytes.
	 * 
	 * @param value
	 *            the double-precision floating point value
	 * @return an array of bytes
	 */
	public static byte[] _byte(double value) {
		return ByteBuffer.allocate(8).putDouble(value).array();
	}

	/**
	 * Converts an integer (32 bit) value into an array of bytes.
	 * 
	 * @param value
	 *            the integer value
	 * @return an array of bytes
	 */
	public static byte[] _byte(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	/**
	 * Converts a long integer (64 bit) into an array of bytes.
	 * 
	 * @param value
	 *            the long integer value
	 * @return an array of bytes
	 */
	public static byte[] _byte(long value) {
		return ByteBuffer.allocate(8).putLong(value).array();
	}

	// <pdt> _<pdt>(byte[] value) (4)
	/**
	 * Converts an array of bytes into a floating point value.
	 * 
	 * @param value
	 *            an array of bytes
	 * @return an array of bytes
	 */
	public static float _float(byte[] value) {
		return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getFloat();
	}

	/**
	 * Converts an array of bytes into a double-precision floating point value.
	 * 
	 * @param value
	 *            an array of bytes
	 * @return the double-precision floating point value
	 */
	public static double _double(byte[] value) {
		return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getDouble();
	}

	/**
	 * Converts an array of bytes into an integer (32 bit) value.
	 * 
	 * @param value
	 *            an array of bytes
	 * @return the integer value
	 */
	public static int _int(byte[] value) {
		return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getInt();
	}

	/**
	 * Converts an array of bytes into a long integer (64 bit) value.
	 * 
	 * @param value
	 *            an array of bytes
	 * @return the long integer value
	 */
	public static long _long(byte[] value) {
		return ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getLong();
	}

	// write<pdt>(FileOutputStream stream, <pdt> value) throws RuntimeException (4)
	/**
	 * Writes to stream a floating poi1nt value.
	 * 
	 * @param stream
	 *            the stream to write into
	 * @param value
	 *            the value to write
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing function
	 */
	public static void writeFloat(FileOutputStream stream, float value) throws RuntimeException {
		try {
			stream.write(_byte(value));
		} catch (IOException e) {
			throw new RuntimeException("can't write to stream");
		}
	}

	/**
	 * Writes to stream a double-precision floating point value.
	 * 
	 * @param stream
	 *            the stream to write into
	 * @param value
	 *            the value to write
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing function
	 */
	public static void writeDouble(FileOutputStream stream, double value) throws RuntimeException {
		try {
			stream.write(_byte(value));
		} catch (IOException e) {
			throw new RuntimeException("can't write to stream");
		}
	}

	/**
	 * Writes to stream an integer value.
	 * 
	 * @param stream
	 *            the stream to write into
	 * @param value
	 *            the value to write
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing function
	 */
	public static void writeInt(FileOutputStream stream, int value) throws RuntimeException {
		try {
			stream.write(_byte(value));
		} catch (IOException e) {
			throw new RuntimeException("can't write to stream");
		}
	}

	/**
	 * Writes to stream a long integer value.
	 * 
	 * @param stream
	 *            the stream to write into
	 * @param value
	 *            the value to write
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream writing function
	 */
	public static void writeLong(FileOutputStream stream, long value) throws RuntimeException {
		try {
			stream.write(_byte(value));
		} catch (IOException e) {
			throw new RuntimeException("can't write to stream");
		}
	}

	// <pdt> read<pdt>(FileInputStream stream) throws RuntimeException
	/**
	 * Reads from stream a floating point value
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the recorded value
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream readingfunction
	 */
	public static float readFloat(FileInputStream stream) throws RuntimeException {
		try {
			byte[] tmp = new byte[4];
			stream.read(tmp);
			return _float(tmp);
		} catch (IOException e) {
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Reads from stream a double-precision floating point value
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the recorded value
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream readingfunction
	 */
	public static double readDouble(FileInputStream stream) throws RuntimeException {
		try {
			byte[] tmp = new byte[8];
			stream.read(tmp);
			return _double(tmp);
		} catch (IOException e) {
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Reads from stream an integer value
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the recorded value
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream readingfunction
	 */
	public static int readInt(FileInputStream stream) throws RuntimeException {
		try {
			byte[] tmp = new byte[4];
			stream.read(tmp);
			return _int(tmp);
		} catch (IOException e) {
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Reads from stream a long integer value
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the recorded value
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream readingfunction
	 */
	public static long readLong(FileInputStream stream) throws RuntimeException {
		try {
			byte[] tmp = new byte[8];
			stream.read(tmp);
			return _long(tmp);
		} catch (IOException e) {
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Reads from stream a grey-scale 8 bit color square image with specified
	 * dimensions.
	 * 
	 * @param stream
	 *            the stream to read from
	 * @param dimension
	 *            the dimension of the square image
	 * @return the image read
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream reading function
	 */
	public PImage readImage(FileInputStream stream, int dimension) throws RuntimeException {
		PImage ans = p.createImage(dimension, dimension, PApplet.RGB);
		ans.loadPixels();
		try {
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					ans.pixels[i * dimension + j] = Env.color(readByte(stream));
				}
			}
			ans.updatePixels();
			return ans;
		} catch (Exception e) {
			throw new RuntimeException("can't read from stream");
		}
	}

	// add biases (4)
	/**
	 * Adds a column containing 1 to a 2d float array. <br>
	 * <br>
	 * This is to simulate the bias artificially.<br>
	 * 
	 * @param inp
	 *            the 2d float array to add the column into
	 * @return the transformed 2d float array
	 */
	public static float[][] addBias(float[][] inp) {
		return addBias(inp, 1);
	}

	/**
	 * Adds a column containing a specified floating point value to a 2d float
	 * array. <br>
	 * <br>
	 * This is to simulate the bias artificially.<br>
	 * 
	 * @param inp
	 *            the 2d float array to add the column into
	 * @param bias
	 *            the value that will be added into the extra column
	 * @return the transformed 2d float array
	 */
	public static float[][] addBias(float[][] inp, float bias) {
		float[][] ans = new float[inp.length][inp[0].length + 1];
		for (int i = 0; i < inp.length; i++) {
			for (int j = 0; j < inp[0].length; j++) {
				ans[i][j] = inp[i][j];
			}
			ans[i][inp[0].length] = bias;
		}
		return ans;
	}

	/**
	 * Adds a column containing a specified floating point value to a matrix. <br>
	 * <br>
	 * This is to simulate the bias artificially. <br>
	 * 
	 * @param inp
	 *            the matrix to add the column into
	 * @param bias
	 *            the value that will be added into the extra column
	 * @return the transformed matrix
	 */
	public static Matrix addBias(Matrix inp, float bias) {
		return new Matrix(addBias(inp.getArray(), bias));
	}

	/**
	 * Adds a column containing 1 to a matrix. <br>
	 * <br>
	 * This is to simulate the bias artificially.<br>
	 * 
	 * @param inp
	 *            the matrix to add the column into
	 * @return the transformed matrix
	 */
	public static Matrix addBias(Matrix inp) {
		return addBias(inp, 1);
	}

	// reconstructing and displaying numbers(3)
	/**
	 * Reconstructs the image from a single floating point array. <br>
	 * <br>
	 * This method will try to guess the image dimensions and begin loading it with
	 * information from the floating point array. <br>
	 * 
	 * @param data
	 *            the image data
	 * @return the image reconstructed
	 */
	public PImage reconstructImage(float[] data) {
		int edge = PApplet.parseInt(PApplet.sqrt(data.length));
		PImage ans = p.createImage(edge, edge, PApplet.RGB);
		ans.loadPixels();
		for (int i = 0; i < ans.width; i++) {
			for (int j = 0; j < ans.height; j++) {
				ans.pixels[j * edge + i] = p.color(data[j * edge + i] * 255);
			}
		}
		ans.updatePixels();
		return ans;
	}

	/**
	 * Displays the data in a matrix as an image. <br>
	 * <br>
	 * The image loading procedure can be found at
	 * {@link Dir#reconstructImage(float[])}<br>
	 * 
	 * @param X
	 *            the matrix that contains the images that we want to display
	 * @param resolution
	 *            the resolution of the individual images
	 * @param width
	 *            the width of the screen that contains the images
	 * @param height
	 *            the height of the screen that contains the images
	 * @return the PGraphics element that contains our images
	 */
	public PGraphics displayNumbers(Matrix X, int resolution, int width, int height) {
		PGraphics pg = p.createGraphics(width, height, PApplet.P2D);
		pg.beginDraw();
		for (int j = 0; j < X.m / (width / resolution); j++) {
			for (int i = 0; i < width / resolution; i++) {
				int index = j * width / resolution + i;
				if (index < X.m) {
					pg.image(reconstructImage(X.getRow(index)), i * resolution, j * resolution, resolution, resolution);
				}
			}
		}
		pg.endDraw();
		return pg;
	}

	/**
	 * Displays the data in a matrix as an image with some default values. <br>
	 * <br>
	 * This method will call {@link Dir#displayNumbers(Matrix, int, int, int)}
	 * directly containing some default values:<br>
	 * - Resolution will be the square root of the number of columns the data has,
	 * rounded down.<br>
	 * - Width and height will both be 300.<br>
	 * 
	 * @param X
	 *            the matrix that contains all the images we want to display
	 * @return the PGraphics element that contains our images
	 */
	public PGraphics displayNumbers(Matrix X) {
		return displayNumbers(X, PApplet.parseInt(PApplet.sqrt(X.n)), 300, 300);
	}

	/**
	 * Reads from stream a number of MNIST images. <br>
	 * <br>
	 * The method will automatically identify what file type it has and will import
	 * it seamlessly
	 * 
	 * @param path
	 *            the absolute path to the MNIST file
	 * @param maxSize
	 *            the maximum size of the data set we want to import
	 * @param progress
	 *            the attachable progress element
	 * @param ans
	 *            the answer variable that the method will return into
	 * @throws RuntimeException
	 *             whenever there's something wrong with the stream reading function
	 * @throws RuntimeException
	 *             whenever it encounters an unrecognized file type
	 */
	public static void loadMNIST(String path, int maxSize, _Float progress, Matrix ans) {
		FileInputStream file = null;
		try {
			file = new FileInputStream(path);
			int magicNumber = Dir.readInt(file);
			int nTest = Dir.readInt(file);
			int size = PApplet.min(maxSize, nTest);
			if (magicNumber == 2051) {
				int sqDimension = Math.round(PApplet.sq(Dir.readInt(file)));
				Dir.readInt(file);
				float[][] ansArray = new float[size][sqDimension];
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < sqDimension; j++) {
						ansArray[i][j] = (float) (1.0 * Dir.readByte(file) / 255);
					}
					progress.setValue(1.0 * i / size);
				}
				ans.init(ansArray);
				progress.setValue(1.0);
			} else if (magicNumber == 2049) {
				float[][] ansArray = Matrix.blankMatrixArray(size, 10);
				for (int i = 0; i < size; i++) {
					ansArray[i][Dir.readByte(file)] = 1;
					progress.setValue(1.0 * i / size);
				}
				ans.init(ansArray);
				progress.setValue(1.0);
			} else {
				throw new RuntimeException("file type not recognized");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't read from stream");
		}
	}

	/**
	 * Exports the 3d error graph. <br>
	 * <br>
	 * This involves sampling the network a lot of times when change 2 specified
	 * weights. Imagine a square with 4 corners at (25, 25), (25, -25), (-25, 25),
	 * (-25, -25). Remember that (0, 0) will be the current state of the network.
	 * This method will sample every natural coordinates in and on that square given
	 * the size of the square. After doing so, it calculates the networks received
	 * and pass the first 3 elements of the test set into it. The error values are
	 * then outputted into a 3d graph.<br>
	 * <br>
	 * Also, this method will export the graphs in 5 scales (the size of the side of
	 * the square): 1, 10, 100, 1000, 10000.<br>
	 * 
	 * @param proc
	 *            the main sketch
	 * @param progress
	 *            the progress tracker variable
	 * @param net
	 *            the network to deviate
	 * @param lX
	 *            the input test set
	 * @param lP
	 *            the correct answer for the input test set
	 * @param synNum
	 *            contains 2 numbers, first number is the first layer of two that
	 *            the first connection tweak located, second number for the second
	 *            connection
	 * @param p0
	 *            the location of the first connection. That means the first
	 *            connection is located at syn[synNum.x].ea[p0.x][p0.y]
	 * @param p1
	 *            the location of the second connection. That means the second
	 *            connection is located at syn[synNum.y].ea[p1.x][p1.y]
	 * @param path
	 *            the path to the intended output file. Must have an extension of
	 *            ".3dg"
	 * @param shift
	 *            shift the center location away from the origin. See the code for
	 *            more details.
	 * @param tag
	 *            any additional tags but this parameter is currently not in use
	 * @throws RuntimeException
	 *             whenever the target file doesn't have the .3dg extension
	 * @throws RuntimeException
	 *             whenever there is something wrong with writing to the file
	 * @throws RuntimeException
	 *             whenever there is something wrong while closing the file
	 */
	public static void export3d(PApplet proc, _Float progress, Network net, Matrix lX, Matrix lP, PVector synNum,
			PVector p0, PVector p1, String path, PVector shift, int tag) {
		int[] _3dScales = { 1, 10, 100, 1000, 10000 };
		boolean free = false;
		float space = (float) (1.0 / _3dScales.length);
		FileOutputStream file;
		int id = net.id();
		if (path.charAt(1) != ':') {
			path = Final_Simple_Net.parentAbsPath + path;
		}
		p.saveStrings(path + ".3dg", Env._String(""));
		String[] info = Env._String("id: " + PApplet.str(id),
				"shift: (" + Env.format(shift.x) + ", " + Env.format(shift.y) + ")",
				"synNum: (" + Env.format(synNum.x) + ", " + Env.format(synNum.y) + ")",
				"p0: (" + Env.format(p0.x) + ", " + Env.format(p0.y) + ")",
				"p1: (" + Env.format(p1.x) + ", " + Env.format(p1.y) + ")");
		if (!free) {
			String[] tmp = PApplet.split(path, "/");
			p.saveStrings(Dir.trace(path, tmp[tmp.length - 2]) + "plane data/" + tmp[tmp.length - 1] + ".txt", info);
		} else {
			p.saveStrings(path + ".txt", info);
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
				// experimental subRun
				FileOutputStream stream = file;
				float range = _3dScales[i];
				float startProg = i * space;
				float endProg = (i + 1) * space;
				/* sub run algorithm */
				float[][] ans = new float[50][50];
				int syn0 = (int) (synNum.x);
				int syn1 = (int) (synNum.y);
				int p00 = (int) (p0.x);
				int p01 = (int) (p0.y);
				int p10 = (int) (p1.x);
				int p11 = (int) (p1.y);
				Network tN = net.clone();
				PVector avg = new PVector((float) (tN.syn[syn0].getValue(p00, p01) + shift.x * range * 0.5),
						(float) (tN.syn[syn1].getValue(p10, p11) + shift.y * range * 0.5));
				for (int j = 0; j < ans.length; j++) {
					for (int k = 0; k < ans[0].length; k++) {
						tN.syn[syn0].setValue(p00, p01, (avg.x + (j - ans.length / 2) * range * 0.02));
						tN.syn[syn1].setValue(p10, p11, (avg.y + (k - ans[0].length / 2) * range * 0.02));
						ans[j][k] = tN.error(lX, lP);
						progress.setValue((j * ans[0].length + k) / (1.0 * ans.length * ans[0].length), startProg,
								endProg);
					}
				}
				Dir.writeMatrix(stream, new Matrix(ans));
				progress.setValue(1.0, startProg, endProg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("some problem with writing onto the file");
		}
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("some problem with closing the file");
		}
		progress.setValue(1.0);
	}

	/**
	 * Exports the 3d error graph. <br>
	 * <br>
	 * This method is the same as
	 * {@link Dir#export3d(PApplet, _Float, Network, Matrix, Matrix, PVector, PVector, PVector, String, PVector, int)}
	 * but have these parameters preset:<br>
	 * - tag: 0<br>
	 * 
	 * @param proc
	 *            the main sketch
	 * @param progress
	 *            the progress tracker variable
	 * @param net
	 *            the network to deviate
	 * @param lX
	 *            the input test set
	 * @param lP
	 *            the correct answer for the input test set
	 * @param synNum
	 *            contains 2 numbers, first number is the first layer of two that
	 *            the first connection tweak located, second number for the second
	 *            connection
	 * @param p0
	 *            the location of the first connection. That means the first
	 *            connection is located at syn[synNum.x].ea[p0.x][p0.y]
	 * @param p1
	 *            the location of the second connection. That means the second
	 *            connection is located at syn[synNum.y].ea[p1.x][p1.y]
	 * @param path
	 *            the path to the intended output file. Must have an extension of
	 *            ".3dg"
	 * @param shift
	 *            shift the center location away from the origin. See the code for
	 *            more details.
	 */
	public static void export3d(PApplet proc, _Float progress, Network net, Matrix lX, Matrix lP, PVector synNum,
			PVector p0, PVector p1, String path, PVector shift) {
		export3d(proc, progress, net, lX, lP, synNum, p0, p1, path, shift, 0);
	}

	/**
	 * Exports the 3d error graph. <br>
	 * <br>
	 * This method is the same as
	 * {@link Dir#export3d(PApplet, _Float, Network, Matrix, Matrix, PVector, PVector, PVector, String, PVector, int)}
	 * but have these parameters preset:<br>
	 * - shift: (0, 0)<br>
	 * - tag: 0<br>
	 * 
	 * @param proc
	 *            the main sketch
	 * @param progress
	 *            the progress tracker variable
	 * @param net
	 *            the network to deviate
	 * @param lX
	 *            the input test set
	 * @param lP
	 *            the correct answer for the input test set
	 * @param synNum
	 *            contains 2 numbers, first number is the first layer of two that
	 *            the first connection tweak located, second number for the second
	 *            connection
	 * @param p0
	 *            the location of the first connection. That means the first
	 *            connection is located at syn[synNum.x].ea[p0.x][p0.y]
	 * @param p1
	 *            the location of the second connection. That means the second
	 *            connection is located at syn[synNum.y].ea[p1.x][p1.y]
	 * @param path
	 *            the path to the intended output file. Must have an extension of
	 *            ".3dg"
	 */
	public static void export3d(PApplet proc, _Float progress, Network net, Matrix lX, Matrix lP, PVector synNum,
			PVector p0, PVector p1, String path) {
		export3d(proc, progress, net, lX, lP, synNum, p0, p1, path, new PVector(0, 0), 0);
	}

	/**
	 * Exports the 3d error graph. <br>
	 * <br>
	 * This method is the same as
	 * {@link Dir#export3d(PApplet, _Float, Network, Matrix, Matrix, PVector, PVector, PVector, String, PVector, int)}
	 * but have these parameters preset:<br>
	 * - synNum: (1, 1)<br>
	 * - p0: (0, 0)<br>
	 * - p1: (0, 1)<br>
	 * - tag: 0<br>
	 * 
	 * @param proc
	 *            the main sketch
	 * @param progress
	 *            the progress tracker variable
	 * @param net
	 *            the network to deviate
	 * @param lX
	 *            the input test set
	 * @param lP
	 *            the correct answer for the input test set
	 * @param path
	 *            the path to the intended output file. Must have an extension of
	 *            ".3dg"
	 * @param shift
	 *            shift the center location away from the origin. See the code for
	 *            more details.
	 */
	public static void export3d(PApplet proc, _Float progress, Network net, Matrix lX, Matrix lP, String path,
			PVector shift) {
		export3d(proc, progress, net, lX, lP, new PVector(1, 1), new PVector(0, 0), new PVector(0, 1), path, shift, 0);
	}

	/**
	 * Exports the 3d error graph. <br>
	 * <br>
	 * This method is the same as
	 * {@link Dir#export3d(PApplet, _Float, Network, Matrix, Matrix, PVector, PVector, PVector, String, PVector, int)}
	 * but have these parameters preset:<br>
	 * - synNum: (1, 1)<br>
	 * - p0: (0, 0)<br>
	 * - p1: (0, 1)<br>
	 * - shift: (0, 0)<br>
	 * - tag: 0<br>
	 * 
	 * @param proc
	 *            the main sketch
	 * @param progress
	 *            the progress tracker variable
	 * @param net
	 *            the network to deviate
	 * @param lX
	 *            the input test set
	 * @param lP
	 *            the correct answer for the input test set
	 * @param path
	 *            the path to the intended output file. Must have an extension of
	 *            ".3dg"
	 */
	public static void export3d(PApplet proc, _Float progress, Network net, Matrix lX, Matrix lP, String path) {
		export3d(proc, progress, net, lX, lP, new PVector(1, 1), new PVector(0, 0), new PVector(0, 1), path,
				new PVector(0, 0), 0);
	}
}
