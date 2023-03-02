package sol;

import src.ITreeNode;

/**
 * A class that represents the edge of an attribute node in the decision tree
 */
public class ValueEdge {
    private String attributeValue;
    private ITreeNode child;

    /**
     * Constructor for an AttributeNode object
     * @param aV - the value of the edge
     * @param ch - the child node of the edge
     */
    public ValueEdge(String aV, ITreeNode ch){
        this.attributeValue = aV;
        this.child = ch;
    }
    /*------------------------------------------------------------------------------------------------------------------
    getters
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * getter method for attribuetValue
     * @return attributeValue field
     */
    public String getAttributeValue() {
        return this.attributeValue;
    }
    /**
     * getter method for child
     * @return child field
     */
    public ITreeNode getChild() {
        return this.child;
    }
}
