import java.util.Scanner;

public class Problem2_01Knapsack {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //gets the number of items
        System.out.print("Enter the number of items: ");
        int numberOfItems = sc.nextInt();

        int[] weights = new int[numberOfItems];
        int[] values = new int[numberOfItems];

        //to get the knapsack capacity and the rest of the item details
        int knapsackCapacity = readInputs(sc, numberOfItems, weights, values);

        //builds the dp table
        int[][]  dp = solveKnapsack(numberOfItems,weights,values,knapsackCapacity);

        //finds out which items are actually placed in the knapsack
        boolean[] selected = selectedItems(dp, numberOfItems, weights, knapsackCapacity);

        //prints the full dp table
        printDPTable(dp, numberOfItems, knapsackCapacity);

        //prints the selected items
        printSelectedItems(weights, values, selected, dp[numberOfItems][knapsackCapacity], knapsackCapacity);

        sc.close();
    }

    //this method gets the details of each item, and then returns with the capacity of the knapsack from the user's input
    private static int readInputs(Scanner sc, int numberOfItems, int[] weights, int[] values) {

        System.out.println("\nEnter details for each item:");
        for (int i = 0; i < numberOfItems; i++) {

            System.out.println("\n--- Item " + (i + 1) + " ---");

            //weight of the item
            System.out.print("Weight: ");
            weights[i] = sc.nextInt();

            //value of the item
            System.out.print("Value: ");
            values[i] = sc.nextInt();
        }

        //capacity of the knapsack
        System.out.print("Knapsack capacity: ");
        return sc.nextInt();
    }

    private static int[][] solveKnapsack(int numberOfItems, int[] weights, int[]values, int knapsackCapacity) {

        //creates the 2d array/dp table
        //+1 for the base case that there are no available items (values set to 0 in the dp table)
        //each dp index points to the total value of the items in the knapsack
        //for example: dp[2][5] points to item 2, with a capacity of 5
        int[][] dp = new int[numberOfItems + 1][knapsackCapacity + 1 ];

        //i represents the rows, which are for each item respectively (i=1 = item 1 etc)
        for (int i = 1; i <= numberOfItems; i++) {

            int currentItemWeight = weights[i-1];
            int currentItemValue = values[i-1];

            //j represents the columns, which are for each capacity
            for (int j=0; j <= knapsackCapacity; j++){

                //check if the current item's weight is less than or equal to the current capacity (means it can fit)
                if (currentItemWeight <= j){

                    //withoutCurrentItem is the total value of the item(s) when excluding the current item (value of the row above it)
                    int withoutCurrentItem = dp[i-1][j];

                    //withCurrentItem is the total value of item(s) when including the current item
                    int withCurrentItem = currentItemValue + dp[i-1][j-currentItemWeight];

                    //compares the values with and without the current item to see if the current item makes a difference
                    //update the value of the current index if it does make a difference (increases the total value)
                    dp[i][j] = Math.max(withoutCurrentItem, withCurrentItem);
                }

                //if the item cant fit in the capacity
                else {
                    //set the current value to the same as the one from the previous row
                    dp[i][j] = dp[i-1][j];
                }
        }

        }

        return dp;

    }

    private static boolean[] selectedItems(int[][] dp, int numberOfItems,int[] weights, int knapsackCapacity){

        boolean[] selected = new boolean[numberOfItems];
        int w = knapsackCapacity;

        //start from the bottom right corner (highest possible value in the knapsack), then try to find which items contributed to that value
        for (int i = numberOfItems; i>0; i--){
            //if the current index is not the same as the row above it, that means the current item i made a difference
            //item i is part of the final solution and marked true
            //otherwise, continue and check the next row above
            if(dp[i][w] != dp[i-1][w]){

                //i-1 because the dp table is indexed from 1, where i=1 is item 1
                //while the boolean array selected is indexed from 0, where i=1 is item 2, and item 1 is supposed to be i=0
                selected[i-1] = true;

                //move to the next possible weight after subtracting the selected item's weight
                w -= weights[i-1];
            }
        }

        return selected;
    }

    // Prints the DP table in a formatted grid
    private static void printDPTable(int[][] dp, int n, int capacity) {
        System.out.println("\n---------------- DP TABLE ----------------");
        System.out.printf("%-6s", "i\\w");
        for (int w = 0; w <= capacity; w++) {
            System.out.printf("%4d", w);
        }
        System.out.println();

        for (int i = 0; i <= n; i++) {
            System.out.printf("%-6d", i);
            for (int w = 0; w <= capacity; w++) {
                System.out.printf("%4d", dp[i][w]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
    }

    // Prints the selected items and summary in a clean table format
    private static void printSelectedItems( int[] weights, int[] values,
                                           boolean[] selected, int maxValue, int capacity) {
        System.out.println("\n================= RESULT =================");
        System.out.printf("%-15s%-10s%-10s%n", "Item", "Weight", "Value");
        System.out.println("--------------------------------------------");

        int totalWeight = 0;
        int totalValue = 0;
        boolean anySelected = false;

        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                System.out.println("Item " + (i+1) + " | Weight: " + weights[i] + " | Value: " + values[i]);
                totalWeight += weights[i];
                totalValue += values[i];
                anySelected = true;
            }
        }

        //if no items are selected for the knapsack then print this
        if (!anySelected) {
            System.out.println("(No items selected)");
        }

        //prints the final details of the knapsack
        System.out.println("--------------------------------------------");
        System.out.println("Knapsack Capacity : " + capacity);
        System.out.println("Total Weight Used : " + totalWeight);
        System.out.println("Total Value       : " + totalValue);
        System.out.println("Max Value (DP)    : " + maxValue);
        System.out.println("=============================================");
    }
}