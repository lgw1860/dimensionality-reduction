import java.io.IOException;
import java.util.ArrayList;

import func.KMeansClusterer;
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

public class ProjectDatasetBuilder {
    private String baseDir = "data/";

    /**
     * @param args
     */
    public static void main(String[] args) {
        DataSet iris = null;
        DataSet segmentation = null;
        try {
            iris = (new ArffDataSetReader("data/wine.csv")).read();
            segmentation      = (new ArffDataSetReader("data/spambase.csv")).read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        (new LabelSplitFilter()).filter(iris);
        (new LabelSplitFilter()).filter(segmentation);

        DataSetWorker adw = new DataSetWorker(iris, "abalone");
        adw.run();
        DataSetWorker hdw = new DataSetWorker(segmentation, "hd");
        hdw.run();
    }

    private static class DataSetWorker implements Runnable {
        // static variables
        private static final String reducedDir = "data/reduced/";
        private static final String clustReducedDir = "data/creduced/";

        // the array of DataSets corresponding to the mountain of nnets we need
        // to train
        private DataSet clean;
        ArrayList<Tuple<ReversibleFilter, String>> filters;
        private String setName;
        private final int toKeep = 5;

        public DataSetWorker(DataSet d, String setName) {
            this.setName = setName;
            this.clean = d;
            filters = new ArrayList<Tuple<ReversibleFilter, String>>();
            init();
        }

        public void reduce() {

        }

        public void init() {
            filters.add(new Tuple<ReversibleFilter, String>(
                    new PrincipalComponentAnalysis(clean), "_pca.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new IndependentComponentAnalysis(clean), "_ica.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new RandomizedProjectionFilter(toKeep, clean.get(0).size()),
                    "_insig.csv"));
            filters.add(new Tuple<ReversibleFilter, String>(
                    new InsignificantComponentAnalysis(clean), "_rp.csv"));
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