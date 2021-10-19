# Exercice 1 (partie 1 : Spring boot) :

**Objectifs** : 
* compréhension des dépendances d'un projet Spring boot.
* écriture du fichier application.properties.
* activation de la configuration automatique.

*****

1. Observer le fichier pom.xml et notamment la déclaration des dépendances correspondant à l'accès aux données.

2. Annoter la classe `com.acme.ex1.ApplicationConfig` pour en faire une classe de configuration Spring Boot, lancer la méthode `main`. Nous constatons l'absence des _beans_ nécessaires au bon fonctionnement de l'application (`DataSource`, `EntityManagerFactory`).

3. Ajouter une dépendance à `spring-jdbc` (_groupId_ : `org.springframework`) et décommenter la ligne 25, relancer la méthode `main`. Spring Boot tente de créer un _bean_ de type `DataSource` mais n'y parvient pas car les propriétés qu'il attend (`spring.datasource.*`) ne figurent pas dans le fichier `application.properties`. 

4. Ajouter la propriété de connexion suivante : 

	```
	spring.datasource.url=jdbc:postgresql://localhost:5432/formation_spring?user=postgres
	```

	https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-connect-to-production-database : _you often do not need to specify the driver-class-name, since Spring Boot can deduce it for most databases from the url._
 	
 	Relancer la méthode `main`. Un _bean_ de type `DataSource` et un autre de type `PlatformTransactionManager` sont désormais présents dans le contexte applicatif.

5. Ajouter une dépendance à `spring-orm` et relancer la méthode `main`, constater qu'un _bean_ de type `EntityManagerFactory` est désormais présent dans le contexte applicatif et que l'implémentation du `PlatformTransactionManager` a changé
	
6. Déclarer une dépendance vers le _starter_ `spring-boot-starter-data-jpa`, supprimer les dépendances devenues redondantes et relancer la méthode `main` pour vérifier la non régression.

7. A faire ensemble : comprendre le rôle des propriétés 
	
	```
	spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
	spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
	```
	
	https://docs.spring.io/spring-boot/docs/current/reference/html/howto-data-access.html#howto-configure-hibernate-naming-strategy : _By default, Spring Boot configures the physical naming strategy with SpringPhysicalNamingStrategy. This implementation provides the same table structure as Hibernate 4: all dots are replaced by underscores and camel casing is replaced by underscores as well. By default, all table names are generated in lower case, but it is possible to override that flag if your schema requires it._
