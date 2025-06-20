- Une fois le projet cloner, configurez votre base de donnée sur le fichier : application.properties
- Les tables seront créées automatiquement grace a JPA/Hibernate
- Pour acceder à l’application : http://localhost:8080/clients


📚Fonctionnalités

🔧 Fonctionnalités principales
- Création et gestion de clients
- Création de comptes bancaires liés à un client
- Dépôts, retraits, virements internes et virements externes seulement si le client est banquier 
- Vérification du solde et du découvert autorisé ( Pas de message d'erreur si echec, seulement sur les logs)
- Historique de toutes les opérations sensibles dans des logs (date, montants,...)

🌐 Fonctionnalités REST 
- API REST pour la gestion des clients et comptes
- API REST pour consulter les logs des opérations

🔁 Règles des virements

🔸 1. Virement interne
Un virement entre deux comptes appartenant au même client.

✅ Autorisé si :
- Les deux comptes appartiennent au même client
- Le solde du compte source est suffisant (solde + découvert autorisé ≥ montant)

❌ Refusé si :
- Les comptes appartiennent à deux clients différents
- Le montant dépasse le solde + découvert autorisé
- Le compte source est introuvable

🔸 2. Virement externe
Un virement entre deux clients différents.

✅ Autorisé si :
- Le solde du compte source est suffisant (idem)
- Le client initiateur est un banquier (autorisation obligatoire)

❌ Refusé si :
- L’autorisation par un banquier n’est pas donnée (autorisationBanquier = false)
- Le solde est insuffisant
- Un des comptes n’existe pas
