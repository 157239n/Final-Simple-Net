package _157239n;

import java.util.ArrayList;

import processing.core.PVector;
import processing.data.FloatList;
import processing.data.IntList;

public class InfoPacket {
	/* not used often at all and can be deprecated */IntList _int = new IntList(0);
	private FloatList _float = new FloatList(0);
	private ArrayList<String> _String = new ArrayList<String>(0);
	private ArrayList<_Float> __Float = new ArrayList<_Float>(0);
	private ArrayList<PVector> _PVector = new ArrayList<PVector>(0);
	private BoolList _boolean = new BoolList();

	InfoPacket() {
	}

	/**
	 * Adds a new integer.
	 * 
	 * @param _int
	 *            the integer value
	 * @return itself
	 */
	public InfoPacket add(int _int) {
		this._int.append(_int);
		return this;
	}

	/**
	 * Adds a new floating point value.
	 * 
	 * @param _float
	 *            the floating point value
	 * @return itself
	 */
	public InfoPacket add(float _float) {
		this._float.append(_float);
		return this;
	}

	/**
	 * Adds a new floating point value.
	 * 
	 * @param _double
	 *            the double-precision floating point value
	 * @return itself
	 */
	public InfoPacket add(double _double) {
		this._float.append((float) _double);
		return this;
	}

	/**
	 * Adds a new _Float.
	 * 
	 * @param __Float
	 *            the __Float value
	 * @return itself
	 */
	public InfoPacket add(_Float __Float) {
		this.__Float.add(__Float);
		return this;
	}

	/**
	 * Adds a new String.
	 * 
	 * @param _String
	 *            the String value
	 * @return itself
	 */
	public InfoPacket add(String _String) {
		this._String.add(_String);
		return this;
	}

	/**
	 * Adds a new PVector.
	 * 
	 * @param _PVector
	 *            the PVector value
	 * @return itself
	 */
	public InfoPacket add(PVector _PVector) {
		this._PVector.add(_PVector);
		return this;
	}

	/**
	 * Adds a new boolean.
	 * 
	 * @param bool
	 *            the boolean value
	 * @return itself
	 */
	public InfoPacket add(boolean bool) {
		_boolean.add(bool);
		return this;
	}

	/**
	 * Gets an integer value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the integer value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public int getInt(int index) throws RuntimeException {
		if (index >= _int.size()) {
			throw new RuntimeException("Array index out of bounds. Function int getInt(int index), class InfoPacket");
		}
		return _int.get(index);
	}

	/**
	 * Gets a floating point value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the floating point value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public float getFloat(int index) throws RuntimeException {
		if (index >= _float.size()) {
			throw new RuntimeException("Array index out of bounds. Function int getFloat(int index), class InfoPacket");
		}
		return _float.get(index);
	}

	/**
	 * Gets a _Float value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the _Float value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public _Float get_Float(int index) throws RuntimeException {
		if (index >= __Float.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function int get_Float(int index), class InfoPacket");
		}
		return __Float.get(index);
	}

	/**
	 * Gets a String value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the String value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public String getString(int index) throws RuntimeException {
		if (index >= _String.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function int getString(int index), class InfoPacket");
		}
		return _String.get(index);
	}

	/**
	 * Gets a PVector value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the PVector value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public PVector getPVector(int index) throws RuntimeException {
		if (index >= _PVector.size()) {
			throw new RuntimeException("Array index out of bounds. Function int getPVector(int index)");
		}
		return _PVector.get(index);
	}

	/**
	 * Gets a boolean value at a specific index.
	 * 
	 * @param index
	 *            the index of the desired value
	 * @return the boolean value
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public boolean getBoolean(int index) throws RuntimeException {
		if (index >= _boolean.size()) {
			throw new RuntimeException("Array index out of bounds. Function int getBoolean(int index)");
		}
		return _boolean.get(index);

	}

	/**
	 * Sets an integer value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setInt(int index, int value) throws RuntimeException {
		if (index >= _int.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setInt(int index, int value), class InfoPacket");
		}
		_int.set(index, value);
		return this;
	}

	/**
	 * Sets a floating point value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setFloat(int index, float value) throws RuntimeException {
		if (index >= _float.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setFloat(int index, float value), class InfoPacket");
		}
		_float.set(index, value);
		return this;
	}

	/**
	 * Sets a double-precision floating point value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setFloat(int index, double value) throws RuntimeException {
		if (index >= _float.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setFloat(int index, float value), class InfoPacket");
		}
		_float.set(index, (float) value);
		return this;
	}

	/**
	 * Sets a _Float value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket set_Float(int index, _Float value) throws RuntimeException {
		if (index >= __Float.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket set_Float(int index, _Float value), class InfoPacket");
		}
		__Float.set(index, value);
		return this;
	}

	/**
	 * Sets a String value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setString(int index, String value) throws RuntimeException {
		if (index >= _String.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setString(int index, String value), class InfoPacket");
		}
		_String.set(index, value);
		return this;
	}

	/**
	 * Sets a PVector value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setPVector(int index, PVector value) throws RuntimeException {
		if (index >= _PVector.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setPVector(int index, PVector value), class InfoPacket");
		}
		_PVector.set(index, value);
		return this;
	}

	/**
	 * Sets a boolean value at a specific index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the value to set it to
	 * @return itself
	 * @throws RuntimeException
	 *             whenever the index is out of bounds
	 */
	public InfoPacket setBoolean(int index, boolean value) throws RuntimeException {
		if (index >= _boolean.size()) {
			throw new RuntimeException(
					"Array index out of bounds. Function InfoPacket setPVector(int index, PVector value), class InfoPacket");
		}
		_boolean.set(index, value);
		return this;
	}

	/**
	 * Returns the current size of the integer array.
	 * 
	 * @return the size of the integer array
	 */
	public int intSize() {
		return _int.size();
	}

	/**
	 * Returns the current size of the floating point array.
	 * 
	 * @return the size of the integer array
	 */
	public int floatSize() {
		return _float.size();
	}

	/**
	 * Returns the current size of the _Float array.
	 * 
	 * @return the size of the _Float array
	 */
	public int _FloatSize() {
		return __Float.size();
	}

	/**
	 * Returns the current size of the String array.
	 * 
	 * @return the size of the String array
	 */
	public int StringSize() {
		return _String.size();
	}

	/**
	 * Returns the current size of the PVector array.
	 * 
	 * @return the size of the PVector array
	 */
	public int PVectorSize() {
		return _PVector.size();
	}

	/**
	 * Returns the current size of the boolean array.
	 * 
	 * @return the size of the boolean array
	 */
	public int booleanSize() {
		return _boolean.size();
	}
}