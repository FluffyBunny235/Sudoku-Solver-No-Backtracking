import java.util.ArrayList;
public class Box {
    private int value;
    private ArrayList<Integer> candidates;

    public Box(int value) {
        this.value = value;
        candidates = new ArrayList<>(10);
        for (int i = 1; i <= 9; i++) {
            candidates.add(i);
        }
    }
    public void eliminate(ArrayList<Integer> list) {
        for (Integer i : list) {
            candidates.remove(i);
        }
    }
    public int getValue() {
        return value;
    }
    public ArrayList<Integer> getCandidates() {
        return candidates;
    }
    public boolean tryToFill() {
        if (candidates.size() == 1 && value == 0) {
            value = candidates.get(0);
            return true;
        }
        return false;
    }
    public void fill(int value) {
        if (this.value == 0) {
            this.value = value;
        }
    }
}
