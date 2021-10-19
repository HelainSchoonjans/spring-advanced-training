#  Exercice 3 (Programmation réactive avec Spring Data R2DBC et Spring Data ElasticSearch, messaging avec AMQP ou Kafka) :

1. Observer le pom.xml, et notamment les dépendances de `spring-starter-webflux` : plus de _servlet container_ mais un serveur embarqué Netty.

2. Observer le fichier `application.properties` : celui-ci déclare les propriétés de connexion à la base PostgreSQL *via R2DBC* (et non pas JDBC) et à la base ElasticSearch. Spring Boot inscrira automatiquement les _beans_ suivants dans l'`ApplicationContext` : 

	* `io.r2dbc.spi.ConnectionFactory`
	* `org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient`
	* `org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate`

3. Observer les classes du _package_ `com.acme.ex3.model` : le _mapping_ utilise les annotations fournies par `spring-data-r2dbc`. Ce dernier est beaucoup moins riche que celui proposé par JPA : pas de relations... Ainsi dans la classe `Reservation`, au lieu d'avoir un champ

	```java
	private Book book;
	```
	
	nous devons avoir un champ 
	
	```java
	private int bookId;
	```

4. Observer les interfaces du _package_ `com.acme.ex3.repository`, et notamment les méthodes héritées (voir le code source de `R2dbcRepository`). Toutes retournnt des `Flux` ou des `Mono`.
	
## Les commandes

Dès les que les _command handlers_ (voir _package_ `com.acm.ex3.business.impl` utilisent des _repositories_ réactifs, ils vont obtenir de leur part des `Mono et des Flux`. La méthode `handle` ne sera donc plus :

```java
void handle(AbstractCommand command)
```
	
mais
	
```
Mono<Void> handle(AbstractCommand command)
```
	
et le `CommandProcessorImpl` a son tour doit s'adapter aux _handlers_ : dès lors que ceux-ci retournent désormais `Mono<Void>`, la méthode `process` devient : 

```
<T extends AbstractCommand> Mono<T> process(T command);
```

Nous remarquons que même les opérations de la méthode `process` qui pourraient être synchrones (appel au _logger_ ou de la méthode  `validateStateBeforeHandling()` doivent être inscrites dans un `Mono.fromRunnable` afin de ne pas s'exécutée lors de l'appel à `process` mais seulement lors de la souscription au `Mono<T>` retourné par la méthode `process`.

Exemple d'appels à `process` : voir les classes `MemberEndpoint` et `ReservationEndpoint` du _package_ `com.acme.ex3.web.endpoint`.

## Les queries

Les _queries_ (exemple : recherche de livres) se font depuis les _endpoints_. 

Ainsi la recherche de livres est implémentée dans  la classe `com.acme.ex3.web.endpoint.BookEndpoint`.

Il s'agit ici d'obtenir les livres auprès d'un index ElasticSearch. Nous utilisons ici le `ReactiveElasticSearchTemplate` : un utilitaire proposé par Spring qui fournit une abstraction par rapport au client HTTP (rappel : les requêtes ElasticSearch sont soumise en REST).

Deux recherches sont proposées : 

* recherche par titre et nom d'auteur, en utilisant les `CriteriaQuery`. Ce type de `query` offre une abstraction vis-à-vis de la syntaxe d'expression de requête ElasticSearch.

* recherche par mot clé sur plusieurs champs en utilisant les `StringQuery`. Ce type de `Query` permet d'utiliser la syntaxe ElasticSearch.

Nous pouvons toutefois regretter l'utilisation de la classe `BookResult` : les résultats retournées par ElasticSearch sont déserialisés en `BookResult` pour alimenter le `Flux<SearchHit<BookResult>>` que nous obtenons du `template`. Ce `Flux` est retourné tel quel donc les éléments seront ensuite sérialisées en JSON dans la réponse HTTP. Dès lors que les `BookResult` n'ont pas été manipulé, nous avons des déserialisations-sérialisations pour rien. Une alternative sera de travailler à un niveau plus bas et de retourner la réponse JSON retournée par ElasticSearch (quitte à transformer le json pour qu'il ne contiennent que les informations essentielles : nombre de résultat, score de chacun).

## Messaging avec AMQP ou Kafka

Si nous préférons écrire la logique métier de manière impérative et utiliser JPA, une possibilité sera d'utiliser un _broker_ RabbitMQ ou Kafka. 

Ainsi chaque commande reçue par les _endpoints_ du projet ex3 sera déposée sur un _exchange_ (RabbitMQ) ou un _topic_ (Kafka). 

Un _listener_ , par exemple dans le projet ex2, recevrait les commandes à traiter. 

Nous ne garderions dans le projet ex3 que les _packages_ `com.acme.ex3.web.endpoint` et `com.acme.ex3.service.command`. Les commandes reçues seraient déposées sur une file d'attente. Attention : 

* les types des objets échangés (ici : les commandes) doivent être les mêmes du côté de l'émetteur (ex3) et du côté du consommateur (ex2). Le nom des classes Java doit être le même, au sens _fully qualified name_, c'est à dire que le _package_ doit être le même. Dans le projet ex3 il faudrait donc renommer le _package_ `com.acme.ex3.service.command` en `com.acme.ex2.service.command`.
* les classes du modèle ne figurent plus dans le projet ex3, les propriétés `member` et `reservation` des classes `MemberRegistrationCommand` et `ReservationCommand` doivent être supprimés.
* l'annotation `@Usecase` n'a plus lieu d'être.


Les deux projets (ex2 et ex3) doivent déclarer :

* une dépendance à `spring-boot-starter-amqp` ou `spring-boot-starter-kafka`.
* les propriétés de connexion. Exemple avec RabbitMQ :
	```
	spring.rabbitmq.host=localhost
	spring.rabbitmq.port=5672
	spring.rabbitmq.username=user
	spring.rabbitmq.password=psw
	```

ex2 n'aurait plus de _endpoints_ REST, et la dépendance à `spring-boot-starter-web` n'aurait plus lieu d'être. 

Dès lors qu'ex2 déclare lui aussi la dépendance `spring-boot-starter-amqp` ou `spring-boot-starter-kafka` et les propriétés de connexion, il peut recevoir les commandes.

Avec RabbitMQ, la classe `ReservationEndpoint` dans ex3 deviendrait :

```java
@RestController @CrossOrigin
class ReservationEndpoint {

	private final AmqpTemplate amqpTemplate;
	
	ReservationEndpoint(AmqpTemplate amqpTemplate) {
		super();
		this.amqpTemplate = amqpTemplate;
	}
	
	@PostMapping("reservations")
	@PreAuthorize("isAuthenticated()")
	Mono<ResponseEntity<Void>> borrow(@RequestBody @Valid ReservationCommand command, Authentication auth) {
		if(!StringUtils.hasText(command.getUsername())) {
			command.setUsername(auth.getName());
		}	    
		return Mono.fromCallable(() -> {
			String routingKey = "commands-to-process";
			this.amqpTemplate.convertAndSend(routingKey, command, postProcessor);
			return ResponseEntity.accepted().build();
		});         
	}
}
```

Exemple avec RabbitMQ dans le projet ex2 : 

```java
	
private final SerializerMessageConverter converter = new SerializerMessageConverter();
		
@Autowired
private CommandProcessor processor;

@RabbitListener(queuesToDeclare = @Queue(name = "commands-to-process"))
public void onNewCommandToProcess(Message message) {
	AbstractCommand command = (AbstractCommand) converter.fromMessage(message);
	processor.process(command);
}	
```

Pour transmettre l'identité de l'utilisateur avec le message : 

Au moment de l'envoi depuis ex3:

```java
@PostMapping("reservations")
@PreAuthorize("isAuthenticated()")
Mono<ResponseEntity<Void>> borrow(@RequestBody @Valid ReservationCommand command, Authentication auth) {
	if(!StringUtils.hasText(command.getUsername())) {
		command.setUsername(auth.getName());
	}	    
	MessagePostProcessor postProcessor = msg -> {
		msg.getMessageProperties().setHeader("x-user-id", auth.getName());
		return msg;	        
	};
		
	return Mono.fromCallable(() -> {
		String routingKey = "commands-to-process";
		this.amqpTemplate.convertAndSend(routingKey, command, postProcessor);
		return ResponseEntity.accepted().build();
	});         
}
```

Au moment de la réception dans ex2 : 

```java
@RabbitListener(queuesToDeclare = @Queue(name = "commands-to-process"))
public void onNewCommandToProcess(Message message) {
	String userId = (String)message.getMessageProperties().getHeaders().get("x-user-id");
	SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null));
		
	AbstractCommand command = (AbstractCommand) converter.fromMessage(message);
	processor.process(command);
}
```

Différences entre RabbitMQ et Kafka : https://www.cloudamqp.com/blog/when-to-use-rabbitmq-or-apache-kafka.html