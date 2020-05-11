import java.io.*;
import java.util.*;

public class anagramDictionnaire extends HashMap<String, List<String>> {
	/* === Paramètres de classe === */

	private static final long serialVersionUID = 1L;

	/* === Constructeur === */

	public anagramDictionnaire(String fichier) throws IOException {
		super();
		BufferedReader bufReader = new BufferedReader(new FileReader((java.lang.String) fichier));

		String ligne = (String) bufReader.readLine();
		while (ligne != null) {

			char[] tempArray = ligne.toCharArray();
			Arrays.sort(tempArray);
			String sortedString = new String(tempArray);

			if (!this.containsKey(sortedString)) {
				List<String> aux = new ArrayList<String>();
				this.put(sortedString, aux);
			}
			List<String> list = this.get(sortedString);
			list.add(ligne);

			this.put(ligne, list);
			ligne = (String) bufReader.readLine();
		}
		bufReader.close();
	}

	/* === Observateurs === */

	public List<String> contains(String word) {
		char[] tempArray = word.toCharArray();
		Arrays.sort(tempArray);
		String anagram = new String(tempArray);

		if (!this.containsKey(anagram))
			return null;
		else
			return this.get(anagram);

	}

	/*
	 * public List<String> couldContain(String word){ char[] tempArray =
	 * word.toCharArray(); Arrays.sort(tempArray); String anagram = new
	 * String(tempArray);
	 * 
	 * this.
	 * 
	 * return null;
	 * 
	 * }
	 */

	/* Fonction main pour debugging */
	public static void main(String[] args) throws IOException {
		anagramDictionnaire dico = new anagramDictionnaire("dico.txt");
		System.out.println(dico.size());
		System.out.println(dico.contains('"' + "abaissait" + '"'));
		System.out.println(dico.contains('"' + "boi" + '"'));
	}

} // Terminé Class Dictionnaire
