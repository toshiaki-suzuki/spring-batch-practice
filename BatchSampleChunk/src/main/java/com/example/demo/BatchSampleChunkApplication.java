package com.example.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing // Job,Step,Bean定義などを自動で設定してくれるアノテーション
public class BatchSampleChunkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchSampleChunkApplication.class, args);
	}
}