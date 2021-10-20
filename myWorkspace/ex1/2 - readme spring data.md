# Exercice 1 (partie 2 : couplage Spring - JPA, Spring Data)

**Objectifs** : 

* couplage Spring - JPA (Java persistence API)
* Création de _repositories_ Spring Data.
* Application de la démarcation transactionnelle.
* Utilisation des _repositories_ Spring Data dans la couche _business_ (pour le traitement des commandes) et dans la couche _web_ (pour les _queries_).

*****

1. Remarquer que `spring-data-jpa` est déjà présent dans le _classpath_ grâce à la dépendance `spring-boot-starter-data-jpa`

2. Ajouter 4 _repositories_ dans le package `com.acme.ex1.repository` : 

	* `AuthorRepository`
	* `ReservationRepository`
	* `BookRepository`
	* `MemberRepository`
	* `CategoryRepository`

   Pour chacun le type de l'`ID` est `Integer`

   Définir dans `MemberRepository` une méthode permettant de rechercher un _member_ par son nom d'utilisateur (rappel :
   un _member_ possède un _account_ qui lui même possède une propriété _username_)
	
3. Décommenter les tests unitaires de la classe `com.acme.ex1.repository.BookRepositoryTest` (puis organiser les imports) et coupler Spring et Junit.

4. Lancer les tests unitaires (un par un pour bien observer les logs).


5. Compléter la classe `com.acme.common.service.impl.CommandProcessorImpl` : une transaction doit entourer l'invocation de la méthode 

6. Compléter les implémentations de `CommandHandler` (package `com.acme.ex1.business.impl`) :
   
	* L'invocation de la méthode `handle` doit être interdite si une transaction n'est pas déjà en cours.
	* Suivre les TODO dans `com.acme.ex3.business.impl.MemberRegistrationCommandHandler`. 
	* Suivre les TODO dans `com.acme.ex3.business.impl.ReservationCommandHandler`.

7. Lancer les tests :

	* `com.acme.ex1.service.command.MemberRegistrationCommandTest`
	* `com.acme.ex1.service.command.ReservationCommandTest`
	
8. Compléter la classe `com.acme.ex1.web.controller.BookController` (suivre les TODO) puis lancer les tests de la classe `com.acme.ex1.web.controller.BookControllerTest`

9. Supprimer la dernière ligne de la méthode `main` de la classe `ApplicationConfig` afin que l'application ne se ferme pas immédiatement après avoir démarrée. Lancer l'application (méthode `main` de la classe `com.acme.ex2.ApplicationConfig`), accéder à http://localhost:8080/books depuis un navigateur :

	* la recherche par le titre Walden doit retourner un résultat (les livres dont le titre contient Walden).
	* la recherche par le titre al doit retourner 4 résultats (les livres dont le titre contient al).
	
10. Dans la fiche détail du livre "Walden", constater le bon affichage des commentaires. Pourtant il s'agit d'une relation _lazy_ qui est accédée pour la première fois lors du rendu de la vue, c'est à dire après la fin de la transaction. Constater que l'`EntityManager` reste ouvert après la fin de la transaction et n'est fermée qu'après le rendu de la vue. Rapprocher ce comportement du message de log _spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning_