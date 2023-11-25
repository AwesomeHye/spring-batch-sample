package dev.hyein.springbatchsample.lecture;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정 클래스
@EnableAutoConfiguration
@EnableBatchProcessing // 스프링 배치 활성화
public class TestBatchConfig {


}
