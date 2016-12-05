package neural;

public class Pixel {
	public int row, column;
	public int label; ////digit 0 to 9
	char content; //' ', +, #
	int feature;
	
	public Pixel(){
		
	}
	
	public Pixel(int row, int column, int label, char content){
		this.row = row;
		this.column = column;
		this.label = label;
		this.content = content;
	}
	
	public Pixel(int row, int column, char content){
		this.row = row;
		this.column = column;
		this.content = content;
	}
	
	
}
