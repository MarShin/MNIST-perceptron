package neural;

public class Image {
	public Pixel[][] pixelTable;
	public int label; //digit 0 to 9
	public double[] likehood = new double[10];
	public double[] wx = new double[10];
	
	int width = 28; 
	int height = 28;
	
	public Image(){
		pixelTable=new Pixel[width][height];
		for (int i=0;i<height;i++){
			for (int j=0;j<width;j++){
				this.pixelTable[i][j] = new Pixel();
			}
		}
	}
	
	public void setPixel(int row, int column, Pixel pixel){
		if (row<height && column<width){
			this.pixelTable[row][column] = pixel; //shallow or deep copy
		}else {
			System.out.println("cant add");
		}
	}

	public void print(){
		System.out.println("Label: "+label);
		for (int i=0;i<height;i++){
			for (int j=0;j<width;j++){
				System.out.print(pixelTable[i][j].feature);
			}
			System.out.println();
		}
	}

	public void setLabel(int label) {
		this.label = label;
	}
}

