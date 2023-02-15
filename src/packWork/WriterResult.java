package packWork;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;

public class WriterResult implements Runnable{
	Thread t;
	private String resultPath;
	private ObjectInputStream in;
	private static FileWriterClass writer = new FileWriterClass();
	
	public WriterResult(String resultPath, ObjectInputStream in) {
		t = new Thread(this);
		this.in = in;
		this.resultPath = resultPath;
	}
	
	public void start() { t.start(); }

	public void run() {
		synchronized (in) {
			RGBImage sharpenedImage = null;
			RGBImage[] sharpenedSubImage = new RGBImage[5];
		
			//Se citesc fragmentele venite prin pipe de la Consumer
			try {
				for (byte i = 0; i < 5; i++) {
					sharpenedSubImage[i] = (RGBImage) in.readObject();
					System.out.println("WriterResult a primit " + 20*(i+1) + "% din imagine...");
					Thread.sleep(200);
				}
				in.close();
			} catch (IOException | ClassNotFoundException | InterruptedException e1) { e1.printStackTrace(); }

			//Se sumeaza segmentele pentru a compune imaginea mare, apoi se scrie in fisier
			sharpenedImage = RGBImage.sumImages(sharpenedSubImage[0], sharpenedSubImage[1], sharpenedSubImage[2], sharpenedSubImage[3], sharpenedSubImage[4]);
			try { ImageIO.write(sharpenedImage.convertToBufferedImage(), "BMP", new File(resultPath)); }
			catch (IOException e) { e.printStackTrace(); }
			
			System.out.println("");
			System.out.println("Gata :)");
			System.out.println("\n-------------");
			System.out.println("Scrierea a durat:  " + (System.nanoTime() - FileWriterClass.startTime)/1000000 + " milisecunde");
			System.out.println("");
			System.out.println("Detalii despre imagine si timpul total de executie in Rezultate.txt");
			
			writer.writeToFile("Detalii despre imagine:\nLatime:  " + sharpenedImage.getWidth() + " px\nInaltime:  " + sharpenedImage.getHeight() + "px\nProcesul a durat in total: " + (System.nanoTime() - FileWriterClass.startTime0)/1000000 + " milisecunde");
		}
	}
}
