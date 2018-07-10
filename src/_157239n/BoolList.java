package _157239n;

public class BoolList {
	private boolean[] values;
	private int size = 8;
	private int num = 0;

	BoolList() {
		values = new boolean[size];
		for (int i = 0; i < values.length; i++) {
			values[i] = false;
		}
	}

	/**
	 * Adds a new boolean to the list.
	 * 
	 * @param newValue
	 *            the new value to add into
	 */
	public void add(boolean newValue) {
		num++;
		if (num >= size) {
			size *= 2;
			boolean[] newValues = new boolean[size];
			for (int i = 0; i < num - 1; i++) {
				newValues[i] = values[i];
			}
			newValues[num - 1] = newValue;
			for (int i = num; i < size; i++) {
				newValues[i] = false;
			}
			values = newValues;
		} else {
			values[num - 1] = newValue;
		}
	}

	/**
	 * Removes a boolean value at a specified index.
	 * 
	 * @param index
	 *            the index to remove the boolean
	 */
	public void remove(int index) {
		while (index < 0) {
			index += num;
		}
		while (index >= num) {
			index -= num;
		}
		for (int i = index; i < num - 1; i++) {
			values[i] = values[i + 1];
		}
		num--;
	}

	/**
	 * Gets the boolean value at a specified index.
	 * 
	 * @param index
	 *            the index to get the value
	 * @return the boolean value at that index
	 */
	public boolean get(int index) {
		return values[index];
	}

	/**
	 * Sets the value at a specified index.
	 * 
	 * @param index
	 *            the index to set the value
	 * @param value
	 *            the boolean value to set
	 */
	public void set(int index, boolean value) {
		while (index < 0) {
			index += num;
		}
		while (index >= num) {
			index -= num;
		}
		values[index] = value;
	}

	/**
	 * Returns the size of the array.
	 * 
	 * @return the size of the array
	 */
	public int size() {
		return num;
	}
}
