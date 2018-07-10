package _157239n;

import processing.core.PApplet;

public class _Float {
	private float value;

	_Float(float value) {
		this.value = value;
	}

	_Float(double value) {
		this((float) value);
	}

	/**
	 * Sets the value
	 * 
	 * @param value
	 *            the value to set it to
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Sets the value
	 * 
	 * @param value
	 *            the value to set it to
	 */
	public void setValue(double value) {
		setValue((float) value);
	}

	/**
	 * Gets the value
	 * 
	 * @return float value of the object
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Sets the value as if it is describing the progress
	 * 
	 * @param value
	 *            the value to set it to
	 * @param start
	 *            the start range of value
	 * @param end
	 *            the end range of value
	 */
	public void setValue(float value, float start, float end) {
		this.value = PApplet.map(value, 0, 1, start, end);
	}

	/**
	 * Sets the value as if it is describing the progress
	 * 
	 * @param value
	 *            the value to set it to
	 * @param start
	 *            the start range of value
	 * @param end
	 *            the end range of value
	 */
	public void setValue(double value, double start, double end) {
		setValue((float) value, (float) start, (float) end);
	}
}
