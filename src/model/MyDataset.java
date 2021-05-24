package model;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import util.Category;
import view.Tool;

import java.util.*;

public abstract class MyDataset {
    
    public static final int CITY_INDEX = 3;
    public static final String TOT_ = "TOT_";
    public static final String MED_ = "MED_";
    public static final String SHAPE__ = "Shape__";
    
    private final ArrayList<String> groupIndicators;
    
    /**
     * Here lies the filtered and processed information of the current dataset.
     * <ul>
     * <li> To access the data call getDataset()
     * <li> The categories are grouped based on what they are about (for example
     *   if there are categories like ONE_BED, TWO_BED, THREE_BED, they would
     *   be grouped as NUMBER_OF_BEDS)
     * <li> To access these groups you call their names as a string like this:
     *
     * <ul><code> getDataset().get("NUMBER_OF_BEDS") </code></ul>
     *   From there you get access to the specific categories through indexing
     *   by integers, which then gives a list of the data attached to all the
     *   cities.
     * </ul>
     *
     * <p>
     * <strong> EXAMPLE </strong>:
     *     If you want to find out how many people have one bed (index 1) in
     *     aurora city, you would write
     *  <ul><code> getDataset().get("NUMBER_OF_BEDS").get(1).get("Aurora") </code></ul>
     * @see #getDataset
     */
    private final HashMap<String, ArrayList<Category>> dataset;
    
    public static final HashMap<String, Integer> cityCount = new HashMap<>();
    
    public MyDataset () {
        groupIndicators = new ArrayList<>(Arrays.asList(TOT_, SHAPE__));
        dataset = new HashMap<>();
    }
    
    public ArrayList<String> getGroupIndicators () {
        return groupIndicators;
    }
    
    public HashMap<String, ArrayList<Category>> getDataset () {
        return dataset;
    }
    
    /**
     * Input all the data into the dataset
     * @param rawDataset = the raw dataset
     * @see controller.FileImportController
     */
    public void setDataset (ArrayList<ArrayList<String>> rawDataset) {
        
        indexDataset(rawDataset, getCities(rawDataset));
        
        ArrayList<Integer> groupIndexes = getGroupIndexes(rawDataset);
        ArrayList<String> categoryRow = rawDataset.get(0);
        
        // Put the data into dataset
        for (int row = 1; row<rawDataset.size(); ++row) {
            for (int col = CITY_INDEX+1; col<rawDataset.get(row).size(); ++col) {
                
                if (rawDataset.get(0).get(col).contains(TOT_) || rawDataset.get(0).get(col).contains(MED_)) {
                    continue;
                }
                
                // Find the group the current category is in with binary search
                int groupIndex = groupIndexes.get(binarySearch(groupIndexes, col));
                String groupName = categoryRow.get(groupIndex);
                
                // Decrement to include shape area's first category
                if (groupName.contains(SHAPE__)) {
                    --groupIndex;
                    groupName = SHAPE__.replace("__", "");
                } else if (groupName.contains(TOT_)) {
                    groupName = groupName.replace(TOT_, "");
                } else if (groupName.contains(MED_)) {
                    groupName = categoryRow.get(groupIndex+1);
                }
                
                ArrayList<Category> categories = this.dataset.get(groupName);
                
                // Get the current category and add the data to it
                ArrayList<String> curRow = rawDataset.get(row);
                categories.get(col-groupIndex-1).addToCity(curRow.get(CITY_INDEX),
                        Double.parseDouble(curRow.get(col)));
            }
        }
        
        // Average out all the cities
        for (ArrayList<Category> group : this.dataset.values()) {
            for (Category category : group) {
                for (Map.Entry<String, Double> city : category.getCities().entrySet()) {
                    city.setValue(city.getValue()/cityCount.get(city.getKey()));
                }
            }
        }
        
    }
    
    /**
     * @return all the city naems in the dataset as an array
     */
    public static String[] getCities () {
        String[] cities = new String[cityCount.size()];
        cityCount.keySet().toArray(cities);
        return cities;
    }
    
    /**
     * @param rawDataset = the raw dataset
     * @return all the city names in the dataset
     */
    private HashSet<String> getCities (ArrayList<ArrayList<String>> rawDataset) {
        
        HashSet<String> cities = new HashSet<>();
        
        // Identify the cities that were surveyed and count them
        for (int i = 1; i<rawDataset.size(); ++i) {
            String city = rawDataset.get(i).get(CITY_INDEX);
            cities.add(city);
            cityCount.put(city, cityCount.getOrDefault(city, 0)+1);
        }
        
        return cities;
        
    }
    
    /**
     * @param rawDataset = the raw dataset
     * @return the group indexes in dataset
     */
    private ArrayList<Integer> getGroupIndexes (ArrayList<ArrayList<String>> rawDataset) {
        
        ArrayList<Integer> groupIndexes = new ArrayList<>();
        ArrayList<String> categoryRow = rawDataset.get(0);
        
        // Index all of groups and categories
        for (int i = CITY_INDEX+1; i<categoryRow.size()-1; ++i) {
            
            String category = categoryRow.get(i);
            
            // If there is a group indicator, add it to the groups
            for (String indicator : groupIndicators) {
                if (category.contains(indicator)) {
                    groupIndexes.add(i);
                    break;
                }
            }
            
        }
        
        return groupIndexes;
        
    }
    
    /**
     * Index the categories and city Arraylist and HashMap in dataset
     * @param rawDataset = the raw dataset
     * @param cities     = all the city names in the dataset
     */
    public abstract void indexDataset (ArrayList<ArrayList<String>> rawDataset, HashSet<String> cities);
    
    /**
     * Provide the tools with the valid groups they can use
     * @param tools = the array with all the tools
     */
    public abstract void assignValidGroupCharts (Tool[] tools);
    
    /**
     * @param arr    = Array to search
     * @param target = target to search for
     * @return the index of the starting interval the target is in in arr
     */
    public static int binarySearch (ArrayList<Integer> arr, int target) {
        
        int l, mid, r;
        l = 0;
        r = arr.size()-1;
        while (l<=r) {
            mid = l+(r-l)/2;
            if (arr.get(mid)==target) {
                return mid;
            } else if (arr.get(mid)<target) {
                l = mid+1;
            } else if (target<arr.get(mid)) {
                r = mid-1;
            }
        }
        return l-1;
        
    }
    
}
