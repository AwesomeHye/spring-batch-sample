package dev.hyein.springbatchsample.lecture.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class springBatchManunalRunner implements ApplicationRunner {
    private final JobLauncher jobLauncher;
    private final Job job;

    public springBatchManunalRunner(JobLauncher jobLauncher, @Qualifier("faultTolerantRetryJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("RUN BY springBatchManunalRunner");
        JobParameters jobParamters = new JobParametersBuilder()
            .addString("name", "hyein")
            .addLong("seq", 9L)
            .addDate("date", new Date())
            .addDouble("weight", 16.5)
            .toJobParameters();

        jobLauncher.run(job, jobParamters);
    }
}
