package dev.hyein.springbatchsample.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChunkJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Primary
    @Bean(name = "chunkJob")
    public Job chunkJob() {
        return jobBuilderFactory.get("chunkJob")
                .start(chunkStep())
                .build();
    }

    @Bean
    @JobScope
    public Step chunkStep() {
        String date = "2023-01-01";
        return stepBuilderFactory.get("chunkStep")
            .<String, String>chunk(1)
            .reader(reader(date))
            .processor(processor(date))
            .writer(writer(date))
            .build();
    }

    @StepScope
    private ItemReader<String> reader(String date) {
        return new ItemReader<String>() {
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                return "READER: ".concat(date);
            }
        };
    }

    @StepScope
    private ItemProcessor<? super String, String> processor(String date) {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String item) throws Exception {
                return "PROCESSOR: ".concat(item);
            }
        };
    }

    @StepScope
    private ItemWriter<? super String> writer(String date) {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                log.info("WRITER: {}", items);
            }
        };
    }
}
