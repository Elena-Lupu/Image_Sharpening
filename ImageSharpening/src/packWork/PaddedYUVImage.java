package packWork;

class PaddedYUVImage extends YUVImage{
	private static byte[][] sharpeningMask = new byte[3][3];
	private static FileWriterClass writer = new FileWriterClass();

	{ System.out.println("A început conversia..."); }
	
	static {
		writer.deleteFile();
		writer.createFile();
		sharpeningMask[0][0] = 0;
		sharpeningMask[0][1] = -1;
		sharpeningMask[0][2] = 0;
		sharpeningMask[1][0] = -1;
		sharpeningMask[1][1] = 5;
		sharpeningMask[1][2] = -1;
		sharpeningMask[2][0] = 0;
		sharpeningMask[2][1] = -1;
		sharpeningMask[2][2] = 0;
	}
	
	public PaddedYUVImage(RGBImage img) {
		this.width = (short) (img.getWidth() + 2);
		this.height = (short) (img.getHeight() + 2);
		
		this.pixels = new YUVPixel[this.height][this.width];
		for (short i = 0; i < this.height; i++)
			for (short j = 0; j < this.width; j++) {
				this.pixels[i][j] = new YUVPixel();
				if (!(i == 0 || i == this.height - 1) && (!(j == 0 || j == this.width - 1)))
					this.pixels[i][j] = img.getPixel(i-1, j-1).convertRGBtoYUV();
			}
	}
	
	//Aici se aplica transformarea efectiva a pixelilor
	//Tin cont de faptul ca doresc modificarea doar a canalului Y
	//Canalele U si V trebuie sa ramana neschimbate
	private static YUVPixel applyConvolutionMask(YUVPixel[][] pixelsToConvolve) {
		YUVPixel resultPixel = new YUVPixel();
		float y=0;
		
		for (byte i = 0; i < 3; i++)
			for (byte j = 0; j < 3; j++)
				y += pixelsToConvolve[i][j].getY() * sharpeningMask[i][j];
		
		resultPixel.setY(y);
		resultPixel.setU(pixelsToConvolve[1][1].getU());
		resultPixel.setV(pixelsToConvolve[1][1].getV());
		
		return resultPixel;
	}
	
	//Procesul in sine de "Image Sharpening"
	public YUVImage sharpenImage() {
		YUVPixel[][] pixelsToConvolve = new YUVPixel[3][3];
		YUVImage sharpenedImage = new YUVImage((short)(this.width - 2), (short)(this.height - 2));
		YUVPixel pix = null;
		
		for (byte i = 0; i < 3; i++)
			for (byte j = 0; j < 3; j++)
				pixelsToConvolve[i][j] = new YUVPixel();
		
		//Pastrez pe rand grupul de pixeli pe care fac o prelucrare si salvez pe rand fiecare nou pixel intr-o noua imagine
		for (short i = 1; i < this.height - 1; i++)
			for (short j = 1; j < this.width - 1; j++) {
				pixelsToConvolve[0][0] = this.pixels[i-1][j-1];
				pixelsToConvolve[0][1] = this.pixels[i-1][j];
				pixelsToConvolve[0][2] = this.pixels[i-1][j+1];
				pixelsToConvolve[1][0] = this.pixels[i][j-1];
				pixelsToConvolve[1][1] = this.pixels[i][j];
				pixelsToConvolve[1][2] = this.pixels[i][j+1];
				pixelsToConvolve[2][0] = this.pixels[i+1][j-1];
				pixelsToConvolve[2][1] = this.pixels[i+1][j];
				pixelsToConvolve[2][2] = this.pixels[i+1][j+1];
				
				if (i == j && i == this.width/2){
					System.out.println("");
				}
				
				pix = PaddedYUVImage.applyConvolutionMask(pixelsToConvolve);
				sharpenedImage.setPixel(i-1, j-1, pix);
			}

		return sharpenedImage;
	}
}
