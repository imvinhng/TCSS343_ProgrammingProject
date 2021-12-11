import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TCSS 343 Autumn 2021
 * Homework #4: Programming Assignment
 *
 * The following program will proceed to solve the Subset Sum problem
 * using 3 different approaches: Brute Force, Dynamic Programming, and Clever Algorithm.
 *
 * The description for each aforementioned algorithms will be included below and in a seperate
 * PDF documentationm as well as more information about the solutions, complexity analyses,
 * and result analyses including the graphics.
 *
 * @author vnguye3
 */

public class tcss343 {

    private static final int MAX_NO_OF_ALGORITHM = 3;

    public static void main(String[] args) {
        // increase n until the the runtime is no more than 5 minutes
        Driver(5, 1000, true);         // test v for true/false
        Driver(10, 1000000, false);     // test v for true/false
    }

    /** Brute Force.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     */
    public static void BruteForce(int[] S, int t) {
        int n = S.length;

    }

    /** Dynamic Programming.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     */
    public static void DynamicProgramming(int[] S, int t) {

    }

    /** Clever Algorithm.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     * @return a Map of boolean value (whether the subset is found) and List of subsets found
     */
    public static Map<Boolean, List<ArrayList<Integer>>> CleverAlgorithm(int[] S, int t) {
        boolean exist = true;
        Map<Boolean, List<ArrayList<Integer>>> result = new HashMap<>();
        List<ArrayList<Integer>> listOfSubsets = new ArrayList<>();

        ArrayList<Integer> subset1 = new ArrayList<>();
        subset1.add(2);
        subset1.add(3);
        listOfSubsets.add(subset1);

        ArrayList<Integer> subset2 = new ArrayList<>();

        subset2.add(1);
        subset2.add(5);
        listOfSubsets.add(subset2);



        int n = S.length;
        int n2 = n/2;

       int[] L = new int[n2];
       int[] H = new int[n2];

        for (int i = 0; i < n2; i++) {
            L[i] = S[i];
            H[i] = S[i + n2];
        }

        result.put(exist, listOfSubsets);
        return result;

    }

    // Driver (for Testing)
    public static void Driver(int n, int r, boolean v) {
        final int t = 1000;

        // create a sequence S of n-elements ranging from 1 to r
        // define the range
        int max = r;
        int min = 1;
        int range = max - min + 1;
        int[] S = new int[n];

        // generate n random numbers within 1 to r
        for (int i = 0; i < n; i++) {
            int rand = (int)(Math.random() * range) + min;

            S[i] = rand;
        }

        for (int order = 1; order <= MAX_NO_OF_ALGORITHM; order++) {
            Map<Boolean, List<ArrayList<Integer>>> listOfSubsets = null;

            // start time
            long startTime = System.currentTimeMillis();

            // run test for BF -> DP -> CA
            if (order == 1) {
                BruteForce(S, t);
            } else if (order == 2) {
                DynamicProgramming(S, t);
            } else {
                listOfSubsets = CleverAlgorithm(S, t);
            }

            // end time
            long endTime = System.currentTimeMillis();

            // total time taken
            long duration = endTime - startTime;

            // display the detail report after every algorithm run
            displayDriver(n, r, order, S, duration, listOfSubsets);
        }

    }

    private static void displayDriver(int n, int r, int order, int[] S, long duration, Map<Boolean,  List<ArrayList<Integer>>> listOfSubsets) {
        System.out.println("\n");

        System.out.println("Number of element in sequence S: " + n);
        System.out.println("Max value for elements in sequence S: " + r);

        System.out.print("Sequence S: ");
        for (int i = 0; i < n; i++) {
            System.out.print(S[i] + " ");
        }
        System.out.println();

        System.out.print("Algorithm implemented: ");
        switch (order) {
            case 1:
                System.out.println("Brute Force");
                break;
            case 2:
                System.out.println("Dynamic Programming");
                break;
            case 3:
                System.out.println("Clever Algorithm");
                if (listOfSubsets.containsKey(false)) {
                    System.out.println("No Subset Sum was found from the given Sequence");
                } else {
                    listOfSubsets.values().forEach(subset  -> System.out.println(subset.stream().toList())
                    );

                }
                break;
        }

        System.out.println("Running Time: " + duration + " ms");


    }
}


