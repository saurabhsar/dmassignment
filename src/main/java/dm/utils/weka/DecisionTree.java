package dm.utils.weka;

import dm.utils.UtilInterface;
import lombok.Data;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;

@Data
public class DecisionTree implements UtilInterface {

    private REPTree tree = new REPTree();

    @Override
    public void train(Instances instances) throws Exception{
        instances.setClassIndex(instances.numAttributes() -1);
        tree.buildClassifier(instances);
    }
}
