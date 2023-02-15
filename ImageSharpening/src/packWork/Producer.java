package packWork;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Producer implements Runnable {
	Thread t;
	String bmpPath;
	private Buffer imageBuffer;
	
	public Producer(String bmpPath, Buffer imageBuffer) {
		t = new Thread(this);
		this.bmpPath = bmpPath;
		this.imageBuffer = imageBuffer;
	}
	
	public void run() {
		BufferedImage img = null;
		FileWriterClass.startTime = System.nanoTime();
			
		//Citeste in intregime fisierul BMP si-l pastreaza sub forma de BufferedImage
		try { img = ImageIO.read(new File(this.bmpPath));}
		catch (IOException e) { e.printStackTrace(); }
		
		short imgWidth = (short)img.getWidth();
		short imgHeight = (short)img.getHeight();
		
		//Se transmite in 4 segmente la Consumer
		for (byte i = 0; i < 4; i++) {
			imageBuffer.put(img.getSubimage((imgWidth / 4) * i, 0, imgWidth / 4, imgHeight));
			System.out.println("Producer a trimis " + 25*(i+1) + "% din imagine...");
		}
	}
	
	public void start() { t.start(); }
}
