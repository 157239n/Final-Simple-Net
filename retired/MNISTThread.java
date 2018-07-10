/*
import java.io.FileInputStream;

import processing.core.PApplet;

public class MNISTThread extends SimpleProgressThread {
	Matrix ans;
	String path;
	int maxSize;

	MNISTThread(String path, int maxSize, _Float progress) {
		super(progress);
		_init(path, maxSize);
	}

	MNISTThread(String path, int maxSize) {
		super(0.0);
		_init(path, maxSize);
	}

	public MNISTThread attachProgress(_Float progress) {
		this.progress = progress;
		return this;
	}

	private void _init(String path, int maxSize) {
		this.path = path;
		this.maxSize = maxSize;
	}

	public void run() {
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
					setProgress(1.0 * i / size);
				}
				setProgress(1);
				ans = new Matrix(ansArray);
			} else if (magicNumber == 2049) {
				float[][] ansArray = Matrix.blankMatrixArray(size, 10);
				for (int i = 0; i < size; i++) {
					ansArray[i][Dir.readByte(file)] = 1;
					setProgress(1.0 * i / size);
				}
				setProgress(1);
				ans = new Matrix(ansArray);
			} else {
				throw new RuntimeException("file type not recognized. Function: Matrix importMNISTFile(String path, int maxSize)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("can't read from stream. Function: Matrix importMNISTFile(String path, int maxSize)");
		}
	}
}
/**/