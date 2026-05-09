import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Predicate;

public class DataProcessorStreams {
    public DataProcessorStreams(Vector<HardDisk> hardDisks) {

    }
    // Can use Vector.sort internally, sorts the internal Vector
    public void sort(Comparator<HardDisk> comparator) {

    }
    // Returns size of the internal vector
    public long count() {

    }
    // Returns a Vector of HardDisks filtered by predicate
    public Vector<HardDisk> filter(Predicate<HardDisk> predicate) {

    }
    // Returns the HardDisk with a specified maximum value
    public HardDisk max(Comparator<HardDisk> comparator) {

    }
    // Returns the HardDisk with a specified minimum value
    public HardDisk min(Comparator<HardDisk> comparator) {

    }
    // Returns a mean value specified by the function
    public double mean(Function<HardDisk, Long> function) {

    }
    // Returns a median value specified by the function
    public long median(Comparator<HardDisk> sortingComparator, Function<HardDisk,
            Long> function) {

    }
// Counts distinct values based on the given HardDisk-to-String mapping
    function
    public long countDistinctStrings(Function<HardDisk, String> function) { ... }
}