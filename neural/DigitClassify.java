package neural;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DigitClassify {
	final static int width = 28;
	final static int height = 28;
	final static int total = 5000; // total training data
	// Image[] trainingImgArr = new Image[total];

	static int[] trainingLabelArr = new int[total];
	static int[] testLabelArr = new int[1000];
	public static int[][] classify = new int[10][10];
	public static double[][] confusion = new double[10][10];
	static int[] freq = new int [10]; // frequency of each label
	public static int correct = 0;
	public static int correctTest = 0;
	
	static ArrayList<Image> trainingImgArr = new ArrayList<Image>();// array of all training img
	static ArrayList<Image> testingImgArr = new ArrayList<Image>();// array of all testing img
	
	public static int epoch = 100;
	public static double[][][] weight = new double [10][28][28];	
	public static double [] bias = new double[10];
	
	public static void main(String[] args) throws IOException {
		loadTrainingLabel();
		loadTrainingImg();
		loadTestingLabel();
		loadTestingImg();
		
		initParam();
		for (int i=0; i<epoch; i++){
		//	System.out.println("Epoch: "+i);
		//	System.out.println("..................................................");
			
			train();
			countAlpha++;
		//	System.out.println("Alpha: "+getAlpha());
			
		}
		test();
		print();
	
	}

	private static void print() {
		System.out.println("Overall accuracy on test: "+(double)correctTest/1000);
		
		//confusion matrix
		for (int i=0; i<testLabelArr.length;i++){
			int index = testLabelArr[i];
			freq[index]++;
		}
		
		for (int i=0; i<10; i++){
			for (int j=0; j<10;j++){
				confusion[i][j] = (double) classify[i][j]/freq[i];
			//	System.out.print(classify[i][j]+" ");
				System.out.print(String.format( "%.2f", confusion[i][j])+" ");
			}
			System.out.println();
		}
	}

	public static int countAlpha = 1;
	public static double alpha=0.0;
	public static double getAlpha(){
		if (countAlpha<epoch){
			alpha = (double)1000/(1000+countAlpha);
			return alpha;
		}
		System.out.println("Exceed epoch");
		return -1;
	}
	
	public static void loadTrainingLabel() throws IOException {
		String fileName = "src/traininglabels.txt";

		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader(fr);

		String line;
		int i = 0;
		while ((line = buffer.readLine()) != null) {
			trainingLabelArr[i] = Integer.parseInt(line);
			i++;
		}
		fr.close();
	}

	public static void loadTestingLabel() throws IOException {
		String fileName = "src/testLabels.txt";

		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader(fr);

		String line;
		int i = 0;
		while ((line = buffer.readLine()) != null) {
			testLabelArr[i] = Integer.parseInt(line);
			i++;
		}
		fr.close();
	}
	
	public static void loadTrainingImg() throws IOException {
		String fileName = "src/trainingimages.txt";

		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader(fr);
		Image tempImg = new Image();
		String line = buffer.readLine();
		int i = 0;
		int labelCount = 0;
		while (line != null) {
			char[] vals = line.toCharArray();

			for (int col = 0; col < width; col++) {
				Pixel tempPix = new Pixel(i,col,vals[col]);
				if (vals[col] == ' '){
					tempPix.feature = 0; //background
				} else {
					tempPix.feature = 1; //foreground
				}
				
				tempImg.setPixel(i, col, tempPix); 
			}
			line = buffer.readLine();
			i++;
			if(i%28==0&&i!=0){
				tempImg.setLabel(trainingLabelArr[labelCount]);
				labelCount++;
				trainingImgArr.add(tempImg);
				tempImg = new Image();
	        	i=0;
	        }
		}
		fr.close();
	}



	public static void loadTestingImg() throws IOException {
		String fileName = "src/testimages.txt";

		FileReader fr = new FileReader(fileName);
		BufferedReader buffer = new BufferedReader(fr);
		Image tempImg = new Image();
		String line = buffer.readLine();
		int i = 0;
		int labelCount = 0;
		while (line != null) {
			char[] vals = line.toCharArray();

			for (int col = 0; col < width; col++) {
				Pixel tempPix = new Pixel(i,col,vals[col]);
				if (vals[col] == ' '){
					tempPix.feature = 0; //background
				} else {
					tempPix.feature = 1; //foreground
				}
				tempImg.setPixel(i, col, tempPix); 
			}
			line = buffer.readLine();
			i++;
			if(i%28==0&&i!=0){
				tempImg.setLabel(testLabelArr[labelCount]);
				labelCount++;
				testingImgArr.add(tempImg);
				tempImg = new Image();
	        	i=0;
	        }
		}

		fr.close();
	}
	
	public static void initParam(){
		for (int i=0;i<10;i++){
			bias[i] = 0.0;
		}
		
		// init weight
		for (int i=0;i<10;i++){
			for (int j=0; j<28; j++){
				for (int k=0; k<28; k++){
					weight[i][j][k] = 0.0;
			//		weight[i][j][k] = Math.random()*10;
				//	System.out.println("Weight "+i+" "+j+" "+k+" "+weight[i][j][k]);
				}
			}
		}
	}
	
	public static void train() {
		double sum = 0.0;
		correct = 0;
		
		for (int count=0; count<trainingImgArr.size(); count++){
			for (int i=0;i<10;i++){
				for (int j=0; j<28; j++){
					for (int k=0; k<28; k++){
						sum = (double) weight[i][j][k]*trainingImgArr.get(count).pixelTable[j][k].feature;
						trainingImgArr.get(count).wx[i] += sum;
					}
				}
				//sigmoid fx
			//	trainingImgArr.get(count).likehood[i] = (double) 1/(1+Math.exp(trainingImgArr.get(count).wx[i]));
				trainingImgArr.get(count).likehood[i] = trainingImgArr.get(count).wx[i] ;
			
			}
			double max = trainingImgArr.get(count).likehood[0];
			int maxLabel = 0;
			for (int i=0; i<10; i++){
				//System.out.println("Likelihood of :"+i+" "+trainingImgArr.get(count).likehood[i]);
				if(max<trainingImgArr.get(count).likehood[i]){
					max = trainingImgArr.get(count).likehood[i];
					maxLabel = i;
				}
			}
			
			if(maxLabel == trainingImgArr.get(count).label){
	//			System.out.println("Correct label: "+maxLabel);
				correct++;
			} else{
	//			System.out.println("Incorrect predict: "+trainingImgArr.get(count).label+" to be "+maxLabel);
					double alpha = getAlpha();
					
					for (int j=0; j<28; j++){
						for (int k=0; k<28; k++){
							weight[trainingImgArr.get(count).label][j][k]+=alpha*trainingImgArr.get(count).pixelTable[j][k].feature;
							weight[maxLabel][j][k]-=alpha*trainingImgArr.get(count).pixelTable[j][k].feature;
						}
					}
			}
			
			if(count==trainingImgArr.size()-1){
			//	System.out.println("Learning curve: "+correct);
				System.out.println("Learning curve: "+(double)correct/5000);
			}
		}
	}
	
	public static void test(){
		double sum = 0.0;
		
		for (int count=0; count<testingImgArr.size(); count++){
			for (int i=0;i<10;i++){
				for (int j=0; j<28; j++){
					for (int k=0; k<28; k++){
						sum = (double) weight[i][j][k]*testingImgArr.get(count).pixelTable[j][k].feature;
						testingImgArr.get(count).wx[i] += sum;
					}
				}
				//sigmoid fx
			//	testingImgArr.get(count).likehood[i] = 1/(1+Math.exp(testingImgArr.get(count).wx[i]));
				testingImgArr.get(count).likehood[i] = testingImgArr.get(count).wx[i];
			}
			double max = testingImgArr.get(count).likehood[0];
			int maxLabel = 0;
			for (int i=0; i<10; i++){
				//System.out.println("Likelihood of :"+i+" "+testingImgArr.get(count).likehood[i]);
				if(max<testingImgArr.get(count).likehood[i]){
					max = testingImgArr.get(count).likehood[i];
					maxLabel = i;
				}
			}
			
			if(maxLabel == testingImgArr.get(count).label){
		//		System.out.println("Correct label: "+testingImgArr.get(count).label);
				classify[testingImgArr.get(count).label][maxLabel]++;
				correctTest++;
			} else{
		//		System.out.println("Incorrect predict: "+testingImgArr.get(count).label+" to be "+maxLabel);
//					no learning of weight
				classify[testingImgArr.get(count).label][maxLabel]++;
			}
		}
	}
	

}
