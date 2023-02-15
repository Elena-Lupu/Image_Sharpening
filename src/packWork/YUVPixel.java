package packWork;

class YUVPixel{
	private float y, u, v;
	
	public YUVPixel() {
		this.y = 0;
		this.u = 0;
		this.v = 0;
	}
	
	public float getY() { return this.y; }
	public float getU() { return this.u; }
	public float getV() { return this.v; }
	
	public void setY(float y) { this.y = y; }
	public void setU(float u) { this.u = u; }
	public void setV(float v) { this.v = v; }

	public RGBPixel convertYUVtoRGB() {
		RGBPixel resultPixel = new RGBPixel();
		
		double r = 1.164 * (this.y - 16) + 1.596 * (this.v - 128);
		double g = 1.164 * (this.y - 16) - 0.395 * (this.u - 128) - 0.813 * (this.v - 128);
		double b = 1.164 * (this.y - 16) + 2.018 * (this.u - 128);
		
		r = (r < 0) ? 0 : r;
		g =	(g < 0) ? 0 : g;
		b = (b < 0) ? 0 : b;
		
		r = (r > 255) ? 255 : r;
		g = (g > 255) ? 255 : g;
		b = (b > 255) ? 255 : b;

		resultPixel.setR((short)r);
		resultPixel.setG((short)g);
		resultPixel.setB((short)b);
		
		return resultPixel;
	}
}
