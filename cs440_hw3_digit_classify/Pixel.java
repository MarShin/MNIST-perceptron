package cs440_hw3_digit_classify;

public class Pixel {
	public int row, column;
	public int label; ////digit 0 to 9
	char content; //' ', +, #
	
	public Pixel(int row, int column, int label, char content){
		this.row = row;
		this.column = column;
		this.label = label;
		this.content = content;
	}
	
}
