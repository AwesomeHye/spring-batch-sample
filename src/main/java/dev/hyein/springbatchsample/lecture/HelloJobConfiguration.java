package dev.hyein.springbatchsample.lecture;

import dev.hyein.springbatchsample.lecture.validator.CustomJobParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
@Slf4j
public class HelloJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Step3Tasklet step3Tasklet;
    private final Step4Tasklet step4Tasklet;
    private final Step5Tasklet step5Tasklet;
    private final JobExecutionListener jobExecutionListener;

    @Primary
    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
            .start(step1())
            .next(step2())
            .next(step3())
            .next(step4())
            .next(step5())
            .listener(jobExecutionListener)
            .preventRestart()
            .build();
    }

    @Bean
    public Job helloJob2() {
        return jobBuilderFactory.get("helloJob2")
            .start(flow())
            .next(step2())
            .end()
            .build();
    }

    @Bean("validatorJob")
    public Job validatorJob() {
        return jobBuilderFactory.get("validatorJob")
            .start(step1())
            .next(step2())
            .validator(new DefaultJobParametersValidator(new String[]{"seq"}, new String[]{"name"}))
//            .validator(new CustomJobParameterValidator())
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .tasklet(((contribution, chunkContext) -> {
                JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                log.info("job parameter by contribution: {}", jobParameters.getString("name"));

                Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
                log.info("job parameter by chunkContext: {}", parameters.get("name"));

                System.out.println("Step1 executed!");

//                if(1==1) throw new RuntimeException("step1에서 실패");
                return RepeatStatus.FINISHED;
            }))
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .tasklet(new Step2Tasklet())
            .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
            .tasklet(step3Tasklet)
            .build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
            .tasklet(step4Tasklet)
            .build();
    }

    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
            .tasklet(step5Tasklet)
            .build();
    }

    @Bean
    public Flow flow() {
        // flow 잡 안에 flow 가 실행된다.
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
        return flowBuilder
            .start(step1())
            .next(step2())
            .end(); // end 써줘야힘
    }
}
