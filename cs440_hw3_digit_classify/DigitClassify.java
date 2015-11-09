package cs440_hw3_digit_classify;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DigitClassify {
	final static int width = 28;
	final static int height = 28;
	final static int total = 5000; //total training data
 //	Image[] trainingImgArr = new Image[total];
	static ArrayList<Image> trainingImgArr = new ArrayList<Image>();
	static int[] trainingLabelArr = new int[total];
	
	//static String[] raw = new String[140001];
	static char[][] raw = new char[140000][width];
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		loadTrainingImg();
		loadTrainingLabel();
		mergeImage();
	}
	
	public static void loadTrainingLabel() throws IOException{
		String fileName = "src/traininglabels.txt";
		
		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader (fr);

		String line;
		int i=0;
		while((line = buffer.readLine())!=null)
		{
			trainingLabelArr[i] = Integer.parseInt(line);         
       //     System.out.println(i + " " + trainingLabelArr[i] );
            i++;
		}
		fr.close();
	}
	
	public static void loadTrainingImg() throws IOException{
		String fileName = "src/trainingimages.txt";
		
		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader (fr);

		String line;
		int i=0;
		while((line = buffer.readLine())!=null)
		{
			char [] vals = line.toCharArray();

            for (int col = 0; col < width; col++) {
                raw[i][col] = vals[col];
             //   System.out.print(raw[i][col]);
            }
            i++;
        //    System.out.println();
		}
		fr.close();
	}
	
	public static void mergeImage(){
		int labelCount = 4916; //=0
		Image temp = new Image();
		for (int i=139916,j=0; i<140000 && labelCount<5000; i++,j++){ // i=0
			if (i%28==0){ //every new image
				temp = new Image();
				j=0;
				System.out.println("image "+labelCount+" , label: "+trainingLabelArr[labelCount]);
				
			}
			
			for (int k=0; k<width; k++){
				Pixel tempPixel = new Pixel(j, k, trainingLabelArr[labelCount], raw[i][k]);
				temp.setPixel(j, k, tempPixel);
				System.out.print(raw[i][k]);
			//	System.out.println("(j,k): "+j+" "+k);
			}
			System.out.println();
			if (i%28==0){
				labelCount++;
				trainingImgArr.add(temp);
			}
		}
	}
	
}
