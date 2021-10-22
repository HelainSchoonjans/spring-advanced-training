package com.acme.ex4;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@EnableBatchProcessing
// @Configuration will make the bean be surrounded by spring proxy
@Configuration
public class ReservationJob {

    public static final String RESERVATION_QUERY = """
            select b.id, b.title, m.username from Reservation r
            join Member m on r.member_id = m.id
            join Book b on r.book_id = b.id""";

    private final StepBuilderFactory stepBuilderFactory;

    public ReservationJob(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    // methods annotated by @Bean will create a bean
    // calling them is equivalent to ctx.getBean; we don't risk executing them twice
    // there are executed only once to create the bean
    @Bean
    public DataSource getDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/formation_spring";
        return new DriverManagerDataSource(url, "postgres", null);
    }

    @Bean
    @StepScope
    public ItemReader<ReservationRow> reader() {
        return new JdbcCursorItemReaderBuilder<ReservationRow>().name("reservationItemReader")
                .dataSource(getDataSource())
                .sql(RESERVATION_QUERY)
                .beanRowMapper(ReservationRow.class)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<ReservationRow> writer() {
        return new FlatFileItemWriterBuilder<ReservationRow>().name("reservationWriter")
                .resource(new FileSystemResource("c:\\formation_spring\\files\\reservations.csv"))
                .delimited()
                .delimiter(";")
                .names(new String[] { "bookId", "username", "title" })
                .build();
    }

}
