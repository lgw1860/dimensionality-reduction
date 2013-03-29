import java.io.*;
import java.util.Scanner;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import util.linalg.DenseVector;

public class SpamICA {
	/**
     * The test main
     * @param args ignored
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        int numInstances = 4601, numAttributes = 57;

        Instance[] instances =  new Instance[numInstances];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/spambase.csv")));

            for(int i = 0; i < instances.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                double[] attributes = new double[numAttributes];
                for(int j = 0; j < numAttributes; j++)
                    attributes[j] = Double.parseDouble(scan.next());

                instances[i] = new Instance(new DenseVector(attributes), new Instance(Integer.parseInt(scan.next())));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        DataSet set = new DataSet(instances);

        IndependentComponentAnalysis filter = new IndependentComponentAnalysis(set, 30);
        filter.filter(set);

        System.out.println("After ICA");
        BufferedWriter out = new BufferedWriter(new FileWriter("data/spam_ica.txt"));
        for(int i = 0; i < set.size(); i++){
        	out.write(set.get(i).toString());
        	out.write("\n");
        }
        out.flush();

    }
}
