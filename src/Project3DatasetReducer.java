

import java.io.IOException;
import java.util.ArrayList;

import shared.DataSet;
import shared.DataSetWriter;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.InsignificantComponentAnalysis;
import shared.filt.LabelSplitFilter;
import shared.filt.PrincipalComponentAnalysis;
import shared.filt.RandomizedProjectionFilter;
import shared.filt.ReversibleFilter;
import shared.reader.ArffDataSetReader;
import shared.reader.CSVDataSetReader;

public class Project3DatasetReducer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DataSet wine = null;
        DataSet spambase = null;

        try {
            wine = (new CSVDataSetReader("data/wine.csv")).read();
            spambase      = (new CSVDataSetReader("data/spambase.csv")).read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        (new LabelSplitFilter()).filter(wine);

        (new LabelSplitFilter()).filter(spambase);

        // REDUCE DATA
        /*
         * Thread abaThread = new Thread(new DataSetWorker(abalone, "abalone"));
         * Thread hdThread = new Thread(new DataSetWorker(hd, "hd"));
         * abaThread.start(); hdThread.start(); try { abaThread.wait();
         * hdThread.wait(); } catch (InterruptedException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); }
         */
        DataSetWorker adw = new DataSetWorker(wine, "wine");
        adw.run();
        //DataSetWorker hdw = new DataSetWorker(spambase, "spambase");
        //hdw.run();
    }

    private static class DataSetWorker implements Runnable {
        // static variables
        private static final String reducedDir = "data/reducedFinal/";
       // private static final String clustReducedDir = "data/creduced/";

        // the array of DataSets corresponding to the mountain of nnets we need
        // to train
        private DataSet clean;
        ArrayList<Tuple<ReversibleFilter, String>> filters;
        private String setName;
        private final int toKeep = 7;

        public DataSetWorker(DataSet d, String setName) {
            this.setName = setName;
            this.clean = d;
            filters = new ArrayList<Tuple<ReversibleFilter, String>>();
            init();
        }


        public void init() {
            filters.add(new Tuple<ReversibleFilter, String>(
                    new PrincipalComponentAnalysis(clean,toKeep,1E-6), "_pca.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new IndependentComponentAnalysis(clean,toKeep), "_ica.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new RandomizedProjectionFilter(toKeep, clean.get(0).size()),
                    "_rp.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new InsignificantComponentAnalysis(clean,toKeep, Double.MAX_VALUE), "_insigca.csv"));
        }

        public void filter() {
            init();
            for (Tuple<ReversibleFilter, String> tup : filters) {
                ReversibleFilter filter = tup.fst();
                String ext = tup.snd();

                filter.filter(clean);
                DataSetWriter wr = new DataSetWriter(clean, reducedDir
                        + setName + ext);
                try {
                    wr.write();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filter.reverse(clean);
            }
        }

        @Override
        public void run() {
            filter();
        }

    }

    public static class Tuple<X, Y> {
        private final X fst;
        private final Y snd;

        public Tuple(X x, Y y) {
            this.fst = x;
            this.snd = y;
        }

        public X fst() {
            return this.fst;
        }

        public Y snd() {
            return this.snd;
        }
    }

}
