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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

// this annotation will make available the StepBuilderFactory
@EnableBatchProcessing
// @Configuration will make the bean be surrounded by spring proxy
// @SpringBootApplication includes Configuration. It will here help configure automatically a datasource
@SpringBootApplication
public class ReservationJob {

    public static final String RESERVATION_QUERY = """
            select b.id as bookId, b.title as bookTitle, m.username as username from Reservation r
            join Member m on r.member_id = m.id
            join Book b on r.book_id = b.id""";

    // you can't use constructor injection in the @configuration class!
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;

    // methods annotated by @Bean will create a bean
    // calling them is equivalent to ctx.getBean; we don't risk executing them twice
    // there are executed only once to create the bean

    // commented because we now create the bean automatically
    /*
    @Bean
    public DataSource getDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/formation_spring";
        return new DriverManagerDataSource(url, "postgres", null);
    }
    */

    /*
    having reader and writer in the singleton/default scope can be problematic
    the cursor of the reader will be kept every time the job is scheduled.
    That's why we have the @StepScope annotation to ensure the cursor is kept for the step only.
    We also need a proxy for that, hence the @Scope and proxyMode TARGET_CLASS

    See page #170 of the training for more information
     */
    @Bean
    // exposes a proxy in front of the bean
    @StepScope
    // a proxy by heritage so we don't lose any method
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ItemReader<ReservationRow> reader() {
        return new JdbcCursorItemReaderBuilder<ReservationRow>().name("reservationItemReader")
                .dataSource(dataSource)
                .sql(RESERVATION_QUERY)
                .beanRowMapper(ReservationRow.class)
                .build();
    }

    @Bean
    @StepScope
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ItemWriter<ReservationRow> writer() {
        return new FlatFileItemWriterBuilder<ReservationRow>().name("reservationWriter")
                .resource(new FileSystemResource("c:\\formation_spring\\files\\reservations.csv"))
                .delimited()
                .delimiter(";")
                .names(new String[] { "bookId", "username", "bookTitle"})
                .build();
    }

    @Bean
    public Step getDayReservations() {
        StepBuilder builder = stepBuilderFactory.get("getDayReservations");
        return builder.<ReservationRow, ReservationRow>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job sendReservationJob(JobBuilderFactory jobBuilderFactory) {
        JobBuilder builder = jobBuilderFactory.get("sendReservationJob");
        return builder.start(getDayReservations())
                .build();
    }

    public static void main(String[] args) {
        // SpringApplication.run can be used to execute jobs, not, only to run web servers
        try (var ctx = SpringApplication.run(ReservationJob.class)) {
            JobLauncher launcher = ctx.getBean(JobLauncher.class);
            Job job = ctx.getBean("sendReservationJob", Job.class);
            launcher.run(job, new JobParameters());
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
