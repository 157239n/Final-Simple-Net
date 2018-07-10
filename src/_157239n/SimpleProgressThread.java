package _157239n;

/**
 * Represents a simple progress thread. <br>
 * <br>
 * This class is intended for very simple progress tracking. You can initialize
 * a new progress, modify and get the progress in the meanwhile. The progress
 * itself is stored inside a {@link _157239n._Float} object so that the progress
 * object can be used to attach to other methods wishing to control and manage
 * the object. <br>
 * <br>
 * Because this class extends {@link Thread}, you can specify the
 * {@link Thread#run()} method to carry out your job and manage it on the fly.
 * 
 * @author 157239n
 * @author www.157239n.com
 * @version 1.0
 */
public class SimpleProgressThread extends Thread {
	/**
	 * Progress value, plain and simple
	 */
	protected _Float progress = new _Float(0.0);
	/**
	 * The intended location of the progress bar (or bars), waiting to be overloaded
	 * by subclasses.
	 */
	protected Rect location = null;

	SimpleProgressThread(_Float progress) {
		this.progress = progress;
	}

	SimpleProgressThread() {

	}

	/**
	 * Attaches a progress ({@link _157239n._Float} object) into this object.
	 * 
	 * @param progress
	 *            the progress object to attach to
	 * @return itself
	 */
	SimpleProgressThread attachProgress(_Float progress) {
		this.progress = progress;
		return this;
	}

	/**
	 * Instantiate a new progress ({@link _157239n._Float} object) at a specified
	 * progress level.
	 * 
	 * @param progress
	 *            the initial progress value to set to
	 * @return itself
	 */
	SimpleProgressThread(float progress) {
		this.progress = new _Float(progress);
	}

	/**
	 * Instantiate a new progress ({@link _157239n._Float} object) at a specified
	 * progress level.
	 * 
	 * @param progress
	 *            the initial progress value to set to
	 * @return itself
	 */
	SimpleProgressThread(double progress) {
		this((float) progress);
	}

	/**
	 * Sets the progress of the object.
	 * 
	 * @param progress
	 *            the progress value to set to
	 */
	public void setProgress(float progress) {
		this.progress.setValue(progress);
	}

	/**
	 * Sets the progress of the object.
	 * 
	 * @param progress
	 *            the progress value to set to
	 */
	public void setProgress(double progress) {
		this.progress.setValue((float) progress);
	}

	/**
	 * Gets the progress of the object.
	 * 
	 * @return the progress value of the object
	 */
	public float getProgress() {
		return progress.getValue();
	}

	/**
	 * Gets the progress ({@link _157239n._Float} object) that can link to other
	 * places.
	 * 
	 * @return the progress object
	 */
	public _Float getProgressAttachment() {
		return progress;
	}

	/**
	 * Dummy method for subclasses to overload
	 */
	protected void display() {
		throw new RuntimeException(
				"display method is still in simpleProgressThread and has not yet been overloaded. Please check for inconsistencies while multithreading.");
	}

	/**
	 * Sets the value of the progress to 0.0 but still retain the progress
	 * ({@link _157239n._Float} object).
	 */
	public void resetProgress() {
		progress.setValue(0.0);
	}
}