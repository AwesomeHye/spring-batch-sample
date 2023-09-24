package dev.hyein.springbatchsample.lecture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Step4Tasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Step4 executed");

        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        Object jobUserName = jobExecutionContext.get("jobUserName");


        if(jobUserName == null) {
            jobExecutionContext.put("jobUserName", "user1");
//            throw new RuntimeException("[step4] jobUserName is null"); // context 에 값 넣어놓고 fail 유발한다. 재실행시 다음스텝이셔 컨텍스트 값을 그대로 들고있는지 확인한다.
        }

        return RepeatStatus.FINISHED;
    }
}
