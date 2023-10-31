package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class RetryItemProcessor implements ItemProcessor<String, String> {
    private int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        if(item.equals("2") || item.equals("3")){
            cnt++;
            log.info("RetryItemProcessor : {}", item);
            throw new RetryableException("RetryException 발생");
        }
        return item;
    }
}
