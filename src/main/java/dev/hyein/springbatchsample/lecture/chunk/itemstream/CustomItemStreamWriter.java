package dev.hyein.springbatchsample.lecture.chunk.itemstream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.util.List;

public class CustomItemStreamWriter implements ItemStreamWriter<String> {

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("open CustomItemStreamWriter");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("update CustomItemStreamWriter");
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("close CustomItemStreamWriter");
    }

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            System.out.println("item=" + item);
        }
    }
}
