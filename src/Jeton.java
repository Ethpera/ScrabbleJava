import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Jeton extends StackPane {

	Main test;
	Rectangle rectangle;
	Rectangle rectangle2;
	Text lettre;
	Text point;
	private double sourisX;
	private double sourisY;
	private double origineX;
	private double origineY;
	private double texteX;
	private double texteY;

	public Jeton(int x, int y, double largeur, double longueur, String letter, Main test) {
		this.test=test;
		Text point=new Text();
    	point.setText(calculpts(letter));
    	point.setFont(new Font(10));
		// create texte
		Text texte = new Text(letter);
		texte.setFont(new Font(20));
		// create rectangle
		Rectangle rectangle = new Rectangle(largeur, longueur);
		rectangle.setStroke(Color.BLACK);
		rectangle.setFill(Color.LIGHTGREEN);

		Rectangle rectangle2 = new Rectangle(largeur, longueur / 3);
		rectangle2.setStroke(Color.BLACK);
		rectangle2.setFill(Color.RED);

		// set position
		origineX = x * largeur;
		origineY = y * longueur;
		rectangle.setLayoutX(origineX);

		// centrer la position de la lettre 2.6 -> longueurcase/longueurlettre 1.3 ->
		// longueurcase/longueurlettre
		texteX = largeur / 2 - texte.getLayoutBounds().getWidth() / 2;
		texteY = longueur / 2 + texte.getLayoutBounds().getHeight() / 4;
		texte.setLayoutX(origineX + texteX);
		texte.setLayoutY(origineY + texteY);

		point.setLayoutX(origineX+texteX);
        point.setLayoutY(origineY+texteY+19);
        point.setLayoutX(origineX+largeur/2 - point.getLayoutBounds().getWidth()/2);
		rectangle2.setLayoutX(x * largeur);
		rectangle2.setLayoutY(longueur);

		rectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				rectangle.setFill(Color.LIGHTGREY);
			}
		});

		rectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				rectangle.setFill(Color.LIGHTGREEN);
			}
		});
		rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				/** Variables pour le drag **/
				sourisX = me.getX();
				sourisY = me.getY();

				appuyer(x, y);
			}
		});
		rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				double base = ((AnchorPane) ((Rectangle) me.getSource()).getParent().getParent()).getChildren().get(2)
						.getLayoutX();
				relacher(base, me.getX(), me.getY());
			}
		});
		rectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// System.out.println(rectangle.getLayoutX()+" "+origineX);
				/** Variables du drag **/
				// positions du carr�
				double xRect = rectangle.getLayoutX();// 198
				double yRect = rectangle.getLayoutY();// 0
				// positions de la souris, par rapport au X du carr� s�lectionn�
				double xSouris = me.getX();// change
				double ySouris = me.getY();// change
				// position stable du carre avec clique
				double posX = xRect + xSouris - sourisX;
				double posY = yRect + ySouris - sourisY;
				/** Update visuelle **/
				rectangle.toFront();
				lettre.toFront();
				rectangle.setLayoutX(posX);
				rectangle.setLayoutY(posY);
				lettre.setLayoutX(posX + texteX);
				lettre.setLayoutY(posY + texteY);

			}

		});
		/**
		 * PARTIE SALE
		 **/

		rectangle2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (rectangle2.getFill().equals(Color.YELLOW)) {
					test.selection.remove(Integer.valueOf(Integer.parseInt(rectangle2.getId())));
					rectangle2.setFill(Color.RED);
				} else {
					test.selection.add(Integer.parseInt(rectangle2.getId()));
					rectangle2.setFill(Color.YELLOW);
				}

			}

		});

		point.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (rectangle2.getFill().equals(Color.YELLOW)) {
					test.selection.remove(Integer.valueOf(Integer.parseInt(rectangle2.getId())));
					rectangle2.setFill(Color.RED);
				} else {
					test.selection.add(Integer.parseInt(rectangle2.getId()));
					rectangle2.setFill(Color.YELLOW);
				}
			}

		});
		texte.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				rectangle.setFill(Color.LIGHTGREY);

			}
		});

		texte.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				rectangle.setFill(Color.LIGHTGREEN);
			}
		});
		texte.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				/** Variables pour le drag **/
				sourisX = me.getX() - texteX;
				sourisY = me.getY() - texteY;

				appuyer(x, y);
			}
		});
		texte.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				double base = ((AnchorPane) ((Text) me.getSource()).getParent().getParent()).getChildren().get(2)
						.getLayoutX();
				relacher(base, (me.getX() + texteX), ((-me.getY() - texteY) * -1));
			}
		});
		texte.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent me) {
				// System.out.println(rectangle.getLayoutX()+" "+origineX);
				/** Variables du drag **/
				// positions du carr�
				double xRect = rectangle.getLayoutX();// 198
				double yRect = rectangle.getLayoutY();// 0
				// positions de la souris, par rapport au X du carr� s�lectionn�
				double xSouris = me.getX() - texteX;// change
				double ySouris = me.getY() - texteY;// change
				// position stable du carre avec clique
				double posX = xRect + xSouris - sourisX;
				double posY = yRect + ySouris - sourisY;
				/** Update visuelle **/
				rectangle.toFront();
				lettre.toFront();
				rectangle.setLayoutX(posX);
				rectangle.setLayoutY(posY);
				lettre.setLayoutX(posX + texteX);
				lettre.setLayoutY(posY + texteY);

			}

		});
		/**
		 * FIN PARTIE SALE
		 **/
		// Attributions
		this.rectangle = rectangle;
		this.rectangle2 = rectangle2;
		this.lettre = texte;
		this.point=point;
	}

	private String calculpts(String letter) {
    	String unpoint="eainorstul";
    	String deuxpoint="dmg";
    	String troispoint="bcp";
    	String quatrepoint="fhv";
    	String huitpoint="jq";
    	String dixpoint="kwxyz";
    	ArrayList points=new ArrayList();
    	points.add(unpoint);
    	points.add(deuxpoint);
    	points.add(troispoint);
    	points.add(quatrepoint);
    	points.add(huitpoint);
    	points.add(dixpoint);
    	int[] val= {1,2,3,4,8,10};
    	for(int a=0;a<points.size();a++) {
    		if(((String)points.get(a)).contains(letter.toLowerCase())) {
    			return (Integer.toString(val[a]));
    		}
    	}
    	return "";
    }

	public void appuyer(int positionX, int positionY) {
		rectangle.setFill(Color.DARKGREY);

		if (rectangle.getId() != null) {
			((Case) ((Group) ((AnchorPane) rectangle.getParent().getParent()).getChildren().get(0)).getChildren()
					.get(Integer.parseInt(rectangle.getId()))).getChildren().get(0).setId("false");
			rectangle.setId(null);
		}
		// System.out.println(positionX+"/"+positionY);
	}

	public void relacher(double base, double posX, double posY) {
		// variable de la position du curseur dans le rectangle
		// base -> layoutX du chevalet / 50 -> marge gauche // 618 -> layoutY grille -
		// layoutY chevalet
		int diffLayoutXGrille=(int)test.acceuilgauche.getChildren().get(0).getLayoutX();
		int diffLayoutYGrille=(int)test.acceuilgauche.getChildren().get(2).getLayoutY()-(int)test.acceuilgauche.getChildren().get(0).getLayoutY();
		double positionJetonX = posX + rectangle.getLayoutX() + base - diffLayoutXGrille;
		double positionJetonY = posY + rectangle.getLayoutY() + diffLayoutYGrille;
		int caseX = (int) positionJetonX / 33;
		int caseY = (int) positionJetonY / 33;

		/** Reposition du Jeton **/
		if (positionJetonY < 0 || positionJetonY > 494 || positionJetonX < 0 || positionJetonX > 494
				|| position(caseX, caseY)) {
			rectangle.setLayoutY(origineY);
			rectangle.setLayoutX(origineX);
			lettre.setLayoutX(origineX + texteX);
			lettre.setLayoutY(origineY + texteY);
			lettre.setId(null);
		} else {
			// extreme gauche 0 - 495 extreme droite // plateau = 500 // case = 33 -> 495 ->
			// exteme droite.
			lettre.setId(Integer.toString(caseX * 15 + caseY % 15));
			((Case)((Group)test.acceuilgauche.getChildren().get(0)).getChildren().get(caseX*15+caseY%15)).getChildren().get(1).setId(lettre.getText());
			caseX = caseX * 33 - (int) base + 50;
			caseY = caseY * 33 - 619;
			rectangle.setLayoutX(caseX);
			rectangle.setLayoutY(caseY);
			lettre.setLayoutX(caseX + texteX);
			lettre.setLayoutY(caseY + texteY);

		}
	}

	public boolean position(int caseX, int caseY) {
		int posCase = caseX * 15 + caseY % 15;
		Rectangle caselibre = (Rectangle) ((Case) ((Group) ((AnchorPane) rectangle.getParent().getParent())
				.getChildren().get(0)).getChildren().get(posCase)).getChildren().get(0);
		if (caselibre.getId().equals("false")) {
			caselibre.setId("true");
			rectangle.setId(Integer.toString(posCase));
			return false;
		}
		return true;

	}

}