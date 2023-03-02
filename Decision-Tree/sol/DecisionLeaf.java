package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {
    String leafValue;

    /**
     * Constructor for creating a DecisionLeaf
     * @param lV the leafValue to be stored in the leaf
     */
    public DecisionLeaf(String lV) {
        this.leafValue = lV;
    }
    /*------------------------------------------------------------------------------------------------------------------
    getDecision()
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * getDecision for a decisionLeaf, which is the base case
     * for the getDecision method
     * @param forDatum the datum to look up a decision for
     * @return the value stored in the leaf
     */
    @Override
    public String getDecision(Row forDatum) {
        return this.leafValue;
    }
}
