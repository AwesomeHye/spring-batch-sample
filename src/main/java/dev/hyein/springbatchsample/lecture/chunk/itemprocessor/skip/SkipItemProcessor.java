package dev.hyein.springbatchsample.lecture.chunk.itemprocessor.skip;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class SkipItemProcessor implements ItemProcessor<String, String> {

    private int cnt;

    @Override
    public String process(String item) throws Exception {
        if("6".equals(item) || "7".equals(item)) {
            // skip 가능한 예외
            throw new SkippableException("PROCESS failed count : " + cnt);
        } else {
            log.info("SkipItemProcessor : {}", item);
            return String.valueOf(Integer.parseInt(item) * -1);
        }
    }

}
