package com.tuxpuckpepe.batchexample;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.tuxpuckpepe.batchexample.listener.JobListener;
import com.tuxpuckpepe.batchexample.model.Dog;
import com.tuxpuckpepe.batchexample.processor.DogProcessor;

/**
 * 
 * @author pepe
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	public FlatFileItemReader<Dog> reader(){
		return new FlatFileItemReaderBuilder<Dog>()
				.name("dogReader")
				.resource(new ClassPathResource("sample-data.csv"))
				.delimited()
				.names(new String [] {"name","breed","age"})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Dog>() {{
					setTargetType(Dog.class);
				}})
				.build();
	}
	
	@Bean
	public DogProcessor processor() {
		return new DogProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Dog> writer(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<Dog>().itemSqlParameterSourceProvider(
			new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO dog (name, breed, age) VALUES(:name, :breed, :age)")
				.dataSource(dataSource)
				.build();
	}
	
	@Bean
	public Job dogJob(JobListener listener, Step step) {
		return jobBuilderFactory.get("dogJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step)
				.end()
				.build();
	}
	
	@Bean
	public Step step(JdbcBatchItemWriter<Dog> writer) {
		return stepBuilderFactory.get("step")
				.<Dog, Dog> chunk(2)
				.reader(reader())
				.processor(processor())
				.writer(writer)
				.build();
	}
}
