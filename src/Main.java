import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static Box[][] arr = new Box[9][9];
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 1 row at a time or enter all at once");
        System.out.println("Use 0 for empty squares");
        for (int i = 0; i < 9; i++) {
            boolean breaking = false;
            String s = input.nextLine();
            if (i == 0 && s.length() == 81){
                for (int k = 0; k < 81; k++) {
                    int x = Integer.parseInt(s.substring(k,k+1));
                    if (x <= 9 && x >= 0) {
                        arr[k/9][k%9] = new Box(x);
                    }
                    else {
                        System.out.println("Restart and enter a valid number");
                        break;
                    }
                }
                break;
            }
            /* TESTING
            if (i == 0 && s.length() == 1) {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col ++ ){
                        arr[row][col] = new Box(0);
                        if (row%3 == 0) {
                            arr[row][col].remove(7);
                        }
                    }
                }
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        for (int num = 1; num <= 9; num++) {
                            System.out.println(boxCount(row, col, num));
                        }
                    }
                }
                System.exit(0);
            }
             */
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
        Instant start = Instant.now();
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
            elimAll();
            filled = filled || checkAllForSingles();
            elimAll();
            filled = filled || checkForPairs();
            filled = filled || onlyOneZone();
            if (filled) {print();}
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Here is your final array after " + counter + " iterations and " + timeElapsed + "ms.");
        print();
        /* Testing
        while(true) {
            int index = input.nextInt();
            if (index == -2) {
                break;
            }
            if (index == -1) {
                print();
                continue;
            }
            System.out.println(arr[index/10][index%10].getCandidates());
        }
         */
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
        elimAll();
        for (int i = 0; i < 9; i++) {
            Box[] boxes = new Box[9];
            for (int k = 0; k < 9; k++) {
                boxes[k] = arr[i][k];
            }
            boolean find = onlyOne(boxes);
            if (find) {retVal = true;}
        }
        elimAll();
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
        elimAll();
        return retVal;
    }
    public static boolean checkForPairs() {
        //for each row
        //for each box
        //for each other box
        //for each pair of numbers that the boxes have in common
        //if there are only two in that row of each number
        //eliminate all other candidates
        boolean eliminatedCandidate = false;
        for (int row = 0; row < 9; row++) {
            for (int box = 0; box < 8; box++) {
                for (int box2 = box+1; box2 < 9; box2++) {
                    ArrayList<Integer> isolatedPairs = new ArrayList<>(10);
                    for (int check = 1; check < 10; check++) {
                        if (arr[row][box].getCandidates().contains(check) && arr[row][box2].getCandidates().contains(check)) {
                            if (rowCount(row, check) == 2) {
                                isolatedPairs.add(check);
                            }
                        }
                    }
                    if (isolatedPairs.size() == 2) {
                        eliminatedCandidate = eliminatedCandidate || arr[row][box].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                        eliminatedCandidate = eliminatedCandidate || arr[row][box2].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                    }
                }
            }
        }
        for (int col = 0; col < 9; col++) {
            for (int box = 0; box < 8; box++) {
                for (int box2 = box+1; box2 < 9; box2++) {
                    ArrayList<Integer> isolatedPairs = new ArrayList<>(10);
                    for (int check = 1; check < 10; check++) {
                        if (arr[box][col].getCandidates().contains(check) && arr[box2][col].getCandidates().contains(check)) {
                            if (colCount(col, check) == 2) {
                                isolatedPairs.add(check);
                            }
                        }
                    }
                    if (isolatedPairs.size() == 2) {
                        eliminatedCandidate = eliminatedCandidate || arr[box][col].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                        eliminatedCandidate = eliminatedCandidate || arr[box2][col].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                    }
                }
            }
        }
        for (int bRow = 0; bRow < 3; bRow++) {
            for (int bCol = 0; bCol < 3; bCol++) {
                for (int row1 = 0; row1 < 3; row1++) {
                    for (int col1 = 0; col1 < 3; col1++) {
                        for (int row2 = 0; row2 < 3; row2++) {
                            for (int col2 = 0; col2 < 3; col2++) {
                                if (row2 == row1 && col2 == col1) {continue;}
                                ArrayList<Integer> isolatedPairs = new ArrayList<>(10);
                                for (int check = 1; check < 10; check++) {
                                    if (arr[bRow*3+row1][bCol*3+col1].getCandidates().contains(check) && arr[bRow*3+row2][bCol*3+col2].getCandidates().contains(check)) {
                                        if (boxCount(bRow, bCol, check) == 2) {
                                            isolatedPairs.add(check);
                                        }
                                    }
                                }
                                if (isolatedPairs.size() == 2) {
                                    eliminatedCandidate = eliminatedCandidate || arr[bRow*3+row1][bCol*3+col1].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                                    eliminatedCandidate = eliminatedCandidate || arr[bRow*3+row2][bCol*3+col2].onlyKeep(isolatedPairs.get(0), isolatedPairs.get(1));
                                }
                            }
                        }
                    }
                }
            }
        }
        return eliminatedCandidate;
    }
    public static int rowCount(int row, int num) {
        int counter = 0;
        for (Box box : arr[row]) {
            if (box.getCandidates().contains(num)) {counter++;}
        }
        return counter;
    }
    public static int colCount(int col, int num) {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            if (arr[i][col].getCandidates().contains(num)) {counter++;}
        }
        return counter;
    }
    public static int boxCount(int row, int col, int num) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if (arr[row*3 + i][col*3+k].getCandidates().contains(num)) {
                    counter++;
                }
            }
        }
        return counter;
    }
    public static boolean onlyOneZone() {
        boolean elim = false;
        for (int bRow = 0; bRow < 3; bRow++) {
            for (int bCol = 0; bCol < 3; bCol++) {
                for (int row = 0; row < 3; row++) {
                    int[] counter = new int[9];
                    for (int col = 0; col < 3; col++) {
                        for (int c : arr[bRow*3+row][bCol*3+col].getCandidates()) {
                            counter[c-1]++;
                        }
                    }
                    for (int i = 0; i < 9; i++) {
                        if (counter[i] == boxCount(bRow, bCol, i+1) && counter[i] > 0) {
                            for (int col = 0; col < 9; col++) {
                                if (col/3 == bCol) {continue;}
                                elim = elim || arr[bRow*3+row][col].remove(i+1);
                            }
                        }
                    }
                }
                for (int col = 0; col < 3; col++) {
                    int[] counter = new int[9];
                    for (int row = 0; row < 3; row++) {
                        for (int c : arr[bRow*3+row][bCol*3+col].getCandidates()) {
                            counter[c-1]++;
                        }
                    }
                    for (int i = 0; i < 9; i++) {
                        if (counter[i] == boxCount(bRow, bCol, i+1) && counter[i] > 0) {
                            for (int row = 0; row < 9; row++) {
                                if (row/3 == bRow) {continue;}
                                elim = elim || arr[row][bCol*3+col].remove(i+1);
                            }
                        }
                    }
                }
            }
        }
        return elim;
    }
}
// if 2 boxes have only 2 candidates that are both the same, then those candidates cannot appear anywhere else in the zone
// try "070630000000000100003800560040005690006000001000900007200004000000000009007500380"
// or try "009800402000304069054096000000000070170000020000127695020509007001780940007003000"