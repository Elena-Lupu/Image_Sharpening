package packTest;

import packWork.Buffer;
import packWork.Consumer;
import packWork.Producer;
import packWork.WriterResult;
import packWork.FileWriterClass;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ImageSharpening {

	public static void main(String[] args) {
		//Declar buffer-ul pentru imagine
		Buffer imageBuffer = new Buffer();
		
		//Fac conexiunea de tip pipe intre thread-urile Consumer si WriterResult
		PipedOutputStream pipeOut = new PipedOutputStream();
		PipedInputStream pipeIn = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			pipeIn = new PipedInputStream(pipeOut);
			out = new ObjectOutputStream(pipeOut);
			in = new ObjectInputStream(pipeIn);
		}
		catch (IOException e) { e.printStackTrace(); }

		//Retine momentul curent de timp pentru calculul timpului de citire al datelor e la tastatura,
		//dar si o copie al acestuia pentru calculul timpului total al executiei algoritmului
		FileWriterClass.startTime = System.nanoTime();
		FileWriterClass.startTime0 = FileWriterClass.startTime;
		
		//Citeste calea pentru citirea imaginii originale si calea pentru scrierea imaginii prelucrate
		Scanner inputReader = new Scanner(System.in);
		System.out.println("De unde se citeste imaginea originala (Scrieti calea absoluta):   ");
		String bmpPath = inputReader.nextLine();
		System.out.println("Unde se depune imaginea prelucrata?:   ");
		String resultPath = inputReader.nextLine();
		inputReader.close();
		System.out.println("\n-------------");
		System.out.println("Timp de citire a datelor de identificare a fisierului:  " + (System.nanoTime() - FileWriterClass.startTime)/1000000 + " milisecunde");
		System.out.println("-------------\n");
		
		//Creeaza thread-ul producer, care ia calea citita de la tastatura si citeste imaginea BMP
		Producer bmpProducer = new Producer(bmpPath, imageBuffer);
		
		//Creeaza thread-ul consumer care preia imaginea si o prelucreaza
		Consumer bmpConsumer = new Consumer(imageBuffer, out);
		
		bmpProducer.start();
		bmpConsumer.start();
		
		//Se porneste thread-ul pentru scrierea rezultatelor (cu intarziere ca sa nu interfereze cu thread-ul de prelucrare)
		WriterResult bmpWriter = new WriterResult(resultPath, in);
		try { TimeUnit.SECONDS.sleep(1); }
		catch (InterruptedException e) { e.printStackTrace(); }
		bmpWriter.start();	
	}

}
