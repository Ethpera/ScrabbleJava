import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Partiecontrol {
	Main test;

	@FXML
	public Joueur joueur1;

	@FXML
	public Joueur joueur2;

	@FXML
	public Label nj1;

	@FXML
	public Label nj2;

	@FXML
	public Label score1;

	@FXML
	public Label score2;

	@FXML
	private ImageView j1img;

	@FXML
	private ImageView j2img;

	@FXML
	private Button valider;

	@FXML
	private Button jeter;

	@FXML
	private Button sugg;

	@FXML
	private TableView<Suggestion> suggestionmain;

	@FXML
	private TableColumn<Suggestion, String> suggestions;

	@FXML
	private TableColumn<Suggestion, String> sens;

	@FXML
	private TableColumn<Suggestion, Integer> point;

	@FXML
	private TableColumn<Suggestion, Integer> x;

	@FXML
	private TableColumn<Suggestion, Integer> y;

	@FXML
	private ProgressBar computing;

	@FXML
	private Button quit;

	private boolean Fini = false;

	public class Suggestion {
		private String Mot;
		private Integer Score;
		private Integer Posx;
		private Integer Posy;
		private String Sens;

		Suggestion(String mots, Integer scores, Integer x, Integer y, String sen) {
			this.Sens = sen;
			this.Mot = mots;
			this.Score = scores;
			this.Posx = x;
			this.Posy = y;
		}

		public String getSens() {
			return Sens;
		}

		public void setSens(String sens) {
			Sens = sens;
		}

		public Integer getScore() {
			return Score;
		}

		public void setScore(Integer score) {
			Score = score;
		}

		public Integer getPosx() {
			return Posx;
		}

		public void setPosx(Integer xx) {
			this.Posx = xx;
		}

		public String getMot() {
			return Mot;
		}

		public void setMot(String mot) {
			this.Mot = mot;
		}

		public Integer getPosy() {
			return Posy;
		}

		public void setPosy(Integer yy) {
			this.Posy = yy;
		}

	}

	Alert alert = new Alert(AlertType.INFORMATION);
	ArrayList<String> uM = new ArrayList<>();

	public void initialize(Main test) {

		this.test = test;
		nj1.setText(test.getNom1().getText());
		nj2.setText(test.getNom2().getText());
		suggestions.setCellValueFactory(new PropertyValueFactory<Suggestion, String>("Mot"));
		point.setCellValueFactory(new PropertyValueFactory<Suggestion, Integer>("Score"));
		x.setCellValueFactory(new PropertyValueFactory<Suggestion, Integer>("Posx"));
		y.setCellValueFactory(new PropertyValueFactory<Suggestion, Integer>("Posy"));
		sens.setCellValueFactory(new PropertyValueFactory<Suggestion, String>("Sens"));
		if (test.sologameInstance == null) {
			joueur1 = test.gameInstance.tourActuel();
			joueur2 = test.gameInstance.adversaireActuel();
		} else {
			joueur1 = test.sologameInstance.tourActuel();
			joueur2 = test.sologameInstance.adversaireActuel();
		}

		test.initchevalet(joueur2.getMain());
		test.initchevalet(joueur1.getMain());
		test.selection.addListener((ListChangeListener<Integer>) change -> {
			if (test.selection.size() > 0) {
				jeter.setText("Jeter Lettres");
			} else {
				jeter.setText("Passer Tour");
			}
		});

	}

	@FXML
	void quitter(ActionEvent event) {
		// test.music("victoire");
		alert.setTitle("BRAVO");
		GridPane icon = new GridPane();
		Image image = new Image("Images/drapeaudamier.png");
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		if (test.sologameInstance == null) {
			alert.setHeaderText("Félicitations " + test.gameInstance.adversaireActuel().verifierNom());
			alert.setContentText(test.gameInstance.tourActuel().verifierNom()+" s'est rendu");
			if (Fini) {
				if (test.gameInstance.tourActuel().verifierScore() > test.gameInstance.adversaireActuel()
						.verifierScore()) {
					alert.setHeaderText("Félicitations " + test.gameInstance.tourActuel().verifierNom());
					alert.setContentText("Tu as gagné avec un score pathétique de "
							+ test.gameInstance.tourActuel().verifierScore());
				} else if (test.gameInstance.tourActuel().verifierScore() < test.gameInstance.adversaireActuel()
						.verifierScore()) {
					alert.setHeaderText("Félicitations " + test.gameInstance.adversaireActuel().verifierNom());
					alert.setContentText("Tu as gagné avec un score pathétique de "
							+ test.gameInstance.adversaireActuel().verifierScore());
				} else {
					alert.setHeaderText("Félicitations ");
					alert.setContentText("Vous êtes de force égale");
				}

			}
		} else {
			alert.setHeaderText(" ");
			alert.setContentText("L'ordinateur a gagné");
			alert.setGraphic(new ImageView(new Image("Images/blanc.png")));
			if (Fini) {
				if (test.sologameInstance.tourActuel().verifierScore() > test.sologameInstance.adversaireActuel()
						.verifierScore()) {
					alert.setHeaderText("Félicitations " + test.sologameInstance.tourActuel().verifierNom());
					alert.setContentText("Tu as gagné avec un score pathétique de "
							+ test.sologameInstance.adversaireActuel().verifierScore());
				} else if (test.sologameInstance.tourActuel().verifierScore() < test.sologameInstance.adversaireActuel()
						.verifierScore()) {
					alert.setHeaderText("Félicitations " + test.sologameInstance.adversaireActuel().verifierNom());
					alert.setContentText("Tu as gagné avec un score pathétique de "
							+ test.sologameInstance.adversaireActuel().verifierScore());
				} else {
					alert.setHeaderText("Félicitations " + "Vous êtes de force égale");
					alert.setContentText("Vous êtes de force égale");
				}

			}
		}

		suggestionmain.getItems().clear();
		test.selection.clear();
		DialogPane dialogPane = alert.getDialogPane();

		File f = new File("src/chart.css");
		dialogPane.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		dialogPane.getStyleClass().add("myDialog");
		alert.showAndWait();
		Button bouton = (Button) event.getSource();
		Group root = ((Group) bouton.getParent().getParent().getParent().getParent());
		root.getChildren().clear();
		test.init("Startmenu.fxml");

		// place la grille

		test.acceuilgauche.getChildren().add(test.grille);

		root.getChildren().add(test.acceuilmain);
		root.getChildren().add(test.rootLayout);
	}

	@FXML
	void suggester(ActionEvent event) throws IOException {
		int[] m;
		anagramDictionnaire aD = new anagramDictionnaire("dico.txt");
		Dictionnaire d = new Dictionnaire("dico.txt");
		ObservableList<Suggestion> mots = FXCollections.observableArrayList();
		if (test.sologameInstance == null) {
			m = test.gameInstance.tourActuel().verifierMain();
			test.gameInstance.plat.montrerPlateau();

			permutationSearch pS = new permutationSearch(aD, d, test.gameInstance.plat, m, test.gameInstance.usedWords);

			for (int i = 0; i < pS.totalPossibleWords(); ++i) {
				if (pS.possibleWord(i).verifierNom().length() >= 3){
					if (pS.possibleWord(i).verifierHorizontal()) {
						mots.add(new Suggestion(pS.possibleWord(i).verifierNom(), pS.possibleWord(i).verifierPoints(),
								pS.possibleWord(i).verifierY(), pS.possibleWord(i).verifierX(), "Horizontal"));
					} else {
						mots.add(new Suggestion(pS.possibleWord(i).verifierNom(), pS.possibleWord(i).verifierPoints(),
								pS.possibleWord(i).verifierY(), pS.possibleWord(i).verifierX(), "Vertical"));
					}
				}
			}

		} else {
			m = test.sologameInstance.tourActuel().verifierMain();
			permutationSearch pS = new permutationSearch(aD, d, test.sologameInstance.plat, m,
					test.sologameInstance.usedWords);

			for (int i = 0; i < pS.totalPossibleWords(); ++i) {
				if (pS.possibleWord(i).verifierNom().length() >= 3){
					if (pS.possibleWord(i).verifierHorizontal()) {
						mots.add(new Suggestion(pS.possibleWord(i).verifierNom(), pS.possibleWord(i).verifierPoints(),
								pS.possibleWord(i).verifierY(), pS.possibleWord(i).verifierX(), "Horizontal"));
					} else {

						mots.add(new Suggestion(pS.possibleWord(i).verifierNom(), pS.possibleWord(i).verifierPoints(),
								pS.possibleWord(i).verifierY(), pS.possibleWord(i).verifierX(), "Vertical"));
					}
				}
			}
		}

		suggestionmain.setEditable(true);
		suggestionmain.setItems(mots);
	}

	@FXML
	void valid(ActionEvent event) {
		/** LETTRE DANS CASE **/
		Group gj = (Group) test.acceuilgauche.getChildren().get(2);
		Group gg = (Group) test.acceuilgauche.getChildren().get(0);
		ArrayList listeCases = new ArrayList();
		ArrayList listeTexte = new ArrayList();
		ArrayList listePos = new ArrayList();
		ArrayList temp = new ArrayList();
		if (test.sologameInstance == null) {

			for (Node a : gj.getChildren()) {
				if (a.getClass().equals(Text.class)) {
					if (a.getId() != null) {
						listePos.add(Integer.parseInt(a.getId()));
						listeCases.add(gg.getChildren().get(Integer.parseInt(a.getId())));
						listeTexte.add(((Text) a).getText());
					}

				}
			}

			temp.add(listePos);
			temp.add(listeTexte);
			temp.add(listeCases);

			boolean horizontale = true;
			if (listePos.size() >= 1) {
				boolean hor=true;
				boolean vert=true;
				for(Object a:listePos) {
					if((int)a/15!=(int)listePos.get(0)/15) {
						hor=false;
					}
					if((int)a%15!=(int)listePos.get(0)%15) {
						vert=false;
					}
				}
				if(hor || vert) {
				if (listePos.size() > 1) {
					for (int a = 1; a < listePos.size(); a++) {
						int x = (int) listePos.get(a) / 15;
						int y = (int) listePos.get(a) % 15;
						if (x == (int) listePos.get(0) / 15) {
							horizontale = false;
						} else if (y == (int) listePos.get(0) % 15) {
							horizontale = true;
						}
					}
				} else {
					if ((int) listePos.get(0) / 15 == 0) {// lettre tout à gauche
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) + 15)).getChildren().get(0))
								.getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					} else if ((int) listePos.get(0) / 15 == 14) {// tout à droite
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) - 15)).getChildren().get(0))
								.getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					} else {// entre les deux
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) - 15)).getChildren().get(0))
								.getId().equals("true")
								|| ((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) + 15)).getChildren()
										.get(0)).getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					}

				}
				boolean flag = false;
				while (!flag) {
					flag = true;

					for (int a = 0; a < listePos.size() - 1; a++) {
						ArrayList temp2 = (ArrayList) temp.get(0);
						ArrayList temp3 = (ArrayList) temp.get(1);
						ArrayList temp4 = (ArrayList) temp.get(2);

						if ((int) temp2.get(a) > (int) temp2.get(a + 1)) {
							flag = false;
							Object aux = temp2.get(a);
							Object aux2 = temp3.get(a);
							Object aux3 = temp4.get(a);
							temp2.set(a, temp2.get(a + 1));
							temp2.set(a + 1, aux);
							temp3.set(a, temp3.get(a + 1));
							temp3.set(a + 1, aux2);
							temp4.set(a, temp4.get(a + 1));
							temp4.set(a + 1, aux3);
						}
					}
				}

				ArrayList posFin = new ArrayList();
				ArrayList texteFin = new ArrayList();
				ArrayList casesFin = new ArrayList();
				int entre=0;
				if (horizontale) {
					entre=((int)listePos.get(listePos.size()-1)/15-(int)listePos.get(0)/15);
					// cases avant
					int val1 = 0;
					while (val1 < (int) listePos.get(0) / 15) {
						if (((Rectangle) ((Case) gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15))
								.getChildren().get(0)).getId().equals("true")) {
							posFin.add((15 * val1) + (int) listePos.get(0) % 15);
							texteFin.add(((Text) ((Case) gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15))
									.getChildren().get(1)).getText());
							casesFin.add(gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15));

						} else {
							posFin = new ArrayList();
							texteFin = new ArrayList();
							casesFin = new ArrayList();
						}
						val1 += 1;
					}
					// cases dedans
					for(int a=0;a<entre+1;a++) {
						if(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(0).getId().equals("true")) {
							if(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1)).getText().equals("")) {
								texteFin.add(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1).getId());
							}
							else {
								texteFin.add(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1)).getText());
							}
							posFin.add((int)listePos.get(0)+15*a);
							casesFin.add(((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a));
						}
						else {
							texteFin.add(null);
						}

					}
					// cases après
					int val = 1;
					if ((int) listePos.get(listePos.size() - 1) / 15 < 14) {
						while (((Rectangle) ((Case) gg.getChildren()
								.get((int) listePos.get(listePos.size() - 1) + (15 * val))).getChildren().get(0))
										.getId().equals("true")
								&& ((int) listePos.get(listePos.size() - 1) + (15 * val)) / 15 < 15) {
							posFin.add((int) listePos.get(listePos.size() - 1) + (15 * val));
							texteFin.add(((Text) ((Case) gg.getChildren()
									.get((int) listePos.get(listePos.size() - 1) + (15 * val))).getChildren().get(1))
											.getText());
							casesFin.add(gg.getChildren().get((int) listePos.get(listePos.size() - 1) + (15 * val)));
							val += 1;
						}
					}
				} else {
					entre=((int)listePos.get(listePos.size()-1)%15-(int)listePos.get(0)%15);
					// cases avant
					int val1 = 0;
					while (val1 < (int) listePos.get(0) % 15) {
						if (((Rectangle) ((Case) gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15))
								.getChildren().get(0)).getId().equals("true")) {
							posFin.add(val1 + ((int) listePos.get(0) / 15) * 15);
							texteFin.add(((Text) ((Case) gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15))
									.getChildren().get(1)).getText());
							casesFin.add(gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15));

						} else {
							posFin = new ArrayList();
							texteFin = new ArrayList();
							casesFin = new ArrayList();
						}
						val1 += 1;
					}
					// cases dedans
					for(int a=0;a<entre+1;a++) {
						if(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(0).getId().equals("true")) {
							if(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1)).getText().equals("")) {
								texteFin.add(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1).getId());
							}
							else {
								texteFin.add(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1)).getText());
							}
							posFin.add((int)listePos.get(0)+1*a);
							casesFin.add(((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a));
						}
						else {
							texteFin.add(null);
						}

					}
					int val = 1;
					// cases après
					if ((int) listePos.get(listePos.size() - 1) % 15 < 14) {
						while (((Rectangle) ((Case) gg.getChildren()
								.get((int) listePos.get(listePos.size() - 1) + (1 * val))).getChildren().get(0)).getId()
										.equals("true")
								&& ((int) listePos.get(listePos.size() - 1) + (1 * val)) % 15 < 15) {
							posFin.add((int) listePos.get(listePos.size() - 1) + (1 * val));
							texteFin.add(((Text) ((Case) gg.getChildren()
									.get((int) listePos.get(listePos.size() - 1) + (1 * val))).getChildren().get(1))
											.getText());
							casesFin.add(gg.getChildren().get((int) listePos.get(listePos.size() - 1) + (1 * val)));
							val += 1;
						}
					}

				}
				listePos = posFin;
				listeTexte = texteFin;
				listeCases = casesFin;
				int[] mot = new int[listePos.size()];
				try {
					for (int a = 0; a < listePos.size(); a++) {
						mot[a] = (int) ((String) listeTexte.get(a)).charAt(0);
					}
				} catch (Exception e) {
					System.out.println(e+"Les lettres ne se suivent pas");
				}

				if (joueur1.verifierTour()) {

					if (test.gameInstance.proceedWithAction(test.gameInstance.tourActuel(), mot,
							(int) listePos.get(0) % 15, (int) listePos.get(0) / 15, horizontale)) {
						// jetons dans case
						if (test.gameInstance.asGagne()) {
							Fini = true;
						}
						test.gameInstance.tourActuel().resetTourPasser();
						suggestionmain.getItems().clear();
						// test.music("gg");

						for (int a = 0; a < listeCases.size(); a++) {
							((Text) ((Case) listeCases.get(a)).getChildren().get(1))
									.setText((String) listeTexte.get(a));
							((Rectangle) ((Case) listeCases.get(a)).getChildren().get(0)).setFill(Color.SEAGREEN);
						}

						test.acceuilgauche.getChildren().remove(2);
						test.initchevalet(test.gameInstance.adversaireActuel().getMain());
						test.acceuilgauche.getChildren().add(test.chevalet);

						// changement de tour
						joueur1.terminerTour();
						joueur2.commencerTour();
						joueur1 = test.gameInstance.adversaireActuel();
						joueur2 = test.gameInstance.tourActuel();
						test.gameInstance.plat.montrerPlateau();

					} else {

						Alert pabon = new Alert(AlertType.INFORMATION);
						pabon.setContentText("Attention le mot est invalide");
						pabon.showAndWait();
						// test.music("no");
					}
				} else {
					if (test.gameInstance.proceedWithAction(test.gameInstance.tourActuel(), mot,
							(int) listePos.get(0) % 15, (int) listePos.get(0) / 15, horizontale)) {
						// jetons dans case
						if (test.gameInstance.asGagne()) {
							Fini = true;
						}
						test.gameInstance.tourActuel().resetTourPasser();
						suggestionmain.getItems().clear();
						for (int a = 0; a < listeCases.size(); a++) {
							((Text) ((Case) listeCases.get(a)).getChildren().get(1))
									.setText((String) listeTexte.get(a));
							((Rectangle) ((Case) listeCases.get(a)).getChildren().get(0)).setFill(Color.SEAGREEN);
						}
						test.acceuilgauche.getChildren().remove(2);
						test.initchevalet(test.gameInstance.adversaireActuel().getMain());
						test.acceuilgauche.getChildren().add(test.chevalet);

						joueur2.terminerTour();
						joueur1.commencerTour();
						joueur2 = test.gameInstance.adversaireActuel();
						joueur1 = test.gameInstance.tourActuel();
						test.gameInstance.plat.montrerPlateau();
					} else {
						Alert pabon = new Alert(AlertType.INFORMATION);
						pabon.setContentText("Attention le mot est invalide");
						pabon.showAndWait();
						// test.music("no");
					}

				}

				}else {
					Alert pabon = new Alert(AlertType.INFORMATION);
					pabon.setContentText("Attention le mot est invalide");
					pabon.showAndWait();
				}

			}
			else {
				Alert pabon = new Alert(AlertType.INFORMATION);
				pabon.setContentText("Vous n'avez pas placé de lettre");
				pabon.showAndWait();

			}
			nj1.setText(String.valueOf(test.gameInstance.tourActuel().verifierNom()));
			nj2.setText(String.valueOf(test.gameInstance.adversaireActuel().verifierNom()));
			score1.setText(String.valueOf(test.gameInstance.tourActuel().verifierScore()));
			score2.setText(String.valueOf(test.gameInstance.adversaireActuel().verifierScore()));
		}
		// SOLO
		else {

			for (Node a : gj.getChildren()) {
				if (a.getClass().equals(Text.class)) {
					if (a.getId() != null) {
						listePos.add(Integer.parseInt(a.getId()));
						listeCases.add(gg.getChildren().get(Integer.parseInt(a.getId())));
						listeTexte.add(((Text) a).getText());
					}

				}
			}

			temp.add(listePos);
			temp.add(listeTexte);
			temp.add(listeCases);

			boolean horizontale = true;
			if (listePos.size() >= 1) {
				boolean hor=true;
				boolean vert=true;
				for(Object a:listePos) {
					if((int)a/15!=(int)listePos.get(0)/15) {
						hor=false;
					}
					if((int)a%15!=(int)listePos.get(0)%15) {
						vert=false;
					}
				}
				if(hor || vert) {
				if (listePos.size() > 1) {
					for (int a = 1; a < listePos.size(); a++) {
						int x = (int) listePos.get(a) / 15;
						int y = (int) listePos.get(a) % 15;
						if (x == (int) listePos.get(0) / 15) {
							horizontale = false;
						} else if (y == (int) listePos.get(0) % 15) {
							horizontale = true;
						}
					}
				} else {
					if ((int) listePos.get(0) / 15 == 0) {// lettre tout à gauche
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) + 15)).getChildren().get(0))
								.getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					} else if ((int) listePos.get(0) / 15 == 14) {// tout à droite
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) - 15)).getChildren().get(0))
								.getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					} else {// entre les deux
						if (((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) - 15)).getChildren().get(0))
								.getId().equals("true")
								|| ((Rectangle) ((Case) gg.getChildren().get((int) listePos.get(0) + 15)).getChildren()
										.get(0)).getId().equals("true")) {
							horizontale = true;
						} else {
							horizontale = false;
						}

					}

				}

				boolean flag = false;
				while (!flag) {
					flag = true;

					for (int a = 0; a < listePos.size() - 1; a++) {
						ArrayList temp2 = (ArrayList) temp.get(0);
						ArrayList temp3 = (ArrayList) temp.get(1);
						ArrayList temp4 = (ArrayList) temp.get(2);

						if ((int) temp2.get(a) > (int) temp2.get(a + 1)) {
							flag = false;
							Object aux = temp2.get(a);
							Object aux2 = temp3.get(a);
							Object aux3 = temp4.get(a);
							temp2.set(a, temp2.get(a + 1));
							temp2.set(a + 1, aux);
							temp3.set(a, temp3.get(a + 1));
							temp3.set(a + 1, aux2);
							temp4.set(a, temp4.get(a + 1));
							temp4.set(a + 1, aux3);
						}
					}
				}

				ArrayList posFin = new ArrayList();
				ArrayList texteFin = new ArrayList();
				ArrayList casesFin = new ArrayList();
				int entre=0;
				if (horizontale) {
					entre=((int)listePos.get(listePos.size()-1)/15-(int)listePos.get(0)/15);
					// cases avant
					int val1 = 0;
					while (val1 < (int) listePos.get(0) / 15) {
						if (((Rectangle) ((Case) gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15))
								.getChildren().get(0)).getId().equals("true")) {
							posFin.add((15 * val1) + (int) listePos.get(0) % 15);
							texteFin.add(((Text) ((Case) gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15))
									.getChildren().get(1)).getText());
							casesFin.add(gg.getChildren().get((15 * val1) + (int) listePos.get(0) % 15));

						} else {
							posFin = new ArrayList();
							texteFin = new ArrayList();
							casesFin = new ArrayList();
						}
						val1 += 1;
					}
					// cases dedans
					for(int a=0;a<entre+1;a++) {
						if(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(0).getId().equals("true")) {
							if(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1)).getText().equals("")) {
								texteFin.add(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1).getId());
							}
							else {
								texteFin.add(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a)).getChildren().get(1)).getText());
							}
							posFin.add((int)listePos.get(0)+15*a);
							casesFin.add(((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+15*a));
						}
						else {
							texteFin.add(null);
						}

					}
					int val = 1;
					// cases après
					if ((int) listePos.get(listePos.size() - 1) / 15 < 14) {
						while (((Rectangle) ((Case) gg.getChildren()
								.get((int) listePos.get(listePos.size() - 1) + (15 * val))).getChildren().get(0))
										.getId().equals("true")
								&& ((int) listePos.get(listePos.size() - 1) + (15 * val)) / 15 < 15) {
							posFin.add((int) listePos.get(listePos.size() - 1) + (15 * val));
							texteFin.add(((Text) ((Case) gg.getChildren()
									.get((int) listePos.get(listePos.size() - 1) + (15 * val))).getChildren().get(1))
											.getText());
							casesFin.add(gg.getChildren().get((int) listePos.get(listePos.size() - 1) + (15 * val)));
							val += 1;
						}
					}
				} else {
					entre=((int)listePos.get(listePos.size()-1)%15-(int)listePos.get(0)%15);
					// cases avant
					int val1 = 0;
					while (val1 < (int) listePos.get(0) % 15) {
						if (((Rectangle) ((Case) gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15))
								.getChildren().get(0)).getId().equals("true")) {
							posFin.add(val1 + ((int) listePos.get(0) / 15) * 15);
							texteFin.add(((Text) ((Case) gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15))
									.getChildren().get(1)).getText());
							casesFin.add(gg.getChildren().get(val1 + ((int) listePos.get(0) / 15) * 15));

						} else {
							posFin = new ArrayList();
							texteFin = new ArrayList();
							casesFin = new ArrayList();
						}
						val1 += 1;
					}
					// cases dedans
					for(int a=0;a<entre+1;a++) {
						if(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(0).getId().equals("true")) {
							if(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1)).getText().equals("")) {
								texteFin.add(((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1).getId());
							}
							else {
								texteFin.add(((Text)((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a)).getChildren().get(1)).getText());
							}
							posFin.add((int)listePos.get(0)+1*a);
							casesFin.add(((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get((int)listePos.get(0)+1*a));
						}
						else {
							texteFin.add(null);
						}

					}
					int val = 1;
					// cases après
					if ((int) listePos.get(listePos.size() - 1) % 15 < 14) {
						while (((Rectangle) ((Case) gg.getChildren()
								.get((int) listePos.get(listePos.size() - 1) + (1 * val))).getChildren().get(0)).getId()
										.equals("true")
								&& ((int) listePos.get(listePos.size() - 1) + (1 * val)) % 15 < 15) {
							posFin.add((int) listePos.get(listePos.size() - 1) + (1 * val));
							texteFin.add(((Text) ((Case) gg.getChildren()
									.get((int) listePos.get(listePos.size() - 1) + (1 * val))).getChildren().get(1))
											.getText());
							casesFin.add(gg.getChildren().get((int) listePos.get(listePos.size() - 1) + (1 * val)));
							val += 1;
						}
					}

				}
				listePos = posFin;
				listeTexte = texteFin;
				listeCases = casesFin;
				int[] mot = new int[listePos.size()];
				try {
					for (int a = 0; a < listePos.size(); a++) {
						mot[a] = (int) ((String) listeTexte.get(a)).charAt(0);
					}
				} catch (Exception e) {
					System.out.println("Les lettres ne se suivent pas");
				}
				if (test.sologameInstance.proceedWithAction(test.sologameInstance.tourActuel(), mot,
						(int) listePos.get(0) % 15, (int) listePos.get(0) / 15, horizontale)) {
					// jetons dans case
					if (test.sologameInstance.asGagne()) {
						Fini = true;
					}
					suggestionmain.getItems().clear();
					test.sologameInstance.tourActuel().resetTourPasser();
					for (int a = 0; a < listeCases.size(); a++) {
						((Text) ((Case) listeCases.get(a)).getChildren().get(1)).setText((String) listeTexte.get(a));
						((Rectangle) ((Case) listeCases.get(a)).getChildren().get(0)).setFill(Color.SEAGREEN);
					}
					test.acceuilgauche.getChildren().remove(2);
					test.initchevalet(test.sologameInstance.tourActuel().getMain());
					test.acceuilgauche.getChildren().add(test.chevalet);
					//((ImageView) test.acceuildroite.getChildren().get(9)).setImage(new Image("Images/user.png"));
					//((ImageView) test.acceuildroite.getChildren().get(10)).setImage(new Image("Images/bot.png"));

					// changement de tour

					joueur1.terminerTour();
					joueur2.commencerTour();
					joueur1 = test.sologameInstance.adversaireActuel();
					joueur2 = test.sologameInstance.tourActuel();

					// Partie IA
					Iajoue().setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				        @Override
				        public void handle(WorkerStateEvent t)
				        {
				        	for(Node a:test.acceuildroite.getChildren()) {
				                if(a.getClass().equals(Button.class)) {
				                    a.setDisable(false);
				                }
				            }
				    		test.acceuilgauche.getChildren().get(2).setDisable(false);
				        	joueur2.terminerTour();
							joueur1.commencerTour();
							joueur2 = test.sologameInstance.adversaireActuel();
							joueur1 = test.sologameInstance.tourActuel();

							test.acceuilgauche.getChildren().remove(2);
							test.initchevalet(test.sologameInstance.tourActuel().getMain());
							test.acceuilgauche.getChildren().add(test.chevalet);
							nj1.setText(test.sologameInstance.tourActuel().verifierNom());
							nj2.setText(test.sologameInstance.adversaireActuel().verifierNom());
							score1.setText(String.valueOf(test.sologameInstance.tourActuel().verifierScore()));
							score2.setText(String.valueOf(test.sologameInstance.adversaireActuel().verifierScore()));

				        }
				    });

				} // mot pas bon
				else {
					Alert pabon = new Alert(AlertType.INFORMATION);
					pabon.setContentText("Attention le mot est invalide");
					pabon.showAndWait();
					// test.music("no");
				}
				}
				else {
					Alert pabon = new Alert(AlertType.INFORMATION);
					pabon.setContentText("Attention le mot est invalide");
					pabon.showAndWait();
				}
			} else {
				Alert pabon = new Alert(AlertType.INFORMATION);
				pabon.setContentText("Vous n'avez pas placé de lettre");
				pabon.showAndWait();

			}


		}
		if (Fini) {
			quitter(event);
		}

	}

	public Task Iajoue() {


		// Partie IA

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				for(Node a:test.acceuildroite.getChildren()) {
		            if(a.getClass().equals(Button.class)) {
		                a.setDisable(true);
		            }
		        }
				test.acceuilgauche.getChildren().get(2).setDisable(true);
				updateProgress(0, 100);
				updateProgress(30, 100);
				Mot ia = test.sologameInstance.getBestMove();
				updateProgress(50, 100);
				if (ia != null) {// L'ia joue un mot
					int[] mot = ia.getLettres();
					boolean horizontale = ia.verifierHorizontal();
					int xPos = ia.verifierX();
					int yPos = ia.verifierY();

					if (test.sologameInstance.proceedWithAction(test.sologameInstance.tourActuel(), mot, xPos, yPos,
							horizontale)) {
						updateProgress(70, 100);
						test.sologameInstance.tourActuel().resetTourPasser();
						if (test.sologameInstance.asGagne()) {
							Fini = true;
						}
						int x = 0;
						int y = 0;
						for (int a = 0; a < mot.length; a++) {
							((Case) ((Group) test.acceuilgauche.getChildren().get(0)).getChildren()
									.get((yPos + y) * 15 + (xPos + x))).lettre
											.setText(Character.toString((char) mot[a]).toUpperCase());
							((Case) ((Group) test.acceuilgauche.getChildren().get(0)).getChildren()
									.get((yPos + y) * 15 + (xPos + x))).rectangle.setFill(Color.SEAGREEN);
							((Case) ((Group) test.acceuilgauche.getChildren().get(0)).getChildren()
									.get((yPos + y) * 15 + (xPos + x))).rectangle.setId("true");
							if (horizontale) {
								y += 1;
							} else {
								x += 1;
							}
						}

					}
					updateProgress(80, 100);
				} else {// L'ia jette sa main
					if (test.sologameInstance.lettresRestant() > 0) { // si il reste des lettres dans la pioche on
						test.sologameInstance.tourActuel().resetTourPasser();
						for (int i = 0; i < test.sologameInstance.tourActuel().verifierMain().length; i++)
							test.sologameInstance.tourActuel().jeterLettre(i, test.sologameInstance.getNombreLettre());
					} else {// fin de partie
						test.sologameInstance.tourActuel().incrementTourPasser();
					}

				}
				updateProgress(100, 100);
				return null;
			}
		};

		computing.progressProperty().bind(task.progressProperty());

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

		return task;



	}
	public void anulTab() {
		for(Node a:((Group)test.acceuilgauche.getChildren().get(2)).getChildren()) {
			if(a.getClass().equals(Rectangle.class)) {
				if(((Rectangle)a).getFill().equals(Color.LIGHTGREEN)) {
					if(a.getId()!=null) {
						int pos=Integer.parseInt(a.getId());
						((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get(pos)).getChildren().get(0).setId("false");
					}
				}

			}
		}
	}
	@FXML
	void jete(ActionEvent event) {
		// selection non vide
		anulTab();
		if (jeter.getText().equals("Jeter Lettres")) {
			if (test.sologameInstance == null) {
				if (test.gameInstance.getNombreLettre().size() < test.selection.size()) {
					Alert restant = new Alert(AlertType.INFORMATION);
					restant.setContentText("Attention, il n'y a pas assez de lettres");
					restant.showAndWait();
				} else {
					for (int i = 0; i < test.selection.size(); i++) {
						test.gameInstance.tourActuel().jeterLettre(test.selection.get(i),
								test.gameInstance.getNombreLettre());
					}
					test.gameInstance.tourActuel().resetTourPasser();

					if (joueur1.verifierTour()) {
						joueur1.terminerTour();
						joueur2.commencerTour();
						joueur1 = test.gameInstance.adversaireActuel();
						joueur2 = test.gameInstance.tourActuel();
					} else {
						joueur2.terminerTour();
						joueur1.commencerTour();
						joueur2 = test.gameInstance.adversaireActuel();
						joueur1 = test.gameInstance.tourActuel();
					}
				}

			}
			// solo
			else {


				if (test.sologameInstance.getNombreLettre().size() < test.selection.size()) {
					Alert restant = new Alert(AlertType.INFORMATION);
					restant.setContentText("Attention, il n'y a pas assez de lettres");
					restant.showAndWait();
				} else {
					for (int i = 0; i < test.selection.size(); i++) {
						test.sologameInstance.tourActuel().jeterLettre(test.selection.get(i),
								test.sologameInstance.getNombreLettre());
					}
					test.sologameInstance.tourActuel().resetTourPasser();
					joueur1.terminerTour();
					joueur2.commencerTour();
					joueur1 = test.sologameInstance.adversaireActuel();
					joueur2 = test.sologameInstance.tourActuel();

					Iajoue().setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				        @Override
				        public void handle(WorkerStateEvent t)
				        {
				        	for(Node a:test.acceuildroite.getChildren()) {
				                if(a.getClass().equals(Button.class)) {
				                    a.setDisable(false);
				                }
				            }
				    		test.acceuilgauche.getChildren().get(2).setDisable(false);
				        	joueur2.terminerTour();
							joueur1.commencerTour();
							joueur2 = test.sologameInstance.adversaireActuel();
							joueur1 = test.sologameInstance.tourActuel();

							test.acceuilgauche.getChildren().remove(2);
							test.initchevalet(test.sologameInstance.tourActuel().getMain());
							test.acceuilgauche.getChildren().add(test.chevalet);
							nj1.setText(test.sologameInstance.tourActuel().verifierNom());
							nj2.setText(test.sologameInstance.adversaireActuel().verifierNom());
							score1.setText(String.valueOf(test.sologameInstance.tourActuel().verifierScore()));
							score2.setText(String.valueOf(test.sologameInstance.adversaireActuel().verifierScore()));

				        }
				    });

				}


			}
		} else {
			if (joueur1.tourPasser >= 3 && joueur2.tourPasser >= 3) {
				if (test.sologameInstance == null) {
					if (test.gameInstance.getNombreLettre().size() != 0) {
						Alert restant = new Alert(AlertType.INFORMATION);
						restant.setHeaderText(null);
						restant.setContentText("Veuillez jouer ou changer de lettres, il y en a encore "
								+ test.gameInstance.getNombreLettre().size());
						restant.showAndWait();
					} else {
						Fini = true;
					}
				} else {

					if (test.sologameInstance.getNombreLettre().size() != 0) {
						Alert restant = new Alert(AlertType.INFORMATION);
						restant.setHeaderText(null);
						restant.setContentText("Veuillez jouer ou changer de lettres, il y en a encore "
								+ test.sologameInstance.getNombreLettre().size());
						restant.showAndWait();
					} else {
						Fini = true;
					}
				}
			} else {
				if (test.sologameInstance == null) {
					test.gameInstance.tourActuel().incrementTourPasser();
					if (joueur1.verifierTour()) {
						joueur1.terminerTour();
						joueur2.commencerTour();
						joueur1 = test.gameInstance.adversaireActuel();
						joueur2 = test.gameInstance.tourActuel();
					} else {
						joueur2.terminerTour();
						joueur1.commencerTour();
						joueur2 = test.gameInstance.adversaireActuel();
						joueur1 = test.gameInstance.tourActuel();
					}
				} else {
					test.sologameInstance.tourActuel().incrementTourPasser();
					joueur1.terminerTour();
					joueur2.commencerTour();
					joueur1 = test.sologameInstance.adversaireActuel();
					joueur2 = test.sologameInstance.tourActuel();

					Iajoue().setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				        @Override
				        public void handle(WorkerStateEvent t)
				        {
				        	for(Node a:test.acceuildroite.getChildren()) {
				                if(a.getClass().equals(Button.class)) {
				                    a.setDisable(false);
				                }
				            }
				    		test.acceuilgauche.getChildren().get(2).setDisable(false);
				        	joueur2.terminerTour();
							joueur1.commencerTour();
							joueur2 = test.sologameInstance.adversaireActuel();
							joueur1 = test.sologameInstance.tourActuel();

							test.acceuilgauche.getChildren().remove(2);
							test.initchevalet(test.sologameInstance.tourActuel().getMain());
							test.acceuilgauche.getChildren().add(test.chevalet);
							nj1.setText(test.sologameInstance.tourActuel().verifierNom());
							nj2.setText(test.sologameInstance.adversaireActuel().verifierNom());
							score1.setText(String.valueOf(test.sologameInstance.tourActuel().verifierScore()));
							score2.setText(String.valueOf(test.sologameInstance.adversaireActuel().verifierScore()));

				        }
				    });



				}
			}
		}

		if (test.sologameInstance == null) {

			test.acceuilgauche.getChildren().remove(2);
			test.initchevalet(test.gameInstance.tourActuel().getMain());
			test.acceuilgauche.getChildren().add(test.chevalet);
			nj1.setText(test.gameInstance.tourActuel().verifierNom());
			nj2.setText(test.gameInstance.adversaireActuel().verifierNom());
			score1.setText(String.valueOf(test.gameInstance.tourActuel().verifierScore()));
			score2.setText(String.valueOf(test.gameInstance.adversaireActuel().verifierScore()));

		} else {



		}
		if (Fini) {
			quitter(event);
		}
		suggestionmain.getItems().clear();
		test.selection.clear();
	}

}
