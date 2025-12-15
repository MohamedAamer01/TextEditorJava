# Mini Éditeur de Texte en Java

MiniEditeur est un petit éditeur de texte développé en Java avec Swing. Il permet de créer, ouvrir, éditer et sauvegarder des fichiers texte et RTF, avec des options de mise en forme du texte.

---

## Fonctionnalités

- Création de plusieurs documents avec des onglets (`JTabbedPane`)
- Ouvrir et sauvegarder des fichiers `.txt` et `.rtf`
- Mise en forme du texte :
  - Gras, italique, souligné
  - Choix de la couleur du texte
  - Choix de la couleur du surlignage
  - Choix de la police et de la taille
- Barre d’outils et menu avec raccourcis clavier
- Fermer un onglet avec confirmation

---

## Raccourcis Clavier

- **Ctrl + N** : Nouveau document  
- **Ctrl + O** : Ouvrir un fichier  
- **Ctrl + S** : Enregistrer en RTF  
- **Ctrl + Q** : Fermer l’éditeur  

---

## Prérequis

- Java JDK 8 ou supérieur

---

## Compilation et exécution

Depuis le terminal dans le dossier du projet :

```bash
javac -d bin src/basedonnes/MiniEditeur.java
java -cp bin basedonnes.MiniEditeur
## Capture d'ecran de l'editeur

![Capture du MiniEditeur](images/MiniEditeur.png)



