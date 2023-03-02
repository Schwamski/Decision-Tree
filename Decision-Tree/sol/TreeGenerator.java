package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {
    private ITreeNode root;
    private Dataset trainingData;
    private String targetAttribute;

    /*------------------------------------------------------------------------------------------------------------------
    generateTree
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Generates a decision tree, which provides an ordered way of examining each attribute of an item,
     * leading to a decision for that item.
     * @param startingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     */
    @Override
    public void generateTree(Dataset startingData, String targetAttribute) {
        this.trainingData = new Dataset(startingData.getAttributeList(), startingData.getDataObjects(), startingData.getSelectionType());
        List<String> copyAttributeList = new ArrayList<>(startingData.getAttributeList());
        Dataset setCopy = new Dataset(copyAttributeList, startingData.getDataObjects(), startingData.getSelectionType());
        setCopy.getAttributeList().remove(targetAttribute);
        this.targetAttribute = targetAttribute;
        this.root = this.generateTreeHelper(setCopy);
    }
    /*------------------------------------------------------------------------------------------------------------------
    generateTreeHelper
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * @param localData - the training dataset
     * @return the root TreeNode of the generated decision tree
     */
    public ITreeNode generateTreeHelper(Dataset localData) {
        String defaultValue = localData.defaultValue(this.targetAttribute);
        if (localData.getAttributeList().isEmpty()) {
            return new DecisionLeaf(defaultValue);
        } else {
            String splitAttribute = localData.getAttributeToSplitOn();
            List<ValueEdge> edges = new ArrayList<ValueEdge>();
            List<Dataset> splitSets = localData.setSplitter(splitAttribute);
            //Assign edges for each splitSet
            for (Dataset subset : splitSets) {
                edges.add(new ValueEdge(
                                subset.getDataObjects().get(0).getAttributeValue(splitAttribute),
                                this.generateTreeHelper(subset)));
            }
            return new AttributeNode(splitAttribute, defaultValue, edges);
        }
    }
    /*------------------------------------------------------------------------------------------------------------------
    getDecision
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * getDecision predicts the expected value of the target attribue for a given datum
     * @param datum the datum to look up a decision for
     * @return the expected value of the datum for the target attribute
     */
    @Override
    public String getDecision(Row datum) {
        return this.root.getDecision(datum);
    }
}