package com.acme.ex4;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@EnableBatchProcessing
@Configuration
public class ReservationJob {

    public static final String RESERVATION_QUERY = """
            select b.id, b.title, m.username from Reservation r
            join Member m on r.member_id = m.id
            join Book b on r.book_id = b.id""";

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
