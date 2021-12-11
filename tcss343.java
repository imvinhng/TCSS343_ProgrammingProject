import java.util.*;


/**
 * TCSS 343 Autumn 2021
 * Homework #4: Programming Assignment
 *
 * The following program will proceed to solve the Subset Sum problem
 * using 3 different approaches: Brute Force, Dynamic Programming, and Clever Algorithm.
 *
 * The description for each aforementioned algorithms will be included below and in a seperate
 * PDF documentationm as well as more information about the solutions, complexity analyses,
 * and output analyses including the graphics.
 *
 * @author vnguye3
 */

public class tcss343 {

    private static final int MAX_NO_OF_ALGORITHM = 3;

    public static void main(String[] args) {
        // increase n until the the runtime is no more than 5 minutes
        Driver(5, 1000, true);         // test v for true/false
        Driver(5, 1000000, false);     // test v for true/false
    }

    /** Brute Force.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     */
    public static  Map<Boolean, List<ArrayList<Integer>>> BruteForce(int[] S, int t) {

        Map<Boolean, List<ArrayList<Integer>>> output = new HashMap<>();
        return output;

    }

    /** Dynamic Programming.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     */
    public static  Map<Boolean, List<ArrayList<Integer>>> DynamicProgramming(int[] S, int t) {

        Map<Boolean, List<ArrayList<Integer>>> output = new HashMap<>();
        List<ArrayList<Integer>> listOfSubsets = new ArrayList<>();

        // sort the sequence S in ascending order
        int[] sequenceSorted = S.clone();
        Arrays.sort(sequenceSorted);


        // Generating a 2D decision matrix
        final int MAX_COLS = t;
        final int MAX_ROWS = sequenceSorted.length;
        boolean T[][] = new boolean[MAX_ROWS + 1][MAX_COLS + 1];

        // the first column (t = 0) is always true
        for (int i = 0; i <= sequenceSorted.length; i++) {
            T[i][0] = true;
        }

        // fill in data for the rest of the matrix
        for (int i = 1; i <= MAX_ROWS; i++) {
            for (int j = 1; j <= MAX_COLS; j++) {
                if (j - sequenceSorted[i - 1] >= 0) {
                    T[i][j] = T[i - 1][j] || T[i - 1][j - sequenceSorted[i - 1]];
                } else {
                    T[i][j] = T[i-1][j];
                }
            }
        }

//        print2DBooleanMatrix(T);
//        printArray(S);
//        printArray(sequenceSorted);

        // if there exist a subset
        if (T[MAX_ROWS][MAX_COLS]) {
            ArrayList<Integer> mEntries = new ArrayList<>();

            // Retrieve the subset
            int currCol = MAX_COLS;
            int currRow = MAX_ROWS;
            boolean endReach = false;

            // trace back to the first row with true value
            while (!T[currRow][currCol]) {
                currRow--;
            }

            while (sumOfArrayList(mEntries) != t && !endReach) {

                if (currCol <= 0 || currRow <= 0) {
                    // if we reach col 0, exit loop
                    endReach = true;
                } else if (T[currRow-1][currCol]) {
                    // if the row above is true, continue trace back
                    currRow--;

                } else {
                    // if the row above is false, add the current element to the subset list
                    int element = sequenceSorted[--currRow];
                    mEntries.add(element);

                    currCol -= element;

                }
            }

            // Add the correct entries into the subset list
            listOfSubsets.add(mEntries);
        }

        output.put(T[MAX_ROWS][MAX_COLS], listOfSubsets);
        return output;

    }

    private static void printArray(int[] sequence) {
        for (int x : sequence) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    private static void print2DBooleanMatrix(boolean[][] matrix) {
       for (boolean[] col : matrix) {
           for (boolean row : col) {
               System.out.print(row + " ");
           }
           System.out.println();
        }

    }

    /** Clever Algorithm.
     *
     * @param S a list S[0 . . . n − 1] of n positive integers,
     * @param t a target integer t ≥ 0
     * @return a Map of boolean value (whether the subset is found) and List of subsets found
     */
    public static Map<Boolean, List<ArrayList<Integer>>> CleverAlgorithm(int[] S, int t) {

        int n = S.length;
        int n2 = (int) Math.floor(n >> 1);

        Map<Boolean, List<ArrayList<Integer>>> output;
        List<ArrayList<Integer>> listOfSubsets = new ArrayList<>();

        // Step 1 : Split into 2
        ArrayList<Integer> subset_L = new ArrayList<>();
        ArrayList<Integer> subset_H = new ArrayList<>();
        for (int i = 0; i < n2; i++) {
            subset_L.add(S[i]);                 // L = {0 . . . ⌊n/2⌋}
            subset_H.add(S[i + n2]);            // H = {⌊n/2⌋ + 1 . . . n − 1}.
            if (i + n2 == n - 2 && n % 2 == 1) {
                subset_H.add(S[i + n2 + 1]);    // adding the last element from S to subset_H
            }
        }

//        System.out.println("Subset L: " + subset_L.stream().toList());
//        System.out.println("Subset H: " + subset_H.stream().toList());

        // Step 2: Create table T of all subsets of L whose sum does not exceed t
        // if there exist a subset I (in table T) such that sum(I) = t, return TRUE and I, and stop
        List<ArrayList<Integer>> table_T = new ArrayList<>();
        generateTable(table_T, subset_L, t);

//        System.out.println("Table T: " + table_T.stream().toList());

        output = verifyTable(table_T, listOfSubsets, t);
        if (output.containsKey(true)) { return output; }


        // Step 3: Create table W of all subsets of H whose sum does not exceed t
        // if there exist a subset J (in table H) such that sum(J) = t, return TRUE and J, and stop
        List<ArrayList<Integer>> table_W = new ArrayList<>();
        generateTable(table_W, subset_H, t);

//        System.out.println("Table W: " + table_W.stream().toList());

        output = verifyTable(table_W, listOfSubsets, t);
        if (output.containsKey(true)) { return output; }

        // Step 4: Sort table W in ascending order
        sortTableAscendingByWeight(table_W);
//        System.out.println("Sorted Table W: " + table_W.stream().toList());

        // Step 5: For each entry I in table T, if I u J such that sum (I u J) = t, return TRUE and (I u J), and stop
        List<ArrayList<Integer>> table_F = new ArrayList<>();
        ArrayList<Integer> mEntry;


        for (ArrayList<Integer> entry : table_T) {
            for (ArrayList<Integer> entry2 : table_W) {
                if (sumOf2ArrayList(entry, entry2) <= t) {
                    mEntry = new ArrayList<>();
                    mEntry.addAll(entry);
                    mEntry.addAll(entry2);
                    table_F.add(mEntry);
                }
            }
        }

        output = verifyTable(table_F, listOfSubsets, t);
        if (output.containsKey(true)) {
            return output;
        }

        // Step 6: If no subsets I and J yield equality, return FALSE and an empty set, and stop
        output.put(false, listOfSubsets);
        return output;

    }

    private static int sumOf2ArrayList(ArrayList<Integer> entry, ArrayList<Integer> entry2) {
        return sumOfArrayList(entry) + sumOfArrayList(entry2);
    }

    /**\
     * This method finds out whether there exists a subset ,within the given subset list, with sum equals t.
     * @param mTable
     * @param mSubsetList
     * @param target
     * @return
     */
    private static Map<Boolean, List<ArrayList<Integer>>> verifyTable(List<ArrayList<Integer>> mTable, List<ArrayList<Integer>> mSubsetList, int target) {
        Map<Boolean, List<ArrayList<Integer>>> output = new HashMap<>();
        if (mTable.size() > 0) {
            for (ArrayList<Integer> entry : mTable) {
                if (sumOfArrayList(entry) == target) {
                    mSubsetList.add(entry);

                    output.put(true, mSubsetList);
                    return output;
                }
            }

        }

        output.put(false, mSubsetList);
        return output;
    }

    /**
     * This method helps generate a table of all subsets with sum less than t.
     * @param mTable
     * @param mSubset
     * @param target
     */
    private static void generateTable(List<ArrayList<Integer>> mTable, ArrayList<Integer> mSubset, int target) {
        ArrayList<Integer> mEntry = null;
        Stack<Integer> elementStack = new Stack<>();

        for (int i = 0; i < mSubset.size(); i++) {

            // need to be rewritten
//            // add the element to the table if it is less than t
//            if (element < target) {
//                mEntry = new ArrayList<>();
//                mEntry.add(element);
//                mTable.add(mEntry);
//            }
//
//            // evaluate the subsequence elements, and add to the table if it's less than t
//            int offset = 1;
//            while(i + offset < mSubset.size()) {
//                int nextElement = mSubset.get(i + offset);
//                if (element + nextElement < target) {
//                    mEntry = new ArrayList<>();
//                    mEntry.add(element);
//                    mEntry.add(nextElement);
//                    mTable.add(mEntry);
//                }
//                offset++;
//
//            }

            // new code here
            int offset = 1;

            elementStack.add(mSubset.get(i));
            while (!elementStack.empty()) {
                if (sumOfStack(elementStack) <= target) {
                    mEntry = new ArrayList<>();
                    mEntry.addAll(elementStack);
                    mTable.add(mEntry);

                } else {
                    elementStack.pop();
                }

                // add the next element
                if (i+offset < mSubset.size()) {
                    elementStack.add(mSubset.get(i + offset++));
                } else {            // reach the end of the list
                    elementStack.removeAllElements();
                }
            }
        }
    }

    private static void sortTableAscendingByWeight(List<ArrayList<Integer>> table) {

        table.sort(new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> al1, ArrayList<Integer> al2) {
                return sumOfArrayList(al1) - sumOfArrayList(al2);
            }

        });
    }

    private static int sumOfArrayList(ArrayList<Integer> i) {
        int sum = 0;
        for (int x : i) {
            sum += x;
        }
        return sum;
    }

    private static int sumOfStack(Stack<Integer> i) {
        int sum = 0;
        for (int x : i) {
            sum += x;
        }
        return sum;
    }



    // Driver (for Testing)
    public static void Driver(int n, int r, boolean v) {


        // create a sequence S of n-elements ranging from 1 to r
        // define the range
        int max = r;
        int min = 1;
        int t = 0;                      // will be instantiated later
        int range = max - min + 1;
        int[] S = new int[n];

        // generate n random numbers within 1 to r
        for (int i = 0; i < n; i++) {
            int rand = (int)(Math.random() * range) + min;

            S[i] = rand;
        }

        // calculate target t based off v-value
        if (v) {
            // define the range
            max = n-1;
            min = 0;
            range = max - min + 1;

            // generate a random number between 1 and n/2
            int rand_n = (int)(Math.random() * max/2) + 1;

            // generate 5 random numbers within min to max
            for (int i = 0; i < rand_n; i++) {
                int rand_i = (int)(Math.random() * range) + min;

                t += S[rand_i];
            }



        } else {
            // find the sum larger than the total of S
            t = Arrays.stream(S).sum() + max;
        }

//        for (int order = 1; order <= MAX_NO_OF_ALGORITHM; order++) {
            for (int order = 2; order <= MAX_NO_OF_ALGORITHM; order++) {
            Map<Boolean, List<ArrayList<Integer>>> listOfSubsets = null;

            // start time
            long startTime = System.currentTimeMillis();

            // run test for BF -> DP -> CA
            if (order == 1) {
                listOfSubsets = BruteForce(S, t);
            } else if (order == 2) {
                listOfSubsets = DynamicProgramming(S, t);
            } else {
                listOfSubsets = CleverAlgorithm(S, t);
            }

            // end time
            long endTime = System.currentTimeMillis();

            // total time taken
            long duration = endTime - startTime;

            // display the detail report after every algorithm run
            displayDriver(n, r, t, order, S, duration, listOfSubsets);
        }

    }

    private static void displayDriver(int n, int r, int t,  int order, int[] S, long duration, Map<Boolean,  List<ArrayList<Integer>>> listOfSubsets) {
        System.out.println("\n");

        System.out.println("Number of element in sequence S: " + n);
        System.out.println("Max value for elements in sequence S: " + r);
        System.out.println("Target Sum for the Subsets: " + t);

        System.out.print("Sequence S: ");
        for (int i = 0; i < n; i++) {
            System.out.print(S[i] + " ");
        }
        System.out.println();

        System.out.print("Algorithm implemented: ");
        switch (order) {
            case 1:
                System.out.println("Brute Force");
                if (listOfSubsets.containsKey(false)) {
                    System.out.println("No Subset Sum was found from the given Sequence");
                } else {
                    printSubset(listOfSubsets);

                }
                break;
            case 2:
                System.out.println("Dynamic Programming");
                if (listOfSubsets.containsKey(false)) {
                    System.out.println("No Subset Sum was found from the given Sequence");
                } else {
                    printSubset(listOfSubsets);
                }
                break;
            case 3:
                System.out.println("Clever Algorithm");
                if (listOfSubsets.containsKey(false)) {
                    System.out.println("No Subset Sum was found from the given Sequence");
                } else {
                    printSubset(listOfSubsets);

                }
                break;
        }

        System.out.println("Running Time: " + duration + " ms");
    }

    private static void printSubset(Map<Boolean, List<ArrayList<Integer>>> listOfSubsets) {
        System.out.print("Subset found: ");
        listOfSubsets.values().forEach(subset  -> System.out.println(subset.stream().toList()));
    }


}


