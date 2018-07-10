package _157239n;

import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.IntList;
import processing.data.FloatList;
import processing.core.PVector;
/**
 * Represents the environment. <br>
 * <br>
 * This class contains all the static methods needed for day-to-day operations.<br>
 * 
 * @author www.157239n.com
 * @version 1.0
 * */
public class Env {
	private static PApplet p = new PApplet();
	/**
	 * The mathematical constant e.
	 */
	final public static float e = 2.7182818284590452353602874713526624977572f;
	/**
	 * A really small value, right on the boundary of how much floats can store.
	 * Obviously floats can store a huge range of values but when dealing with
	 * calculations with numbers between 0 and 1 then 0.0000001 is just about as
	 * small as you can get.
	 */
	final public static float epsilon = 0.0000001f;
	/**
	 * Default empty String array containing 1 "" element.
	 */
	final public static String[] emptyStringArray = { "" };
	/**
	 * Number and letter states.
	 */
	final public static boolean NUMBERS = true, LETTERS = false;
	/**
	 * Color of typical progress bar to indicate the portion of work hasn't been
	 * done. This is typically red.
	 */
	final public static int notDoneColor = color(255, 0, 0);
	/**
	 * Color of typical progress bar to indicate the portion of work has been done.
	 * This is typically green.
	 */
	final public static int doneColor = color(0, 255, 0);
	/**
	 * Typical erase color, usually black or coal-like.
	 */
	final public static int eraseColor = color(50, 50, 50);
	/**
	 * Typical text color, usually white or sky-like.
	 */
	final public static int textColor = color(255, 255, 255);
	/**
	 * Average number of pixels per letter.
	 */
	final public static float textToBoxWidth = 8;
	/**
	 * Padding space for boxes in pixels. This is around 2.5 letters.
	 */
	final public static float textToBoxDefault = 20;
	/**
	 * Refresh time in milliseconds for any time-dependent methods
	 */
	final public static int refreshTime = 200;

	Env(PApplet proc) {
		Env.p = proc;
	}

	/* color (9) */
	/**
	 * Returns an integer describing the color when input the color's red, green and
	 * blue elements.
	 * 
	 * @param r
	 *            how much red? Values ranging from 0 to 255
	 * @param g
	 *            how much green? Values ranging from 0 to 255
	 * @param b
	 *            how much blue? Values ranging from 0 to 255
	 * 
	 * @return the integer describing the color
	 */
	public static int color(int r, int g, int b) {
		return -1 - (255 - b) * 1 - (255 - g) * 256 - (255 - r) * 256 * 256;
	}

	/**
	 * Returns an integer describing the color when input the color on a grey scale.
	 * 
	 * @param c
	 *            how much color? Values ranging from 0 to 255
	 * @return the integer describing the color
	 */
	public static int color(int c) {
		return color(c, c, c);
	}

	/**
	 * Returns an integer describing the color when input the color on a grey scale.
	 * 
	 * @param c
	 *            how much color? Values ranging from 0 to 255
	 * @return the integer describing the color
	 */
	public static int color(double c) {
		return color((int) c, (int) c, (int) c);
	}

	/**
	 * Returns an integer describing the redness of a particular color.
	 * 
	 * @param color
	 *            the color we want the redness of
	 * @return the redness of the color
	 */
	public static int red(int color) {
		return 255 - (-color - 1) / (256 * 256);
	}

	/**
	 * Returns an integer describing the greenness of a particular color.
	 * 
	 * @param color
	 *            the color we want the greenness of
	 * @return the greenness of the color
	 */
	public static int green(int color) {
		return 255 - ((-color - 1) % (256 * 256)) / 256;
	}

	/**
	 * Returns an integer describing the blueness of a particular color.
	 * 
	 * @param color
	 *            the color we want the blueness of
	 * @return the blueness of the color
	 */
	public static int blue(int color) {
		return 255 - (-color - 1) % 256;
	}

	/**
	 * Makes an image grey, meaning turning it into a grey scale image.
	 * 
	 * @param img
	 *            the image to make grey
	 * @return the grey-scaled image
	 * @see Env#makeGrey(int)
	 */
	public static PImage makeGrey(PImage img) {
		PImage ans = img.get();
		img.loadPixels();
		ans.loadPixels();
		for (int i = 0; i < img.width; i++) {
			for (int j = 0; j < img.height; j++) {
				ans.pixels[j * img.width + i] = makeGrey(img.pixels[j * img.width + i]);
			}
		}
		ans.updatePixels();
		return ans;
	}

	/**
	 * Returns a grey-scaled value of a specific color.
	 * 
	 * @param color
	 *            the color to make it grey-scaled
	 * @return the grey-scaled color value
	 */
	public static int makeGrey(int color) {
		return color(0.2989 * red(color) + 0.5870 * green(color) + 0.1141 * blue(color));
	}

	/**
	 * Mixes 3 colors together with the portions set by a parameter. <br>
	 * <br>
	 * If the parameter is in the range [-1, 0], it will mix the first and second
	 * colors.<br>
	 * If the parameter is in the range [0, 1], it will mix the second and third
	 * colors.<br>
	 * 
	 * @param c1
	 *            the first color
	 * @param c
	 *            the second color
	 * @param c2
	 *            the third color
	 * @param alpha
	 *            the parameter
	 * @return the color
	 * @throws RuntimeException
	 *             whenever alpha does not lie in the [-1, 1] range
	 */
	public static int mix3(int c1, int c, int c2, float alpha) {
		if (Math.abs(alpha) > 1) {
			throw new RuntimeException("can't mix 3 colors with a portion of " + format(alpha));
		}
		if (alpha < 0) {
			return mix2(c1, c, PApplet.abs(alpha));
		} else {
			return mix2(c2, c, PApplet.abs(alpha));
		}
	}

	/**
	 * Mixes 3 colors together with the portions set by a parameter. <br>
	 * <br>
	 * If the parameter is in the range [-1, 0], it will mix the first and second
	 * colors.<br>
	 * If the parameter is in the range [0, 1], it will mix the second and third
	 * colors.<br>
	 * 
	 * @param c1
	 *            the first color
	 * @param c
	 *            the second color
	 * @param c2
	 *            the third color
	 * @param alpha
	 *            the parameter
	 * @return the color
	 * @throws RuntimeException
	 *             whenever alpha does not lie in the [-1, 1] range
	 */
	public static int mix3(int c1, int c, int c2, double alpha) {
		return mix3(c1, c, c2, (float) alpha);
	}

	/**
	 * Mixes 2 colors together with a portion set by a parameter. <br>
	 * <br>
	 * This method splits the two colors into it's red, green and blue components.
	 * Then for each base color, the new base color will be calculated as r =
	 * r<sub>1</sub> * alpha + r<sub>2</sub> * (1 - alpha).<br>
	 * 
	 * @param c1
	 *            the first color
	 * @param c2
	 *            the second color
	 * @param alpha
	 *            the portion of the first color
	 * @return the new color
	 * @throws RuntimeException
	 *             whenever alpha does not lie in the [0, 1] range
	 */
	public static int mix2(int c1, int c2, float alpha) throws RuntimeException {
		if (alpha < 0 || alpha > 1) {
			throw new RuntimeException("can't mix 2 colors with a portion of less than 0 or greater than 1");
		}
		float r = alpha * red(c1) + (1 - alpha) * red(c2);
		float g = alpha * green(c1) + (1 - alpha) * green(c2);
		float b = alpha * blue(c1) + (1 - alpha) * blue(c2);
		return p.color(r, g, b);
	}

	/**
	 * Mixes 2 colors together with a portion set by a parameter. <br>
	 * <br>
	 * This method splits the two colors into it's red, green and blue components.
	 * Then for each base color, the new base color will be calculated as r =
	 * r<sub>1</sub> * alpha + r<sub>2</sub> * (1 - alpha).<br>
	 * 
	 * @param c1
	 *            the first color
	 * @param c2
	 *            the second color
	 * @param alpha
	 *            the portion of the first color
	 * @return the new color
	 * @throws RuntimeException
	 *             whenever alpha does not lie in the [0, 1] range
	 */
	public static int mix2(int c1, int c2, double alpha) throws RuntimeException {
		return mix2(c1, c2, (float) alpha);
	}

	/**
	 * Converts an image to a matrix. <br>
	 * <br>
	 * This method first grey-scale the image by using the {@link Env#makeGrey(int)}
	 * method to each pixel.<br>
	 * Each pixel now contains integer values from 0 to 255. The method will map the
	 * current values between 0 and 255 to values from lowerBound to upperBound.<br>
	 * Finally, the method will add a bias if the bias parameter is set to a value
	 * different from 0 and returns the matrix.<br>
	 * 
	 * @param image
	 *            the image to be converted
	 * @param lowerBound
	 *            the lower bound of the pixels
	 * @param upperBound
	 *            the upper bound of the pixels
	 * @param bias
	 *            the additional bias for the matrix
	 * @return the matrix-converted image
	 */
	public static Matrix imageToMatrix(PImage image, float lowerBound, float upperBound, float bias) {
		float[][] ans;
		if (!kron(kron(bias, 0))) {
			ans = new float[1][image.width * image.height + 1];
			ans[0][image.width * image.height] = bias;
		} else {
			ans = new float[1][image.width * image.height];
		}
		for (int j = 0; j < image.height; j++) {
			for (int i = 0; i < image.width; i++) {
				ans[0][j * image.width + i] = makeGrey(image.get(i, j));
			}
		}
		return new Matrix(ans);
	}

	/**
	 * Converts an image to a matrix. <br>
	 * <br>
	 * This method first grey-scale the image by using the {@link Env#makeGrey(int)}
	 * method to each pixel.<br>
	 * Each pixel now contains integer values from 0 to 255. The method will map the
	 * current values between 0 and 255 to values from lowerBound to upperBound.<br>
	 * Finally, the method will add a bias if the bias parameter is set to a value
	 * different from 0 and returns the matrix.<br>
	 * 
	 * @param image
	 *            the image to be converted
	 * @param lowerBound
	 *            the lower bound of the pixels
	 * @param upperBound
	 *            the upper bound of the pixels
	 * @param bias
	 *            the additional bias for the matrix
	 * @return the matrix-converted image
	 */
	public static Matrix imageToMatrix(PImage image, double lowerBound, double upperBound, double bias) {
		return imageToMatrix(image, (float) lowerBound, (float) upperBound, (float) bias);
	}

	/**
	 * Converts an image to a matrix. <br>
	 * <br>
	 * This method first grey-scale the image by using the {@link Env#makeGrey(int)}
	 * method to each pixel.<br>
	 * Each pixel now contains integer values from 0 to 255. The method will map the
	 * current values between 0 and 255 to values from lowerBound to upperBound.<br>
	 * The method will not add a bias.<br>
	 * 
	 * @param image
	 *            the image to be converted
	 * @param lowerBound
	 *            the lower bound of the pixels
	 * @param upperBound
	 *            the upper bound of the pixels
	 * @return the matrix-converted image
	 */
	public static Matrix imageToMatrix(PImage image, double lowerBound, double upperBound) {
		return imageToMatrix(image, lowerBound, upperBound, 0);
	}

	/**
	 * Converts an image to a matrix. <br>
	 * <br>
	 * This method first grey-scale the image by using the {@link Env#makeGrey(int)}
	 * method to each pixel.<br>
	 * Each pixel now contains integer values from 0 to 255. The method will map the
	 * current values between 0 and 255 to values from 0 to 1.<br>
	 * Finally, the method will add a bias if the bias parameter is set to a value
	 * different from 0 and returns the matrix.<br>
	 * 
	 * @param image
	 *            the image to be converted
	 * @param bias
	 *            the additional bias for the matrix
	 * @return the matrix-converted image
	 */
	public static Matrix imageToMatrix(PImage image, double bias) {
		return imageToMatrix(image, 0.0, 1.0, bias);
	}

	/**
	 * Converts an image to a matrix. <br>
	 * <br>
	 * This method first grey-scale the image by using the {@link Env#makeGrey(int)}
	 * method to each pixel.<br>
	 * Each pixel now contains integer values from 0 to 255. The method will map the
	 * current values between 0 and 255 to values from 0 to 1.<br>
	 * The method will not add a bias.<br>
	 * 
	 * @param image
	 *            the image to be converted
	 * @return the matrix-converted image
	 */
	public static Matrix imageToMatrix(PImage image) {
		return imageToMatrix(image, 0.0, 1.0, 0);
	}

	/* sign(2) */
	/**
	 * Returns the sign of a value. <br>
	 * <br>
	 * There are 3 possible outputs:<br>
	 * -1 occurs when the number is less than or equal to -epsilon 0 occurs when the
	 * number's absolute value is less than epsilon 1 occurs when the number is
	 * greater than or equal to epsilon
	 * 
	 * @param value
	 *            the value under consideration
	 * @return the sign of that value based on the rules above
	 */
	public static int sign(float value) {
		if (PApplet.abs(value) < epsilon) {
			return 0;
		} else if (value > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Returns the sign of a value. <br>
	 * <br>
	 * See {@link Env#sign(float)} for more information
	 * 
	 * @param value
	 *            the value under consideration
	 * @return the sign of that value
	 */
	public static int sign(double value) {
		return sign((float) value);
	}

	/*
	 * standard deviation (2), float SD(FloatList values), float SD(float[] values)
	 */
	/**
	 * Calculates the standard deviation for a list of floating point values.
	 * 
	 * @param values
	 *            the values under consideration
	 * @return the standard deviation of the number list
	 */
	public static float SD(FloatList values) {
		float avg = (float) 0.0;
		for (int i = 0; i < values.size(); i++) {
			avg += values.get(i);
		}
		avg /= values.size();
		float s = (float) 0.0;
		for (int i = 0; i < values.size(); i++) {
			s += PApplet.sq(values.get(i) - avg);
		}
		s /= values.size();
		s = PApplet.sqrt(s);
		return s;
	}

	/**
	 * Calculates the standard deviation for a list of floating point values.
	 * 
	 * @param values
	 *            the values under consideration
	 * @return the standard deviation of the number list
	 */
	public static float SD(float[] values) {
		return SD(FloatList(values));
	}

	/*
	 * gaussian (2), float gaussian(x, mu, sig), float gaussianReverse(y, mu, sig)
	 */
	/**
	 * Returns the Gaussian distribution value at a specific coordinate, mean and
	 * deviation.
	 * 
	 * @param x
	 *            the location we want to calculate it
	 * @param mu
	 *            the average value
	 * @param sig
	 *            the deviation
	 * @return the Gaussian distribution value
	 */
	public static float gaussian(float x, float mu, float sig) {
		return PApplet.exp(-PApplet.sq(x - mu) / (2 * PApplet.sq(sig)))
				/ PApplet.sqrt(PApplet.TWO_PI * PApplet.sq(sig));
	}

	/**
	 * Returns the inverse function of the Gaussian distribution at a specific
	 * coordinate, mean and deviation.
	 * 
	 * @param y
	 *            the output value (this function finds the input value from the
	 *            output value)
	 * @param mu
	 *            the average value
	 * @param sig
	 *            the deviation
	 * @return the inverse of the Gaussian distribution value
	 * @throws RuntimeException
	 *             whenever the suppied y exceeds the domain of the function
	 */
	public static float gaussianReverse(float y, float mu, float sig) {
		float a = (float) (y * Math.sqrt(PApplet.TWO_PI * PApplet.sq(sig)));
		float b = PApplet.log(a);
		float c = -2 * b * PApplet.sq(sig);
		if (a < 0 || b > 0 || c < 0) {
			throw new RuntimeException(
					"some math stuff is wrong with the gaussianReverse method. Go over there, here's the variables: a: "
							+ PApplet.str(a) + ", b:" + PApplet.str(b) + ", c:" + PApplet.str(c));
		}
		return PApplet.sqrt(c) + mu;
	}

	/*
	 * primitive arrays and Lists conversion (4), _float(FloatList), _int(IntList),
	 * FloatList(float[]), IntList(int[])
	 */
	/**
	 * Converts from a FloatList into an array of floats.
	 * 
	 * @param numbers
	 *            the initial FloatList array
	 * @return the array of floats
	 */
	public static float[] _float(FloatList numbers) {
		float[] ans = new float[numbers.size()];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = numbers.get(i);
		}
		return ans;
	}

	/**
	 * Converts from an IntList into an array of integers.
	 * 
	 * @param numbers
	 *            the initial IntList array
	 * @return the array of integers
	 */
	public static int[] _int(IntList numbers) {
		int[] ans = new int[numbers.size()];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = numbers.get(i);
		}
		return ans;
	}

	/**
	 * Converts from an array of floats into a FloatList array.
	 * 
	 * @param numbers
	 *            the initial float array
	 * @return the FloatList array
	 */
	public static FloatList FloatList(float[] numbers) {
		FloatList ans = new FloatList(0);
		for (int i = 0; i < numbers.length; i++) {
			ans.append(numbers[i]);
		}
		return ans;
	}

	/**
	 * Converts from an array of integers into an IntList array.
	 * 
	 * @param numbers
	 *            the initial integer array
	 * @return the IntList array
	 */
	public static IntList IntList(int[] numbers) {
		IntList ans = new IntList(0);
		for (int i = 0; i < numbers.length; i++) {
			ans.append(numbers[i]);
		}
		return ans;
	}

	/*
	 * array creation (7), int[] _int(int...), int[][] _int(int[]...), float[]
	 * _float(float...), float[][] _float(float[]...), double[] _double(double...),
	 * double[][] _double(double[]...), String[] _String(String...)
	 */
	/**
	 * Creates an array out of consecutive numbers.
	 * 
	 * @param numbers
	 *            the consecutive numbers
	 * @return the array of integers
	 */
	public static int[] _int(int... numbers) {
		return numbers;
	}

	/**
	 * Creates a 2d array out of the 1d integer array.
	 * 
	 * @param numbers
	 *            the consecutive 1d integer array
	 * @return the array of 1d arrays
	 */
	public static int[][] _int(int[]... numbers) {
		return numbers;
	}

	/**
	 * Creates an array out of consecutive floats.
	 * 
	 * @param numbers
	 *            the consecutive numbers
	 * @return the array of floats
	 */
	public static float[] _float(float... numbers) {
		return numbers;
	}

	/**
	 * Creates a 2d array out of the 1d floating point array.
	 * 
	 * @param numbers
	 *            the consecutive 1d float array
	 * @return the array of 1d arrays
	 */
	public static float[][] _float(float[]... numbers) {
		return numbers;
	}

	/**
	 * Creates an array out of consecutive double precision floats.
	 * 
	 * @param numbers
	 *            the consecutive numbers
	 * @return the array of floats
	 */
	public static float[] _float(double... numbers) {
		float[] ans = new float[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			ans[i] = (float) numbers[i];
		}
		return ans;
	}

	/**
	 * Creates a 2d array out of the 1d double precision floating point array.
	 * 
	 * @param numbers
	 *            the consecutive 1d double precision floating array
	 * @return the array of 1d arrays
	 */
	public static float[][] _float(double[]... numbers) {
		float[][] ans = new float[numbers.length][numbers[0].length];
		for (int i = 0; i < numbers.length; i++) {
			ans[i] = _float(numbers[i]);
		}
		return ans;
	}

	/**
	 * Creates an array out of consecutive doubles.
	 * 
	 * @param numbers
	 *            the consecutive numbers
	 * @return the array of doubles
	 */
	public static double[] _double(double... numbers) {
		return numbers;
	}

	/**
	 * Creates a 2d array out of the 1d floating point array.
	 * 
	 * @param numbers
	 *            the consecutive 1d double array
	 * @return the array of 1d arrays
	 * 
	 */
	public static double[][] _double(double[]... numbers) {
		return numbers;
	}

	/**
	 * Creates an array out of consecutive Strings.
	 * 
	 * @param values
	 *            the consecutive Strings
	 * @return the array of Strings
	 */
	public static String[] _String(String... values) {
		return values;
	}

	/* format some num (1), String _format(float number) */
	/**
	 * Returns a String value of the floating point number passed in with 3 decimal
	 * places.
	 * 
	 * @param number
	 *            the number to be formatted
	 * @return the formatted number
	 */
	public static String format3(float number) {
		return format(number, 3);
	}

	/**
	 * Returns a String value of the floating point number passed in with 5 decimal
	 * places.
	 * 
	 * @param number
	 *            the number to be formatted
	 * @return the formatted number
	 */
	public static String format5(float number) {
		return format(number, 5);
	}

	/**
	 * Returns a String value of the floating point number passed in with 7 decimal
	 * places.
	 * 
	 * @param number
	 *            the number to be formatted
	 * @return the formatted number
	 */
	public static String format7(float number) {
		return format(number, 7);
	}

	/**
	 * Returns a String value of the floating point number passed in with specified
	 * decimal places.
	 * 
	 * @param number
	 *            the number to be formatted
	 * @param decimal
	 *            the number of decimals to format
	 * @return the formatted number
	 */
	public static String format(float number, int decimal) {
		return String.format("%." + PApplet.str(decimal) + "f", number);
	}

	/**
	 * Returns a String value of the floating point number passed in with 3 decimal
	 * places.
	 * 
	 * @param number
	 *            the number to be formatted
	 * @return the formatted number
	 */
	public static String format(float number) {
		return format(number, 3);
	}

	/*
	 * kronecker deltas (4), int kron(boolean), int kron(int, int), int kron(float,
	 * float), boolean kron(int)
	 */
	/**
	 * Returns 1 if true, 0 if false. <br>
	 * <br>
	 * This is inspired by the knonecker delta.<br>
	 * 
	 * @param bit
	 *            the boolean passed in
	 * @return 1 if true, 0 if false
	 */
	public static int kron(boolean bit) {
		if (bit) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Kronecker delta, returns 1 if two values are the same and 0 otherwise.
	 * 
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return 1 if two values are the same and 0 otherwise
	 */
	public static int kron(int a, int b) {
		if (a == b) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Kronecker delta, returns 1 if two values are closer than Env#epsilon.
	 * 
	 * @param a
	 *            the first value
	 * @param b
	 *            the second value
	 * @return 1 if two values are closer than Env#epsilon
	 */
	public static int kron(float a, float b) {
		if (PApplet.abs(a - b) < 0.0001) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Returns false if 1, true if otherwise.
	 * 
	 * @param a
	 *            the integer value
	 * @return false if 1, true if otherwise
	 */
	public static boolean kron(int a) {
		return a != 0;
	}

	/* reflection stuff (2) */
	/**
	 * Calls a method using reflection with an attached progress.
	 * 
	 * @param obj
	 *            the object you are calling from
	 * @param function
	 *            the name of the function to call
	 * @param progress
	 *            the progress attached to the method
	 * @throws RuntimeException
	 *             whenever Java's Reflection API doesn't behave
	 */
	public static void call(Object obj, String function, _Float progress) {
		try {
			Method method = obj.getClass().getMethod(function, _Float.class);
			method.invoke(obj, progress);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't execute function \"" + function + "\"");
		}
	}

	/**
	 * Calls a method using reflection with an attached progress inside the data.
	 * 
	 * @param obj
	 *            the object you are calling from
	 * @param function
	 *            the name of the function to call
	 * @param data
	 *            the data to pass into the function
	 * @throws RuntimeException
	 *             whenever Java's Reflection API doesn't behave
	 */
	public static void call(Object obj, String function, InfoPacket data) {
		try {
			Method method = obj.getClass().getMethod(function, InfoPacket.class);
			method.invoke(obj, data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't execute function \"" + function + "\"");
		}
	}

	/* max (4) */
	/**
	 * Returns the maximum value in a list of consecutive integers.
	 * 
	 * @param values
	 *            the integer values
	 * @return the maximum value
	 */
	public static int max(int... values) {
		int m = values[0];
		for (int i = 1; i < values.length; i++) {
			m = PApplet.max(m, values[i]);
		}
		return m;
	}

	/**
	 * Returns the maximum value in a list of floating point numbers.
	 * 
	 * @param values
	 *            the floating point values
	 * @return the maximum value
	 */
	public static float max(float... values) {
		float m = values[0];
		for (int i = 1; i < values.length; i++) {
			m = PApplet.max(m, values[i]);
		}
		return m;
	}

	/**
	 * Returns the maximum value in a list of double-precision floating point
	 * numbers.
	 * 
	 * @param values
	 *            the double-precision floating point values
	 * @return the maximum value
	 */
	public static float max(double... values) {
		float m = (float) values[0];
		for (int i = 1; i < values.length; i++) {
			m = PApplet.max(m, (float) values[i]);
		}
		return m;
	}

	/* shorthand operations on PVectors (5) */
	/**
	 * Adds two PVectors together and return the result.
	 * 
	 * @param A
	 *            the first vector
	 * @param B
	 *            the second vector
	 * @return the resulting PVector
	 */
	public static PVector add(PVector A, PVector B) {
		return PVector.add(A, B);
	}

	/**
	 * Subtracts the second PVector from the first PVector.
	 * 
	 * @param A
	 *            the first vector
	 * @param B
	 *            the second vector
	 * @return the resulting vector
	 */
	public static PVector sub(PVector A, PVector B) {
		return PVector.sub(A, B);
	}

	/**
	 * Multiplies a PVector with a variable.
	 * 
	 * @param A
	 *            the vector
	 * @param b
	 *            the variable
	 * @return the resulting vector
	 */
	public static PVector mul(PVector A, float b) {
		return new PVector(A.x * b, A.y * b);
	}

	/**
	 * Delays the current thread by a specified number of milliseconds using while
	 * loops.
	 * 
	 * @param millis
	 *            the number of milliseconds to delay
	 */

	public static void loopedDelay(int millis) {
		int time = p.millis();
		while (p.millis() - time < millis) {
		}
	}

	/**
	 * Delays the current thread by a specified number of milliseconds using while
	 * loops and is identical to {@link _157239n.Env#loopedDelay(int)}.
	 * 
	 * @param millis
	 *            the number of milliseconds to delay
	 */

	public static void loopedSleep(int millis) {
		loopedDelay(millis);
	}

	/**
	 * Delays the current thread by a specified number of milliseconds using
	 * {@link Thread#sleep(long)} method.
	 * 
	 * @param millis
	 *            the number of milliseconds to delay
	 */
	public static void threadDelay(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException("thread interrupted while sleeping");
		}
	}

	/**
	 * Delays the current thread by a specified number of milliseconds using
	 * {@link Thread#sleep(long)} method and is identical to
	 * {@link _157239n.Env#threadDelay(int)}.
	 * 
	 * @param millis
	 *            the number of milliseconds to delay
	 */
	public static void threadSleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException("thread interrupted while sleeping");
		}
	}
}
