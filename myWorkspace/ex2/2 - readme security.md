# Exercice 2 (partie 2 : sécurisation d'une API REST avec Open ID Connect et Spring security) :

Ajouter une dépendance vers `spring-boot-starter-security`, accéder à http://localhost:8080/books depuis le navigateur et remarquer l'activation automatique de la sécurité sur l'application.

## Configuration du serveur Open ID Connect

**Sans Docker** : lancer le fichier c:\formation_spring\env\keycloak-start.bat

Dans la console d'administration Keycloak (http://localhost:8282, admin/admin), créer un _realm_ nommé `my-realm`

Constater qu'il est possible d'accéder à http://localhost:8282/auth/realms/my-realm/.well-known/openid-configuration 

## Sécurisation de l'API REST et authentification par _token_ JWT.

1. Ajouter au fichier `application.properties` une propriété `spring.security.oauth2.resourceserver.jwt.issuer-uri` (valeur : `http://localhost:8282/auth/realms/my-realm`)  et ajouter les dépendances suivantes :

	```xml
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-oauth2-resource-server</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.security</groupId>
		<artifactId>spring-security-oauth2-jose</artifactId>
	</dependency>
	```

2. Dans la classe de configuration : déclarer un _bean_ de type `WebSecurityConfigurerAdapter` et activer la reconnaissance des tokens JWT.

3. Activer la sécurisation des méthodes annotées par `@PreAuthorize`

4. Appliquer une règle de sécurité afin que la réservation d'un livre ne soit possible que si l'utilisateur est authentifié.

5. Lancer les tests de la classe `ReservationRestEndpointTest`, remarquer l'échec du test `testReservations201`, annoter la méthode de test afin qu'elle s'exécute sous l'identité de l'utilisateur _jdoe_ puis exécuter le test.
