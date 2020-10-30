package Modele;
import Patterns.Observable;
import Global.Configuration;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Jeu extends Observable implements Serializable {
	public boolean enCours;
	public int joueurCourant;
	public ArrayList<Piece> []piecesJ;
	public Plateau plateau;
	public ArrayList<Piece> pieces;
	public ArrayList<Position> coord;
	public Plateau[] plateauPiece;
	public Plateau plateauAffiche;
	public Piece pieceCourant;
	public int PosPieceL;
	public int PosPieceC;
	public int [] Score;
	public int ScoreT;
	public boolean[] enCoursJ;
	public boolean[] piecesPossible;

	String fichierPieces = "resources/Pieces/pieces.txt";

	public Jeu(int n) {
		plateau = new Plateau(n,n, this);
		plateauPiece = new Plateau[4];
		for (int i = 0; i < 4; i++) {
			plateauPiece[i] = new Plateau(12, 23, this);
			plateauPiece[i].initPlateauPiece();
		}
		plateauAffiche = new Plateau(5,5, this);
		enCours = false;
		for (int i = 0; i < plateau.p.length; i++)
			for (int j = 0; j < plateau.p[0].length; j++)
				plateau.newVal(i, j, -1);
		plateau.newVal(plateau.taille() - 1, 0, 8);
		joueurCourant = 0;
		pieces = new ArrayList<>();
		piecesJ = new ArrayList[4];
		coord = new ArrayList<>();
		piecesPossible = new boolean[21];

		initPiecesPossible();
		initScores_Joueurs_Jeu();
		initialiserPieces();
		initPiecesJoueurs();
		initEnCoursJ();

		plateauAffiche.initPlateauAffiche();
		plateau.availableCases(joueurCourant, coord);
	}

	public void jouer(Position posPlateau, Position posPiece, Piece p){
		int debutI = posPlateau.l-posPiece.l;
		int debutJ = posPlateau.c-posPiece.c;
		for(int i=0;i<p.taille;i++){
			for(int j=0;j<p.taille;j++) {
				if (p.carres[i][j])
					plateau.p[debutI + i][debutJ + j] = joueurCourant;
			}
		}
		ScoreT += pieceCourant.getNbCarres();
		Score[joueurCourant]+= pieceCourant.getNbCarres();

		setupNextJoueur();
	}

	public void setPieceL(int num) {
		PosPieceL = num;
	}

	public void setPieceC(int num) {
		PosPieceC = num;
	}

	public void setSelected(int num) {
		pieceCourant = new Piece(5);
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if(pieces.get(num).carres[i][j])
					pieceCourant.ajout(true, i, j);
			}
		}
		pieceCourant.num = pieces.get(num).num;
	}

	public boolean enCours() {
		return enCours;
	}

	public void refaire() {
		plateau.p = new int[plateau.taille()][plateau.taille()];
		for (int i = 0; i < 4; i++) {
			plateauPiece[i].initPlateauPiece();
		}
		enCours = false;
		for (int i = 0; i < plateau.p.length; i++)
			for (int j = 0; j < plateau.p[0].length; j++)
				plateau.newVal(i, j, -1);

		plateau.newVal(plateau.taille()-1,0, 8);

		joueurCourant = 0;

		coord = new ArrayList<>();

		initialiserPieces();
		initPiecesJoueurs();
		initPiecesPossible();
		initEnCoursJ();
		initScores_Joueurs_Jeu();

		plateauAffiche.initPlateauAffiche();
		plateau.availableCases(joueurCourant, coord);
		metAJour();
	}

	public void updateJoueurCour(){
		joueurCourant = ((joueurCourant+1) %4);
	}

	public void initialiserPieces() {
		FileInputStream in;
		in = Configuration.charge(fichierPieces);
		LecteurPiece l = new LecteurPiece(in);
		pieces = l.pieces;
	}

	public void initPiecesJoueurs() {
		for (int i = 0; i < 4; i++) {
			piecesJ[i] = new ArrayList<>();
			piecesJ[i].addAll(pieces);
		}


	}

	public boolean noPiecesPosées(int player){
		return piecesJ[player].size() == pieces.size();
	}

	public boolean libre(Position posPlateau, Position posPiece, Piece p) {
		boolean lib = true;
		boolean dans = false;
		int debutI = posPlateau.l-posPiece.l;
		int debutJ = posPlateau.c-posPiece.c;
		for(int i=0;i<p.taille;i++){
			for(int j=0;j<p.taille;j++){
				if (p.carres[i][j]) {
					if(plateau.horsBord(new Position(debutI+i,debutJ+j)))
						return false;
					else {
						if(!(plateau.p[debutI+i][debutJ+j]==-1|| plateau.p[debutI+i][debutJ+j]==8))
							lib = false;
						if(plateau.p[debutI+i][debutJ+j]==8)
							dans = true;
					}
				}
			}
		}
		return lib && dans;
	}

	public boolean connecter(Position posPlateau, Position posPiece, Piece p){
		boolean res = false;
		int debutI = posPlateau.l-posPiece.l;
		int debutJ = posPlateau.c-posPiece.c;
		for(int i=0;i<p.taille;i++){
			for(int j=0;j<p.taille;j++){
				if(p.carres[i][j]){
					if(!plateau.horsBord(new Position(debutI+i-1,debutJ+j)))
						res = res || plateau.p[debutI+i-1][debutJ+j]==joueurCourant;
					if(!plateau.horsBord(new Position(debutI+i+1,debutJ+j)))
						res = res || plateau.p[debutI+i+1][debutJ+j]==joueurCourant;
					if(!plateau.horsBord(new Position(debutI+i,debutJ+j-1)))
						res = res || plateau.p[debutI+i][debutJ+j-1]==joueurCourant;
					if(!plateau.horsBord(new Position(debutI+i,debutJ+j+1)))
						res = res || plateau.p[debutI+i][debutJ+j+1]==joueurCourant;
				}
			}
		}
		return res;
	}

	public boolean placerPossible(Position posPlateau, Position posPiece, Piece p){
		return libre(posPlateau,posPiece,p) && (!connecter(posPlateau,posPiece,p));
	}

	public Piece choixPiece(int num) {
		return pieces.get(num);
	}

	public void  initScores_Joueurs_Jeu(){
		Score = new int[4];
		for (int i = 0; i < 4; i++)
			Score[i] = 0;
		ScoreT = 0;
	}

	public boolean jouable(){
		boolean res = true;
		res = res && (coord.size()>0) && (piecesJ[joueurCourant].size()>0);
		return res;
	}

	public void setupNextJoueur(){
		updateJoueurCour();
		plateau.availableCases(joueurCourant, coord);
		updateEncours();
		metAJour();

	}

	public boolean updateEncours(){
		if(!jouable()){
			enCoursJ[joueurCourant]=false;
			int j=joueurCourant+1;
			System.out.println("Joueur "+ j +" ne peut plus jouer ------ Prochain Joueur !");
		}
		int compte = 0;int gagne=0;
		for(int i=0;i<4;i++){
			if(enCoursJ[i]) {
				gagne=i+1;
				compte++;
			}
		}
		if(compte==1)
			System.out.println("Joueur "+ gagne +" est le dernier joueur !");
		else if(compte==0) {
			System.out.println("Fin du jeu");
			enCours = false;
			return false;
		}
		return true;
	}

	public void initEnCoursJ(){
		enCoursJ = new boolean[4];
		for(int i=0;i<4;i++) {
			enCoursJ[i] = true;
		}
	}

	public void initPiecesPossible(){
		for(int i=0;i<piecesPossible.length;i++){
			piecesPossible[i]=false;
		}
	}

	public void affiche(){
		System.out.println("le joueur:  "+joueurCourant);
		plateau.AffichePlateauP();
	}

	public void recommencer() {
		plateau.p = new int[plateau.taille()][plateau.taille()];
		for (int i = 0; i < 4; i++) {
			plateauPiece[i] = new Plateau(12, 23, this);
			plateauPiece[i].initPlateauPiece();
		}
		plateauAffiche = new Plateau(5,5, this);
		enCours = false;
		for (int i = 0; i < plateau.p.length; i++)
			for (int j = 0; j < plateau.p[0].length; j++)
				plateau.newVal(i, j, -1);

		plateau.newVal(plateau.taille()-1,0, 8);
		joueurCourant = 0;
		pieces = new ArrayList<>();
		piecesJ = new ArrayList[4];
		coord = new ArrayList<>();
		initialiserPieces();
		initPiecesJoueurs();
		initEnCoursJ();
		initScores_Joueurs_Jeu();

		plateauAffiche.initPlateauAffiche();
		plateau.availableCases(joueurCourant, coord);
		metAJour();
	}

	public void load(Jeu j) {
		plateau = j.plateau;
		for (int i = 0; i < 4; i++) {
			plateauPiece[i] = j.plateauPiece[i];
		}
		plateauAffiche = j.plateauAffiche;
		enCours = false;

		joueurCourant = j.joueurCourant;
		pieces = j.pieces;
		piecesJ = j.piecesJ;
		coord = j.coord;
		for(int i=0;i<4;i++) {
			enCoursJ[i] = j.enCoursJ[i];
			Score[i] = j.Score[i];
		}

		plateauAffiche.initPlateauAffiche();
		plateau.availableCases(joueurCourant, coord);
		metAJour();
	}

}
