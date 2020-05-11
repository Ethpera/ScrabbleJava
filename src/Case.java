import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Case extends StackPane {

	Text lettre;
	Rectangle rectangle = new Rectangle();

	public Case(int x, int y, double largeur, double longueur, int coucouleur) {
		Color couleur = Color.GREEN;
		// Set color
		if (coucouleur == 4) {
			couleur = Color.RED;
		} else if (coucouleur == 3) {
			couleur = Color.ROSYBROWN;
		} else if (coucouleur == 2) {
			couleur = Color.BLUE;
		} else if (coucouleur == 1) {
			couleur = Color.LIGHTBLUE;
		}

		// create rectangle
		Text lettre = new Text();
		lettre.setFont(new Font(20));
		Rectangle rectangle = new Rectangle(largeur, longueur);
		rectangle.setStroke(Color.BLACK);
		rectangle.setFill(couleur);
		rectangle.setId("false");

		// set position
		lettre.setTranslateX(x * largeur);
		lettre.setTranslateY(y * longueur);
		rectangle.setTranslateX(x * largeur);
		rectangle.setTranslateY(y * longueur);

		this.rectangle = rectangle;
		this.lettre = lettre;
		getChildren().addAll(this.rectangle);
		getChildren().addAll(this.lettre);

	}

}
