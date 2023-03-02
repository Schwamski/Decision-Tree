package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    Dataset training;
    String trainingPath = "data/classSet.csv";
    String targetAttribute = "likedClass";
    TreeGenerator testGenerator;
    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "likedClass"
        this.testGenerator = new TreeGenerator();
        this.testGenerator.generateTree(this.training, this.targetAttribute);
    }
    /**
     * Tests the expected classification of the "science" row is a liked class
     */
    @Test
    public void testClassification() {
        Row science = new Row("test row (science)");
        science.setAttributeValue("avgGrade", "A");
        science.setAttributeValue("goodProf", "TRUE");
        science.setAttributeValue("goodTAs", "TRUE");
        Assert.assertEquals("TRUE", this.testGenerator.getDecision(science));
    }
}