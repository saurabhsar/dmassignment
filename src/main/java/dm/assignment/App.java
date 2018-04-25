package dm.assignment;

import dm.file.Parser;
import dm.utils.weka.DecisionTree;
import dm.utils.weka.FuzzyClustering;
import dm.utils.weka.HierarchicalClustering;
import dm.utils.weka.KMeans;
import weka.core.Instances;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {

        Instances instances = null;

        if (args[1].equalsIgnoreCase("true")) {
            instances = Parser.processAsLumpsum(Parser.getFileList(args[0]));
            clusterAndPrint(instances);
        } else {
            for (String filename : Parser.getFileList(args[0])) {
                instances = Parser.processAndFetchInstances(filename);
                clusterAndPrint(instances);
            }
        }
    }

    private static void clusterAndPrint(Instances instances) throws Exception {
        HierarchicalClustering hierarchicalClustering = new HierarchicalClustering();
        hierarchicalClustering.train(instances);

        KMeans kMeans = new KMeans();
        kMeans.train(instances);

        DecisionTree decisionTree = new DecisionTree();
        decisionTree.train(instances);

        FuzzyClustering fuzzyClustering = new FuzzyClustering();
        fuzzyClustering.train(instances);

        System.out.println(hierarchicalClustering.getHierarchicalClusterer().toString());
        System.out.println(kMeans.getKMeans().toString());
        System.out.println(decisionTree.getTree().toString());
        System.out.println(fuzzyClustering.getEm().toString());
        System.out.println(fuzzyClustering.getEm().getCapabilities());

    }
}
