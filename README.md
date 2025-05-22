# Projet-Intergiciel-Kafka
Simon DUMOULIN - 22003591 - simon.dumoulin1@uphf.fr

Alexis PAGNON - 22103274 - alexis.pagnon@uphf.fr

Adam SANCHEZ - 22104850 - adam.sanchez@uphf.fr

### Description du projet:
L'objectif de ce projet était de développer une messagerie intra-entreprise simple avec une traduction automatique des échanges en anglais vers le français avec un archivage des messages dans une base de données. Tout ça en utilisant Apache Kafka, PostgreSQL ainsi que LibreTranslate.

### Comment lancer le projet ?
Pour le lancement, nous avons implémenté 2 manières de faire:
- La première consiste simplement à lancer les fichiers numérotés de 1 à 7 à la main:
  1) Lancer les fichiers de 1 à 3 dans un terminal
  2) Attendre que les 3 premiers soient lancés, puis lancer le 4 et le 5 dans un nouveau terminal chacun
  3) Attendre que le 4 et 5 soient lancés, puis lancer le 6 et le 7 dans un nouveau terminal chacun
- La seconde consiste à simplement lancer le fichier nommé 'runEverything.sh' qui va venir tout lancer d'un coup. Cependant, ce fichier peut poser un problème, car pour le fonctionnement de l'attente entre les 5 premiers lancements et les 2 derniers, nous avons mis un sleep de 15 secondes, donc si jamais votre machine est trop lente il faudra augmenter ce chiffre, sinon vous aurez des problèmes lors du lancement.

### Améliorations apportées au projet:
- Comme expliqué au-dessus, nous avons mis en place un fichier nous permettant de lancer tout le projet d'un coup
- Notre système de traduction ne détecte pas seulement l'anglais, il peut traduire plus de langues (pour cela, il suffit juste de modifier le fichier numéro 3 et d'enlever le paramètre '--load-only en,fr' qui limite les langues au français et à l'anglais)
