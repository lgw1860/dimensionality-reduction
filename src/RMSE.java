import java.util.*;
import java.io.*;

public class RMSE {
	public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            /*System.out.println("Enter name of first text file>>");
            BufferedReader br1 = new BufferedReader(new FileReader(new File(scan.nextLine())));

            System.out.println("Enter name of second text file>>");
            BufferedReader br2 = new BufferedReader(new FileReader(new File(scan.nextLine())));
            */

            BufferedReader br1 = new BufferedReader(new FileReader(new File("src/a.txt")));
            BufferedReader br2 = new BufferedReader(new FileReader(new File("src/b.txt")));

            System.out.println("Enter the number of instances in this data set>>");
            int numInstances = scan.nextInt();

            double total = 0;
            int counter = 0;
            for(int i = 0; i < numInstances; i++) {
                Scanner scan1 = new Scanner(br1.readLine());
                Scanner scan2 = new Scanner(br2.readLine());

                scan1.useDelimiter(",");
                scan2.useDelimiter(",");

                double a, b;

                do {
                    a = Double.parseDouble(scan1.next());
                    b = Double.parseDouble(scan2.next());

                    total += Math.pow(Math.abs(a-b),2);
                    counter++;
                } while(scan1.hasNext() && scan2.hasNext());
            }

            total /= counter;

            System.out.println("Root Mean Squared Error: " + total);
        }
        catch(Exception e) {
            System.out.println("Enter the attribute data from before and after PCA / RA into a.txt and b.txt.");
            e.printStackTrace();
        }

    }
}
