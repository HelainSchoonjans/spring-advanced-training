package com.acme.ex3.web.endpoint;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
class BookEndpoint {

	private final ReactiveElasticsearchTemplate template;
	
	public BookEndpoint(ReactiveElasticsearchTemplate template) {
		this.template = template;
	}

	@Document(indexName = "books")
	public static class BookResult{
		@Id
		public int id;
		public String title,author,category;
	}
	
	@GetMapping(path = "books/{id}")
	Mono<ResponseEntity<BookResult>> getOne(@PathVariable String id) {
		
		return this.template
				.get(id, BookResult.class)
				.map(x -> ResponseEntity.ok(x))
				.defaultIfEmpty(ResponseEntity.notFound().build());
				
	}

	// attention, here we are transforming the Elasticsearch json in java then back in json...
	// a better way to use a webclient and pass a flux instead of serializing.
	// use webclient
	@GetMapping(path = "books",params = "title")
	Flux<SearchHit<BookResult>> find(@RequestParam String title, String author) {
		Query query = new CriteriaQuery(new Criteria("title").fuzzy(title).and("author").fuzzy(author));
		return this.template.search(query, BookResult.class);
	}
	
	@GetMapping(path = "books",params = "keyword")
	Flux<SearchHit<BookResult>> find(@RequestParam String keyword) {
		Query query = new StringQuery(String.format("""
				{
					"multi_match" : {
						"query" : "%s",
						"fields" : [ "title^2", "author^1" ],
						"fuzziness":"AUTO"
					}
				}
				""", keyword));
		return this.template.search(query, BookResult.class);
	}
}
