package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

// ItemProcessorを実装したクラスでバッチ処理内容を実行する
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	// ログ生成に使用する
	// クラス名をとログを関連付ける
	// クラスリテラルを引数に持たせる
  private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

  @Override
  public Person process(final Person person) {
    final String firstName = person.firstName().toUpperCase();
    final String lastName = person.lastName().toUpperCase();

    final Person transformedPerson = new Person(firstName, lastName);

    log.info("Converting (" + person + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }

}