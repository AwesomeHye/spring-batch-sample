package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.skip;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class SkipItemWriter implements ItemWriter<String> {

    private int cnt;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            if("-12".equals(item)) {
                // skip 가능한 예외
                throw new SkippableException("WRITE failed count : " + cnt);
            } else {
                log.info("SkipItemWriter : {}", item);
            }
        }
    }
}
