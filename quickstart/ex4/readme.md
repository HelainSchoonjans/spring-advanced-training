#  Exercice 4 (Spring Batch) :

Objectifs : 
- Compréhension des notions de Job, Step, chunk, tasklet
- Implémentation d'un _chunk oriented step_
- Implémentation d'une _tasklet_
- Démarrage d'un `Job`
*****

1. Annoter la classe `ReservationJob` pour en faire une classe de configuration.

2. Activer la prise en charge des batchs sur cette classe de configuration.

3. Définir un job qui 
	1. Obtient en base de données les réservations du jour.
	
		Datasource : `new DriverManagerDataSource("jdbc:postgresql://localhost:5432/formation_spring", "postgres", null)`
		
		Requête SQL : 
		```sql
		select b.id, b.title, m.username from Reservation r 
		join Member m on r.member_id = m.id 
		join Book b on r.book_id = b.id
		```
	2. Fabrique pour chaque ligne une instance de `ReservationRow`
	
	3. Ecrit les instances de `ReservationRow` dans un fichier csv (c:\formation_spring\files\reservations.csv)
	
	4. Envoie le fichier reservations.csv par mail (ne pas écrire le code d'implémentation de l'envoi du mail).
	
4. Ajouter une méthode `main` qui : 

	1. instancie un contexte Spring basé sur la classe `ReservationJob`
	2. Obtient un `jobLauncher` auprès du contexte applicatif.
	3. Lance le job
