package com.example.batchforcdef.service;

import com.example.batchforcdef.model.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public FlatFileItemReader<Transaction> reader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("src/main/resources/TxtFile.txt"));
        reader.setLineMapper(new DefaultLineMapper<Transaction>() {{
            setLineTokenizer(new FixedLengthTokenizer() {{
                setNames("stringNumber", "svc2Agr2", "company", "bmoServAcc2Agr2", "ptc", "abc");
                setColumns(new Range[] {new Range(1, 10), new Range(11, 30), new Range(31, 40),
                        new Range(41, 60), new Range(61, 90), new Range(91, 100)});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                setTargetType(Transaction.class);
            }});
        }});
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> writer() {
        JdbcBatchItemWriter<Transaction> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO shema (id, stringNumber, svc2Agr2, company, bmoServAcc2Agr2, ptc, abc) " +
                "VALUES (:id, :stringNumber, :svc2Agr2, :company, :bmoServAcc2Agr2, :ptc, :abc)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Step myStep() {
        return stepBuilderFactory.get("myStep")
                .tasklet((contribution, chunkContext) -> {
                    // Step logic goes here
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job myJob(Step myStep) {
        return jobBuilderFactory.get("myJob")
                .start(myStep)
                .build();
    }
}
