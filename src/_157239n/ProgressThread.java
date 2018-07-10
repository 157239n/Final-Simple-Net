package _157239n;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Represents a complex progress thread. <br>
 * <br>
 * This class is intended for tracking multiple progresses at a time and extends
 * on {@link _157239n.SimpleProgressThread}. This class can however, act as if
 * it is a {@link SimpleProgressThread}. This can be done by calling
 * {@link ProgressThread#initProgresses(int)} with a parameter of 0. To
 * initialize a bunch of progresses, call
 * {@link ProgressThread#initProgresses(int)} with the parameter specifying how
 * many progresses you need. Once initialized, you can get, set and retrieve
 * each progresses independently. The single progress element inherited from
 * {@link SimpleProgressThread} will be passive (meaning it cannot be set) and
 * will act as the global progress for this object. To calculate/update this
 * global progress, call {@link ProgressThread#calcFinalProgress()}. If you call
 * {@link ProgressThread#getProgress()} then it will automatically update the
 * global progress.
 * 
 * @author 157239n
 * @author www.157239n.com
 * @version 1.0
 */
public class ProgressThread extends SimpleProgressThread {
	// private _Float progress = new _Float(0.0);
	/**
	 * The link to the main sketch. <br>
	 * <br>
	 * Have to be public because other classes can still use it internally.<br>
	 */
	protected PApplet p;
	/**
	 * Progress values if there are many tasks
	 */
	private _Float[] progresses = new _Float[0];

	/**
	 * Constructor.
	 * 
	 * @parent the main Processing sketch
	 * @Rect the expected location of the sketch
	 */
	ProgressThread(PApplet parent, Rect location) {
		this.location = location;
		this.p = parent;
		resetProgress();
	}

	/**
	 * Constructor. <br>
	 * <br>
	 * The location will be automatically set to Rect(0, 0, 0, 0)
	 * 
	 * @parent the main Processing thread
	 */
	ProgressThread(PApplet parent) {
		this(parent, new Rect(0, 0, 0, 0));
	}

	/**
	 * Initializes multiple progress mode.
	 * 
	 * @param num
	 *            the number of progresses
	 */
	public void initProgresses(int num) {
		progresses = new _Float[num];
	}

	/**
	 * Calculates the final progress when take into account all the progresses.
	 * 
	 * @return the final progress
	 */
	public float calcFinalProgress() {
		float ans = (float) 0.0;
		if (progresses.length > 0) {
			float fraction = (float) (1.0 / progresses.length);
			for (int i = 0; i < progresses.length; i++) {
				if (progresses[i] == null) {
					progresses[i] = new _Float(1.0);
				}
				ans += fraction * progresses[i].getValue();
			}
			progress.setValue(ans);
		}
		return ans;
	}

	/**
	 * Attaches a _Float value that can be used to track the progress.
	 * 
	 * @param progress
	 *            the _Float value to be attached
	 */
	public ProgressThread attachProgress(_Float progress) {
		this.progress = progress;
		return this;
	}

	/**
	 * Attaches a _Float value that can be used to track a specific progress in
	 * multiple progresses.
	 * 
	 * @param index
	 *            the index to attach the value
	 * @param progress
	 *            the _Float value to be attached
	 */
	public void attachProgress(int index, _Float progress) {
		this.progresses[index] = progress;
	}

	/**
	 * Checks whether the progresses are null and replace it with a full progress
	 * bar. <br>
	 * <br>
	 * This needs to be done as other threads that link its progress with this
	 * thread can be terminated, preferably due to .remove() of the Work ArrayList
	 * in this architecture. We assume that every work must be carried out before it
	 * can be remove by the jvm garbage collector and thus setting it to a new
	 * _Float object representing the progress with value 1.0 is sufficient.
	 */
	private void checkNull() {
		for (int i = 0; i < progresses.length; i++) {
			if (progresses[i] == null) {
				progresses[i] = new _Float(1.0);
			}
		}
	}

	/**
	 * Draws the progress(es) into a PGraphics element passed in.
	 * 
	 * @param pg
	 *            the PGraphics element
	 * @param desc
	 *            the array of String to label the progress bars
	 * @throws RuntimeException
	 *             whenever the width or height of the progress bar is 0
	 */
	public void drawProgress(PGraphics pg, String[] desc) throws RuntimeException {
		int indent = -1;
		if (location.w == 0 || location.h == 0) {
			throw new RuntimeException("width or height is zero, can't draw a progress bar like that");
		}
		checkNull();
		calcFinalProgress();
		pg.textAlign(PApplet.CENTER, PApplet.CENTER);
		pg.fill(Env.eraseColor);
		pg.rect(location.x, location.y, location.w, location.h);
		float commonHeight = (float) (1.0 * location.h / (progresses.length + 1));
		/* 1 description */
		if (desc.length == 1) {
			indent = PApplet.parseInt(Env.textToBoxDefault + desc[0].length() * Env.textToBoxWidth);
			pg.fill(Env.notDoneColor);
			pg.rect(location.x + indent, location.y, location.w - indent, location.h);
			pg.fill(Env.doneColor);
			/* red bg */
			checkNull();
			for (int i = 0; i < progresses.length; i++) {
				pg.rect(location.x + indent, location.y + i * commonHeight,
						(location.w - indent) * progresses[i].getValue(), commonHeight);
				/* green bar */
			}
			pg.fill(Env.textColor);
			pg.text(desc[0], location.x + indent / 2, location.y + location.h / 2);
		}
		/* many description */
		if (desc.length > 1) {

			for (int i = 0; i < progresses.length; i++) {
				indent = PApplet.parseInt(Env.textToBoxDefault + desc[i].length() * Env.textToBoxWidth);
				pg.fill(Env.notDoneColor);
				pg.rect(location.x + indent, location.y + i * commonHeight, location.w - indent, commonHeight);
				/* red bg */
				pg.fill(Env.doneColor);
				pg.rect(location.x + indent, location.y + i * commonHeight,
						(location.w - indent) * progresses[i].getValue(), commonHeight);
				/* green bar */
				pg.fill(Env.textColor);
				pg.text(desc[i], location.x + indent / 2, (float) (location.y + (i + 0.5) * commonHeight));
				/* desc */
			}
			indent = PApplet.parseInt(Env.textToBoxDefault + desc[progresses.length].length() * Env.textToBoxWidth);
			pg.fill(Env.notDoneColor);
			pg.rect(location.x + indent, location.y + progresses.length * commonHeight, location.w - indent,
					commonHeight);
			/* red bg */
			pg.fill(Env.textColor);
			pg.text(desc[progresses.length], location.x + indent / 2,
					(float) (location.y + (progresses.length + 0.5) * commonHeight));
			/* desc */
		}
		pg.fill(Env.doneColor);
		pg.rect(location.x + indent, location.y + commonHeight * progresses.length,
				(location.w - indent) * progress.getValue(), commonHeight);
		/* common final green bar */
	}

	/**
	 * Draws the progress(es) into a PGraphics element passed in with a single
	 * String label.
	 * 
	 * @param pg
	 *            the PGraphics element
	 * @param desc
	 *            the description
	 */
	public void drawProgress(PGraphics pg, String desc) {
		drawProgress(pg, Env._String(desc));
	}

	/**
	 * Draws the progress(es) into a PGraphics element passed in with a single
	 * numeric label.
	 * 
	 * @param pg
	 *            the PGraphics element
	 * @param index
	 *            the numeric variable
	 */
	public void drawProgress(PGraphics pg, int index) {
		drawProgress(pg, Env._String(PApplet.str(index)));
	}

	/**
	 * Draws the progress(es) into a PApplet element passed in with a single String
	 * label.
	 * 
	 * @param pg
	 *            the PApplet element
	 * @param desc
	 *            the description
	 */
	public void drawProgress(PApplet pg, String desc) {
		drawProgress(pg.g, desc);
	}

	/**
	 * Draws the progresses(es) into a PApplet element passed in with a single
	 * numeric label.
	 * 
	 * @param pg
	 *            the PApplet element
	 * @param index
	 *            the numeric label
	 */
	public void drawProgress(PApplet pg, int index) {
		drawProgress(pg.g, index);
	}

	/**
	 * Draws the progress(es) into a PApplet element passed in in the beginning with
	 * a String label.
	 * 
	 * @param desc
	 *            the description
	 */
	public void drawProgress(String desc) {
		drawProgress(p, desc);
	}

	/**
	 * Draws the progress(es) into a PApplet element passed in in the beginning with
	 * a single numeric variable.
	 * 
	 * @param index
	 *            the numeric value
	 */
	public void drawProgress(int index) {
		drawProgress(p, index);
	}

	/**
	 * Sets the general progress.
	 * 
	 * @param value
	 *            the value to set to
	 * @throws RuntimeException
	 *             if the object is recording multiple progresses
	 */
	public void setProgress(float value) {
		if (progresses.length > 0) {
			throw new RuntimeException(
					"This ProgressThread object's main progress should not be changed because it is using multiple progresses");
		}
		progress.setValue(value);
	}

	/**
	 * Sets the general progress.
	 * 
	 * @param value
	 *            the value to set to
	 */
	public void setProgress(double value) {
		setProgress((float) value);
	}

	/**
	 * Sets the general progress inside a range.
	 * 
	 * @param value
	 *            the value to set to
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the end value
	 * @throws RuntimeException
	 *             if the object is recording multiple progresses
	 * @see _Float#setValue(float, float, float)
	 */
	public void setProgress(float value, float begin, float end) {
		if (progresses.length > 0) {
			throw new RuntimeException(
					"This ProgressThread object's main progress should not be changed because it is using multiple progresses");
		}
		progress.setValue(value, begin, end);
	}

	/**
	 * Sets the general progress inside a range.
	 * 
	 * @param value
	 *            the value to set to
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the ending value
	 * @see _Float#setValue(double, double, double)
	 */
	public void setProgress(double value, double begin, double end) {
		setProgress((float) value, (float) begin, (float) end);
	}

	/**
	 * Sets a specific progress in multiple progresses.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @param value
	 *            the value to set to
	 */
	public void setProgress(int index, float value) {
		progresses[index].setValue(value);
	}

	/**
	 * Sets a specific progress in multiple progresses.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @param value
	 *            the value to set to
	 */
	public void setProgress(int index, double value) {
		setProgress(index, (float) value);
	}

	/**
	 * Sets a specific progress in multiple progresses inside a range.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @param value
	 *            the value to set to
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the ending value
	 * @see _Float#setValue(float, float, float)
	 */
	public void setProgress(int index, float value, float begin, float end) {
		progresses[index].setValue(value, begin, end);
	}

	/**
	 * Sets a specific progress in multiple progresses inside a range.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @param value
	 *            the value to set to
	 * @param begin
	 *            the beginning value
	 * @param end
	 *            the ending value
	 * @see _Float#setValue(double, double, double)
	 */
	public void setProgress(int index, double value, double begin, double end) {
		setProgress(index, (float) value, (float) begin, (float) end);
	}

	/**
	 * Returns a specific progress in multiple progresses.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @return a specific progress in multiple progresses
	 */
	float getProgress(int index) {
		return progresses[index].getValue();
	}

	/**
	 * Returns the final progress after averaging multiple progresses (if
	 * applicable).
	 * 
	 * @return the final progress
	 */
	public float getProgress() {
		calcFinalProgress();
		return progress.getValue();
	}

	/**
	 * Gets the progress element of this object to link to other objects.
	 * 
	 * @return the progress element of this object
	 */
	public _Float getProgressAttachment() {
		return progress;
	}

	/**
	 * Gets a specific progress in multiple progresses to link to other objects.
	 * 
	 * @param index
	 *            the index of the progress to set
	 * @return the progress element of this object
	 */
	public _Float getProgressAttachment(int index) {
		return progresses[index];
	}

	/**
	 * Returns the size of the number of progresses in operation (currently).
	 * 
	 * @return the number of progresses
	 */
	public int progressSize() {
		return progresses.length;
	}

	/**
	 * Returns the size of the number of progresses in operation (currently)
	 * 
	 * @return the number of progresses
	 */
	public int progressLength() {
		return progressSize();
	}

	/**
	 * Returns the size of the number of progresses in operation (currently).
	 * 
	 * @return the number of progresses
	 */
	public int progressesSize() {
		return progresses.length;
	}

	/**
	 * Returns the size of the number of progresses in operation (currently)
	 * 
	 * @return the number of progresses
	 */
	public int progressesLength() {
		return progressSize();
	}

	/**
	 * Resets every progress bar available to 0 but don't nullify it.
	 */
	public void resetProgress() {
		progress.setValue(0.0);
		for (int i = 0; i < progresses.length; i++) {
			progresses[i].setValue(0.0);
		}
	}

	/**
	 * Checks whether this process has done.
	 * 
	 * @return whether this process has done.
	 */
	public boolean done() {
		float progress = getProgress();
		if (progress >= 0 && progress <= 1 - Env.epsilon) {
			return false;
		}
		if (progress > 1 - Env.epsilon) {
			return true;
		}
		throw new RuntimeException(
				"this exception should never be called. If you're seeing this then it's a very serious bug");
	}
}