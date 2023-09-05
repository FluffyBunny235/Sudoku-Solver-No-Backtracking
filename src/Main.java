import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static Box[][] arr = new Box[9][9];
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 1 row at a time");
        System.out.println("Use 0 for empty squares");
        for (int i = 0; i < 9; i++) {
            boolean breaking = false;
            String s = input.nextLine();
            if (s.length() != 9) {
                i--;
                System.out.println("Enter a valid number");
                continue;
            }
            for (int k = 0; k < 9; k++) {
                int x = Integer.parseInt(s.substring(k,k+1));
                if (x <= 9 && x >= 0) {
                    arr[i][k] = new Box(x);
                }
                else {
                    i--;
                    System.out.println("Enter a valid number");
                    breaking = true;
                    break;
                }
            }
            if (!breaking) {
                System.out.println("Next Line");
            }
        }
        System.out.println("Thanks, here is your array:");
        print();
        boolean filled = true;
        int counter = 0;
        while (filled) {
            counter++;
            elimAll();
            filled = false;
            for (int i = 0; i < 9; i++) {
                for (int k = 0; k < 9; k++) {
                    boolean f = arr[i][k].tryToFill();
                    if (f) {
                        filled = true;
                    }
                }
            }
            boolean c = checkAllForSingles();
            if (c) {filled = c;}
            if (filled) {print();}
        }
        System.out.println("Here is your final array after " + counter + " interations.");
        print();
    }
    public static void print(){
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                System.out.print(arr[i][k].getValue()+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void elimAll() {
        elimRows();
        elimCols();
        elimSquares();
    }
    public static void elimRows() {
        ArrayList<Integer> elims = new ArrayList<>(2);
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                if (arr[i][k].getValue() != 0) {
                    elims.add(arr[i][k].getValue());
                }
            }
            for (int k = 0; k < 9; k++) {
                arr[i][k].eliminate(elims);
            }
            elims.clear();
        }
    }
    public static void elimCols() {
        ArrayList<Integer> elims = new ArrayList<>(2);
        for (int k = 0; k < 9; k++) {
            for (int i = 0; i < 9; i++) {
                if (arr[i][k].getValue() != 0) {
                    elims.add(arr[i][k].getValue());
                }
            }
            for (int i = 0; i < 9; i++) {
                arr[i][k].eliminate(elims);
            }
            elims.clear();
        }
    }
    public static void elimSquares() {
        ArrayList<Integer> elims = new ArrayList<>(2);
        for (int bRow = 0; bRow < 3; bRow++) {
            for (int bCol = 0; bCol < 3; bCol++) {
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        if (arr[bRow*3+row][bCol*3+col].getValue() != 0) {
                            elims.add(arr[bRow*3+row][bCol*3+col].getValue());
                        }
                    }
                }
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        arr[bRow*3+row][bCol*3+col].eliminate(elims);
                    }
                }
                elims.clear();
            }
        }
    }
    public static boolean onlyOne(Box[] boxes) {
        int[] counter = new int[9];
        for (Box box : boxes) {
            ArrayList<Integer> can = box.getCandidates();
            if (can.contains(1)) {counter[0]++;}
            if (can.contains(2)) {counter[1]++;}
            if (can.contains(3)) {counter[2]++;}
            if (can.contains(4)) {counter[3]++;}
            if (can.contains(5)) {counter[4]++;}
            if (can.contains(6)) {counter[5]++;}
            if (can.contains(7)) {counter[6]++;}
            if (can.contains(8)) {counter[7]++;}
            if (can.contains(9)) {counter[8]++;}
        }
        boolean didWeFindIt = false;
        for (int i = 0; i < 9; i++) {
            if (counter[i] == 1) {
                didWeFindIt = true;
                for (Box box : boxes) {
                    if (box.getCandidates().contains(i+1)) {
                        box.fill(i+1);
                        break;
                    }
                }
            }
        }
        return didWeFindIt;
    }
    public static boolean checkAllForSingles() {
        boolean retVal = false;
        for (int k = 0; k < 9; k++) {
            Box[] boxes = new Box[9];
            for (int i = 0; i < 9; i++) {
                boxes[i] = arr[i][k];
            }
            boolean find = onlyOne(boxes);
            if (find) {retVal = true;}
        }
        for (int i = 0; i < 9; i++) {
            Box[] boxes = new Box[9];
            for (int k = 0; k < 9; k++) {
                boxes[k] = arr[i][k];
            }
            boolean find = onlyOne(boxes);
            if (find) {retVal = true;}
        }
        for (int bRow = 0; bRow < 3; bRow++) {
            for (int bCol = 0; bCol < 3; bCol++) {
                Box[] boxes = new Box[9];
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        boxes[row*3+col] = arr[bRow*3+row][bCol*3+col];
                    }
                }
                boolean find = onlyOne(boxes);
                if (find) {retVal = true;}
            }
        }
        return retVal;
    }
}