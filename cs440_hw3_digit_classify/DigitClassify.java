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

	static int[] trainingLabelArr = new int[total];
	static int[] testLabelArr = new int[1000]; 
	static double[] prior = new double [10];
	static int[] freq = new int [10]; // frequency of each label, used to compute prior
	static char[][] raw = new char[140000][width];
	
	static ArrayList<Image> trainingImgArr = new ArrayList<Image>(); //array of all training images
	
	
//	public static class imgClass{
//		static ArrayList<Image> imgClass = new ArrayList<Image>();
//		int label;
//		
//		public void setClassLabel(int label){
//			this.label = label;
//		}
//	}
	
	public static void main(String[] args) throws IOException {
		loadTrainingImg();
		loadTrainingLabel();
		mergeImage();
	//	trainingImgArr.get(trainingImgArr.size()-1).print(); //last image gone
	//	trainingImgArr.get(trainingImgArr.size()-2).print(); 
		countPrior();
		train();
		loadTestingLabel();
		loadTestingImg();
		
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
	
	public static void loadTestingLabel() throws IOException{
		String fileName = "src/testLabels.txt";
		
		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader (fr);

		String line;
		int i=0;
		while((line = buffer.readLine())!=null)
		{
			testLabelArr[i] = Integer.parseInt(line);         
      //      System.out.println(i + " " + testLabelArr[i] );
            i++;
		}
		fr.close();
	}
	
	
	public static void loadTestingImg() throws IOException{
		String fileName = "src/trainingimages.txt";
		
		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader (fr);

		Image testImg = new Image();
		String line;
		int count=0;
		while((line = buffer.readLine())!=null)
		{
			if(count%28 == 0){
				testImg = new Image();
			}
			
			
			char [] vals = line.toCharArray();

           for(int i=0; i<height; i++){
        	   for (int j=0; j<width; j++){
        		 testImg.setPixel(i, j, new Pixel(i,j,vals[j]));  
        		  System.out.print(vals[j]);
        	   }
        	    System.out.println();
           }
            count++;
        
		}
		fr.close();
	}
	
	public static void mergeImage(){
		int labelCount = 0;
		Image temp = new Image();
		for (int i=0,j=0; i<140000 && labelCount<5000; i++,j++){ //140000
			if (i%28==0){ //every new image
				temp = new Image();
				j=0;
				System.out.println("current line: "+i);
				System.out.println("image "+labelCount+" , label: "+trainingLabelArr[labelCount]);
				temp.setLabel(trainingLabelArr[labelCount]);
				
			}
			
			for (int k=0; k<width; k++){
				Pixel tempPixel = new Pixel(j, k, trainingLabelArr[labelCount], raw[i][k]);
				temp.setPixel(j, k, tempPixel);
			//	System.out.print(raw[i][k]);
			//	System.out.println("(j,k): "+j+" "+k);
			}
		//	System.out.println();
			if (i%28==0){
				labelCount++;
				trainingImgArr.add(temp);
			}
		}
	}
	
	public static void countPrior(){
		
		for (int i=0; i<trainingLabelArr.length;i++){
			int index = trainingLabelArr[i];
			freq[index]++;
		}
		
		for (int i=0; i<10; i++){
			//System.out.println(i+": "+prior[i]);
			prior[i] = (double) freq[i]/total;
			//System.out.println(i+": "+prior[i]);
		}
	}
	
	public static Image[] classImage = new Image[10];
	
	
	
	public static void train(){
		//init classImage
		for (int i=0; i<10; i++){
			classImage[i] = new Image();
			classImage[i].setLabel(i);
		}
		
		int label = 0;
		for (int i=0; i<trainingImgArr.size();i++){
			label = trainingImgArr.get(i).label;
			switch (label){
			case 0: countChar(0, classImage[label], trainingImgArr.get(i));
					break;
					
			case 1: countChar(1, classImage[label], trainingImgArr.get(i));
					break;					
			case 2: countChar(2, classImage[label], trainingImgArr.get(i));
					break;
			case 3: countChar(3, classImage[label], trainingImgArr.get(i));
					break;
			case 4: countChar(4, classImage[label], trainingImgArr.get(i));
					break;
			case 5: countChar(5, classImage[label], trainingImgArr.get(i));
					break;
			case 6: countChar(6, classImage[label], trainingImgArr.get(i));
					break;
			case 7: countChar(7, classImage[label], trainingImgArr.get(i));
					break;
			case 8: countChar(8, classImage[label], trainingImgArr.get(i));
					break;
			case 9: countChar(9, classImage[label], trainingImgArr.get(i));
					break;
			}
		}
		
		//compute likelihood
		for (int i=0; i<10; i++){ //for each class
			//debug print out each pixel overall count
//			System.out.println("Class : "+i);
//			for (int j=0; j<height; j++){ //for pixelTable
//				for (int k=0; k<width; k++){
//				 System.out.print(classImage[i].pixelTable[i][j].plus+ " ");
//				}
//				System.out.println();
//			}
			
			for (int j=0; j<height; j++){ //for pixelTable
				for (int k=0; k<width; k++){
					classImage[i].pixelTable[i][j].spaceProb = (double) classImage[i].pixelTable[i][j].space/freq[i];
					classImage[i].pixelTable[i][j].plusProb = (double) classImage[i].pixelTable[i][j].plus/freq[i];
					classImage[i].pixelTable[i][j].sharpProb = (double) classImage[i].pixelTable[i][j].sharp/freq[i];
				}
			}
			
//			debug pixel prob
//			System.out.println("Class : "+i);
//			for (int j=0; j<height; j++){ //for pixelTable
//				for (int k=0; k<width; k++){
//				 System.out.print(classImage[i].pixelTable[i][j].plusProb+ " ");
//				}
//				System.out.println();
//			}
			
		}
	}
	
	public static void countChar(int label,Image overall, Image trainImg ){
		for (int i=0;i<height;i++){
			for (int j=0; j<width; j++){
				if(trainImg.pixelTable[i][j].content == ' '){
					overall.pixelTable[i][j].space++;
				} else if(trainImg.pixelTable[i][j].content == '+'){
					overall.pixelTable[i][j].plus++;
				} else if(trainImg.pixelTable[i][j].content == '#'){
					overall.pixelTable[i][j].sharp++;
				}
			}
		}
	}
	
	
}
