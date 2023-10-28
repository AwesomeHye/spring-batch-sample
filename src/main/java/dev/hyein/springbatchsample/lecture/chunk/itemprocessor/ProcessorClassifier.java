package dev.hyein.springbatchsample.lecture.chunk.itemprocessor;

import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {
    @Setter
    private Map<String, ItemProcessor<String, String>> processorMap = new HashMap<>();

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get((String)classifiable);
    }
}
