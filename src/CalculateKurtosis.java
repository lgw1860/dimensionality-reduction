import java.util.*;
import java.io.*;

/**
 * Calculates kurtosis of a distribution of data
 * @author Hannah Lau
 * @version 1.0
 */
public class CalculateKurtosis {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/b.txt")));

            System.out.println("Enter the number of Components in this data set>>");
            int numAttributes = scan.nextInt();
            System.out.println("Enter the number of Instances in this data set>>");
            int numInstances = scan.nextInt();
            double[] data = new double[numInstances];
            for(int r = 0; r<numAttributes; r++)
            {
            	br = new BufferedReader(new FileReader(new File("src/b.txt")));
            	data = new double[numInstances];
	            double mean = 0;
	            for(int i = 0; i < data.length; i++) {
	            	scan = new Scanner(br.readLine());
	            	String line = scan.nextLine();
	            	String[] lineArr = line.split(",");
	                data[i] = Double.parseDouble(lineArr[r]);
	                mean += data[i];
	            }
	
	            mean/= data.length;
	
	            double[] differences = new double[data.length];
	            for(int i = 0; i < differences.length; i++) {
	                differences[i] = data[i] - mean;
	            }
	
	            double numerator = 0, denominator = 0;
	
	            for(double d : differences) {
	                numerator += Math.pow(d, 4);
	                denominator += Math.pow(d, 2);
	            }
	
	            numerator /= data.length;
	            denominator /= data.length;
	            denominator = Math.pow(denominator, 2);
	
	            System.out.println((numerator/denominator - 3));
            }
        }
        catch(Exception e) {
            System.out.println("Enter the data with 1 data point on each line in c.txt.");
            e.printStackTrace();
        }
    }

}
