package Modele;

import javafx.scene.paint.Color;

public interface PieceInterface {
    
	public void retationGauche();
	public void retationDroite();
	public void Miroir();
	public Color getColor();
	public void setColor(Color color);
	public int getNum();
	public void setNum(int num);
	public int getTaille();
	public void ajout(boolean b, int i, int j);
	
}
