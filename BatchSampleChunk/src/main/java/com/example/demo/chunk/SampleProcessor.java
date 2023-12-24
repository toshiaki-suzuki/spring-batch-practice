package com.example.demo.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class SampleProcessor implements ItemProcessor<String,String>{
	// 文字列を受け取って、文字列を返す

	Logger logger = LoggerFactory.getLogger(SampleProcessor.class);
	
	@Override
	public String process(String item) throws Exception{
		
		//文字列の加工(小文字に変換)
		item = item.toLowerCase();
		logger.info("Processor:{}",item);
		return item;
	}
}