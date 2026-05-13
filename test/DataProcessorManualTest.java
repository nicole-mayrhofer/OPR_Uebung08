import at.fhhgb.mc.opr.backblazedata.model.HardDisk;
import at.fhhgb.mc.opr.backblazedata.model.SMARTValue;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorManualTest {

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

    private DataProcessorManual createProcessor() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(1000, false, "A"));
        disks.add(createDisk(2000, true, "B"));
        disks.add(createDisk(3000, false, "A"));
        return new DataProcessorManual(disks);
    }

    @Test
    void testCount() {
        DataProcessorManual dp = createProcessor();
        assertEquals(3, dp.count());
    }

    @Test
    void testFilter() {
        DataProcessorManual dp = createProcessor();

        Vector<HardDisk> failing = dp.filter(hd -> hd.isFailing());

        assertEquals(1, failing.size());
        assertTrue(failing.get(0).isFailing());
    }

    @Test
    void testFilterEmptyResult() {
        DataProcessorManual dp = createProcessor();

        Vector<HardDisk> result = dp.filter(hd -> false);

        assertEquals(0, result.size());
    }

    @Test
    void testMax() {
        DataProcessorManual dp = createProcessor();

        HardDisk max = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(3000, max.getCapacityInBytes());
    }

    @Test
    void testMin() {
        DataProcessorManual dp = createProcessor();

        HardDisk min = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(1000, min.getCapacityInBytes());
    }

    @Test
    void testMean() {
        DataProcessorManual dp = createProcessor();

        double mean = dp.mean(hd -> hd.getCapacityInBytes());

        assertEquals(2000.0, mean);
    }

    @Test
    void testMedianOdd() {
        DataProcessorManual dp = createProcessor();

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

        DataProcessorManual dp = new DataProcessorManual(disks);

        long median = dp.median(
                (a, b) -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                hd -> hd.getCapacityInBytes()
        );

        assertEquals(2500, median);
    }

    @Test
    void testCountDistinctStrings() {
        DataProcessorManual dp = createProcessor();

        long distinct = dp.countDistinctStrings(hd -> hd.getModel());

        assertEquals(2, distinct);
    }

    @Test
    void testCountDistinctStringsWithNull() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(1000, false, null));

        DataProcessorManual dp = new DataProcessorManual(disks);

        long result = dp.countDistinctStrings(hd -> hd.getModel());

        assertEquals(-1, result);
    }

    @Test
    void testSort() {
        DataProcessorManual dp = createProcessor();

        dp.sort((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(1000, dp.hardDisks.get(0).getCapacityInBytes());
        assertEquals(3000, dp.hardDisks.get(2).getCapacityInBytes());
    }

    @Test
    void testEmptyProcessor() {
        DataProcessorManual dp = new DataProcessorManual(new Vector<>());

        assertEquals(0, dp.count());
        assertNull(dp.max((a, b) -> 0));
        assertNull(dp.min((a, b) -> 0));
        assertEquals(-1.0, dp.mean(hd -> 1L));
        assertEquals(-1, dp.median((a, b) -> 0, hd -> 1L));
    }

    @Test
    void testSingleElementMaxMin() {
        Vector<HardDisk> disks = new Vector<>();
        disks.add(createDisk(5000, false, "X"));

        DataProcessorManual dp = new DataProcessorManual(disks);

        HardDisk max = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        HardDisk min = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));

        assertEquals(5000, max.getCapacityInBytes());
        assertEquals(5000, min.getCapacityInBytes());
    }
}