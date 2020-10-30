package Modele;


import java.io.Serializable;

import javafx.scene.paint.Color;

public class Piece implements PieceInterface ,Serializable  {
	public boolean [][] carres;
	Color color;
	int num;
	public int  taille;
	public int nbAngles;


	public Piece(int taille) {
		carres = new boolean[taille][taille];
		this.taille = taille;
	}

	public Piece(int num, Color color,int taille) {
		this.num = num;
		this.color = color;
		this.taille = taille;
		this.carres = new boolean [taille][taille];
	}

	public void setPiece(boolean [][] carres) {
		this.carres = carres;
	}

	public void AffichePiece() {
		System.out.println("Afficher Piece");
		for(int i =0;i<taille;i++) {
			for(int j = 0;j<taille;j++) {
				if(carres[i][j]==false)
					System.out.print("0");
				else
					System.out.print("1");
			}
			System.out.println();
		}
	}

	@Override
	public void retationGauche() {
		boolean[][] carres = new boolean[this.taille][this.taille];

		for (int i = 0; i < this.taille; i++) {
			for (int j = 0; j < this.taille; j++) {
				carres [i][j] = this.carres [j][this.taille-i-1];
			}
		}
		setPiece(carres);
	}

	@Override
	public void retationDroite() {
		boolean[][] carres = new boolean[this.taille][this.taille];

		for (int i = 0; i < this.taille; i++) {
			for (int j = 0; j < this.taille; j++) {
				carres[j][this.taille - i - 1 ] = this.carres[i][j];
			}
		}
		setPiece(carres);
	}

	@Override
	public void Miroir() {
		boolean[][] carres = new boolean[this.taille][this.taille];
		for (int i = 0; i < this.taille; i++) {
			for (int j = 0; j < this.taille; j++) {
				carres[i][j] = this.carres[i][this.taille- j - 1];
			}
		}
		setPiece(carres);
	}

	public boolean PlacePiece(int l, int c, Piece piece, int [][] Plateau, int player){

		for(int i=l;i<piece.taille;i++) {
			for (int j = c; j < piece.taille; j++) {
				if (Plateau[i][j] != 0){
					return false;
				}
			}
		}

		for(int i=0;i<piece.taille;i++) {
			for (int j = 0; j < piece.taille; j++) {
				if(piece.carres[i][j] == true){
					Plateau[i][j] = player;
				}
			}
		}

		return  true;
	}

	@Override
	public Color getColor() {
		return this.color;
	}


	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int getNum() {
		return this.num;
	}

	@Override
	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public int getTaille() {
		return this.taille;
	}

	@Override
	public void ajout(boolean b ,int i, int j) {
		this.carres[i][j] = b;
	}

	public void setAngle(int a){
		this.nbAngles = a;
	}

	public int nbAngles(){ return this.nbAngles;}

	@Override
	public String toString() {
		//return "num:"+this.num+" Color:"+this.color;
		return "";
	}
	public int getNbCarres() {
		int nbCarres= 0;
		for(int i = 0; i< this.taille; i++)
			for(int j= 0; j<this.taille;j++)
				if(carres[i][j]==true){
					nbCarres+=1;
				}
		return nbCarres;
	}

	//Si vous voulez tester
/*
	public void affiche() {
		for(int i = 0; i< this.taille; i++) {
			for(int j= 0; j<this.taille;j++)
				{System.out.print(this.carres[i][j]+"\t");}
			System.out.println("");}
		System.out.println("");
	}

*/



}
