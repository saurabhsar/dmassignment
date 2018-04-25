package dm.utils.weka;

import dm.utils.UtilInterface;
import lombok.Data;
import weka.clusterers.EM;
import weka.core.Instances;

@Data
public class FuzzyClustering implements UtilInterface {

    private EM em = new EM();

    @Override
    public void train(Instances instances) throws Exception {
        em.setOptions(new String[]{"-do-not-check-capabilities"});
        em.buildClusterer(instances);
    }
}
