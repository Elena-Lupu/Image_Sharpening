package packWork;

import java.io.Serializable;

class RGBPixel implements Serializable{
	private short r, g, b;
	
	public short getR() { return this.r; }
	public short getG() { return this.g; }
	public short getB() { return this.b; }
	
	public void setR(short r) { this.r = r; }
	public void setG(short g) { this.g = g; }
	public void setB(short b) { this.b = b; }
	
	public YUVPixel convertRGBtoYUV() {
		double red = (double)this.r;
		double green = (double)this.g;
		double blue = (double)this.b;
		YUVPixel resultPixel = new YUVPixel(); 

		double y = 0.257 * red + 0.504 * green + 0.098 * blue + 16;
		double u = -0.148 * red - 0.291 * green + 0.439 * blue + 128;
		double v = 0.439 * red - 0.368 * green - 0.071 * blue + 128;
		
		y = (y < 0) ? 0 : y;
		u = (u < 0) ? 0 : u;
		v = (v < 0) ? 0 : v;
		
		y = (y > 255) ? 255 : y;
		u = (u > 255) ? 255 : u;
		v = (v > 255) ? 255 : v;
		
		resultPixel.setY((float)y);
		resultPixel.setU((float)u);
		resultPixel.setV((float)v);
		
		return resultPixel;
	}
}
