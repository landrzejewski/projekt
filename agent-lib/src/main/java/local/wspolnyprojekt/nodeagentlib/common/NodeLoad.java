package local.wspolnyprojekt.nodeagentlib.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeLoad {
// TODO Docelowo uzgodnić jakie dane potrzebne do loadbalancera
    Long totalMemorySize;
    Long freeMemorySize;
    Long totalSwapSpace;
    Long freeSwapSpace;
    /** Liczba z zakresu 0.0 - 1.0 wskazujące chwilowe obciążenie procesora */
    Double cpuLoad;
    /** Liczba z zakresu 0.0 - 1.0 wskazująca średnie obciążenie procesora
     * w ciągu ostatniej minuty.
     * Nie działa pod Windowsem (zawsze zwraca -1) - może docelowo znajdę obejście */
    Double averageCpuLoad;
    Integer numberOfProcessors;
    /** W bajtach, pokazuje tylko dysk na którym jest skonfigurowane workspace */
    Long totalDiskSpace;
    /** W bajtach, pokazuje tylko przestrzeń dysku na którym jest workspace */
    Long availableDiskSpace;
    Integer totalTasks;
    Integer activeTasts;
}
