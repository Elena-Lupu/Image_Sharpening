package packWork;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileWriterClass implements FileOperations{
	public static long startTime = 0;
	public static long startTime0 = 0;
	
	public void writeToFile(String text) {
        try { Files.write(Paths.get("Rezultate.txt"), text.getBytes()); }
        catch (IOException e) { e.printStackTrace(); }
	}
	
	public void deleteFile() {
		File resultFile = new File("Rezultate.txt");
		resultFile.delete();
	}
	
	public void createFile() {
		File resultFile = new File("Rezultate.txt");
		try { resultFile.createNewFile(); }
		catch (IOException e) { e.printStackTrace(); }
	}
}
