package Vue;


import Modele.Jeu;
import Modele.Piece;
import Modele.Plateau;
import Patterns.Observateur;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;

public abstract class View implements Observateur {


	private final BorderPane pane;
	public Jeu jeu;
	public Plateau plateau;
	Plateau[] plateauPiece;
	public Plateau plateauAffiche;
	public View(Jeu j) {
		jeu = j;
		plateau = jeu.plateau;
		plateauPiece = jeu.plateauPiece;
		plateauAffiche = jeu.plateauAffiche;
		pane = new BorderPane();
	}

	public BorderPane getPane() {
		return pane;
	}

	public double getWidth() {
		return pane.getWidth();
	}

	public double getHeight() {
		return pane.getHeight();
	}

	void line(GraphicsContext g, double largeur, double hauteur) {
		g.clearRect(0, 0, largeur, hauteur);

		g.strokeLine(0, 0, 0, hauteur);
		g.strokeLine(0, 0, largeur, 0);
		g.strokeLine(largeur, 0, largeur, hauteur);
		g.strokeLine(0, hauteur, largeur, hauteur);
	}
	/*public StackPane getPane() {
			return pane;
	}*/


	public ObservableList<Node> getChildren(){
		return pane.getChildren();
	}

	//public abstract void redimension();

	public abstract void onLaunch();



	public void onEnter() {

	}

	public void onLeave() {

	}

	public void onFinish() {

	}

	public void modify(Jeu j) {
		jeu = j;
		plateau = jeu.plateau;
		plateauPiece = jeu.plateauPiece;
		plateauAffiche = jeu.plateauAffiche;
	}
}
