package Controleur;

import Modele.Jeu;
import Modele.Piece;
import Modele.Position;


public abstract class Joueur {
	Jeu jeu;
	int num;

	Joueur(int n, Jeu j) {
		num = n;
		jeu = j;
	}

	public void modify(Jeu j){
		jeu = j;
	}
	int num() {
		return this.num;
	}

	boolean tempsEcoule() {
		return false;
	}
	boolean tempsEcoulePiecePrefere() {
		return false;
	}
	boolean tempsEcouleMechant() {return false;}

	public boolean jeu(Position posPlateau, Position posPiece, Piece p) {
		return false;
	}

}
