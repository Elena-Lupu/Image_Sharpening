package packWork;

import java.awt.image.BufferedImage;

public class Buffer {
	private BufferedImage subImage;
	private boolean available = false;
	
	public synchronized BufferedImage get() {
		while (!available) {
			try { wait(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		available = false;
		notifyAll();
		
		return subImage;
	}
	
	public synchronized void put(BufferedImage subImage) {
		while (available) {
			try { wait(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		this.subImage = subImage;
		available = true;
		notifyAll();
	}
}
