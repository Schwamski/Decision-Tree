package sol;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset {
    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;
    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param attributeSelection - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection attributeSelection) {
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
        this.selectionType = attributeSelection;
    }
    /*------------------------------------------------------------------------------------------------------------------
    getAttributeToSplitOn()
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * getAttributeToSplitOn() selects the next attribute that will be split upon when generating the decision
     * tree. This method orders the list of attributes into an alphabetical list and then reads the selectoinType
     * of the given data set. If selecting on an ascending alphabetical basis, this method returns the first item of the
     * attribute list. If selecting on a descending alphabetical basis, this method returns the last item of the
     * attribute list. If selecting on a random basis, this method generates a random integer on the range of indices
     * of the list of attributes, and then selects the attribuet at the randomly generated index.
     * @return a string indicating which attribute will next be split on
     */
    public String getAttributeToSplitOn() {
        switch (this.selectionType) {
            case ASCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(0);
            }
            case DESCENDING_ALPHABETICAL -> {
                return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
            }
            case RANDOM -> {
                Random rando = new Random();
                int randomIndex = rando.nextInt(this.attributeList.size());
                return this.attributeList.stream().sorted().toList().get(randomIndex);
            }
        }
        throw new RuntimeException("Non-Exhaustive Switch Case");
    }
    /*------------------------------------------------------------------------------------------------------------------
    Setters/Getters/Size
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * @return attributeList field
     */
    @Override
    public List<String> getAttributeList() {
        List<String> returnList = this.attributeList;
        return returnList;
    }
    /**
     * @return dataObjects field
     */
    @Override
    public List<Row> getDataObjects() {
        return this.dataObjects;
    }
    /**
     * @return selectionType field
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }
    /**
     * @return the number of rows in the dataset
     */
    @Override
    public int size() {
        return this.dataObjects.size();
    }
    /*------------------------------------------------------------------------------------------------------------------
    uniqueAttributeValues
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * uniqueAttributeValues finds all of the unique values in the dataset for a given attribute
     * @param splitAttribute - the attribute we want to use to split the dataset
     * @return a list of strings, each a unique value for the given attribute
     */
    public List<String> uniqueAttributeValues(String splitAttribute) {
        ArrayList output = new ArrayList<>();
        for (Row dataPoint : this.dataObjects) {
            String localValue = dataPoint.getAttributeValue(splitAttribute);
            if (!output.contains(localValue)) {
                output.add(localValue);
            }
        }
        return output;
    }
    /*------------------------------------------------------------------------------------------------------------------
    setSplitter
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * setSplitter creates a list of sub datasets such that each subDataset contains data points with
     * all the same values for the attribute upon which we are splitting
     * @param splitAttribute - the attribute we want to use to split the dataset
     * @return a list of datasets, each a with datapoints of a unique value for the given attribute
     */
    public List<Dataset> setSplitter(String splitAttribute) {
        ArrayList output = new ArrayList<Dataset>();
        List<String> valueList = this.uniqueAttributeValues(splitAttribute);
        List<String> localAttributeList = new ArrayList<String>();
        localAttributeList.addAll(this.attributeList);
        localAttributeList.remove(splitAttribute);
        for (String splitValue : valueList) {
            ArrayList localRows = new ArrayList<Row>();
            for (Row dataPoint : this.dataObjects) {
                String localValue = dataPoint.getAttributeValue(splitAttribute);
                if (localValue.equals(splitValue)) {
                    localRows.add(dataPoint);
                }
            }
            Dataset localSubset = new Dataset(localAttributeList, localRows, this.selectionType);
            output.add(localSubset);
        }
        return output;
    }
    /*------------------------------------------------------------------------------------------------------------------
    defaultValue
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * defaultValue finds the most common value for a given attribute in a dataset
     * @param attribute - the attribute we want to find the most common value for
     * @return a string indicating the most common value for the given attribute
     */
    public String defaultValue(String attribute) {
        List<Dataset> splitSet = this.setSplitter(attribute);
        Integer leadingSize = 0;
        String leadingValue = "defaultValue Error: No Default Value Found";
        Integer localSize = 0;
        for (Dataset localSet : splitSet) {
            localSize = localSet.dataObjects.size();
            if (localSize > leadingSize) {
                leadingSize = localSize;
                leadingValue = localSet.dataObjects.get(0).getAttributeValue(attribute);
            }
        }
        return leadingValue;
    }
}
