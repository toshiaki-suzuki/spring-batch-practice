package com.example.demo.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class SampleWriter implements ItemWriter<String>{

	Logger logger = LoggerFactory.getLogger(SampleProcessor.class);
	
//	@Override
//	public void write(List<? extends String> items) throws Exception{
//		logger.info("writer:{}",items);
//		logger.info("=========");
//	}

	@Override
	public void write(Chunk<? extends String> items) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		logger.info("writer:{}",items);
		logger.info("=========");
	}
}