package packWork;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Consumer implements Runnable {
	private Buffer imageBuffer;
	Thread t;
	private ObjectOutputStream out;
	
	public Consumer(Buffer imageBuffer, ObjectOutputStream out) {
		this.imageBuffer = imageBuffer;
		t = new Thread(this);
		this.out = out;
	}
	
	public void start() { t.start(); }
	
	public void run() {
		synchronized (out) {
			BufferedImage[] subImage = new BufferedImage[4];
			RGBImage originalImage = null;
			PaddedYUVImage convertedYUVImage = null;
			YUVImage sharpenedYUVImage = null;
		
			//Se preiau segmentele de imagine de la Producer si se pastreaza intr-un vector
			for (byte i = 0; i < 4; i++) {
				subImage[i] = imageBuffer.get();
				System.out.println("Consumer a primit " + 25*(i+1) + "% din imagine...");
			}
			System.out.println("\n-------------");
			System.out.println("Citirea fisierului a durat:  " + (System.nanoTime() - FileWriterClass.startTime)/1000000 + " milisecunde");
			System.out.println("-------------\n");
			
			//Se transforma segmentele de imagine din BufferedImage in RGBImage, se sumeaza pentru a obtine imaginea mare, apoi se prelucreaza
			FileWriterClass.startTime = System.nanoTime();
			originalImage = RGBImage.sumImages(new RGBImage(subImage[0]), new RGBImage(subImage[1]), new RGBImage(subImage[2]), new RGBImage(subImage[3]));
			convertedYUVImage = new PaddedYUVImage(originalImage);
			sharpenedYUVImage = convertedYUVImage.sharpenImage();
			RGBImage sharpenedRGBImage = new RGBImage(sharpenedYUVImage);
			
			System.out.println("\n-------------");
			System.out.println("Prelucrarea a durat:  " + (System.nanoTime() - FileWriterClass.startTime)/1000000 + " milisecunde");
			System.out.println("-------------\n");
			
			//Se trimite imaginea pe fragmente la WriterResult
			FileWriterClass.startTime = System.nanoTime();
			try {
				for (byte i = 0; i < 5; i++) {
					out.writeObject(sharpenedRGBImage.cutImage((sharpenedRGBImage.getWidth() / 5) * i, 0, sharpenedRGBImage.getWidth() / 5, sharpenedRGBImage.getHeight()));
					out.flush();
					System.out.println("Consumer a trimis " + 20*(i+1) + "% din imagine...");
					Thread.sleep(500);
				}
				out.close();
			}catch (IOException | InterruptedException e) { e.printStackTrace(); }
		}
	}
}
