package dev.hyein.springbatchsample.lecture.partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ColumnRangePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> executionContextMap = new HashMap<>();
        for (int i = 1; i <= gridSize; i++) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putInt("minValue", i);
            executionContext.putInt("maxValue", i * 10);

            executionContextMap.put("partition" + i, executionContext);
        }
        return executionContextMap;
    }
}
