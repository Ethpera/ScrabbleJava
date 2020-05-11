
public class Mot {
	private String mot;
	private int xPos;
	private int yPos;
	private boolean horizontal;
	private int points;

	public Mot(String m, int x, int y, boolean h, int p) {
		mot = m;
		xPos = x;
		yPos = y;
		horizontal = h;
		points = p;
	}

	/* Observateurs */

	public String verifierNom() {
		return mot;
	}

	public int verifierX() {
		return xPos;
	}

	public int verifierY() {
		return yPos;
	}

	public boolean verifierHorizontal() {
		return horizontal;
	}

	public int verifierPoints() {
		return points;
	}

	public void modifierPoints(int p) {
		this.points = p;
	}

	public int[] getLettres() {
		int[] lettres = new int[this.mot.length()];
		for (int i = 0; i < this.mot.length(); i++) {
			lettres[i] = (int) this.mot.charAt(i);
		}
		return lettres;
	}
}
