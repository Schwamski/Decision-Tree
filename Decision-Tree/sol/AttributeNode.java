package sol;

import java.util.List;
import src.ITreeNode;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode {
    private String attributeName;
    private String defaultValue;
    private List<ValueEdge> outgoingEdges;

    /**
     * Constructor for an AttributeNode object
     * @param aN - the name of the attribute split at the node
     * @param dV - the default value for the targetAttribuet at the node
     * @param oE - a list of edges
     */
    public AttributeNode(String aN, String dV, List<ValueEdge> oE) {

        this.attributeName = aN;
        this.defaultValue = dV;
        this.outgoingEdges = oE;
    }
    /*------------------------------------------------------------------------------------------------------------------
    getDecision(Row datum)
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Returns a predicted value for the targetAttribute of the datum using a decision tree
     * rooted at this node
     * @param datum the data row to get a decision for
     * @return the predicted value for the target attribute in the provided datum
     */
    @Override
    public String getDecision(Row datum) {
        for (ValueEdge localEdge : this.outgoingEdges) {
            if (localEdge.getAttributeValue().equals(datum.getAttributeValue(this.attributeName))) {
                return localEdge.getChild().getDecision(datum);
            }
        }
        return this.defaultValue;
    }
}