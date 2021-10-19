# Exercice 1 (préalable) :

1. Lancer la base de données:

    # à faire dans le dossier workspaces
    docker compose up pg

2. Ajouter une connection Intellij vers posgres, type 'User', username 'postgres' 

3. Examiner le fichier `pom.xml` et plus particulièrement les dépendances : 
   * `org.hibernate hibernate-core` (implémentation JPA)
   * `org.hibernate hibernate-ehcache` (cache de niveau 2 pour Hibernate)
   * `org.hibernate hibernate-hikaricp` (pool de connexion jdbc)
   * `org.springframework spring-orm` (couplage spring-jpa + spring-txt comme dépendance transitive)

4. Mapping (par annotations) pour les classes du package com.acme.ex2.model : 
   
   Comprendre pourquoi certaines classes sont des entités et d'autres des composants.
   
   Annoter chacune des classes avec l'annotation appropriée : `@Entity` ou `@Embeddable`.
   
   Mapper chaque champ de chaque classe avec l'annotation appropriée : 
   * `Category`
   * `Reservation`
   * `Member`(et donc `Account`)
   * `Book` (et donc `Comment`)
   * `Author`

   Rappel :
   * `@Id` ou `@Basic` si le type est simple (primitif, Date, String). `@Id` a réserver pour l'id, `@Basic` optionel car le champ est mappé par défaut.
   * `@ManyToOne` pour une relation de type association n-1 vers une `@Entity`
   * `@Embedded` pour une relation de type composition 1-1 vers un `@Embeddable`
   * `@ElementCollection` pour une relation de type composition 1-n vers un `@Embeddable`
   * `@OneToMany` pour une relation de type association 1-n vers une `@Entity`
   * `@ManyToMany` pour une relation de type association n-n vers une `@Entity`