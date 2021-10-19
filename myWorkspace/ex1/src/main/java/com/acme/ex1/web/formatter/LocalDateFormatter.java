package com.acme.ex1.web.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {

	private final DateTimeFormatter formatter;

    public LocalDateFormatter(String pattern) {
        super();
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public LocalDateFormatter() {
        this("yyyy-MM-dd");
    }

	@Override
	public String print(LocalDate object, Locale locale) {
		return object == null ? null : object.format(formatter);
	}

	@Override
	public LocalDate parse(String text, Locale locale) throws ParseException {
		return StringUtils.hasText(text) ? LocalDate.parse(text, formatter) : null;
	}

}
