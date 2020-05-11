import java.io.IOException;
import java.util.*;

public class MatchIA {

	private static Dictionnaire dico;
	private static anagramDictionnaire dicoAnagram;
	static Plateau plat;
	// nombreLettre = 9, 2, 2, 3, 15, 2, 2, 2, 8, 1, 1, 5, 3, 6, 6, 2, 1, 6, 6, 6,
	// 6, 2, 1, 1, 1, 1;
	private ArrayList<Integer> nombreLettre = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2,
			2, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 10,
			11, 11, 11, 11, 11, 12, 12, 12, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 15, 15, 16, 17, 17, 17, 17,
			17, 17, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 21, 21, 22, 23, 24, 25)); // tout
	// l'alphabet
	private Joueur joueur;
	private Joueur ordi;
	private int difficulte;
	ArrayList<String> usedWords;

	/* === Constructeur === */

	public MatchIA(String nomJoueur, int difficulte) throws IOException {
		// charge toutes les instances nécessaires
		dico = new Dictionnaire("dico.txt");
		plat = new Plateau();
		dicoAnagram = new anagramDictionnaire("dico.txt");
		this.difficulte = difficulte;

		joueur = new Joueur(nomJoueur, getNombreLettre());
		setNombreLettre(joueur.remainingLettersPostBuilder());

		// Vérifiez si player2 essaie de copier le nom du player1
		if (nomJoueur == "ordi")
			ordi = new Joueur("ordinateur", getNombreLettre());
		else
			ordi = new Joueur("ordi", getNombreLettre());

		setNombreLettre(ordi.remainingLettersPostBuilder());

		// Commencer le tour
		joueur.commencerTour();
		ordi.terminerTour();

		usedWords = new ArrayList<String>();
	}

	/* === Observateurs === */
	public void montrerTableauMultijoueur() {
		plat.montrerPlateau();
	}

	public void montrerMainMultijoueur(Joueur nomJoueur) {
		System.out.print("  { ");
		for (int i = 0; i < 6; ++i)
			System.out.print(nomJoueur.verifierLettre(i) + ", ");
		System.out.print(nomJoueur.verifierLettre(6) + " }\n");
	}

	public Joueur tourActuel() {
		if (joueur.verifierTour() == true)
			return joueur;
		else
			return ordi;
	}

	public Joueur adversaireActuel() {
		if (joueur.verifierTour() == true)
			return ordi;
		else
			return joueur;
	}

	public boolean isPlayerTurn() {
		return joueur.verifierTour();
	}

	public boolean asGagne() { // test pour savoir si un joueur n'a plus de lettres a placer.
		for (int i = 0; i < 7; i++) {
			if (tourActuel().verifierLettre(i) != 0)
				return false;
		}
		return true;
	}

	public int totalUsedWords() {
		return usedWords.size();
	}

	public int lettresRestant() {
		return getNombreLettre().size();
	}

	public boolean isWordUsed(String value) {
		return usedWords.contains(value);
	}

	/* === Modificateurs === */

	public void newWordUsed(String value) {
		usedWords.add(value);
	}

	public void suivantTour() {
		if (tourActuel().verifierNom() == joueur.verifierNom()) {
			joueur.terminerTour();
			ordi.commencerTour();
		} else {
			ordi.terminerTour();
			joueur.commencerTour();
		}
	}

	public Joueur pointsFin() {
		if (tourActuel().verifierTourPasser() && adversaireActuel().verifierTourPasser()) {
			// La partie est terminé car il n'y a pas plus de lettres pour les deux personnes.
			// Juste comparer qui a plus de points
			if (tourActuel().verifierScore() == adversaireActuel().verifierScore()) {
				Joueur gagner = new Joueur("Match Null", getNombreLettre());
				return gagner;
			} else {
				if (tourActuel().verifierScore() > adversaireActuel().verifierScore())
					return tourActuel();
				else
					return adversaireActuel();
			}

		} else {
			// si la partie est terminee alors le joueur qui viens d'utiliser toutes ces
			// lettres restantes doit ce voir ajouter a son score la somme des lettres
			// restantes a son adversaires.
			int points = 0;
			int[] scoreLettre = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10,
					0 };

			// le joueur actuel qui a fini la partie donc on calcule la somme des lettres
			// restante du joueur adversaire.
			for (int i = 0; i < 7; ++i) {
				if (adversaireActuel().verifierLettre(i) >= 97)
					points += scoreLettre[adversaireActuel().verifierLettre(i) - 97];
			}

			// actualise le nouveau scores des deux joueurs
			tourActuel().nouveauScore(tourActuel().verifierScore() + points);
			// Il y a deux types de points pour l'adversaire!!!!
			// Adversaire Actuel avec points negatif
			adversaireActuel().nouveauScore(adversaireActuel().verifierScore() - points);
			// Adversaire Actuel avec min valeur 0
			// int scoreAdversaire = (adversaireActuel().verifierScore() - points >= 0) ?
			// adversaireActuel().verifierScore() - points : 0 ;
			// adversaireActuel().nouveauScore(scoreAdversaire);

			if (tourActuel().verifierScore() == adversaireActuel().verifierScore()) {
				Joueur gagner = new Joueur("Match Null", getNombreLettre());
				return gagner;
			} else {
				if (tourActuel().verifierScore() > adversaireActuel().verifierScore())
					return tourActuel();
				else
					return adversaireActuel();
			}
		}
	}

	public Mot getBestMove() { // retourne le mot devant être saisi par l'ia (dépend de la difficulté choisie)
		permutationSearch pSResult = new permutationSearch(dicoAnagram, dico, plat, ordi.verifierMain(), this.usedWords);
		Mot retVal = null;

		if (pSResult.totalPossibleWords() == 0)
			return retVal;

		ArrayList<Mot> threeWordList = new ArrayList<Mot>();
		for (int index = 0; index < pSResult.totalPossibleWords(); ++index){
			if(pSResult.possibleWord(index).verifierNom().length() >= 3)
				threeWordList.add(pSResult.possibleWord(index));
		}

		if(threeWordList.size() == 0){
			return retVal;
		}

		switch (difficulte) {
		case 1:
			// Facile
			// Seulement choisir le mot qui donne le moins de points
			retVal = threeWordList.get(threeWordList.size() - 1);
			break;
		case 2:
			// Normale
			// choisir le mots qui est au milieu
			retVal = threeWordList.get(Math.floorDiv(threeWordList.size() - 1, 2));
			break;
		case 3:
			// Difficile
			// La premier mot de la liste sera celui qui rapporte le plus de points de tout
			// les possibilites
			retVal = threeWordList.get(0);
			break;
		}

		return retVal;
	}

	public int completeIATurn() {
		Mot motASaisir = getBestMove();
		if (motASaisir != null) {
			// insérer un mot dans le tableau
			proceedWithAction(ordi, motASaisir.getLettres(), motASaisir.verifierX(), motASaisir.verifierY(),
					motASaisir.verifierHorizontal());
			return 0;
		} else {
			// si ce n'est pas le cas on regarde combien de lettres il reste dans la pioche
			if (lettresRestant() > 0) { // si il reste des lettres dans la pioche on decide de jeter nos lettres
				for (int i = 0; i < ordi.verifierMain().length; i++)
					setNombreLettre(ordi.jeterLettre(i, getNombreLettre()));

				return -1;
			} else // Juste un autre tour qu'il ne peut rien faire, vérifiez si l'autre joueur ne peut pas aussi gagner ou donner un autre tour sans se déplacer
			if (tourActuel().verifierTourPasser() && adversaireActuel().verifierTourPasser()) {
				// Il n'y a plus de lettres et les deux joueurs n'ont rien à jouer
				System.out.print("\n\n\t\t=== There aren't more combinations available!!! ===");
				if (tourActuel().verifierScore() == adversaireActuel().verifierScore()) {
					// La partie s'est terminée par une égalité (les deux joueurs ont le même nombre de points)
					System.out.print("\n\n\t\t === MATCH NULL ===");
					System.out.print("\n\n\t\t   Classement final!!");
					System.out
							.print("\n\t\tJoueur " + joueur.verifierNom() + ": " + joueur.verifierScore() + " points!");
					System.out.print("\n\t\tJoueur " + ordi.verifierNom() + ": " + ordi.verifierScore() + " points!");
				} else {
					Joueur gagnant = (tourActuel().verifierScore() > adversaireActuel().verifierScore()) ? tourActuel()
							: adversaireActuel();
					System.out.print("\n\n\t\t=== Le gagnant est " + gagnant.verifierNom() + "!!! ===");
					System.out.print("\n\n\t\t   Classement final!!");
					System.out
							.print("\n\t\tJoueur " + joueur.verifierNom() + ": " + joueur.verifierScore() + " points!");
					System.out.print("\n\t\tJoueur " + ordi.verifierNom() + ": " + ordi.verifierScore() + " points!");
				}
				// Déclare la fin du jeu
				return 1;
			} else {
				// Le jeu n'a pas pris fin pour le moment, passez au tour suivant
				ordi.incrementTourPasser();
				return -2;
			}
		}
	}

	// === Fonction continueWithAction ===
	// Cette fonction fait presque tout:
	// - Vérifie si c'est le tour du joueur
	// - Vérifie si le mot existe dans le dictionnaire
	// - Vérifie si le mot peut rentrer dans le tableau
	// - Si tout fonctionne:
	// - Insère le mot et donne les points au joueur respectif
	// - Change le tour
	// - Retourne "vrai" comme dans "tout allait bien"
	// - Si quelque chose donne une erreur:
	// - Il suffit de retourner "false" et le joueur devra utiliser une autre
	// combinaison de mots
	//
	//
	// Note à tous ceux qui font l'interface: Le but de cette fonction est d'être
	// utilisé dans la partie de l'interface où tous les mouvements des joueurs sont
	// enregistrés
	// Donnez juste tous les paramètres dont il a besoin et il devrait tout faire

	public boolean proceedWithAction(Joueur nomJoueur, int[] mot, int xPos, int yPos, Boolean horizontal) {

		// Si ce n'est pas au tour du joueur actuel, ne le laisse rien faire
		if (tourActuel() != nomJoueur)
			return false;

		// Nombre de combination trouve
		int combinations = 0;

		if (plat.returnBoite(7, 7) == 3 && horizontal) {
			// this is the first turn of the game
			if(xPos == 7) {
				if(yPos > 7 || (yPos + mot.length) < 7 )
					return false;
			} else
				return false;
		}

		if (plat.returnBoite(7, 7) == 3 && !horizontal) {
			if(yPos == 7) {
				if(xPos > 7 || (xPos + mot.length) < 7 )
					return false;
			} else
				return false;
		}


		// vérifier qu'il n'y a pas de majuscule
		for (int i = 0; i < mot.length; ++i) {
			if ((65 <= mot[i]) && (mot[i] <= 90))
				mot[i] += 32;
		}

		if (horizontal) {
			// Vérifie primordialement dans l'ordre vertical chacune des lettres et à la fin
			// il vérifie chaque extrême avec ses voisins horizontaux
			int topX = 0;
			int subX = 0;
			int letterCount = 0;

			for (int i = yPos; i < (yPos + mot.length); ++i) {
				letterCount = 1;
				// Il commence à se déplacer vers la droite à partir du placement de la première
				// lettre
				// Je vérifie au-dessus de la ligne de lettres

				if(plat.returnBoite(xPos, i) <= 6) {
					subX = xPos;
					while (plat.returnBoite(subX - 1, i) >= 5) {
						letterCount++;
						subX--;
					}

					// Je vérifie sous la ligne de lettres
					topX = xPos;
					while (plat.returnBoite(topX + 1, i) >= 5) {
						letterCount++;
						topX++;
					}
				}

				// La lettre initiale est comptée deux fois, donc le compteur doit avoir une
				// valeur supérieure à 2 pour être admis
				if (letterCount >= 2) {

					// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
					// d'un mot reconnaissable
					String possibleMot = "";
					for (int j = subX; j <= topX; ++j) { //
						if (j == xPos)
							possibleMot += Character.toString((char) mot[i - yPos]);
						else
							possibleMot += Character.toString((char) plat.returnBoite(j, i));
					}

					if (dico.contains('"' + possibleMot + '"') && !isWordUsed(possibleMot)) {

						// Le mot trouvé a été reconnu dans le dictionnaire !!!!!

						newWordUsed(possibleMot);
						nomJoueur.nouveauScore(
								nomJoueur.verifierScore() + nomJoueur.calculerScore(mot, subX, yPos, false, plat));
						combinations++;
					} else {
						return false;
					}
				}
			}

			// Fin des révisions verticales, il est maintenant temps de réviser horizontalement
			int subY = yPos;
			int topY = yPos + mot.length - 1;
			letterCount = mot.length;

			// Je vérifie à gauche du premier élément de la ligne de lettres
			while (((int) plat.returnBoite(xPos, subY - 1) >= 5)) {
				letterCount++;
				subY--;
			}

			// Je vérifie à droite de la ligne de lettres
			while (((int) plat.returnBoite(xPos, topY + 1) >= 5)) {
				letterCount++;
				topY++;
			}


			// Encore une fois, la lettre initiale est comptée deux fois, donc le compteur
			// doit avoir une valeur supérieure à 2 pour être admis
			if (letterCount > 2) {

				// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
				// d'un mot reconnaissable
				String possibleMot = "";
				Boolean usedPlateau = false;
				for (int i = subY; i <= topY; ++i) { //
					if (plat.returnBoite(xPos, i) < 5 && plat.returnBoite(xPos, i) >= 0)
						possibleMot += Character.toString((char) mot[i - yPos]);
					else {
						possibleMot += Character.toString((char) plat.returnBoite(xPos, i));
						usedPlateau = true;
					}
				}

				if (plat.returnBoite(7, 7) != 3 && usedPlateau == false)
					return false;

				if (dico.contains('"' + possibleMot + '"') && !isWordUsed(possibleMot)) {

					// Le mot trouvé a été reconnu dans le dictionnaire !!!!!
					newWordUsed(possibleMot);
					nomJoueur.nouveauScore(
							nomJoueur.verifierScore() + nomJoueur.calculerScore(mot, xPos, subY, true, plat));
					combinations++;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			// Vérifie principalement dans l'ordre horizontal chacune des lettres et à la
			// fin il vérifie chaque extrême avec ses voisins verticaux


			int subY = yPos;
			int topY = yPos + mot.length - 1;
			int letterCount = mot.length;

			for (int i = xPos; i < (xPos + mot.length); ++i) {
				letterCount = 1;

				// Commence à descendre du placement de la première lettre
				subY = yPos;

				// Si le mot a déjà été placé, ne vérifiez pas s'il crée un mot dans l'autre sens car il sera évidemment lié à un mot déjà créé
				if(plat.returnBoite(i, yPos) <= 6) {
					// Je vérifie à gauche du premier élément de la ligne de lettres
					while (plat.returnBoite(i, subY - 1) >= 5) {
						letterCount++;
						subY--;
					}

					topY = yPos;
					// Je vérifie à droite du même élément dans la rangée de lettres
					while (plat.returnBoite(i, topY + 1) >= 5) {
						letterCount++;
						topY++;
					}
				}


				// Encore une fois, la lettre initiale est comptée deux fois, donc le compteur
				// doit avoir une valeur supérieure à 2 pour être admis
				if (letterCount >= 2) {

					// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
					// d'un mot reconnaissable
					String possibleMot = "";
					for (int j = subY; j <= topY; ++j) { //
						if (j == yPos)
							possibleMot += Character.toString((char) mot[i - xPos]);
						else
							possibleMot += Character.toString((char) plat.returnBoite(i, j));
					}

					if (dico.contains('"' + possibleMot + '"') && !isWordUsed(possibleMot)) {
						// Le mot trouvé a été reconnu dans le dictionnaire !!!!!

						newWordUsed(possibleMot);
						nomJoueur.nouveauScore(
								nomJoueur.verifierScore() + nomJoueur.calculerScore(mot, xPos, subY, true, plat));
						combinations++;
					} else {
						return false;
					}
				}
			}

			// Fin des révisions horizontalement, il est maintenant temps de réviser
			// verticalement
			int subX = xPos;
			int topX = xPos + mot.length - 1;
			letterCount = mot.length;

			// Je vérifie au-dessus du premier élément de la ligne de lettres
			while (plat.returnBoite(subX - 1, yPos) >= 5) {
				letterCount++;
				subX--;
			}

			// Je vérifie sous la ligne de lettres
			while (plat.returnBoite(topX + 1, yPos) >= 5) {
				letterCount++;
				topX++;
			}

			// Encore une fois, la lettre initiale est comptée deux fois, donc le compteur
			// doit avoir une valeur supérieure à 2 pour être admis
			if (letterCount > 2) {
				//System.out.println("Entré car il y a 3 lettres ou plus");

				// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
				// d'un mot reconnaissable
				String possibleMot = "";
				Boolean usedPlateau = false;
				for (int i = subX; i <= topX; ++i) { //
					if (plat.returnBoite(i, yPos) < 5 && plat.returnBoite(i, yPos) >= 0) {
						possibleMot += Character.toString((char) mot[i - xPos]);
					} else {
						possibleMot += Character.toString((char) plat.returnBoite(i, yPos));
						usedPlateau = true;
					}
				}

				if (plat.returnBoite(7, 7) != 3 && usedPlateau == false)
					return false;

				if (dico.contains('"' + possibleMot + '"') && !isWordUsed(possibleMot)) {
					// Le mot trouvé a été reconnu dans le dictionnaire !!!!!

					newWordUsed(possibleMot);
					nomJoueur.nouveauScore(
							nomJoueur.verifierScore() + nomJoueur.calculerScore(mot, subX, yPos, false, plat));
					combinations++;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		// Si il y a quelque combination de mot trouvï¿½, supprimer les lettres
		// utilises.
		if (combinations > 0) {
			// Supprimer les lettres de la main (sans compter les lettres que sont deja sur
			// le table pour former la mot) et
			// mettre de nouvelle lettre tirer au hazard parmis celle encore disponible

			// mot: C'est le mot qui est forme sur le Plateau
			// main: C'est la main avec toutes les lettres a ta disposition.
			int j; // index pour trouver la position de la lettre selectione (mot[i]) dans la main
			Boolean trouve; // Boolean qui stope si le lettre est trouvï¿½
			for (int i = 0; i < mot.length; ++i) {

				if (!(mot[i] == plat.returnBoite(xPos, yPos + i) && horizontal)
						&& !(mot[i] == plat.returnBoite(xPos + i, yPos) && !horizontal)) {
					// Lettre c'est ne pas deja pose d'avant
					// trouve le lettre dans notre main et echanger

					j = 0; // On doit trouver une AUTRE lettre dans notre main, donc on restart index et Boolean
					trouve = false;
					while (j < 7 && !trouve) { // recherche un a un sur la main jusqu'a que la lettre soit trouvï¿½ dans la main ....
						if (mot[i] == nomJoueur.verifierLettre(j)) { // si la lettre que je suis maintenant est la  lettre chercher
							this.setNombreLettre(nomJoueur.echangerLettre(j, getNombreLettre()));
							trouve = true; // pour ï¿½viter la boucle infini, clore cette recherche
						} else
							j++; // Je n'ai pas trouve, on passe a l'index suivant
					} // Fin de la recherche pour la lettre utiliser
				}

			} // Fin de la recherche de toute les lettres utiliser

			// Inserter mot dans le Plateau
			plat.modifierPlateau(mot, xPos, yPos, horizontal);

			return true;
		}

		return false;
	}

	/* Fonction main pour debugging */

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("\n\n\n === EXEMPLE D'UNE GAME EN MODE VS ORDINATEUR === \n");
		System.out.print("\n\n\tEntrée vos pseudo!");
		System.out.print("\n\tNom du Joueur: ");
		String premiereJoueur = scanner.nextLine();
		System.out.print("\n\tQuelle difficulté ?");
		System.out.print("\n\t1: Gentil");
		System.out.print("\n\t2: Normal");
		System.out.print("\n\t3: Sans pitié");
		System.out.print("\n\n\tQuelle option choisi tu ?: ");
		int difficulte = Integer.parseInt(scanner.nextLine());
		MatchIA gameInstance = new MatchIA(premiereJoueur, difficulte); // A MODIFIER

		int iterations = 0;
		int choice = -1;

		do {
			System.out.print("\n\n\t\t == Game Menu ==\n\n");

			System.out.print("\n\t Tour actuel: " + gameInstance.tourActuel().verifierNom() + "\n\n");
			gameInstance.montrerTableauMultijoueur();

			if (gameInstance.isPlayerTurn()) {

				System.out.print("\n\n\tStatut de la main du joueur");
				gameInstance.montrerMainMultijoueur(gameInstance.tourActuel());

				System.out.print("\n\n\tOption 1: Mettre un mot sur le plateau");
				System.out.print("\n\tOption 2: Jetter des lettres");
				System.out.print("\n\tOption 3: Passer mon tour");
				System.out.print("\n\tOption 4: Suggestion Mots");

				System.out.print("\n\n\tQuelle option choisi tu ?: ");
				choice = Integer.parseInt(scanner.nextLine());
				switch (choice) {
				case 1: // Jouer avec les mots dispo
					System.out.print("\n\nQuel mot veux-tu formé ?: ");
					String readWord = scanner.nextLine();

					System.out.print("Quelle position? (x): ");
					int readXPos = Integer.parseInt(scanner.nextLine());

					System.out.print("Quelle position? (y): ");
					int readYPos = Integer.parseInt(scanner.nextLine());

					System.out.print("Horizontal (1) ou vertical (0)?: ");
					int readHorizontal = Integer.parseInt(scanner.nextLine());

					int[] motArray = new int[readWord.length()];
					for (int i = 0; i < readWord.length(); ++i)
						motArray[i] = (int) readWord.charAt(i);

					if (gameInstance.proceedWithAction(gameInstance.tourActuel(), motArray, readXPos, readYPos,
							readHorizontal == 1)) {
						System.out.print("\n\nMot inséré! Félicitations!! ");
						System.out.print("\n\nJoueur " + gameInstance.tourActuel().verifierNom() + " a un total de "
								+ gameInstance.tourActuel().verifierScore() + " points!");

						if (gameInstance.asGagne()) {
							Joueur gagnant = gameInstance.pointsFin();
							if (gagnant.verifierNom() == "Match Null") {
								// La partie s'est terminée par une égalité (les deux joueurs ont le même nombre
								// de points)
								System.out.print("\n\n\t\t === MATCH NULL ===");
								System.out.print("\n\n\t\t   Classement final!!");
								System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
										+ gameInstance.joueur.verifierScore() + " points!");
								System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
							} else {
								System.out.print("\n\n\t\t=== Le gagnant est " + gagnant.verifierNom() + "!!! ===");
								System.out.print("\n\n\t\t   Classement final!!");
								System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
										+ gameInstance.joueur.verifierScore() + " points!");
								System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
							}
							iterations = 5;
						} else {
							gameInstance.suivantTour();
							System.out.print("\n\n\tStatut actuel!");
							System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
									+ gameInstance.joueur.verifierScore() + " points!");
							System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
							iterations++;
						}
					} else {
						System.out.print("\n\nÇa ne marchera pas ... ");
						System.out.print("\nRéessayer ^^");
					}
					break;
				case 2: // Jetter lettres
					int listJetter[] = { 0, 0, 0, 0, 0, 0, 0 };
					System.out.print("\n\n\tStatut de la main du joueur");
					gameInstance.montrerMainMultijoueur(gameInstance.tourActuel());

					System.out.print("\n\n Indiquez quelle lettre vous voulez jetter (avec index entre 1 et 7): ");
					int counter = 0;
					boolean finished = false;

					while (counter <= 7 && !finished) {
						int indexJetter = Integer.parseInt(scanner.nextLine());
						if ((1 <= indexJetter) && (indexJetter <= 7)) {
							listJetter[indexJetter - 1] = 1;
							counter++;
						} else {
							if (indexJetter == 0)
								finished = true;
							else
								System.out.print("\n ERREUR: Je ne connais pas cette lettre... ");
						}
						System.out.print(
								"\n Avez-vous fini (0) ? ou voulez vous continuer de changer des letrres (avec index entre 1 et 7) ? ");
					}

					for (int i = 0; i < listJetter.length; ++i) {
						if (listJetter[i] == 1)
							gameInstance.setNombreLettre(gameInstance.tourActuel().jeterLettre(i, gameInstance.getNombreLettre()));
					}

					gameInstance.suivantTour();
					break;
				case 3:
					System.out.print("\n\n Le joueur passe son tour... ");
					if (gameInstance.lettresRestant() == 0)
						gameInstance.tourActuel().incrementTourPasser();

					if (gameInstance.tourActuel().verifierTourPasser()
							&& gameInstance.adversaireActuel().verifierTourPasser()) {
						// There are no more letters and both players have nothing to play
						System.out.print("\n\n\t\t=== There aren't more combinations available!!! ===");
						if (gameInstance.tourActuel().verifierScore() == gameInstance.adversaireActuel()
								.verifierScore()) {
							// La partie s'est terminée par une égalité (les deux joueurs ont le même nombre
							// de points)
							System.out.print("\n\n\t\t === MATCH NULL ===");
							System.out.print("\n\n\t\t   Classement final!!");
							System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
									+ gameInstance.joueur.verifierScore() + " points!");
							System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
						} else {
							Joueur gagnant = (gameInstance.tourActuel().verifierScore() > gameInstance
									.adversaireActuel().verifierScore()) ? gameInstance.tourActuel()
											: gameInstance.adversaireActuel();
							System.out.print("\n\n\t\t=== Le gagnant est " + gagnant.verifierNom() + "!!! ===");
							System.out.print("\n\n\t\t   Classement final!!");
							System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
									+ gameInstance.joueur.verifierScore() + " points!");
							System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
						}
						// Déclarez la fin du jeu
						iterations = 5;
					} else {
						// Le jeu n'a pas pris fin pour le moment, passez au tour suivant
						gameInstance.suivantTour();
						iterations++;
					}
					break;

				case 4:
					System.out.print("\n\n Suggestion de mots ");
					permutationSearch pS = new permutationSearch(dicoAnagram, dico, plat,
							gameInstance.tourActuel().verifierMain(), gameInstance.usedWords);

					System.out.println(pS.totalPossibleWords() + " résultats trouvés! \n");

					if (pS.totalPossibleWords() > 25) {
						System.out
								.println("Il y a plein de resulats, donc je vais montrer seulement les 25 meilleurs\n");
						for (int i = 0; i < 25; ++i)
							System.out.println("Mots: " + pS.possibleWord(i).verifierNom() + " pour un total de "
									+ pS.possibleWord(i).verifierPoints() + " points! (PosX:"
									+ pS.possibleWord(i).verifierX() + "|PosY:" + pS.possibleWord(i).verifierY() + ")");
					} else {
						for (int i = 0; i < pS.totalPossibleWords(); ++i)
							System.out.println("Mots: " + pS.possibleWord(i).verifierNom() + " pour un total de "
									+ pS.possibleWord(i).verifierPoints() + " points! (PosX:"
									+ pS.possibleWord(i).verifierX() + "|PosY:" + pS.possibleWord(i).verifierY() + ")");
					}
					break;

				default:
					System.out.print("\n ERREUR: Je ne connais pas cette mode... ");
				}
			} else {
				System.out.println("\n\tL'ordi fait son action...");
				switch (gameInstance.completeIATurn()) {
				case 1:
					// Tous les tours nécessaires pour terminer le jeu sont passés, terminer le jeu
					iterations = 5;
					break;
				case 0:
					// IA a inséré une lettre
					System.out.print("\n\nJoueur " + gameInstance.tourActuel().verifierNom() + " a un total de "
							+ gameInstance.tourActuel().verifierScore() + " points!");

					if (gameInstance.asGagne()) {
						Joueur gagnant = gameInstance.pointsFin();
						if (gagnant.verifierNom() == "Match Null") {
							// La partie s'est terminée par une égalité (les deux joueurs ont le même nombre
							// de points)
							System.out.print("\n\n\t\t === MATCH NULL ===");
							System.out.print("\n\n\t\t   Classement final!!");
							System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
									+ gameInstance.joueur.verifierScore() + " points!");
							System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
						} else {
							System.out.print("\n\n\t\t=== Le gagnant est " + gagnant.verifierNom() + "!!! ===");
							System.out.print("\n\n\t\t   Classement final!!");
							System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
									+ gameInstance.joueur.verifierScore() + " points!");
							System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
						}
						iterations = 5;
					} else {
						gameInstance.suivantTour();
						System.out.print("\n\n\tStatut actuel!");
						System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
								+ gameInstance.joueur.verifierScore() + " points!");
						System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
						iterations++;
					}
					break;
				case -1:
					// IA ne peux pas insérer de lettre , IA échangé ses lettres
					System.out.print("\n\n L'IA a decider jetter ses lettres...");
					gameInstance.suivantTour();
					System.out.print("\n\n\tStatut actuel!");
					System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
							+ gameInstance.joueur.verifierScore() + " points!");
					System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
					iterations++;
					break;
				case -2:
					// IA ne peut pas échanger ses lettres et a passé le tour sans rien faire de pertinent
					System.out.print("\n\n L'IA NEW MESSAGE THAT SAYS THAT THE IA CANT DO ANYTHING AT ALL...");
					gameInstance.suivantTour();
					System.out.print("\n\n\tStatut actuel!");
					System.out.print("\n\t\tJoueur " + gameInstance.joueur.verifierNom() + ": "
							+ gameInstance.joueur.verifierScore() + " points!");
					System.out.print("\n\t\tIA: " + gameInstance.ordi.verifierScore() + " points!");
					iterations++;
					break;
				}
			}
		} while (iterations < 5);
	}

	public ArrayList<Integer> getNombreLettre() {
		return nombreLettre;
	}

	public void setNombreLettre(ArrayList<Integer> nombreLettre) {
		this.nombreLettre = nombreLettre;
	}

} // Classe MatchIA Terminé
