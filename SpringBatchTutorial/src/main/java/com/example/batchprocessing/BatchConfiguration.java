package com.example.batchprocessing;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfiguration {
	
	// ファイルからデータを読み込むインスタンスを生成
	@Bean // ファクトリーメソッドにつけるとDIコンテナでインスタンスが管理される
	public FlatFileItemReader<Person> reader() {
	  return new FlatFileItemReaderBuilder<Person>() // FlatFileItemReaderのビルダー
	    .name("personItemReader") // リーダーの名前を設定
	    .resource(new ClassPathResource("sample-data.csv")) // 読み込むファイルのパスを設定
	    .delimited() // 区切り文字を設定
	    .names("firstName", "lastName") // 読み込むファイルの列とフィールドをマッピング
	    .targetType(Person.class) // データを読み込むオブジェクトをマッピング
	    .build(); // インスタンス生成
	}

	// 読み込んだデータに処理を実行するインスタンスを生成
	@Bean
	public PersonItemProcessor processor() {
	  return new PersonItemProcessor();
	}

	// DBにデータを書き込むインスタンスを生成
	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<Person>() // JdbcBatchItemWriterのビルダー
	    .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)") // 実行するSQL
	    .dataSource(dataSource) // DB接続設定
	    .beanMapped() // JavaBeanのプロパティをSQL文にマッピング
	    .build(); // インスタンス生成
	}
	
	@Bean
	public Job importUserJob(
			JobRepository jobRepository, // DBにジョブの状態を保存
			Step step1, // ジョブの最初のステップ
			JobCompletionNotificationListener listener // ジョブの完了時に通知を受け取る
		) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .listener(listener) // ジョブにリスナーを設定
	    .start(step1) // 最初のジョブを設定
	    .build();
	}

	@Bean
	public Step step1(
			JobRepository jobRepository,
			DataSourceTransactionManager transactionManager, // トランザクションを管理
	        FlatFileItemReader<Person> reader, // ファイル読み恋
	        PersonItemProcessor processor, // 変換処理
	        JdbcBatchItemWriter<Person> writer) { // DBへ書き込み
	  return new StepBuilder("step1", jobRepository)
	    .<Person, Person> chunk(3, transactionManager) // トランザクションにつき3つのアイテムを処理
	    .reader(reader)
	    .processor(processor)
	    .writer(writer)
	    .build();
	}
}
