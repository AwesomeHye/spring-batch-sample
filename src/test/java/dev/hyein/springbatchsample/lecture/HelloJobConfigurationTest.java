package dev.hyein.springbatchsample.lecture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(classes = {HelloJobConfiguration.class, TestBatchConfig.class})
class HelloJobConfigurationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils; // by SpringBatchTest

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void simpleJob_test() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("name", "user1")
            .addLong("date", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            .toJobParameters();

        // when & then
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
        assertEquals(jobExecution.getExitStatus().getExitCode(), ExitStatus.COMPLETED);

        // when & then 2
        JobExecution jobExecution1 = jobLauncherTestUtils.launchStep("step1");

        StepExecution stepExecution = (StepExecution) ((List) jobExecution1.getStepExecutions()).get(0);
        assertEquals(stepExecution.getCommitCount(), 10);
        assertEquals(stepExecution.getReadCount(), 1000); // chunk size
        assertEquals(stepExecution.getWriteCount(), 1000);
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("delete from customer");
    }
}
