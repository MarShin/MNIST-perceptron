package cs440_hw3_digit_classify;

public class Image {
	public Pixel[][] pixelTable;
	public int label; //digit 0 to 9
	
	int width = 28; 
	int height = 28;
	
	public Image(){
		pixelTable=new Pixel[width][height];
		label=0;
	}
	
	public void setPixel(int row, int column, Pixel pixel){
		if (row<height && column<width){
			this.pixelTable[row][column] = pixel; //shallow or deep copy
		}else {
			System.out.println("cant add");
		}
		
		
	}
}
