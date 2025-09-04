package finalproject;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static finalproject.Sorting.fastSort;
import static finalproject.Sorting.slowSort;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SortingTest {
    private HashMap<Integer, Integer> testData;

    @Before
    public void setUp() {
        // Generate test data
        testData = generateTestData(1000); // Generate test data of size 1000
    }

    @Test
    public void testSlowSort() {
        ArrayList<Integer> sortedKeys = slowSort(testData);
        assertTrue(isSortedDescending(sortedKeys));
    }

    @Test
    public void testFastSort() {
        ArrayList<Integer> sortedKeys = fastSort(testData);
        assertTrue(isSortedDescending(sortedKeys));
    }

    // Helper method to generate test data
    private HashMap<Integer, Integer> generateTestData(int size) {
        HashMap<Integer, Integer> data = new HashMap<>();
        for (int i = 0; i < size; i++) {
            data.put(i, i); // Use a deterministic sequence of numbers
        }
        return data;
    }

    // Helper method to check if the list is sorted in descending order
    private boolean isSortedDescending(ArrayList<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) < list.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testSlowSortSpeed() {
        HashMap<Integer, Integer> testData = generateTestData(1000); // Generate test data with 1000 elements

        long startTime = System.nanoTime();
        slowSort(testData);
        long slowSortTime = System.nanoTime() - startTime;
        System.out.println("Time taken for slowSort: " + slowSortTime + " nanoseconds");

        assertTrue("SlowSort took too long", slowSortTime < 100000000); // Adjust the threshold as needed
    }

    @Test
    public void testQuickSortSpeed() {
        HashMap<Integer, Integer> testData = generateTestData(1000); // Generate test data with 1000 elements

        long startTime = System.nanoTime();
        fastSort(testData);
        long quickSortTime = System.nanoTime() - startTime;
        System.out.println("Time taken for quickSort: " + quickSortTime + " nanoseconds");

        assertTrue("QuickSort took too long", quickSortTime < 100000000); // Adjust the threshold as needed
    }

    @Test
    public void testSortPerformanceComparison() {
        HashMap<Integer, Integer> testData = generateTestData(1000); // Generate test data with 1000 elements

        long startTimeSlow = System.nanoTime();
        slowSort(testData);
        long slowSortTime = System.nanoTime() - startTimeSlow;
        System.out.println("Time taken for slowSort: " + slowSortTime + " nanoseconds");

        long startTimeQuick = System.nanoTime();
        fastSort(testData);
        long quickSortTime = System.nanoTime() - startTimeQuick;

        System.out.println("Time taken for quickSort: " + quickSortTime + " nanoseconds");
        if (slowSortTime < quickSortTime) {
            System.out.println("slowSort is faster.");
        } else if (slowSortTime > quickSortTime) {
            System.out.println("quickSort is faster.");
            long speedupFactor = slowSortTime / quickSortTime;
            System.out.println("Quicksort is approximately " + speedupFactor + " times faster than slowsort.");
        } else {
            System.out.println("Both sorting algorithms took the same time.");
        }
    }

    @Test
    public void testSlowSort_NullInput() {
        try {
            slowSort(null); // Try sorting with null input
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testFastSort_NullInput() {
        try {
            fastSort(null); // Try sorting with null input
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

}
