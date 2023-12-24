package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component // DIコンテナで管理
public class JobCompletionNotificationListener implements JobExecutionListener {

  // ロガー生成
  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
  // 
  private final JdbcTemplate jdbcTemplate;

  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override // ジョブ実行完了後に呼び出される
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) { // ジョブのステータスをチェック
      log.info("!!! JOB FINISHED! Time to verify the results");

      jdbcTemplate
          .query(
        		  "SELECT first_name, last_name FROM people", // SQL実行
        		  new DataClassRowMapper<>(Person.class) // クエリの実行結果をPersonに読み込み
        		 )
          .forEach(person -> log.info("Found <{{}}> in the database.", person));  // 実行結果を出力
    }
  }
}