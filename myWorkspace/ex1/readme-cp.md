Chaque commande correspond aux informations échangées lors de la réalisation d'un cas d'utilisation. Elle contient : 
* les champs utilisés par l'appelant (disons l'IHM).
* les champs utilisées par l'application pour fournir les résultats attendus par l'appelant.
* éventuellement : des routines de validation avant traitement/aprés traitement, car la commande est la mieux placée pour savoir si elle est en état d'être traitée ou non.

Ainsi la commande est une structure d'échange entre l'IHM et l'application, il est possible d'ajouter de nouvelles propriétés
sans modifier la manière d'échanger les messages. Cela favorise l'évolutivité et l'acceptation de nouvelles demandes de la part de la MOA.

Dans `AbstractCommand`, les méthodes `validateStateBeforeHandling` et `validateStateAfterHandling` permettent de vérifier les pré conditions et les post conditions.

une fois la commande reçue par le `CommandProcessor` (méthode `process`), celui-ci 

* déclenche la validation d'état avant traitement (`validateStateBeforeHandling()`)
* _dispatch_ la commande auprès des _handlers_ capables de la traiter.
* déclenche la validation d'état avant traitement (`validateStateAfterHandling()`) 

Le `CommandProcessor` orchestre donc le traitement des commandes en ignorant tout des commandes concrètes ou des _handlers_ concrets : il est possible d'associer et désassocier des _handlers_ à un type de commande, sans modifier le code de la méthode _process_. Cela favorise l'évolutivité et l'adaptation de l'application à de nouveaux besoins.

A propos de la gestion des exceptions

* les exceptions applicatives sont des `CommandException`
* les exceptions techniques sont les autres exceptions.

Cette séparation claire permet un traitement différencié :

* pour les `CommandException` : minimum de log et présentation du message à l'utilisateur
* pour les autres exceptions : maximum de log et présentation d'un message générique à l'utilisateur.

Au final, les classes et interfaces suivantes pourraient faire partie d'un socle technique réutilisé dans plusieurs projets : 

* `CommandProcessor`
* `CommandHandler`
* `CommandException`
* `AbstractCommand`

Et chaque projet aurait ses commandes et ses _handlers_ spécifiques.