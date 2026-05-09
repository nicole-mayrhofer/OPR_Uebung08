import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Predicate;

public class DataProcessorManual {
    Vector<HardDisk> hardDisks;

    public DataProcessorManual(Vector<HardDisk> hardDisks) {
        this.hardDisks = hardDisks;
    }

    // Can use Vector.sort internally, sorts the internal Vector
    public void sort(Comparator<HardDisk> comparator) {
        for (int i = 0; i<hardDisks.size(); i++) {
        hardDisks.sort(comparator);
        }
    }

    // Returns size of the internal vector
    public long count() {
        return hardDisks.size();
    }

    // Returns a Vector of HardDisks filtered by predicate
    public Vector<HardDisk> filter(Predicate<HardDisk> predicate) {
        Vector<HardDisk> filterdDisks = new Vector<HardDisk>();
        for (int i = 0; i < hardDisks.size();i++) {
            if (predicate.test(hardDisks.get(i))) {
                filterdDisks.add(hardDisks.get(i));
            }
        }
        return filterdDisks;
    }

    // Returns the HardDisk with a specified maximum value
    public HardDisk max(Comparator<HardDisk> comparator) {
        if (hardDisks.isEmpty()) return null;
        HardDisk maxHD = hardDisks.get(0);
        for (int i = 1; i < hardDisks.size(); i++) {
            HardDisk hd = hardDisks.get(i);
            if (comparator.compare(hd, maxHD) > 0) {
                maxHD = hd;
            }
        }
        return maxHD;
    }

    // Returns the HardDisk with a specified minimum value
    public HardDisk min(Comparator<HardDisk> comparator) {
        if (hardDisks.isEmpty()) return null;
        HardDisk minHD = hardDisks.get(0);
        for (int i = 1; i < hardDisks.size(); i++) {
            HardDisk hd = hardDisks.get(i);
            if (comparator.compare(hd, minHD) < 0) {
                minHD = hd;
            }
        }
        return minHD;
    }
    // Returns a mean value specified by the function
    public double mean(Function<HardDisk, Long> function) {
        if (hardDisks.isEmpty()) return -1.0;     // default for empty lists

        double sum = 0.0;
        for (HardDisk hd : hardDisks) {
            sum += function.apply(hd);
        }
        return sum / hardDisks.size();
    }

    // Returns a median value specified by the function
    public long median(Comparator<HardDisk> sortingComparator, Function<HardDisk, Long> function) {
        if (hardDisks.isEmpty()) return -1;     // default for empty lists

        hardDisks.sort(sortingComparator);
        long med = 0;
        HardDisk hd = hardDisks.get(hardDisks.size() / 2);

        if (hardDisks.size() % 2 == 0) {                  // ungerade anzahl an elementen
        hd += (hardDisks.get(hardDisks.size()/2) + hardDisks.get(hardDisks.size()/2 - 1));
        }
        return function.apply(hd);
    }
// Counts distinct values based on the given HardDisk-to-String mapping function
    public long countDistinctStrings(Function<HardDisk, String> function) { ... }
    }