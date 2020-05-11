
public class Plateau {
	/* === Paramètres de classe === */

	private static int[][] Plateau = new int[15][15];

	/* === Constructeur === */

	public Plateau() {
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				Plateau[i][j] = 0;

		// Multiplicateur lettre
		// x2 (bleu ciel)
		Plateau[0][3] = Plateau[0][11] = 1;
		Plateau[3][0] = Plateau[11][0] = 1;
		Plateau[3][14] = Plateau[11][14] = 1;
		Plateau[14][3] = Plateau[14][11] = 1;
		Plateau[2][6] = Plateau[3][7] = Plateau[2][8] = 1;
		Plateau[6][2] = Plateau[7][3] = Plateau[8][2] = 1;
		Plateau[12][6] = Plateau[11][7] = Plateau[12][8] = 1;
		Plateau[6][12] = Plateau[7][11] = Plateau[8][12] = 1;
		Plateau[6][6] = Plateau[6][8] = Plateau[8][6] = Plateau[8][8] = 1;
		// x3 (bleu foncé)
		Plateau[1][5] = Plateau[1][9] = 2;
		Plateau[5][1] = Plateau[9][1] = 2;
		Plateau[5][13] = Plateau[9][13] = 2;
		Plateau[13][5] = Plateau[13][9] = 2;
		Plateau[5][5] = Plateau[5][9] = 2;
		Plateau[9][5] = Plateau[9][9] = 2;

		// Multiplicateur mots
		// x2 (rose)
		Plateau[7][7] = 3;
		Plateau[1][1] = Plateau[2][2] = Plateau[3][3] = Plateau[4][4] = 3;
		Plateau[1][13] = Plateau[2][12] = Plateau[3][11] = Plateau[4][10] = 3;
		Plateau[13][1] = Plateau[12][2] = Plateau[11][3] = Plateau[10][4] = 3;
		// Plateau[14][14] = Plateau[13][13] = Plateau[12][12] = Plateau[11][11] = 3;
		Plateau[10][10] = Plateau[13][13] = Plateau[12][12] = Plateau[11][11] = 3;
		// x3 (rouge)
		Plateau[0][0] = Plateau[0][7] = Plateau[0][14] = 4;
		Plateau[7][0] = Plateau[7][14] = 4;
		Plateau[14][0] = Plateau[14][7] = Plateau[14][14] = 4;
	}

	/* === Observateurs === */

	public int returnBoite(int x, int y) {
		if (x < 0 || x > 14 || y < 0 || y > 14)
			return -1;
		else
			return Plateau[x][y];
	}

	public void montrerPlateau() {
		System.out.println("\t\t\tPlateau actuel! \n");
		for (int i = 0; i < 15; ++i) {
			System.out.print("\t{ ");
			for (int j = 0; j < 14; ++j) {
				if (Plateau[i][j] > 5)
					System.out.print((char) Plateau[i][j] + ", ");
				else
					System.out.print(Plateau[i][j] + ", ");
			}
			if (Plateau[i][14] > 5)
				System.out.print((char) Plateau[i][14] + " }\n");
			else
				System.out.print(Plateau[i][14] + " }\n");
		}
	}

	/* === Modificateurs === */

	public void modifierPlateau(int[] mot, int xPos, int yPos, Boolean horizontal) {
		if (horizontal) { // Movement soulement dans le axis Y
			for (int i = yPos; i < (yPos + mot.length); ++i) // Bouge de yPos à (yPos + taille de la mot)
				Plateau[xPos][i] = mot[i - yPos]; // Changer un a un les valeurs trouve dans le Plateau (string -> char
													// -> integer)

		} else { // Movement soulement dans le axis X
			for (int i = xPos; i < (xPos + mot.length); ++i) // Bouge de xPos à (xPos + taille de la mot)
				Plateau[i][yPos] = mot[i - xPos];
		}

	}

	/* Fonction main pour debugging */

	public static void main(String[] args) {
		Plateau aux = new Plateau();
		aux.montrerPlateau();

		int[] value = { 104, 101, 108, 108, 111 }; // "hello"
		aux.modifierPlateau(value, 3, 2, false); // placer hello vertical a partir de la case (3,2)

		aux.montrerPlateau();
	}

} // Terminé Class Plateau
