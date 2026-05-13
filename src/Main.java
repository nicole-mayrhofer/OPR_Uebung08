import at.fhhgb.mc.opr.backblazedata.loaders.HardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.loaders.LiveHardDiskDataSource;
import at.fhhgb.mc.opr.backblazedata.model.HardDisk;

import java.util.Vector;

public class Main {

    private static Vector<HardDisk> loadAll(HardDiskDataSource source) {
        Vector<HardDisk> disks = new Vector<>();
        HardDisk disk;
        while ((disk = source.next()) != null) {
            disks.add(disk);
        }
        return disks;
    }

    private static void printHeader(String title) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.printf("║  %-40s  ║%n", title);
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private static double toTB(long bytes) {
        return bytes / 1_000_000_000_000.0;
    }

    private static void printResults(String implName, DataProcessorStreams dp) {
        printHeader(implName);

        System.out.println("Anzahl Festplatten:           " + dp.count());

        long failing = dp.filter(hd -> hd.isFailing()).size();
        System.out.println("Davon fehlerhaft (failing):   " + failing);

        HardDisk largest = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));
        System.out.printf("Größte Festplatte:            %s  (%.2f TB)%n",
                largest.getModel(), toTB(largest.getCapacityInBytes()));

        HardDisk smallest = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));
        System.out.printf("Kleinste Festplatte:          %s  (%.2f TB)%n",
                smallest.getModel(), toTB(smallest.getCapacityInBytes()));

        double meanBytes = dp.mean(hd -> hd.getCapacityInBytes());
        System.out.printf("Ø Festplattengröße (mean):    %.2f TB%n", toTB((long) meanBytes));

        long medianBytes = dp.median(
                (a, b) -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                hd -> hd.getCapacityInBytes()
        );
        System.out.printf("Mittlere Größe (median):      %.2f TB%n", toTB(medianBytes));

        long distinctModels = dp.countDistinctStrings(hd -> hd.getModel());
        System.out.println("Unterschiedliche Modelle:     " + distinctModels);

        double meanSmart = dp.mean(hd -> (long) hd.getSmartValues().size());
        System.out.printf("Ø Anzahl SMART-Werte:         %.2f%n", meanSmart);
    }

    private static void printResults(String implName, DataProcessorManual dp) {
        printHeader(implName);

        System.out.println("Anzahl Festplatten:           " + dp.count());

        long failing = dp.filter(hd -> hd.isFailing()).size();
        System.out.println("Davon fehlerhaft (failing):   " + failing);

        HardDisk largest = dp.max((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));
        System.out.printf("Größte Festplatte:            %s  (%.2f TB)%n",
                largest.getModel(), toTB(largest.getCapacityInBytes()));

        HardDisk smallest = dp.min((a, b) ->
                Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()));
        System.out.printf("Kleinste Festplatte:          %s  (%.2f TB)%n",
                smallest.getModel(), toTB(smallest.getCapacityInBytes()));

        double meanBytes = dp.mean(hd -> hd.getCapacityInBytes());
        System.out.printf("Ø Festplattengröße (mean):    %.2f TB%n", toTB((long) meanBytes));

        long medianBytes = dp.median(
                (a, b) -> Long.compare(a.getCapacityInBytes(), b.getCapacityInBytes()),
                hd -> hd.getCapacityInBytes()
        );
        System.out.printf("Mittlere Größe (median):      %.2f TB%n", toTB(medianBytes));

        long distinctModels = dp.countDistinctStrings(hd -> hd.getModel());
        System.out.println("Unterschiedliche Modelle:     " + distinctModels);

        double meanSmart = dp.mean(hd -> (long) hd.getSmartValues().size());
        System.out.printf("Ø Anzahl SMART-Werte:         %.2f%n", meanSmart);
    }

    public static void main(String[] args) {
        System.out.println("Lade Backblaze-Daten ...");

        Vector<HardDisk> disksForStreams = loadAll(new LiveHardDiskDataSource());
        Vector<HardDisk> disksForManual = loadAll(new LiveHardDiskDataSource());

        System.out.println("Daten geladen. Starte Auswertung ...");

        printResults("DataProcessorStreams", new DataProcessorStreams(disksForStreams));
        printResults("DataProcessorManual", new DataProcessorManual(disksForManual));

    }
}
