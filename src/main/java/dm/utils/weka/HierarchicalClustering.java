package dm.utils.weka;

import dm.utils.UtilInterface;
import lombok.Data;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instances;

@Data
public class HierarchicalClustering implements UtilInterface {
    private HierarchicalClusterer hierarchicalClusterer = new HierarchicalClusterer();

    public void train(Instances instances) throws Exception {
        hierarchicalClusterer.buildClusterer(instances);
    }
}
