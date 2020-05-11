import java.util.*;
import java.util.stream.*;

public class Joueur {
	/* === Paramètres de classe === */

	private static int[] scoreLettre = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10,
			10 }; // , 0 }; // tout l'alphabet + Jocker
	private int[] main = { -1, -1, -1, -1, -1, -1, -1 }; // peut s'adapter mais il est demander d'avoir 7 lettre
															// en main
	private int score = 0;
	private String nom = "";
	private boolean monTour = false;
	int tourPasser = 0;

	private ArrayList<Integer> totalLettersCreation = null;

	/* === Constructeur === */

	public Joueur(String nouveauNom, ArrayList<Integer> nombreLettre) {
		setNom(nouveauNom);
		// Mettre tout les lettres necessaires dans la main
		for (int i = 0; i < main.length; i++)
			nombreLettre = echangerLettre(i, nombreLettre);

		totalLettersCreation = nombreLettre;
	}

	/* === Observateurs === */

	public ArrayList<Integer> remainingLettersPostBuilder() {
		return totalLettersCreation;
	}

	public char verifierLettre(int index) {
		// Afficher la lettre avec l'index de localisation
		return (char) main[index];
	}

	public int[] verifierMain() {
		return main;
	}

	public String verifierNom() {
		return nom;
	}

	public int verifierScore() {
		return score;
	}

	public boolean verifierTour() {
		return monTour;
	}

	public boolean verifierTourPasser() {
		return (tourPasser >= 3);
	}

	/* === Modificateurs === */

	public void incrementTourPasser() {
		tourPasser++;
	}

	public void resetTourPasser() {
		tourPasser = 0;
	}

	public void setNom(String nouveauNom) {
		nom = nouveauNom;
	}

	public void commencerTour() {
		monTour = true;
	}

	public void terminerTour() {
		monTour = false;
	}

	public void nouveauScore(int nouveauScoreObtenu) {
		score = nouveauScoreObtenu;
	}

	public int donneLettre(ArrayList<Integer> nombreLettre) {
		if (nombreLettre.size() != 0) { // Si il'y a plus de lettres alors la somme de toute les cellules
										// du tableau "nombreLettre" est égale a 0
			Random rand = new Random();
			Integer index = rand.nextInt(nombreLettre.size());

			int value = nombreLettre.get(index); // enlève la lettre utiliser
			nombreLettre.remove((int) index);
			return (97 + value); // code ASCII de a=97 à z=122

		} else // Il n'y a pas de lettres
			return 0; // (NULL)
	}

	public ArrayList<Integer> echangerLettre(int index, ArrayList<Integer> nombreLettre) {
		// Retirez une lettre de la main du joueur et ajoutez-en une autre (si il y a
		// plus!)
		main[index] = donneLettre(nombreLettre);
		return nombreLettre;
	}

	public ArrayList<Integer> jeterLettre(int index, ArrayList<Integer> nombreLettre) {
		nombreLettre.add(main[index] - 97); // Lettre de retour à la pile de dessins
		main[index] = donneLettre(nombreLettre); // Dessine une autre lettre
		return nombreLettre;
	}

	public int calculerScore(int[] mot, int xPos, int yPos, Boolean horizontal, Plateau plat) {
		int motScore = 0;
		int motMultiplicateur = 1;
		int counter = 0;

		if (horizontal) {
			for (int i = yPos; i < (yPos + mot.length); ++i) {
				// Avancer horizontalement (axe Y uniquement)
				switch (plat.returnBoite(xPos, i)) {
				case 0:
					motScore += scoreLettre[mot[i - yPos] - 97];
					counter++;
					break;
				case 1:
					// Boite bleu ciel (lettre x2)
					// Seule cette lettre est multipliée par 2
					motScore += scoreLettre[mot[i - yPos] - 97] * 2;
					counter++;
					break;
				case 2:
					// Boite bleu foncé (lettre x3)
					// Seule cette lettre est multipliée par 3
					motScore += scoreLettre[mot[i - yPos] - 97] * 3;
					counter++;
					break;
				case 3:
					// Boite rose (mot x2)
					// Le score du mot entier sera multipliée par 2 plus tard
					// (score de la lettre individuelle n'est pas multiplier )
					motScore += scoreLettre[mot[i - yPos] - 97];
					counter++;
					motMultiplicateur *= 2;
					break;
				case 4:
					// Boite rouge (mot x3)
					// Le score du mot entier sera multipliée par 3 plus tard
					// (score de la lettre individuelle n'est pas multiplier )
					motScore += scoreLettre[mot[i - yPos] - 97];
					counter++;
					motMultiplicateur *= 3;
					break;
				default:
					motScore += scoreLettre[mot[i - yPos] - 97];
				} // Fin du switch
			} // fin de la boucle
		} else {
			for (int i = xPos; i < (xPos + mot.length); ++i) {
				// Avancer verticalement (axe X uniquement)
				switch (plat.returnBoite(i, yPos)) {
				case 0:
					motScore += scoreLettre[mot[i - xPos] - 97];
					counter++;
					break;
				case 1:
					// Boite bleu ciel (lettre x2)
					// Seule cette lettre est multipliée par 2
					motScore += scoreLettre[mot[i - xPos] - 97] * 2;
					counter++;
					break;
				case 2:
					// Boite bleu foncé (lettre x3)
					// Seule cette lettre est multipliée par 3
					motScore += scoreLettre[mot[i - xPos] - 97] * 3;
					counter++;
					break;
				case 3:
					// Boite rose (mot x2)
					// Le score du mot entier sera multipliée par 2 plus tard
					// (score de la lettre individuelle n'est pas multiplier )
					motScore += scoreLettre[mot[i - xPos] - 97];
					counter++;
					motMultiplicateur *= 2;
					break;
				case 4:
					// Boite rouge (mot x3)
					// Le score du mot entier sera multipliée par 3 plus tard
					// (score de la lettre individuelle n'est pas multiplier )
					motScore += scoreLettre[mot[i - xPos] - 97];
					counter++;
					motMultiplicateur *= 3;
					break;
				default:
					motScore += scoreLettre[mot[i - xPos] - 97];
				} // Fin de switch
			} // fin de la boucle
		}

		// Considérer la valeur multiplicative du mot entier
		motScore *= motMultiplicateur;

		if (counter == 7) // Si toutes les lettres de votre main sont utilisées, +50 points
			motScore += 50;

		return motScore;
	}

	public ArrayList<String> getMain() {
		ArrayList<String> liste = new ArrayList<String>();
		for (int a : main) {
			if (a != 0) {
				liste.add(Character.toString((char) a));
			}
		}
		return liste;
	}

	/* Fonction main pour debugging */

	public static void main(String[] args) throws InterruptedException {
		ArrayList<Integer> nombreLettre = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // tout
																												// l'alphabet
		Joueur jouer1 = new Joueur("Player 1", nombreLettre);
		Joueur jouer2 = new Joueur("Player 2", nombreLettre);

		System.out.println("Jouer 1");
		for (int i = 0; i < 7; i++)
			System.out.println(jouer1.verifierLettre(i));

		System.out.println("Jouer 2");
		for (int i = 0; i < 7; i++)
			System.out.println(jouer2.verifierLettre(i));
	}

} // Terminé Class Jouer
