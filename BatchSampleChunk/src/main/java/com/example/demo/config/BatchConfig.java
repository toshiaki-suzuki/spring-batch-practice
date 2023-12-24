package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfig {

//	//JobBuilderのFactoryクラス
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//	
//	//StepBuilderのFactoryクラス
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemReader<String> reader;
	@Autowired
	private ItemProcessor<String, String> processor;
	@Autowired
	private ItemWriter<String> writer;
	
	// ChunkのStepを生成
	@Bean
	public Step chunkStep(
			JobRepository jobRepository,
			DataSourceTransactionManager transactionManager, // トランザクションを管理
			ItemReader<String> reader,
			ItemProcessor<String, String> processor,
			ItemWriter<String> writer
		) {
		return new StepBuilder("SampleChunkStep", jobRepository)//Builderの取得
			.<String, String> chunk(3, transactionManager) //チャンクの設定
			.reader(reader) 			//readerセット
			.processor(processor) 		//processorセット
			.writer(writer) 			//writerセット
			.build(); 				//Stepの生成
	}
	
	// Jobを生成
	@Bean
	public Job chunkJob(JobRepository jobRepository, Step step) throws Exception {
		return new JobBuilder("SampleChunkJob", jobRepository)	//Builderの取得
			.incrementer(new RunIdIncrementer())	//IDのインクリメント
			.start(step)				//最初のStep
			.build();					//Jobの生成
	}
}