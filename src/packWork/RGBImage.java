package packWork;

import java.awt.image.BufferedImage;
import java.io.Serializable;

class RGBImage extends Image implements Serializable{
	private RGBPixel[][] pixels;
	private short width, height;
	
	public RGBImage(BufferedImage img) {
		short imgWidth = (short)img.getWidth();
		short imgHeight = (short)img.getHeight();
		this.pixels = new RGBPixel[imgHeight][imgWidth];
		
		for (short i = 0; i < imgHeight; i++)
			for (short j = 0; j < imgWidth; j++) {
				this.pixels[i][j] = new RGBPixel();
				this.pixels[i][j].setB((short)Integer.parseInt((Integer.toBinaryString(img.getRGB(j, i))).substring(24, 32), 2));
				this.pixels[i][j].setG((short)Integer.parseInt((Integer.toBinaryString(img.getRGB(j, i))).substring(16, 24), 2));
				this.pixels[i][j].setR((short)Integer.parseInt((Integer.toBinaryString(img.getRGB(j, i))).substring(8, 16), 2));
			}
		
		this.width = imgWidth;
		this.height = imgHeight;
	}
	
	public RGBImage(YUVImage img) {
		this.width = (short)img.getWidth();
		this.height = (short)img.getHeight();
		
		this.pixels = new RGBPixel[this.height][this.width];
		for (short i = 0; i < this.height; i++)
			for (short j = 0; j < this.width; j++) {
				this.pixels[i][j] = new RGBPixel();
				this.pixels[i][j] = img.getPixel(i, j).convertYUVtoRGB();
			}
	}
	
	private RGBImage(short bigWidth, short bigHeight, RGBPixel[][] bigPixels) {
		this.width = bigWidth;
		this.height = bigHeight;
		this.pixels = bigPixels.clone();
	}

	public RGBPixel getPixel(int x, int y) { return this.pixels[x][y]; }
	public short getWidth() { return this.width; }
	public short getHeight() { return this.height; }
	
	//Functie care lipeste mai multe imagini pentru a forma una singura
	//Se considera sectiuni de inaltimi egale si latime diferita
	public static RGBImage sumImages(RGBImage...images) {
		short bigWidth=0, bigHeight=images[0].height;
		RGBPixel[][] bigPixels;
		short h=0; //Aceasta variabila este folosita pentru a ma ajuta sa determin unde trebuie scris pixelul pe imaginea mare

		for(RGBImage img : images) bigWidth += img.width;
		bigPixels = new RGBPixel[bigHeight][bigWidth];
		
		for(RGBImage img : images) {
			for (short j = 0; j < bigHeight; j++)
				for (short k = 0; k < img.width; k++) {
					bigPixels[j][h + k] = new RGBPixel();
					bigPixels[j][h + k].setB(img.getPixel(j, k).getB());
					bigPixels[j][h + k].setG(img.getPixel(j, k).getG());
					bigPixels[j][h + k].setR(img.getPixel(j, k).getR());
				}
			h += img.width;
		}
		
		RGBImage bigImage = new RGBImage(bigWidth, bigHeight, bigPixels);
		
		return bigImage;
	}

	//Aceasta functie converteste imaginea la BufferedImage pentru a putea fi scrisa pe fisier
	public BufferedImage convertToBufferedImage() {
		int r, g, b;
		short width = this.width;
		short height = this.height;
		BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (short i = 0; i < height; i++)
			for (short j = 0; j < width; j++) {
				r = this.pixels[i][j].getR();
				g = this.pixels[i][j].getG();
				b = this.pixels[i][j].getB();
				finalImage.setRGB(j, i, ((r << 16) | (g << 8) | b));
			}
		
		return finalImage;
	}

	//Prin aceasta functie se decupeaza o sectiune de imagine
	//(x, y) sunt coordonatele puntului din stanga sus al sectiunii
	public RGBImage cutImage(int x, int y, int miniWidth, int miniHeight) {
		RGBPixel[][] miniPixels = new RGBPixel[miniHeight][miniWidth];
		
		for (short i = 0; i < miniHeight; i++)
			for (short j = 0; j < miniWidth; j++) {
				miniPixels[i][j] = new RGBPixel();
				miniPixels[i][j] = this.pixels[i + y][j + x];
			}
				
		return (new RGBImage((short)miniWidth, (short)miniHeight, miniPixels));
	}
}
