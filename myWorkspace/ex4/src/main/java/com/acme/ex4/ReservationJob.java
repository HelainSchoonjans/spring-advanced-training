package com.acme.ex4;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public ItemReader<Reservation> reader() {
        return new JdbcCursorItemReaderBuilder<Reservation>().name("reservationItemReader")
                .dataSource(getDataSource())
                .sql(RESERVATION_QUERY)
                .beanRowMapper(Reservation.class)
                .build();
    }

}
