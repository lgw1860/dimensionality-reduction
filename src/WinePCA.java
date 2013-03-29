import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import shared.DataSet;
import shared.Instance;
import shared.filt.PrincipalComponentAnalysis;
import util.linalg.DenseVector;
import util.linalg.Matrix;

public class WinePCA {
  public static void main(String[] args) throws IOException {
    int numInstances = 178, numAttributes = 13;

        Instance[] instances =  new Instance[numInstances];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/wine.csv")));

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
        PrincipalComponentAnalysis filter = new PrincipalComponentAnalysis(set);
        filter.filter(set);

        Matrix reverse = filter.getProjection().transpose();
        for (int i = 0; i < set.size(); i++) {
            Instance instance = set.get(i);
            instance.setData(reverse.times(instance.getData()).plus(filter.getMean()));
        }

        BufferedWriter out = new BufferedWriter(new FileWriter("data/wine_pca.txt"));
        for(int i = 0; i < set.size(); i++){
          out.write(set.get(i).toString());
          out.write("\n");
        }
        out.flush();
        System.out.println("PCA done");
    }
}
