import at.fhhgb.mc.opr.backblazedata.model.HardDisk;
import at.fhhgb.mc.opr.backblazedata.model.SMARTValue;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorStreamsTest {

    private HardDisk createDisk(long capacity, boolean failing, String model) {
        return new HardDisk(
                new java.util.Date(),
                "SN" + capacity,
                model,
                capacity,
                failing,
                new LinkedList<SMARTValue>()
        );
    }

    private DataProcessorStreams createProcessor() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(1000, false, "A"));
        disks.add(createDisk(2000, true, "B"));
        disks.add(createDisk(3000, false, "A"));
        return new DataProcessorStreams(disks);
    }

    @Test
    void testCount() {
        DataProcessorStreams dp = createProcessor();
        assertEquals(3, dp.count());
    }

    @Test
    void testFilter() {
        DataProcessorStreams dp = createProcessor();

        Vector<HardDisk> failing = dp.filter(hd -> hd.isFailing());

        assertEquals(1, failing.size());
        assertTrue(failing.get(0).isFailing());
    }

    @Test
    void testFilterEmptyResult() {
        DataProcessorStreams dp = createProcessor();

        Vector<HardDisk> result = dp.filter(hd -> false);

        assertEquals(0, result.size());
    }

    @Test
    void testMax() {
        DataProcessorStreams dp = createProcessor();

        HardDisk max = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(3000, max.getCapacityInBytes());
    }

    @Test
    void testMin() {
        DataProcessorStreams dp = createProcessor();

        HardDisk min = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(1000, min.getCapacityInBytes());
    }

    @Test
    void testMean() {
        DataProcessorStreams dp = createProcessor();

        double mean = dp.mean(hd -> hd.getCapacityInBytes());

        assertEquals(2000.0, mean);
    }

    @Test
    void testMedianOdd() {
        DataProcessorStreams dp = createProcessor();

        long median = dp.median(
                (a, b) -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                hd -> hd.getCapacityInBytes()
        );

        assertEquals(2000, median);
    }

    @Test
    void testMedianEven() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(1000, false, "A"));
        disks.add(createDisk(2000, false, "B"));
        disks.add(createDisk(3000, false, "C"));
        disks.add(createDisk(4000, false, "D"));

        DataProcessorStreams dp = new DataProcessorStreams(disks);

        long median = dp.median(
                (a, b) -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                hd -> hd.getCapacityInBytes()
        );

        assertEquals(2500, median);
    }

    @Test
    void testCountDistinctStrings() {
        DataProcessorStreams dp = createProcessor();

        long distinct = dp.countDistinctStrings(hd -> hd.getModel());

        assertEquals(2, distinct);
    }

    @Test
    void testSort() {
        DataProcessorStreams dp = createProcessor();

        dp.sort((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(1000, dp.hardDisks.get(0).getCapacityInBytes());
        assertEquals(3000, dp.hardDisks.get(2).getCapacityInBytes());
    }

    @Test
    void testEmptyProcessor() {
        DataProcessorStreams dp = new DataProcessorStreams(new Vector<>());

        assertEquals(0, dp.count());
        assertEquals(-1.0, dp.mean(hd -> 1L));
        assertEquals(-1, dp.median((a, b) -> 0, hd -> 1L));
    }

    @Test
    void testMaxThrowsOnEmpty() {
        DataProcessorStreams dp = new DataProcessorStreams(new Vector<>());

        assertThrows(Exception.class, () ->
                dp.max((a, b) -> 0)
        );
    }

    @Test
    void testMinThrowsOnEmpty() {
        DataProcessorStreams dp = new DataProcessorStreams(new Vector<>());

        assertThrows(Exception.class, () ->
                dp.min((a, b) -> 0)
        );
    }

    @Test
    void testSingleElementMaxMin() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(5000, false, "X"));

        DataProcessorStreams dp = new DataProcessorStreams(disks);

        HardDisk max = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        HardDisk min = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(5000, max.getCapacityInBytes());
        assertEquals(5000, min.getCapacityInBytes());
    }
}
