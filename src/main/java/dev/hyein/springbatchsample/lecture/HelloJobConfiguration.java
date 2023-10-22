package dev.hyein.springbatchsample.lecture;

import dev.hyein.springbatchsample.lecture.chunk.CustomItemProcessor;
import dev.hyein.springbatchsample.lecture.chunk.CustomItemWriter;
import dev.hyein.springbatchsample.lecture.chunk.Customer;
import dev.hyein.springbatchsample.lecture.chunk.itemReader.CustomerFieldSetMapper;
import dev.hyein.springbatchsample.lecture.chunk.itemReader.DefaultLineMapper;
import dev.hyein.springbatchsample.lecture.chunk.itemstream.CustomItemStreamReader;
import dev.hyein.springbatchsample.lecture.chunk.itemstream.CustomItemStreamWriter;
import dev.hyein.springbatchsample.lecture.decider.CutomDecider;
import dev.hyein.springbatchsample.lecture.chunk.CustomItemReader;
import dev.hyein.springbatchsample.lecture.tasklet.Step2Tasklet;
import dev.hyein.springbatchsample.lecture.tasklet.Step3Tasklet;
import dev.hyein.springbatchsample.lecture.tasklet.Step4Tasklet;
import dev.hyein.springbatchsample.lecture.tasklet.Step5Tasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            .next(step2(null))
            .next(step3())
            .next(step4())
            .next(step5())
            .listener(jobExecutionListener)
//            .preventRestart()
            .incrementer(new RunIdIncrementer())
//            .incrementer(new CustomJobParametersIncrementer())
            .build();
    }

    @Bean
    public Job helloJob2() {
        return jobBuilderFactory.get("helloJob2")
            .start(flow())
            .next(step2(null))
            .end() // simpleJob 이 아닌 FlowJob 으로 실행됨
            .build();
    }

    @Bean("validatorJob")
    public Job validatorJob() {
        return jobBuilderFactory.get("validatorJob")
            .start(step1())
            .next(step2(null))
            .validator(new DefaultJobParametersValidator(new String[]{"seq"}, new String[]{"name"}))
//            .validator(new CustomJobParameterValidator())
            .build();
    }

    @Bean("parentJob")
    public Job parentJob() {
        return jobBuilderFactory.get("parentJob")
            .start(jobStep(null))
            .next(step2(null))
            .build();
    }

    @Bean
    public Step jobStep(JobLauncher jobLauncher) {
        return stepBuilderFactory.get("jobStep")
            .job(childJob())
            .launcher(jobLauncher)
            .parametersExtractor(getJobParametersExtractor())
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    stepExecution.getExecutionContext().put("name", "hyein"); // child job 에서 사용하게 job parameter 넣어줌
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    return null;
                }
            })
            .build();
    }

    @Bean
    public Job childJob() {
        return jobBuilderFactory.get("childJob")
            .start(step1())
            .build();
    }

    @Bean
    public Job flowStepJob() {
        return jobBuilderFactory.get("flowStepJob")
            .start(flowStep())
            .next(step3())
            .build();
    }

    @Bean
    public Step flowStep() {
        return stepBuilderFactory.get("flowStep")
            .flow(flow())
            .build();
    }

    private DefaultJobParametersExtractor getJobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }

    @Bean("flowJob")
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
            .start(step1())
            .on("FAILED")
            .to(step2(null))
            .on("PASS").stop()
            .from(step1())
            .on("*")
            .to(step3())
            .next(step4())
            .from(step2(null))
            .on("*")
            .to(step5())
            .end()
            .build();
    }

    @Bean("deciderJob")
    public Job deciderJob() {
        return jobBuilderFactory.get("deciderJob")
            .start(step1())
            .next(decider()) // JobExecutionDecider 선언
            .from(decider()).on("ODD").to(step2(null))
            .from(decider()).on("EVEN").to(step3())
            .end()
            .build();
    }

    @Bean("chunkJob")
    public Job chunkJob() {
        return jobBuilderFactory.get("chunkJob")
            .start(step6())
            .build();
    }

    @Bean("chunkJob2")
    public Job chunkJob2() {
        return jobBuilderFactory.get("chunkJob2")
            .start(step7())
            .build();
    }

    @Bean("flatFileJob")
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
            .start(step8())
            .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new CutomDecider();
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
//                contribution.setExitStatus(ExitStatus.FAILED);
                return RepeatStatus.FINISHED;
            }))
            .build();
    }

    @Bean
    @JobScope
    public Step step2(@Value("#{jobParameters['message']}") String message) {
        log.info("message from jobScope: {}", message);
        return stepBuilderFactory.get("step2")
            .tasklet(getTasklet2(null))
//            .listener(new CustomStatusListener())
            .build();
    }

    @Bean
    @StepScope
    public Step2Tasklet getTasklet2(@Value("#{jobParameters['stepMessage']}") String stepMessage) {
        log.info("message from StepScope: {}", stepMessage);
        return new Step2Tasklet();
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
            .startLimit(10)
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step step5() {
        return stepBuilderFactory.get("chunkStep5")
            .<String, String> chunk(3) // 데이터 10개 단위로 자름
            .reader(new ListItemReader<>(IntStream.rangeClosed(1, 5).boxed().map(Object::toString).collect(Collectors.toList())))
            .processor((ItemProcessor<? super String, ? extends String>) item -> item + " processed")
            .writer(items -> {
                for (String item : items) {
                    log.info("chunk item: {}", item);
                }
            })
            .build();
    }

    @Bean
    public Step step6() {
        return stepBuilderFactory.get("chunkStep6")
            .<Customer, Customer> chunk(3)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
    }

    @Bean
    public CustomItemReader itemReader() {
        return new CustomItemReader(new ArrayList<>(Arrays.asList(new Customer("user1"), new Customer("user2"), new Customer("user3"))));
    }

    @Bean
    public CustomItemProcessor itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public CustomItemWriter itemWriter() {
        return new CustomItemWriter();
    }

    @Bean
    public Step step7() {
        return stepBuilderFactory.get("chunkStep7")
            .<String, String> chunk(3)
            .reader(customItemStreamReader())
            .writer(customItemStreamWriter())
            .build();
    }

    @Bean
    public CustomItemStreamReader customItemStreamReader() {
        ArrayList<String> items = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }

    @Bean
    public CustomItemStreamWriter customItemStreamWriter() {
        return new CustomItemStreamWriter();
    }

    @Bean
    public Flow flow() {
        // flow 잡 안에 flow 가 실행된다.
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
        return flowBuilder
            .start(step1())
            .next(step2(null))
            .end(); // end 써줘야 Flow 객체 생성됨
    }



    @Bean
    public Step step8() {
        return stepBuilderFactory.get("chunkStep8")
            .<Customer, Customer> chunk(5)

            .reader(jdbcCursorItemReader())
//            .reader(jsonItemReader())
//            .reader(xmlItemReader())
//            .reader(csvItemReader(false))
//            .reader(csvItemReader(true))
            .writer((items) -> {
                for (Customer item : items) {
                    log.info("Customer: {}", item.toString());
                }
            })
            .build();
    }


    public ItemReader<Customer> csvItemReader(boolean isDelimited) {
        if(isDelimited) {
            // 구분자
            return new FlatFileItemReaderBuilder<Customer>()
                .name("flatFile") // FlatFileItemReader 이름
                .resource(new ClassPathResource("/customer.csv")) // 읽을 파일 위치
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>()) // CustomerFieldSetMapper 도 구현할 필요 없음
                .targetType(Customer.class) // CustomerFieldSetMapper 도 구현할 필요 없음
                .linesToSkip(1)
                .delimited().delimiter(",") // DelimitedLineTokenizer. 구분자 방식 토크나이징
                .names("name", "age", "year") // 이름으로 fieldSet 접근할 수 있음
                .build();
            // DefaultLineMapper 는 구현할 필요 없다. 스프링 배치에서 같은 이름으로 제공.
        } else {
            // 고정 길이
            return new FlatFileItemReaderBuilder<Customer>()
                .name("flatFile")
                .resource(new FileSystemResource("/Users/hiseo/IdeaProjects/AwesomeHye/spring-batch-sample/src/main/resources/customer.csv"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(Customer.class)
                .linesToSkip(1)
                .fixedLength()
                .strict(false) // 토큰화 검증 안 함. year 자리수 달라도 마지막까지 잘 들고 온다.
                .addColumns(new Range(1, 5))
                .addColumns(new Range(7, 8))
                .addColumns(new Range(10, 13))
                .names("name", "age", "year")
                .build();
        }

        // 빌더 말고 직접 선언
//        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new ClassPathResource("/customer.csv"));
//
//        DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();
//        defaultLineMapper.setLineTokenizer(new DelimitedLineTokenizer());
//        defaultLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
//
//        itemReader.setLineMapper(defaultLineMapper);
//        itemReader.setLinesToSkip(1);
    }

    @Bean
    public ItemReader<Customer> xmlItemReader() {
        return new StaxEventItemReaderBuilder<Customer>()
            .name("xmlItemReader")
            .resource(new ClassPathResource("/customer.xml"))
            .addFragmentRootElements("customer") // 단위 기준이 될 태그
            .unmarshaller(itemMarshaller()) // xml 을 객체로 바인딩
            .build()
            ;
    }

    public Unmarshaller itemMarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("customer", Customer.class); // 처음은 루트 element 가 되야함
        aliases.put("id", Long.class);
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);

        return xStreamMarshaller;
    }


    @Bean
    public ItemReader<Customer> jsonItemReader() {
        return new JsonItemReaderBuilder<Customer>()
            .name("jsonItemReader")
            .jsonObjectReader(new JacksonJsonObjectReader<>(Customer.class))
            .resource(new ClassPathResource("/customer.json"))
            .build();
    }

    public ItemReader<Customer> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder()
            .name("jdbcCursorItemReader")
            .fetchSize(5)
            .beanRowMapper(Customer.class)
            .sql()
    }

    public ItemReader<Customer> jdbcPagingItemReader() {
    }

    public ItemReader<Customer> jpaCursorItemReader() {
    }

    public ItemReader<Customer> jpaPagingItemReader() {
    }

}
