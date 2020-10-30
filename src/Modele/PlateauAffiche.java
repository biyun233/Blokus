package Modele;

public class PlateauAffiche {
	boolean[][] p;

	public PlateauAffiche() {
		p = new boolean[5][5];
	}
	public void initPlateauAffiche() {
		for(int i =0;i<p.length;i++) {
			for(int j = 0;j<p[0].length;j++) {
				p[i][j]=false;
			}
		}
	}
	public void AffichePlateau() {
		System.out.println("Afficher PlateauAffiche");
		for(int i =0;i<p.length;i++) {
			for(int j = 0;j<p[0].length;j++) {
				if(p[i][j]==false)
					System.out.print("0");
				else
					System.out.print("1");
			}
			System.out.println();
		}
	}
	public void PlacerPiece(Piece piece) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				p[i][j] = piece.carres [i][j];
			}
		}
	}
	public boolean valeur(int i, int j) {
		return p[i][j];
	}
	public void setPiece(boolean [][] carres) {
		this.p = carres;
	}
	public void retationGauche() {
		boolean[][] carres = new boolean[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				carres [i][j] = p[j][5-i-1];
			}
		}
		setPiece(carres);
	}
	public void Miroir() {
		boolean[][] carres = new boolean[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				carres[i][j] = p[i][5- j - 1];
			}
		}
		setPiece(carres);
	}
}
