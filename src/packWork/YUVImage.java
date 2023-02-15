package packWork;

class YUVImage extends Image{
	protected YUVPixel[][] pixels;
	protected short width, height;
	
	protected YUVImage(short width, short height) {
		this.width = width;
		this.height = height;
		
		this.pixels = new YUVPixel[height][width];
		for (short i = 0; i < height; i++)
			for (short j = 0; j < width; j++) {
				this.pixels[i][j] = new YUVPixel();
				this.pixels[i][j].setY(0);
				this.pixels[i][j].setU(0);
				this.pixels[i][j].setV(0);
			}
	}
	
	protected YUVImage() {
		this.width = 0;
		this.height = 0;
		
		this.pixels = new YUVPixel[height][width];
		for (short i = 0; i < height; i++)
			for (short j = 0; j < width; j++) {
				this.pixels[i][j] = new YUVPixel();
				this.pixels[i][j].setY(0);
				this.pixels[i][j].setU(0);
				this.pixels[i][j].setV(0);
			}
	}
	
	public YUVPixel getPixel(int x, int y) { return this.pixels[x][y]; }
	public short getWidth() { return this.width; }
	public short getHeight() { return this.height; }
	
	public void setPixel(int x, int y, YUVPixel pixel) {
		this.pixels[x][y].setY(pixel.getY());
		this.pixels[x][y].setU(pixel.getU());
		this.pixels[x][y].setV(pixel.getV());
	}
}