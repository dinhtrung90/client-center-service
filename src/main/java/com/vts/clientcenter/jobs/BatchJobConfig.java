package com.vts.clientcenter.jobs;

import com.vts.clientcenter.domain.EmployeeEntity;
import com.vts.clientcenter.domain.EmployeeRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
//@EnableAutoConfiguration(exclude={BatchAutoConfiguration.class})
public class BatchJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilder;

    @Autowired
    private StepBuilderFactory stepBuilder;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job readCSVFile(EmployeeListener listener) {
        return jobBuilder
            .get("process-employee-import")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .start(step())
            .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilder
            .get("step")
            .<EmployeeRecord, EmployeeEntity>chunk(5)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    @Bean
    public ItemProcessor<EmployeeRecord, EmployeeEntity> processor() {
        return new EmployeeProcessor();
    }

    // reading from csv file
    @Bean
    public FlatFileItemReader<EmployeeRecord> reader() {
        FlatFileItemReader<EmployeeRecord> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource("csv/test.csv"));
        return itemReader;
    }

    //convert csv rows to beans
    @Bean
    public LineMapper<EmployeeRecord> lineMapper() {
        DefaultLineMapper<EmployeeRecord> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("source_id", "first_name", "middle_initial","last_name","email_address","phone_number","street","city","state","zip","birth_date","action","ssn");
        lineTokenizer.setIncludedFields(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        BeanWrapperFieldSetMapper<EmployeeRecord> fieldSetMapper = new BeanWrapperFieldSetMapper<EmployeeRecord>();
        fieldSetMapper.setTargetType(EmployeeRecord.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    // writting into mysql database
    @Bean
    public JdbcBatchItemWriter<EmployeeEntity> writer() {
        JdbcBatchItemWriter<EmployeeEntity> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("insert into employees (source_id,first_name,middle_initial,last_name,email_address,phone_number, street, city, state, zip_code, birth_date, social_security_number) VALUES (:sourceId, :firstName, :middleInitial, :lastName, :emailAddress, :phoneNumber, :street, :city, :state, :zipCode, :birthDate, :socialSecurityNumber)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<EmployeeEntity>());
        return itemWriter;
    }

}
