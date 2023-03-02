package sol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.Row;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    Dataset fruitTraining;
    Dataset classTraining;
    Dataset mushroomTraining;
    Dataset simpleTraining;
    Dataset villainTraining;

    Dataset mushroomTesting;
    Dataset villainTesting;
    Dataset simpleTesting;

    TreeGenerator fruitTreeGenerator;
    TreeGenerator classLikeTreeGenerator;
    TreeGenerator mushroomTreeGenerator;
    TreeGenerator villainTreeGenerator;
    TreeGenerator simpleTreeGenerator;

    /**
     * Method that generates a data set made up of the information within an input file
     * to be used to help set up for testing
     * @param filePath - the string of the filepath of the dataset CSV
     * @return a data set made up of the information within a specific file
     */
    public Dataset generateDataset(String filePath) {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(filePath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        return new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
    }
    /**
     * Establish data sets and decision trees that will be used for testing
     */
    @Before
    public void setUp() {
        this.fruitTraining = this.generateDataset("data/fruits-and-vegetables.csv");
        this.fruitTreeGenerator = new TreeGenerator();
        this.fruitTreeGenerator.generateTree(this.fruitTraining, "foodType");

        this.classTraining = this.generateDataset("data/classSet.csv");
        this.classLikeTreeGenerator = new TreeGenerator();
        this.classLikeTreeGenerator.generateTree(this.classTraining, "likedClass");

        this.mushroomTraining = this.generateDataset("data/mushrooms/training.csv");
        this.mushroomTreeGenerator = new TreeGenerator();
        this.mushroomTreeGenerator.generateTree(this.mushroomTraining, "isPoisonous");
        this.mushroomTesting = this.generateDataset("data/mushrooms/testing.csv");

        this.villainTraining = this.generateDataset("data/villains/training.csv");
        this.villainTreeGenerator = new TreeGenerator();
        this.villainTreeGenerator.generateTree(this.villainTraining, "isVillain");
        this.villainTesting = this.generateDataset("data/villains/testing.csv");

        this.simpleTraining = this.generateDataset("data/Simple Set for Test CSV - shelfSet.csv");
        this.simpleTreeGenerator = new TreeGenerator();
        this.simpleTreeGenerator.generateTree(this.simpleTraining, "eatsIceCream");
        this.simpleTesting = this.generateDataset("data/Simple Set for Test CSV - shelfSet.csv");
    }
    /**
     * check that size() works on simple and small data sets
     */
    @Test
    public void smallSizeTest() {
        Assert.assertEquals(5, this.classTraining.size());
        Assert.assertEquals(7, this.fruitTraining.size());
    }
    /**
     * check that size() works on large data sets
     */
    @Test
    public void largeSizeTest() {
        Assert.assertEquals(2125, this.mushroomTesting.size());
        Assert.assertEquals(5999, this.mushroomTraining.size());
    }
    /**
     * check getAttributeList() on small and large data sets
     */
    @Test
    public void getAttributeListTest() {
        Assert.assertEquals(4, this.fruitTraining.getAttributeList().size());
        Assert.assertEquals("[highProtein, calories, color, foodType]",
                this.fruitTraining.getAttributeList().toString());
        Assert.assertEquals(4, this.classTraining.getAttributeList().size());
        Assert.assertEquals("[goodProf, likedClass, avgGrade, goodTAs]",
                this.classTraining.getAttributeList().toString());
        Assert.assertEquals(17, this.villainTraining.getAttributeList().size());
        Assert.assertEquals(17, this.villainTesting.getAttributeList().size());
    }
    /**
     * check getSelectionType() on small and large data sets
     */
    @Test
    public void getSelectionTypeTest() {
        Assert.assertEquals("ASCENDING_ALPHABETICAL", this.fruitTraining.getSelectionType().toString());
        Assert.assertEquals("ASCENDING_ALPHABETICAL", this.classTraining.getSelectionType().toString());
    }
    /**
     * check uniqueAttributeValues() works on simple(small) data set tests by examining sizes
     */
    @Test
    public void uniqueAttributeValuesSizeTest() {
        Assert.assertEquals(3, this.classTraining.uniqueAttributeValues("avgGrade").size());
        Assert.assertEquals(2, this.classTraining.uniqueAttributeValues("goodProf").size());
        Assert.assertEquals(2, this.classTraining.uniqueAttributeValues("goodTAs").size());
        Assert.assertEquals(3, this.fruitTraining.uniqueAttributeValues("color").size());
    }
    /**
     * check uniqueAttributeValues() works on simple(small) data set tests
     */
    @Test
    public void uniqueAttributeValuesTest() {
        Assert.assertEquals("[A, B, C]",
                this.classTraining.uniqueAttributeValues("avgGrade").toString());
        Assert.assertEquals("[green, orange, yellow]",
                this.fruitTraining.uniqueAttributeValues("color").toString());
        Assert.assertEquals("[low, high, medium]",
                this.fruitTraining.uniqueAttributeValues("calories").toString());
    }
    /**
     * check setSplitter() works on simple(small) data set tests
     */
    @Test
    public void setSplitterTest() {
        List<Dataset> splitSet = this.classTraining.setSplitter("avgGrade");
        Assert.assertEquals(2, splitSet.get(0).getDataObjects().size());
        Assert.assertEquals(2, splitSet.get(1).getDataObjects().size());
        Assert.assertEquals(1, splitSet.get(2).getDataObjects().size());
        Assert.assertEquals("TRUE", splitSet.get(0).defaultValue("goodTAs"));
        Assert.assertEquals("FALSE", splitSet.get(1).defaultValue("goodTAs"));
        Assert.assertEquals("TRUE", splitSet.get(2).defaultValue("goodTAs"));

        List<Dataset> splitSet2 = this.fruitTraining.setSplitter("color");
        Assert.assertEquals(3, splitSet2.get(0).getDataObjects().size());
        Assert.assertEquals(3, splitSet2.get(1).getDataObjects().size());
        Assert.assertEquals(1, splitSet2.get(2).getDataObjects().size());
        Assert.assertEquals("low", splitSet2.get(0).defaultValue("calories"));
        Assert.assertEquals("high", splitSet2.get(1).defaultValue("calories"));
        Assert.assertEquals("high", splitSet2.get(2).defaultValue("calories"));
    }
    /**
     * check that defaultValue() works on simple(small) data set
     */
    @Test
    public void defaultValueTest() {
        Assert.assertEquals("TRUE", this.classTraining.defaultValue("goodTAs"));
        Assert.assertEquals("A", this.classTraining.defaultValue("avgGrade"));
        Assert.assertEquals("TRUE", this.classTraining.defaultValue("likedClass"));

        Assert.assertEquals("high", this.fruitTraining.defaultValue("calories"));
        Assert.assertEquals("true", this.fruitTraining.defaultValue("highProtein"));
        Assert.assertEquals("vegetable", this.fruitTraining.defaultValue("foodType"));
    }
    /**
     * check that generateTree() and getDecision() works on a simple(small) sata set
     */
    @Test
    public void generateTreeGetDecisionTest() {
        Row tangerine = new Row("test row (tangerine)");
        tangerine.setAttributeValue("color", "orange");
        tangerine.setAttributeValue("highProtein", "false");
        tangerine.setAttributeValue("calories", "high");
        Assert.assertEquals("fruit", this.fruitTreeGenerator.getDecision(tangerine));

        Row pepper = new Row("test row (pepper)");
        pepper.setAttributeValue("color", "yellow");
        pepper.setAttributeValue("highProtein", "false");
        pepper.setAttributeValue("calories", "low");
        Assert.assertEquals("vegetable", this.fruitTreeGenerator.getDecision(pepper));
    }
    /**
     * check that generateTree() and getDecision() works on our own simple data set "classSet"
     */
    @Test
    public void singleNodeDataSetTest() {
        Row johnny = new Row("test row (Johnny)");
        johnny.setAttributeValue("likesIceCream", "TRUE");

        Assert.assertEquals("TRUE", this.simpleTreeGenerator.getDecision(johnny));
    }
    /**
     * check that generateTree() and getDecision() works on our own simple data set "classSet"
     */
    @Test
    public void classSetTest() {
        Row science = new Row("test row (science)");
        science.setAttributeValue("avgGrade", "A");
        science.setAttributeValue("goodProf", "TRUE");
        science.setAttributeValue("goodTAs", "TRUE");
        Assert.assertEquals("TRUE", this.classLikeTreeGenerator.getDecision(science));

        Row math = new Row("test row (math)");
        math.setAttributeValue("avgGrade", "C");
        math.setAttributeValue("goodProf", "TRUE");
        math.setAttributeValue("goodTAs", "TRUE");
        Assert.assertEquals("FALSE", this.classLikeTreeGenerator.getDecision(math));
    }
    /**
     * check that generateTree() and getDecision() works when approaching a value that is not accounted for
     * by utilizing default values
     */
    @Test
    public void classSetUnaccountedForValueTest() {
        Row art = new Row("test row (art)");
        art.setAttributeValue("avgGrade", "F");
        art.setAttributeValue("goodProf", "TRUE");
        art.setAttributeValue("goodTAs", "TRUE");
        Assert.assertEquals("TRUE", this.classLikeTreeGenerator.getDecision(art));

        Row eggplant = new Row("test row (eggplant)");
        eggplant.setAttributeValue("color", "purple");
        eggplant.setAttributeValue("highProtein", "false");
        eggplant.setAttributeValue("calories", "low");
        Assert.assertEquals("vegetable", this.fruitTreeGenerator.getDecision(eggplant));
    }
    /**
     * check getDecision() on the mushrooms training data set
     */
    @Test
    public void testMushroomsTrainingData() {
        Assert.assertEquals("true",
                this.mushroomTreeGenerator.getDecision(this.mushroomTraining.getDataObjects().get(0)));
        Assert.assertEquals("false",
                this.mushroomTreeGenerator.getDecision(this.mushroomTraining.getDataObjects().get(1)));
        Assert.assertEquals("false",
                this.mushroomTreeGenerator.getDecision(this.mushroomTraining.getDataObjects().get(9)));
        Assert.assertEquals("true",
                this.mushroomTreeGenerator.getDecision(this.mushroomTraining.getDataObjects().get(5998)));
    }
    /**
     * check getDecision() on the mushrooms testing data set
     */
    @Test
    public void testMushroomsTestingData() {
        Assert.assertEquals("true",
                this.mushroomTreeGenerator.getDecision(this.mushroomTesting.getDataObjects().get(0)));
        Assert.assertEquals("true",
                this.mushroomTreeGenerator.getDecision(this.mushroomTesting.getDataObjects().get(618)));
        Assert.assertEquals("true",
                this.mushroomTreeGenerator.getDecision(this.mushroomTesting.getDataObjects().get(775)));
        Assert.assertEquals("false",
                this.mushroomTreeGenerator.getDecision(this.mushroomTesting.getDataObjects().get(41)));
        Assert.assertEquals("false",
                this.mushroomTreeGenerator.getDecision(this.mushroomTesting.getDataObjects().get(121)));
    }
    /**
     * check getDecision() on the villains testing data set
     */
    @Test
    public void testVillainsTestingData() {
        Assert.assertEquals("false",
                this.villainTreeGenerator.getDecision(this.villainTesting.getDataObjects().get(0)));
        Assert.assertEquals("false",
                this.villainTreeGenerator.getDecision(this.villainTesting.getDataObjects().get(20)));
        Assert.assertEquals("true",
                this.villainTreeGenerator.getDecision(this.villainTesting.getDataObjects().get(62)));
        Assert.assertEquals("true",
                this.villainTreeGenerator.getDecision(this.villainTesting.getDataObjects().get(60)));
    }
    /**
     * check getDecision() on the villains training data set
     */
    @Test
    public void testVillainsTrainingData() {
        Assert.assertEquals("true",
                this.villainTreeGenerator.getDecision(this.villainTraining.getDataObjects().get(0)));
        Assert.assertEquals("true",
                this.villainTreeGenerator.getDecision(this.villainTraining.getDataObjects().get(11)));
        Assert.assertEquals("false",
                this.villainTreeGenerator.getDecision(this.villainTraining.getDataObjects().get(85)));
    }
}