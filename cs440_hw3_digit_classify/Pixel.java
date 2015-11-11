package cs440_hw3_digit_classify;

public class Pixel {
	public int row, column;
	public int label; ////digit 0 to 9
	char content; //' ', +, #
	public double spaceProb;
	public double plusProb;
	public double sharpProb;
	
	public int space = 0;
	public int plus = 0;
	public int sharp = 0;
	
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
	
	public char toChar(){
		return this.content;
	}
	
}
