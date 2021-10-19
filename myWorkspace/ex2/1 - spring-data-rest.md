# Exercice 2 (partie 1 : utilisation de Spring data rest)


Objectifs : 
- exposition de _repositories_ en REST
- tests d'intégration avec MockMvc
*****

1. Ajouter une dépendance à `spring data rest` pour exposer les _repositories_ en REST : 

	```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
	```
	
	Lancer l'application (via la classe `com.acme.ex2.ApplicationConfig`) puis exécuter la commande `curl http://localhost:8080/books` (ou accéder à l'URL depuis le navigateur ou Postman). Réessayer avec l'URL http://localhost:8080/books/1
	
3. Créer une projection pour l'entité `Book`, n'exposant que les propriétés suivantes : 

	* id
	* titre
	* nom complet de l'auteur.
	* nom de la categorie.
	
	Constater la différence en accédant à nouveau à http://localhost:8080/books puis http://localhost:8080/books/1
	
4. Ajouter dans `BookRepository` une méthode permettant de rechercher les livres par leur titre et nom d'auteur (opérateur : contient). Annoter cette méthode afin qu'elle soit accessible en REST par un GET sur `/books/search/byTitleAndAuthor`.

5. Redéfinir les méthodes `deleteById` des _repositories_ afin qu'elles ne soient pas accessibles en REST.

6. Lancer les tests de la classe `BookRestEndpointTest`.

7. Annoter la classe `com.acme.ex2.web.endpoint.MemberEndpoint` par `@RepositoryRestController` afin de reprendre le contrôle sur le traitement des requêtes `POST` sur le _path_ `/members`
