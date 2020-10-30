package Controleur;

import java.util.*;

import Modele.Jeu;
import Modele.Piece;
import Modele.Position;

public class JoueurIA extends Joueur {
	Random r;
	ArrayList<Integer> []preferencee;
	ArrayList<Integer> []preference;

	public JoueurIA(int n, Jeu j) {
		super(n, j);
		r = new Random();
		initPreferencee();
		initPreference();
	}

	boolean tempsEcoule(){
		if(!jeu.jouable()){
			jeu.setupNextJoueur();
			return true;
		}
		else{
			int bound = jeu.piecesJ[jeu.joueurCourant].size();
			Position posPlateau;
			boolean trouver=false;
			while(!trouver){
				num = r.nextInt(bound);
				posPlateau = jeu.coord.get(r.nextInt(jeu.coord.size()));
				jeu.setSelected(jeu.piecesJ[jeu.joueurCourant].get(num).getNum());
				getFormePiece();
				Piece choix = jeu.pieceCourant;
				Position posPiece = getPosPiece(posPlateau, choix);
				if (jeu.placerPossible(posPlateau, posPiece, choix)) {
					jeu.piecesJ[jeu.joueurCourant].remove(jeu.pieces.get(choix.getNum()));
					jeu.plateauPiece[jeu.joueurCourant].enlevePiece(choix.getNum());
					jeu.jouer(posPlateau, posPiece, choix);
					trouver = true;
				}
			}
			return true;
		}
	}

	boolean tempsEcoulePiecePrefere(){
		if(!jeu.jouable()){
			jeu.setupNextJoueur();
			return true;
		}
		else{
			getNextPiece();
			Piece choix = jeu.pieceCourant;
			Position posPlateau;
			boolean trouver=false;
			while(!trouver){
				posPlateau = jeu.coord.get(r.nextInt(jeu.coord.size()));
				getFormePiece();
				Position posPiece = getPosPiece(posPlateau, choix);
				if (jeu.placerPossible(posPlateau, posPiece, choix)) {
					jeu.piecesJ[jeu.joueurCourant].remove(jeu.pieces.get(choix.getNum()));
					jeu.plateauPiece[jeu.joueurCourant].enlevePiece(choix.getNum());
					jeu.jouer(posPlateau, posPiece, choix);
					trouver = true;
				}
			}
			return true;
		}
	}

	boolean tempsEcouleMechant(){
		if(!jeu.jouable()){
			jeu.setupNextJoueur();
			return true;
		}
		else {
			int max = getNextPieceMax();
			Piece choix = jeu.pieceCourant;
			Position posPlateau;
			boolean trouver=false;
			while(!trouver){
				posPlateau = jeu.coord.get(r.nextInt(jeu.coord.size()));
				getFormePiece();
				Position posPiece = getPosPiece(posPlateau, choix);
				if (jeu.placerPossible(posPlateau, posPiece, choix) && essayerDePlacer(posPlateau,posPiece,choix)==max) {
					jeu.piecesJ[jeu.joueurCourant].remove(jeu.pieces.get(choix.getNum()));
					jeu.plateauPiece[jeu.joueurCourant].enlevePiece(choix.getNum());
					jeu.jouer(posPlateau, posPiece, choix);
					trouver = true;
				}
			}
			return true;
		}
	}

	void getFormePiece(){
		int tourner = r.nextInt(4);
		int inverser = r.nextInt(2);
		for(int i=0;i<=tourner;i++){
			jeu.pieceCourant.retationGauche();
		}
		if(inverser==0){
			jeu.pieceCourant.Miroir();
		}
	}

	Position getPosPiece(Position posPl, Piece p) {
		Position pos = new Position(0, 0);
		for (int i = 0; i < p.taille; i++) {
			for (int j = 0; j < p.taille; j++) {
				pos = new Position(i, j);
				if (p.carres[i][j]) {
					if (jeu.placerPossible(posPl, pos, p)) {
						return pos;
					}
				}
			}
		}
		return pos;
	}

	void initPreferencee(){
		preferencee = new ArrayList[5];
		ArrayList<Integer> buf = new ArrayList<>();
		for(int i=0;i<preferencee.length;i++){
			preferencee[i] = new ArrayList<>();
		}
		buf.add(19);buf.add(18);buf.add(16);buf.add(20);buf.add(10);buf.add(12);buf.add(13);preferencee[0].addAll(buf); buf.clear();
		buf.add(5);buf.add(8);preferencee[1].addAll(buf); buf.clear();
		buf.add(9);buf.add(11);buf.add(15);buf.add(17);preferencee[2].addAll(buf); buf.clear();
		buf.add(7);buf.add(2);buf.add(14);buf.add(4);buf.add(6);buf.add(3);buf.add(1);buf.add(0);preferencee[3].addAll(buf); buf.clear();
	}

	void initPreference(){
		preference = new ArrayList[5];
		ArrayList<Integer> buf = new ArrayList<>();
		for(int i=0;i<preference.length;i++){
			preference[i] = new ArrayList<>();
		}
		buf.add(12);buf.add(14);buf.add(16);buf.add(18);buf.add(19);buf.add(20);preference[0].addAll(buf); buf.clear();
		buf.add(9);buf.add(10);buf.add(11);buf.add(13);buf.add(15);buf.add(17);preference[1].addAll(buf); buf.clear();
		buf.add(4);buf.add(5);buf.add(6);buf.add(7);buf.add(8);preference[2].addAll(buf); buf.clear();
		buf.add(2);buf.add(3);preference[3].addAll(buf); buf.clear();
		buf.add(1);buf.add(0);preference[4].addAll(buf); buf.clear();
	}

	void getNextPiece(){
		boolean pieceTrouve = false; int i=0;
		while(!pieceTrouve && i<4){
			int compte=0;
			for(int j=0;j<preferencee[i].size();j++){
				if(jeu.piecesPossible[preferencee[i].get(j)])
					compte++;
			}
			if(compte!=0){
				int k=r.nextInt(preferencee[i].size());
				while(!jeu.piecesPossible[preferencee[i].get(k)]){
					k=r.nextInt(preferencee[i].size());
				}
				jeu.setSelected(preferencee[i].get(k));
				pieceTrouve = true;
			}
			i++;
		}
	}

	int essayerDePlacer(Position posPlateau,Position posPiece,Piece p){
		if(jeu.placerPossible(posPlateau,posPiece,p)){
			int debutI = posPlateau.l-posPiece.l;
			int debutJ = posPlateau.c-posPiece.c;
			for(int i=0;i<p.taille;i++){
				for(int j=0;j<p.taille;j++) {
					if (p.carres[i][j])
						jeu.plateau.p[debutI + i][debutJ + j] = jeu.joueurCourant;
				}
			}
			jeu.piecesJ[jeu.joueurCourant].remove(jeu.pieces.get(p.getNum()));
			jeu.plateau.availableCases(jeu.joueurCourant, jeu.coord);
			int moi = jeu.coord.size();
			int autres = calculerAutres();
			for(int i=0;i<p.taille;i++){
				for(int j=0;j<p.taille;j++) {
					if (p.carres[i][j])
						jeu.plateau.p[debutI + i][debutJ + j] = -1;
				}
			}
			jeu.piecesJ[jeu.joueurCourant].add(jeu.pieces.get(p.getNum()));
			jeu.plateau.availableCases(jeu.joueurCourant, jeu.coord);
			return moi-autres+100;
		}
		else return 0;
	}

	int maxCasePosition(Position posPlateau,Piece p){
		int tourner=0;	int inverser=0;
		int max = 0;
		while(!(tourner==4 && inverser==1)) {
			Position posPiece = getPosPiece(posPlateau,p);
			int buf = essayerDePlacer(posPlateau,posPiece,p);
			if(buf>max) {
				max = buf;
			}
			p.retationGauche();tourner++;
			if(tourner==4 && inverser==0){
				p.Miroir();	inverser=1;	tourner=0;
			}
		}
		return max;
	}

	int maxCasePiece(Piece p){
		int max = 0;
		ArrayList<Position> copy = new ArrayList<>();
		copy.addAll(jeu.coord);
		for(Position posPlateau:copy){
			int buf = maxCasePosition(posPlateau,p);
			if(buf > max) {
				max = buf;
			}
		}
		return max;
	}

	int maxCaseListPiece(ArrayList<Integer> pieces){
		int max = 0;
		for(int p:pieces){
			int buf;
			if(!jeu.piecesPossible[p]) {
				buf = 0;
			}
			else {
				Piece pieceCourant = new Piece(5);
				for(int i=0;i<5;i++) {
					for(int j=0;j<5;j++) {
						if(jeu.pieces.get(p).carres[i][j])
							pieceCourant.ajout(true, i, j);
					}
				}
				pieceCourant.setNum(p);
				buf = maxCasePiece(pieceCourant);
			}
			if(buf>=max){
				max = buf;
				if(jeu.piecesPossible[p])
					jeu.setSelected(p);
			}
		}
		return max;
	}

	int getNextPieceMax(){
		boolean pieceTrouve = false; int i=0;
		int max = 0;
		while(!pieceTrouve && i<5){
			int compte=0;
			for(int j=0;j<preference[i].size();j++){
				if(jeu.piecesPossible[preference[i].get(j)])
					compte++;
			}
			if(compte!=0){
				max = maxCaseListPiece(preference[i]);
				pieceTrouve = true;
			}
			i++;
		}
		return max;
	}

	int calculerAutres(){
		int nombre = 0;
		int j = jeu.joueurCourant;
		jeu.updateJoueurCour();
		while(j!=jeu.joueurCourant){
			jeu.plateau.availableCases(jeu.joueurCourant, jeu.coord);
			nombre = nombre + jeu.coord.size();
			jeu.updateJoueurCour();
			jeu.plateau.availableCases(jeu.joueurCourant, jeu.coord);
		}
		return nombre;
	}
}