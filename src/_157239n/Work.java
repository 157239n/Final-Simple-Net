package _157239n;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Work extends ProgressThread {// if net, 1 net only
	final public static int NO_WORK = -1, FUNC = 0, EXP = 1;// for work index
	public ArrayList<String> works = new ArrayList<String>(0);
	public ArrayList<SimpleProgressThread> threads = new ArrayList<SimpleProgressThread>(0);
	public int workIndex = NO_WORK;
	public long magicNumber=-1;
	private String desc = "";
	private boolean autoDescription = true;
	private ArrayList<InfoPacket> args = new ArrayList<InfoPacket>(0);
	private boolean multithreading = false;
	private boolean processSpecificDisplay=false;

	Work(PApplet parent, Rect location) {
		super(parent, location);
	}

	Work(PApplet parent) {
		super(parent);
	}
	
	
	/**
	 * Specifies the signature (magic number) of the object this handles.
	 * 
	 * @param magicNumber the signature (magic number) to specify
	 * @return itself
	 * */
	public Work magicNumber(long magicNumber) {
		this.magicNumber=magicNumber;
		return this;
	}
	/**
	 * Specifies the signature (magic number) of the object this handles.
	 * 
	 * @param magicNumber the signature (magic number) to specify
	 * @return itself
	 * */
	public Work signature(long magicNumber) {
		return magicNumber(magicNumber);
	}
	/**
	 * Returns the signature (magic number) of the object this handles. <br>
	 * <br>
	 * If this object's signature hasn't been initialized then it will return a default value of -1.<br>
	 * 
	 * @return the signature of the object this handles
	 * */
	public long magicNumber() {
		return this.magicNumber;
	}
	/**
	 * Returns the signature (magic number) of the object this handles. <br>
	 * <br>
	 * If this object's signature hasn't been initialized then it will return a default value of -1.<br>
	 * 
	 * @return the signature of the object this handles
	 * */
	public long signature() {
		return magicNumber();
	}

	/**
	 * Adds a new method to execute.
	 * 
	 * @param instance the String contains the name of the method
	 * @return itself
	 * */
	public Work add(String instance) {
		this.works.add(instance);
		workIndex = FUNC;
		return this;
	}

	/**
	 * Adds a new method with some parameters to execute.
	 * 
	 * @param instance the String contains the name of the method
	 * @param arg the arguments stored inside an InfoPacket element
	 * @return itself
	 * */
	public Work add(String instance, InfoPacket arg) {
		this.works.add(instance);
		this.args.add(arg);
		workIndex = FUNC;
		return this;
	}
	/**
	 * Adds a new thread under the form of a SimpleProgressThread.
	 * 
	 * @param thread the thread to be executed
	 * @return itself
	 * */
	public Work add(SimpleProgressThread thread) {
		this.threads.add(thread);
		workIndex = EXP;
		return this;
	}
	/**
	 * Adds a description for the entire object and removes the automatic description feature. <br>
	 * <br>
	 * Normally the description will appeared on the left of the progress bar that this object will draw.<br>
	 * 
	 * @param desc the description for this object
	 * @return itself
	 * */
	public Work addDescription(String desc) {
		this.desc = desc;
		removeAutoDescription();
		return this;
	}
	/**
	 * Remove the automatic description feature.
	 * 
	 * @return itself
	 * */
	public Work removeAutoDescription() {
		autoDescription = false;
		return this;
	}
	//TODO: document this monstrous thing
	/**
	 * Specifies whether to leave the displayed graphics to the inner threads.
	 * */
	public Work processSpecificDisplay() {
		if(workIndex!=EXP) {throw new RuntimeException("must have a work index of experimental value in order to have process specific displays");}
		processSpecificDisplay=true;
		//because of independent drawing, location of work passed in must also be passed into the processes' renderers
		for(int i=0;i<threads.size();i++) {
			threads.get(i).location=location;
		}
		return this;
	}
	/**
	 * Specifies whether you want to multithread this object.
	 * 
	 * @return itself
	 * */
	public Work multithread() {
		multithreading = true;
		return this;
	}

	/**
	 * Main run function, overloading {@link Thread#run()}.
	 * 
	 * @throws RuntimeException whenever there is more than 1 type of work in here (both FUNC and EXP)
	 * @throws RuntimeException whenever the user has not specified enough arguments when the object is executing several methods
	 * @throws RuntimeException whenever the user is trying to multithreadedly run several methods. This feature is not supported
	 * @throws RuntimeException whenever other threads have interrupted this thread while it is sleeping
	 * */
	public void run() throws RuntimeException {
		int lc = 0;
		if (works.size() > 0) {
			lc++;
		}
		if (threads.size() > 0) {
			lc++;
		}
		if (lc > 1) {
			throw new RuntimeException(
					"can't handle more than one operation inside a single Work instance. Please divide your task into smaller chunks. Function: run() throws RuntimeException, class Work, tab Threads");
		}
		if (lc == 0) {
			setProgress(1.0);
			return;
		}
		if (workIndex == FUNC) {
			if (multithreading) {
				throw new RuntimeException("can't run a random function as a thread. Function run() class Work");
			} else {
				if (args.size() > 0 && (args.size() != works.size())) {
					throw new RuntimeException("not enough arguments in this work. Funcion run() class Work");
				}
				while (works.size() > 0) {
					setProgress(0.0);
					if (args.size() > 0) {
						if (args.get(0)._FloatSize() > 0) {
							args.get(0).set_Float(0, getProgressAttachment());
						} else {
							args.get(0).add(getProgressAttachment());
						}
						Env.call(p, works.get(0), args.get(0));
						args.remove(0);
					} else {
						Env.call(p, works.get(0), new InfoPacket().add(getProgressAttachment()));// .addFunc(0)
					}
					works.remove(0);
					setProgress(1.0);
				}
			}
		}
		if (workIndex == EXP) {
			if (multithreading) {
				if (threads.size() != progressLength()) {
					initProgresses(threads.size());
					for (int i = 0; i < progressLength(); i++) {
						_Float tmpProgress = new _Float(0.0);
						attachProgress(i, tmpProgress);
						threads.get(i).attachProgress(tmpProgress);
					}
				} else {
					for (int i = 0; i < progressLength(); i++) {
						attachProgress(i, threads.get(i).getProgressAttachment());
					}
				}
				boolean doneImporting = false;
				/* start threads */
				for (int i = 0; i < threads.size(); i++) {
					threads.get(i).start();
				}
				/* wait for all to be done and global variables assigning */
				while(!doneImporting) {
					Env.threadDelay(Env.refreshTime);
					doneImporting = (getProgress() > 1 - Env.epsilon);
				}
			} else {
				// no multithreading
				// this is to declare that this work object should only have 1 overall progress
				// only and not multiple small ones
				initProgresses(0);
				int num = threads.size();
				boolean done = false;
				_Float threadProgress = null;

				while (threads.size() > 0) {
					if (threads.get(0).getProgressAttachment() != null) {
						threadProgress = threads.get(0).getProgressAttachment();
						//threads.get(0).setProgress(0.0);
					} else {
						threadProgress = new _Float(0.0);
						threads.get(0).attachProgress(threadProgress);
					}
					threads.get(0).start();
					done = false;
					while(!done) {
						Env.threadDelay(Env.refreshTime);
						done = (threadProgress.getValue() > 1 - Env.epsilon);
						setProgress(1.0 * (num - threads.size()) / num + threadProgress.getValue() / num);
					}
					threads.remove(0);
				}
				setProgress(1.0);
			}

		}
		if (getProgress() < 1 - Env.epsilon) {
			throw new RuntimeException(
					"internal Work error " + PApplet.str(getProgress()) + " " + PApplet.str(1 - Env.epsilon));
		}
	}

	public int numWork() {
		return works.size() + threads.size();
	}
	//TODO: document this
	/**
	 * */
	public void display(PGraphics pg, String additionalString) {
		if(!processSpecificDisplay){
			if (autoDescription) {
				drawProgress(pg, desc + additionalString + Env.max(works.size(), threads.size()));
			} else {
				drawProgress(pg, desc + additionalString);
			}
			return;
		}else {
			if(workIndex!=EXP) {throw new RuntimeException("must have a work index of experimental value in order to have process specific displays");}
			if(multithreading) {
				for(int i=0;i<threads.size();i++) {
					threads.get(i).display();
				}
				return;
			}else if(threads.size()>0){
				threads.get(0).display();
				return;
			}else {
				return;
			}
		}
	}

	public void display(String additionalString) {
		display(p.g, additionalString);
		return;
	}
	
	public void display() {
		display(p.g, "");
		return;
	}
	/**
	 * Returns the number of remaining threads this object has to execute.
	 * 
	 * @return the number of remaining threads this object has to execute
	 * */
	int threadsLength() {
		return threads.size();
	}
	/**
	 * Returns the number of remaining threads this object has to execute.
	 * 
	 * @return the number of remaining threads this object has to execute
	 * */
	int threadSize() {
		return threads.size();
	}
	/**
	 * Returns the number of remaining methods this object has to execute.
	 * 
	 * @return the number of remaining methods this object has to execute
	 * */
	int funcLength() {
		return works.size();
	}
	/**
	 * Returns the number of remaining methods this object has to execute.
	 * 
	 * @return the number of remaining methods this object has to execute
	 * */
	int funcSize() {
		return works.size();
	}
}