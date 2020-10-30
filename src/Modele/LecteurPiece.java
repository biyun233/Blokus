package Modele;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LecteurPiece {
	Scanner s;
	Piece piece;
	ArrayList<Piece> pieces;
	int nbrPieces;

	public LecteurPiece(FileInputStream in) {
		s = new Scanner(in);
		pieces = new ArrayList <Piece>();
		nbrPieces = 0;
		chargePieces();
	}

	public String lisLigne() {
		if (s.hasNextLine()) {
			String ligne;
			ligne = s.nextLine();

			int i;
			int dernier = -1;
			boolean commentaire = false;
			for (i = 0; (i < ligne.length()) && !commentaire; i++) {
				char c = ligne.charAt(i);
				if (!Character.isWhitespace(c) && (c != ';')) {
					dernier = i;
				}
				if (c == ';') {
					commentaire = true;
				}
			}

			// Un commentaire non vide sera pris comme nom de niveau
			// -> le dernier commentaire non vide sera le nom final

			if (commentaire) {
				char c = ' ';
				while (Character.isWhitespace(c) && (i < ligne.length())) {
					c = ligne.charAt(i);
					if (!Character.isWhitespace(c))
						piece.setNum(Integer.parseInt(ligne.substring(i)));
					i++;
				}
			}


			return ligne.substring(0, dernier + 1);
		} else {
			return null;
		}

	}
	Piece lisProchainePiece() {
		piece = new Piece(5);

		String ligne = "";
		while (ligne.length() == 0) {
			ligne = lisLigne();
			if (ligne == null)
				return null;
		}
		int i = 0;
		while ((ligne != null) && (ligne.length() > 0)) {
			for (int j = 0; j < piece.getTaille(); j++) {
				char c = ligne.charAt(j);
				switch (c) {
					case ' ':
						break;
					case '1':
						piece.ajout(true,i, j);
						break;
					case '0':
						piece.ajout(false,i, j);
						break;
					default:
						System.err.println("CaractÃ¨re inconnu : " + c);
				}
			}
			ligne = lisLigne();
			i++;
		}
		if (i > 0)
			return piece;
		else
			return null;

	}

	void chargePieces() {
		Piece piece = null;
		while((piece =lisProchainePiece())!=null) {
			pieces.add(piece);
		}
		nbrPieces = pieces.size();
	}

	Piece getPiece(int num){
		boolean condition = (pieces.size()> 0)&&(num>0)&&(num<21);
		if(condition)
			return  pieces.get(num-1);
		return null;
	}

	int getNbrPieces() {
		return nbrPieces;
	}



}

