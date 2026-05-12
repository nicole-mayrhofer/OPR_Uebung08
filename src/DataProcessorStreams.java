import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Comparator;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataProcessorStreams {
    Vector<HardDisk> hardDisks;

    public DataProcessorStreams(Vector<HardDisk> hardDisks) {
        this.hardDisks = hardDisks;
    }

    // Can use Vector.sort internally, sorts the internal Vector
    public Stream<HardDisk> sort(Comparator<HardDisk> comparator) {
        hardDisks.sort(comparator);
        return hardDisks.stream();
    }

    // Returns size of the internal vector
    public long count() {
        return hardDisks.stream().count();
    }

    // Returns a Vector of HardDisks filtered by predicate
    public Vector<HardDisk> filter(Predicate<HardDisk> predicate) {
        return new Vector<>(
                hardDisks.stream()
                        .filter(predicate)
                        .toList()
        );
    }

    // Returns the HardDisk with a specified maximum value
    public HardDisk max(Comparator<HardDisk> comparator) {
        return hardDisks.stream().max(comparator).orElseThrow();
    }

    // Returns the HardDisk with a specified minimum value
    public HardDisk min(Comparator<HardDisk> comparator) {
        return hardDisks.stream().min(comparator).orElseThrow();
    }

    // Returns a mean value specified by the function
    public double mean(Function<HardDisk, Long> function) {
        if (hardDisks.isEmpty()) return -1.0;     // default for empty lists
        return hardDisks.stream()
                .mapToLong(function::apply)
                .average()
                .orElse(-1.0);
    }

    // Returns a median value specified by the function
    public long median(Comparator<HardDisk> sortingComparator, Function<HardDisk, Long> function) {
        Vector<HardDisk> sorted = hardDisks.stream().sorted(sortingComparator).collect(Collectors.toCollection(Vector::new));

        int size = sorted.size();
        if (size == 0) return -1;
        int mid = size / 2;

        if (size % 2 == 1) {
            return function.apply(sorted.get(mid));
        } else {
            long v1 = function.apply(sorted.get(mid - 1));
            long v2 = function.apply(sorted.get(mid));
            return (v1 + v2) / 2;
        }
    }

    // Counts distinct values based on the given HardDisk-to-String mapping
    public long countDistinctStrings(Function<HardDisk, String> function) {
        return hardDisks.stream().map(function).distinct().count();
    }
}


