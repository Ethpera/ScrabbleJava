import java.io.IOException;
import java.util.*;

public class permutationSearch {

	ArrayList<String> listAnagram;
	ArrayList<Mot> listMots;
	ArrayList<String> usedMots;
	anagramDictionnaire aDico;
	Dictionnaire dico;
	Plateau plat;
	int[] main;

	/* === Constructeur === */

	public permutationSearch(anagramDictionnaire aD, Dictionnaire d, Plateau p, int[] m, ArrayList<String> uM) {
		listAnagram = new ArrayList<String>();
		listMots = new ArrayList<Mot>();
		dico = d;
		usedMots = uM;
		aDico = aD;
		plat = p;
		main = m;
		suggestionMots();
	}

	/* === Observateurs === */

	public int totalPossibleWords() {
		return listMots.size();
	}

	public Mot possibleWord(int index) {
		return listMots.get(index);
	}

	/* === Modificateurs === */

	public void combinationFinder(String str, String base, ArrayList<String> permutationsTrouve) {
		// Ne faites ce processus que s'il y a 4 lettres dans le mot (car nous allons en
		// supprimer une à la fois et elle ne peut pas être inférieure à 3)
		if ((base.length() >= 3 && str.length() > 0) || (base.length() >= 2 && str.length() > 1)
				|| (base.length() >= 1 && str.length() > 2) || (base.length() >= 0 && str.length() > 3)) {
			// Supprimer et ajouter à nouveau chaque lettre autant de fois qu'il y a de lettres dans la chaîne
			for (int i = 0; i < str.length(); ++i) {
				// Enregistrez la lettre avant de la supprimer
				char aux = str.charAt(i);
				// Utilisez StringBuilder pour tout simplifier
				StringBuilder newStr = new StringBuilder(str);
				// Supprime le caractère de la chaîne à la position "i"
				newStr.deleteCharAt(i);
				// C'est un nouveau mot! Enregistrez dans la base de données la nouvelle
				// permutation avec les lettres nécessaires du tableau
				permutationsTrouve.add(base + newStr.toString());
				// Rappelle cette fonction mais sans la lettre que l'on vient de supprimer
				combinationFinder(newStr.toString(), base, permutationsTrouve);
				// Insère la lettre que nous avons utilisée et passe à une autre itération de la
				// fonction
				newStr.insert(i, aux);
			}
		}
	}

	public void creerPermutations(ArrayList<Integer> lettresDePlat, int xPos, int yPos, boolean horizontal) {
		ArrayList<String> permutationsTrouve = new ArrayList<String>();

		String lettresMain = "";
		String motPlateau = "";
		for (int i = 0; i < lettresDePlat.size(); ++i)
			motPlateau += (char) (int) lettresDePlat.get(i);

		for (int i = 0; i < 7; ++i) {
			if (main[i] != 0)
				lettresMain += (char) main[i];
		}

		// Actuellement, letterMain n'a que les lettres de la main du joueur. Nous
		// ajoutons les lettres du tableau plus tard (car le cant me modifié ou
		// supprimé)
		// Vérification de toutes les combinaisons possibles
		permutationsTrouve.add(motPlateau + lettresMain);
		combinationFinder(lettresMain, motPlateau, permutationsTrouve);

		int index = 0;
		while (index < permutationsTrouve.size()) {
			if ((!permutationsTrouve.get(index).contains(motPlateau))
					|| (listAnagram.contains(permutationsTrouve.get(index)))
					|| (Collections.frequency(permutationsTrouve, permutationsTrouve.get(index)) > 1)
					|| (aDico.contains('"' + permutationsTrouve.get(index) + '"') == null)) {
				permutationsTrouve.remove(index);
			} else {
				index++;
			}
		}

		// Jusqu'à ici, le seul processus est de trouver toutes les permutations
		// existantes
		// Maintenant, le reste du code mettra à jour la sortie de l'algorithme
		String auxStr = "";

		ArrayList<String> realWordList = new ArrayList<String>();
		// Échangez l'anagramme avec de vrais mots
		for (int indexMotList = 0; indexMotList < permutationsTrouve.size(); ++indexMotList) {
			List<String> dicoTranslator = aDico.contains('"' + permutationsTrouve.get(indexMotList) + '"');
			for (int indexMotDico = 0; indexMotDico < dicoTranslator.size(); ++indexMotDico) {
				// Lecture de chaque mot de chaque entité dans le dictionnaire dont nous avons
				// trouvé les résultats
				auxStr = dicoTranslator.get(indexMotDico);
				// (supprimer le "dans les deux extrêmes)
				auxStr = auxStr.substring(1, auxStr.length() - 1);
				// Insérer un mot dans wordList
				realWordList.add(auxStr);
			}
		}

		// Vérifie s'il essaie d'insérer un mot qui a déjà été inséré avant
		index = 0;
		while (index < realWordList.size()) {
			if ((Collections.frequency(realWordList, realWordList.get(index)) > 1)
					|| (Collections.frequency(usedMots, realWordList.get(index)) == 1))
				realWordList.remove(index);
			else
				index++;
		}

		// Vérifier si le mot peut réellement être placé dans le tableau
		int[] scoreLettre = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10 };
		int sum = -1;
		index = 0;
		boolean delete = false;
		int letterCount = 1;
		ArrayList<Integer> mannequinSet = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0));
		Joueur mannequin = new Joueur("-", mannequinSet);

		if (horizontal) {
			// Faites simplement défiler le y
			while (index < realWordList.size()) {
				delete = false;
				int start = yPos - realWordList.get(index).indexOf(motPlateau);
				int startWord = yPos;
				int endWord = (motPlateau.length() == 0) ? yPos : yPos + motPlateau.length() - 1;
				int end = start + realWordList.get(index).length() - 1;
				sum = 0;
				int subX = 0;
				int topX = 0;


				if (realWordList.get(index).indexOf(motPlateau) == -1)
					delete = true;

				if (start >= 0 && end <= 14 && !delete) {
					//
					// Tout vérifier avant le mot trouvé dans le tableau
					//
					if(plat.returnBoite(xPos, start-1) > 5 || plat.returnBoite(xPos, end+1) > 5 ) {
						delete = true;
					}

					for (int itY = start; itY < startWord && !delete; ++itY) {
						if (plat.returnBoite(xPos, itY) > 5)
							delete = true;
						else {
							// Vérifier verticalement s'il forme involontairement un nouveau mot valide
							letterCount = 1;

							subX = xPos;
							while (plat.returnBoite(subX - 1, itY) >= 5) {
								letterCount++;
								subX--;
							}

							// Je vérifie sous la ligne de lettres
							topX = xPos;
							while (plat.returnBoite(topX + 1, itY) >= 5) {
								letterCount++;
								topX++;
							}

							// La lettre initiale est comptée deux fois, donc le compteur doit avoir une
							// valeur supérieure à 2 pour être admis
							if (letterCount >= 2) {
								// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
								// d'un mot reconnaissable
								String possibleMot = "";
								for (int j = subX; j <= topX; ++j) { //
									if (j == xPos) {
										possibleMot += realWordList.get(index).charAt(itY - start);
									}else
										possibleMot += Character.toString((char) plat.returnBoite(j, itY));
								}

								if (dico.contains('"' + possibleMot + '"') && (Collections.frequency(usedMots, possibleMot) == 0)) {
									// Le mot trouvé a été reconnu dans le dictionnaire !!!!!
									int[] wordChar = new int[possibleMot.length()];
									for (int letterIndex = 0; letterIndex < possibleMot.length(); ++letterIndex)
										wordChar[letterIndex] = (int) possibleMot.charAt(letterIndex);

									sum += mannequin.calculerScore(wordChar, subX, itY, true, plat);
								} else {
									delete = true;
								}
							}
						}
					}

					//
					// Tout vérifier après le mot trouvé déjà positionné dans le tableau
					for (int itY = endWord + 1; itY <= end && delete != true; ++itY) {
						if (plat.returnBoite(xPos, itY) > 5)
							delete = true;
						else {
							// Vérifier verticalement s'il forme involontairement un nouveau mot valide
							letterCount = 1;

							subX = xPos;
							while (plat.returnBoite(subX - 1, itY) >= 5) {
								letterCount++;
								subX--;
							}

							// Je vérifie sous la ligne de lettres
							topX = xPos;
							while (plat.returnBoite(topX + 1, itY) >= 5) {
								letterCount++;
								topX++;
							}

							// La lettre initiale est comptée deux fois, donc le compteur doit avoir une
							// valeur supérieure à 2 pour être admis
							if (letterCount >= 2) {
								// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit d'un mot reconnaissable
								String possibleMot = "";
								for (int j = subX; j <= topX; ++j) { //
									if (j == xPos) {
										possibleMot += realWordList.get(index).charAt(itY - start);
									} else
										possibleMot += Character.toString((char) plat.returnBoite(j, itY));
								}

								if (dico.contains('"' + possibleMot + '"') && (Collections.frequency(usedMots, possibleMot) == 0)) {
									// Le mot trouvé a été reconnu dans le dictionnaire !!!!!
									int[] wordChar = new int[possibleMot.length()];
									for (int letterIndex = 0; letterIndex < possibleMot.length(); ++letterIndex)
										wordChar[letterIndex] = (int) possibleMot.charAt(letterIndex);

									sum += mannequin.calculerScore(wordChar, subX, itY, true, plat);
								} else {
									delete = true;
								}
							}
						}
					}
				} else
					delete = true;

				if (delete == true) {
					realWordList.remove(index);
				} else {
					// Le mot peut être placé dans le tableau et suit toutes les directives à
					// insérer dans le tableau, insérez-le

					int[] wordChar = new int[realWordList.get(index).length()];
					for (int letterIndex = 0; letterIndex < realWordList.get(index).length(); ++letterIndex)
						wordChar[letterIndex] = (int) realWordList.get(index).charAt(letterIndex);

					sum += mannequin.calculerScore(wordChar, xPos, start, true, plat);

					Mot auxMotVariable = new Mot(realWordList.get(index), xPos, start, true, sum);
					listMots.add(auxMotVariable);
					realWordList.remove(index);
				}
			}
		} else {
			// Faites simplement défiler le x
			while (index < realWordList.size()) {
				delete = false;
				int start = xPos - realWordList.get(index).indexOf(motPlateau);
				int startWord = xPos;
				int endWord = xPos + motPlateau.length() - 1;
				int end = start + realWordList.get(index).length() - 1;
				sum = 0;
				int subY = 0;
				int topY = 0;

				if (realWordList.get(index).indexOf(motPlateau) == -1)
					delete = true;

				if(plat.returnBoite(start - 1, yPos) > 5 || plat.returnBoite(end+1, yPos) > 5 ) {
					delete = true;
				}

				if (start >= 0 && end <= 14 && !delete) {
					for (int itX = start; itX < startWord && delete != true; ++itX) {
						if (plat.returnBoite(itX, yPos) > 5)
							delete = true;
						else {
							// Vérifier horizontalement s'il forme involontairement un nouveau mot valide
							letterCount = 1;

							subY = yPos;
							while (plat.returnBoite(itX, subY - 1) >= 5) {
								letterCount++;
								subY--;
							}

							// Je vérifie sous la ligne de lettres
							topY = yPos;
							while (plat.returnBoite(itX, topY + 1) >= 5) {
								letterCount++;
								topY++;
							}

							// La lettre initiale est comptée deux fois, donc le compteur doit avoir une
							// valeur supérieure à 2 pour être admis
							if (letterCount >= 2) {
								// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
								// d'un mot reconnaissable
								String possibleMot = "";
								for (int j = subY; j <= topY; ++j) { //
									if (j == yPos) {
										possibleMot += realWordList.get(index).charAt(itX - start);
									} else
										possibleMot += Character.toString((char) plat.returnBoite(itX, j));
								}

								if (dico.contains('"' + possibleMot + '"') && (Collections.frequency(usedMots, possibleMot) == 0)) {
									// Le mot trouvé a été reconnu dans le dictionnaire !!!!!
									int[] wordChar = new int[possibleMot.length()];
									for (int letterIndex = 0; letterIndex < possibleMot.length(); ++letterIndex)
										wordChar[letterIndex] = (int) possibleMot.charAt(letterIndex);

									sum += mannequin.calculerScore(wordChar, itX, subY, false, plat);
								} else {
									delete = true;
								}
							}
						}
					}

					for (int itX = endWord + 1; itX <= end && delete != true; ++itX) {
						if (plat.returnBoite(itX, yPos) > 5)
							delete = true;
						else {
							// Vérifier horizontalement s'il forme involontairement un nouveau mot valide
							letterCount = 1;

							subY = yPos;
							while (plat.returnBoite(itX, subY - 1) >= 5) {
								letterCount++;
								subY--;
							}

							// Je vérifie sous la ligne de lettres
							topY = yPos;
							while (plat.returnBoite(itX, topY + 1) >= 5) {
								letterCount++;
								topY++;
							}

							// La lettre initiale est comptée deux fois, donc le compteur doit avoir une
							// valeur supérieure à 2 pour être admis
							if (letterCount >= 2) {
								// Une séquence de lettres a été trouvée, elle doit être calculée s'il s'agit
								// d'un mot reconnaissable
								String possibleMot = "";
								for (int j = subY; j <= topY; ++j) { //
									if (j == yPos) {
										possibleMot += realWordList.get(index).charAt(itX - start);
									} else
										possibleMot += Character.toString((char) plat.returnBoite(itX, j));
								}
								if (dico.contains('"' + possibleMot + '"') && (Collections.frequency(usedMots, possibleMot) == 0)) {
									// Le mot trouvé a été reconnu dans le dictionnaire !!!!!
									int[] wordChar = new int[possibleMot.length()];
									for (int letterIndex = 0; letterIndex < possibleMot.length(); ++letterIndex)
										wordChar[letterIndex] = (int) possibleMot.charAt(letterIndex);

									sum += mannequin.calculerScore(wordChar, itX, subY, false, plat);
								} else {
									delete = true;
								}
							}
						}
					}
				} else
					delete = true;

				if (delete == true) {
					realWordList.remove(index);
				} else {
					// Le mot peut être placé dans le tableau et suit toutes les directives à
					// insérer dans le tableau, insérez-le
					int[] wordChar = new int[realWordList.get(index).length()];
					for (int letterIndex = 0; letterIndex < realWordList.get(index).length(); ++letterIndex)
						wordChar[letterIndex] = (int) realWordList.get(index).charAt(letterIndex);

					sum += mannequin.calculerScore(wordChar, start, yPos, false, plat);

					Mot auxMotVariable = new Mot(realWordList.get(index), start, yPos, false, sum);
					listMots.add(auxMotVariable);
					index++;
				}

			}
		}

		// Ordre basé sur des points plus élevés par mot en premier
		// Fabriqué de manière à ce que les deux listes (mot et points) effectuent les
		// mêmes mouvements de commande

		for (int i = 0; i < totalPossibleWords(); i++) {
			for (int j = i + 1; j < totalPossibleWords(); j++) {
				if (possibleWord(i).verifierPoints() < possibleWord(j).verifierPoints()) {
					Collections.swap(listMots, i, j);
				}
			}
		}
	}

	public void suggestionMots() {
		ArrayList<Integer> lettresDePlat = new ArrayList<Integer>();
		listMots.clear();

		// Tout d'abord, je vais vérifier s'il y a des mots dans le tableau pour gagner
		// du temps d'exécution
		if (plat.returnBoite(7, 7) == 3) {
			// Il n'y a pas de mots dans le tableau
			// La seule permutation que nous pouvons trouver est celle avec les lettres dans
			// notre main
			creerPermutations(lettresDePlat, 7, 7, true);
		} else {
			// Regardant horizontalement
			for (int i = 0; i < 15; ++i) {
				for (int j = 0; j < 15; ++j) {
					// Rechercher la table horizontalement
					if ((plat.returnBoite(i, j) > 5) && (j == 0 || plat.returnBoite(i, j - 1) <= 5)) {
						lettresDePlat.clear();
						lettresDePlat.add(plat.returnBoite(i, j));
						// Lettre trouvée
						int indexJ = j;
						while (plat.returnBoite(i, indexJ + 1) > 5) {
							indexJ++;
							lettresDePlat.add(plat.returnBoite(i, indexJ));
						}
						// Terminé avec la succession de lettres ensemble
						creerPermutations(lettresDePlat, i, j, true);
					}
				}
			}

			// Regardant verticalement
			for (int i = 0; i < 15; ++i) {
				for (int j = 0; j < 15; ++j) {
					// Recherche de table verticalement
					if ((plat.returnBoite(i, j) > 5) && (i == 0 || plat.returnBoite(i - 1, j) <= 5)) {
						lettresDePlat.clear();
						lettresDePlat.add(plat.returnBoite(i, j));
						// Lettre trouvée
						int indexI = i;
						while (plat.returnBoite(indexI + 1, j) > 5) {
							indexI++;
							lettresDePlat.add(plat.returnBoite(indexI, j));
						}
						// Terminé avec la succession de lettres ensemble
						creerPermutations(lettresDePlat, i, j, false);
					}
				}
			}

		} // Fin si -> Dans le cas où il y a déjà des mots sur la table

	} // Fin de fonction

	public static void main(String[] args) throws IOException {
		anagramDictionnaire aD = new anagramDictionnaire("dico.txt");
		Dictionnaire d = new Dictionnaire("dico.txt");
		Plateau p = new Plateau();
		int[] m = { 100, 101, 102, 112, 108, 117, 115 };
		ArrayList<String> uM = new ArrayList<String>();

		permutationSearch pS = new permutationSearch(aD, d, p, m, uM);

		System.out.println(pS.totalPossibleWords() + " résultats trouvés! \n");

		for (int i = 0; i < pS.totalPossibleWords(); ++i)
			System.out.println("Mots: " + pS.possibleWord(i).verifierNom() + " pour un total de "
					+ pS.possibleWord(i).verifierPoints() + " points! (PosX:" + pS.possibleWord(i).verifierX()
					+ "|PosY:" + pS.possibleWord(i).verifierY() + ")");

	}
} // Fin de la classe permutationSearch
