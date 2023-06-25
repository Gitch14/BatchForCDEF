package com.example.batchforcdef.configuration;

import com.example.batchforcdef.model.Transaction;
import com.example.batchforcdef.service.CustomLineTokenizer;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.regex.Pattern;

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
        reader.setResource(new ClassPathResource("CPA-multipleCDEF.txt"));

        CustomLineTokenizer tokenizer = new CustomLineTokenizer();
        tokenizer.setDelimiterPattern(Pattern.compile("\\s+"));

        String[] fieldNames = {"stringNumber", "svc2Agr2", "company", "bmoServAcc2Agr2", "ptc", "abc"};
        tokenizer.setNames(fieldNames);

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
            setTargetType(Transaction.class);
        }});
        reader.setLineMapper(lineMapper);

        // Игнорирование ошибок неправильного количества токенов
        reader.setLinesToSkip(1); // Пропустить заголовок
        reader.setSkippedLinesCallback(line -> {
            System.out.println("Ignored line: " + line);
        });

        // Exception handler
        reader.setStrict(false); // Allow parsing errors

        return reader;
    }


    @Bean
    public JdbcBatchItemWriter<Transaction> writer() {
        JdbcBatchItemWriter<Transaction> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO shema1 (stringNumber, svc2Agr2, company, bmoServAcc2Agr2, ptc, abc) " +
                "VALUES (:stringNumber, :svc2Agr2, :company, :bmoServAcc2Agr2, :ptc, :abc)");
        writer.setDataSource(dataSource);
        return writer;
    }


    @Bean
    public Step myStep(FlatFileItemReader<Transaction> reader, JdbcBatchItemWriter<Transaction> writer) {
        return stepBuilderFactory.get("myStep")
                .<Transaction, Transaction>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Job myJob(Step myStep) {
        return jobBuilderFactory.get("myJob")
                .start(myStep)
                .build();
    }
}
