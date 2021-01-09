package com.vts.clientcenter.config;

import com.vts.clientcenter.domain.Employee;
import com.vts.clientcenter.domain.EmployeeRecord;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Function;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

@Configuration
public class BatchJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory batchEntityManagerFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public Job job(Step step) throws Exception {
        return this.jobBuilderFactory.get(Constants.JOB_NAME).validator(validator()).start(step).build();
    }

    @Bean
    public Step step(ItemReader<EmployeeRecord> itemReader, Function<EmployeeRecord, Employee> processor, JpaItemWriter<Employee> writer)
        throws Exception {
        return this.stepBuilderFactory.get(Constants.STEP_NAME)
            .<EmployeeRecord, Employee>chunk(2)
            .reader(itemReader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public JobParametersValidator validator() {
        return parameters -> {
            //            String fileName = parameters.getString(Constants.JOB_PARAM_FILE_NAME);
            //            if (StringUtils.isBlank(fileName)) {
            //                throw new JobParametersInvalidException(
            //                    "The patient-batch-loader.fileName parameter is required.");
            //            }
            //            try {
            //                Path file = Paths.get(applicationProperties.getBatch().getInputPath() +
            //                    File.separator + fileName);
            //                if (Files.notExists(file) || !Files.isReadable(file)) {
            //                    throw new Exception("File did not exist or was not readable");
            //                }
            //            } catch (Exception e) {
            //                throw new JobParametersInvalidException(
            //                    "The input path + patient-batch-loader.fileName parameter needs to " +
            //                        "be a valid file location.");
            //            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeRecord> reader(@Value("#{jobParameters['" + Constants.JOB_PARAM_FILE_NAME + "']}") String fileName) {
        return new FlatFileItemReaderBuilder<EmployeeRecord>()
            .name(Constants.ITEM_READER_NAME)
            .resource(new PathResource(Paths.get(fileName)))
            .linesToSkip(1)
            .lineMapper(lineMapper())
            .build();
    }

    @Bean
    @StepScope
    public Function<EmployeeRecord, Employee> processor() {
        return employeeRecord ->
            new Employee()
                .employeeId(UUID.randomUUID().toString())
                .sourceId(employeeRecord.getSourceId())
                .ssn(employeeRecord.getSsn())
                .birthday(
                    LocalDate
                        .parse(employeeRecord.getBirthDate(), DateTimeFormatter.ofPattern("M/dd/yyyy"))
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
                )
                .zip(employeeRecord.getZip())
                .state(employeeRecord.getState())
                .city(employeeRecord.getCity())
                .street(employeeRecord.getStreet())
                .phoneNumber(employeeRecord.getPhoneNumber())
                .emailAddress(employeeRecord.getEmailAddress())
                .middleInitial(employeeRecord.getMiddleInitial())
                .firstName(employeeRecord.getFirstName())
                .lastName(employeeRecord.getLastName());
    }

    @Bean
    @StepScope
    public JpaItemWriter<Employee> writer() {
        JpaItemWriter<Employee> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(batchEntityManagerFactory);
        return writer;
    }

    @Bean
    public LineMapper<EmployeeRecord> lineMapper() {
        DefaultLineMapper<EmployeeRecord> mapper = new DefaultLineMapper<>();
        mapper.setFieldSetMapper(
            fieldSet ->
                new EmployeeRecord(
                    fieldSet.readString(0),
                    fieldSet.readString(1),
                    fieldSet.readString(2),
                    fieldSet.readString(3),
                    fieldSet.readString(4),
                    fieldSet.readString(5),
                    fieldSet.readString(6),
                    fieldSet.readString(7),
                    fieldSet.readString(8),
                    fieldSet.readString(9),
                    fieldSet.readString(10),
                    fieldSet.readString(11),
                    fieldSet.readString(12)
                )
        );
        mapper.setLineTokenizer(new DelimitedLineTokenizer());
        return mapper;
    }
}
