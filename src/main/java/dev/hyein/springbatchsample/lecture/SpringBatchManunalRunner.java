package dev.hyein.springbatchsample.lecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class SpringBatchManunalRunner implements ApplicationRunner {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        JobParameters jobParamters = new JobParametersBuilder()
//            .addString("name", "hyein")
//            .addLong("seq", 6L)
//            .addDate("date", new Date())
//            .addDouble("weight", 16.5)
//            .toJobParameters();
//
//        jobLauncher.run(job, jobParamters);
    }
}
