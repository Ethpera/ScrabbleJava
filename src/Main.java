
import java.awt.Transparency;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Main extends Application {

	/* Lance l'application, gère les controls de la page d'acceuil */

	private Stage primaryStage;
	BorderPane rootLayout;
	private AnchorPane loader2;
	SplitPane acceuilmain;
	AnchorPane acceuilgauche;
	AnchorPane acceuildroite;
	MediaPlayer mediaPlayer;
	Multijoueur gameInstance;
	MatchIA sologameInstance;
	// Création d'une liste qui contient les id des lettres que le joueur
	// sélectionne pour echanger (voir particontrol.java rubrique jeter lettres)
	ObservableList<Integer> selection = FXCollections.observableArrayList();


	Plateau P = new Plateau();

	// difficulté
	int diff = 2;

	int largeurPlat = 500;
	int longueurPlat = 500;

	String a;

	int ligne = 15;
	int colonne = 15;

	double largeurCase = largeurPlat / ligne;
	double longueurCase = longueurPlat / colonne;

	Case[][] plalateau = new Case[ligne][colonne];
	Jeton[][] comptoir = new Jeton[7][1];

	// Group = regroupement d'objets pour facilement interagir avec (fonctionne un
	// peu comme une liste)
	Group grid;
	Group root;
	Group grille;
	Group chevalet;

	/* Initialize chaque partie */

	public void initrootlayout() {

		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("Rootlayout.fxml"));
			rootLayout = (BorderPane) loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initacceuil(String url) {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			if (url.equals("Startmenu.fxml")) {
				loader.setLocation(getClass().getResource(url));
				loader2 = (AnchorPane) loader.load();
			} else {
				loader.setLocation(getClass().getResource(url));
				loader2 = (AnchorPane) loader.load();
				Partiecontrol controller = loader.<Partiecontrol>getController();
				controller.initialize(this);
			}

			acceuilmain = (SplitPane) loader2.getChildren().get(0);
			acceuilgauche = (AnchorPane) acceuilmain.getItems().get(0);
			acceuildroite = (AnchorPane) acceuilmain.getItems().get(1);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initgrille() {
		grille = new Group();
		for (int i = 0; i < ligne; i++) {
			for (int j = 0; j < colonne; j++) {

				Case carreau = new Case(i, j, largeurCase, longueurCase, P.returnBoite(i, j));

				grille.getChildren().add(carreau);

				plalateau[i][j] = carreau;

			}
		}
		grille.setLayoutX(50);
		grille.setLayoutY(80);
	}

	public void initchevalet(ArrayList<String> lettres) {
		chevalet = new Group();
		for (int i = 0; i < lettres.size(); i++) {

			Jeton carreau = new Jeton(i, 0, largeurCase, longueurCase, lettres.get(i).toUpperCase(), this);
			carreau.rectangle2.setId(String.valueOf(i));
			chevalet.getChildren().add(carreau.rectangle);
			chevalet.getChildren().add(carreau.rectangle2);
			chevalet.getChildren().add(carreau.lettre);
			chevalet.getChildren().add(carreau.point);

			comptoir[i][0] = carreau;

		}
		// 50 -> marge gauche // 250-> moitié taille plateau // size -> nombres
		// d'éléments dans chevalet // 6 -> 3*2 / 3 éléménts différents / 2 moitié
		chevalet.setLayoutX(50 + 250 - ((double) chevalet.getChildren().size() / 8) * longueurCase);
		chevalet.setLayoutY(700);
	}

	public void initgrid() {
		grid = new Group();
		// X axis
		NumberAxis xAxis = new NumberAxis(0, 14, 1);
		xAxis.setLabel("X");
		xAxis.setTranslateX(16.5);
		xAxis.setMinorTickVisible(false);

		// Y axis
		NumberAxis yAxis = new NumberAxis(-14, 0, 1);
		yAxis.setLabel("Y");
		yAxis.setTranslateY(-16.5);
		yAxis.setMinorTickVisible(false);
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
			@Override
			public String toString(Number value) {
				// note we are printing minus value
				return String.format("%d", -value.intValue());
			}
		});

		// Creation linechart
		LineChart linechart = new LineChart(xAxis, yAxis);

		linechart.setPrefSize(540, 540);

		// change grid position
		grid.setLayoutX(-14);
		grid.setLayoutY(97);
		grid.getChildren().add(linechart);
	}

	/* raccourci pour tout initialiser */

	public void init(String fxml) {
		initrootlayout();
		initacceuil(fxml);
		initgrille();
		initgrid();
		// initchevalet();
	}

	/* différents bouton sur la page d'acceuil */

	@FXML
	private Button multijoueur;

	@FXML
	private TextField nom2;

	@FXML
	private TextField nom1;

	@FXML
	private Button unijoueur;

	@FXML
	private Button diff3;

	@FXML
	private Button diff1;

	@FXML
	private Button diff2;

	@FXML
	void lunatic(ActionEvent event) {
		diff = 3;
		Button bouton = (Button) event.getSource();
		bouton.setStyle("-fx-background-color: red");
		diff1.setStyle("-fx-background-color: lightgreen");
		diff2.setStyle("-fx-background-color: lightgreen");
	}

	@FXML
	void facile(ActionEvent event) {
		diff = 1;
		Button bouton = (Button) event.getSource();
		bouton.setStyle("-fx-background-color: red");
		diff3.setStyle("-fx-background-color: lightgreen");
		diff2.setStyle("-fx-background-color: lightgreen");
	}

	@FXML
	void normal(ActionEvent event) {
		diff = 2;
		Button bouton = (Button) event.getSource();
		bouton.setStyle("-fx-background-color: red");
		diff1.setStyle("-fx-background-color: lightgreen");
		diff3.setStyle("-fx-background-color: lightgreen");
	}

	// Lance une parti en multijoueur
	@FXML
	void multi(ActionEvent event) throws IOException {

		Button bouton = (Button) event.getSource();

		gameInstance = new Multijoueur(nom1.getText(), getNom2().getText());

		Group root = ((Group) bouton.getParent().getParent().getParent().getParent());
		root.getChildren().clear();

		init("Partiemenumulti.fxml");

		// place la grille

		acceuilgauche.getChildren().add(grille);

		// place le chevalet
		acceuilgauche.getChildren().add(grid);
		acceuilgauche.getChildren().add(chevalet);

		root.getChildren().add(acceuilmain);
		root.getChildren().add(rootLayout);

	}
	// Lance une partie contre l'ordinateur

	@FXML
	void solo(ActionEvent event) throws IOException {
		Button bouton = (Button) event.getSource();

		sologameInstance = new MatchIA(nom1.getText(), diff);

		Group root = ((Group) bouton.getParent().getParent().getParent().getParent());
		root.getChildren().clear();

		init("Partiemenusolo.fxml");

		// place la grille

		acceuilgauche.getChildren().add(grille);

		// place le chevalet
		acceuilgauche.getChildren().add(grid);
		acceuilgauche.getChildren().add(chevalet);

		root.getChildren().add(acceuilmain);
		root.getChildren().add(rootLayout);

	}

	// Permet d'appeler un music qui se jouera sur la page uniquement

	public static void music(String nom) {
		String path = "music/" + nom + ".mp3";

		// Instantiating Media class
		Media media = new Media(new File(path).toURI().toString());

		// Instantiating MediaPlayer class
		MediaPlayer mediaPlayer = new MediaPlayer(media);

		// by setting this property to true, the audio will be played
		mediaPlayer.setAutoPlay(true);
	}

	// Démarre le programme
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Jeux du Scrabble");
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			primaryStage.setWidth(1205);
			primaryStage.centerOnScreen();

		});
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			primaryStage.setHeight(800);
			primaryStage.centerOnScreen();
		});
		this.primaryStage.setResizable(false);

//créer grille,chevalet,affichage

		init("Startmenu.fxml");

//créer un scene

		root = new Group();
		Scene scenePrincipale = new Scene(root, 1200, 1000, Color.WHITE);

//place la grille

		acceuilgauche.getChildren().add(grille);

//ajoute l'affichage à la scene

		root.getChildren().add(acceuilmain);
		root.getChildren().add(rootLayout);

//montre la scene
		// music();
		MediaView mediaView = new MediaView(mediaPlayer);
		root.getChildren().add(mediaView);

		scenePrincipale.getStylesheets().clear();
		scenePrincipale.getStylesheets().add("chart.css");
		primaryStage.setScene(scenePrincipale);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

	public TextField getNom1() {
		return nom1;
	}

	public void setNom1(TextField nom1) {
		this.nom1 = nom1;
	}

	public TextField getNom2() {
		return nom2;
	}

	public void setNom2(TextField nom2) {
		this.nom2 = nom2;
	}

}