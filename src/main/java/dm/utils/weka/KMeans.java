package dm.utils.weka;

import dm.utils.UtilInterface;
import lombok.Data;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

@Data
public class KMeans implements UtilInterface {

    private SimpleKMeans kMeans = new SimpleKMeans();


    @Override
    public void train(Instances instances) throws Exception {
        kMeans.buildClusterer(instances);
    }
}
