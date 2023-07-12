package dev.hyein.springbatchsample.jobs;

import dev.hyein.springbatchsample.tasklets.TutorialTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class TaskletJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "tutJob")
    public Job tutJob() {
        return jobBuilderFactory.get("tutJob")
                .start(tutStep())
                .build();
    }

    @Bean
    public Step tutStep() {
        return stepBuilderFactory.get("tutStep")
                .tasklet(new TutorialTasklet())
                .build();
    }
}
