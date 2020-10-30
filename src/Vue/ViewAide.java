package Vue;
import static blokus.Framework.app;
import javafx.scene.text.*;

import Modele.Jeu;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewAide extends View{
	public ViewAide(Jeu j) {
		super(j);
	}

	private Button retourBtn;
	private  TextArea  editor;


	@Override
	public void onLaunch() {


		Text t = new Text( "Les Règles du Jeu");
		t.setFont(Font.font("Verdana", 40));


		editor = new TextArea(" Le jeu se compose d’un plateau de cases dans lesquelles on doit encastrer des pièces de différentes formes. Chacune des 21 pièces est de forme différente, un peu comme au Tétris. Le but de chaque joueur est de recouvrir le plus de cases du plateau, tout en essayant de bloquer ses adversaires. Il est possible de jouer à 2 ou 4 personnes (4 couleurs différentes de pièces). Le premier joueur pose la pièce de son choix sur le plateau de telle sorte que celle-ci recouvre une case d’angle du plateau. Les autres joueurs jouent à tour de rôle et placent leur pièce de la même manière. Pour les tours suivants, chaque nouvelle pièce posée doit toucher une pièce de la même couleur par un ou plusieurs coins et jamais par les côtés. En revanche, les pièces de couleur différente peuvent se toucher par les côtés, le jeu peut etre joué en utilisant la souris ou le clavier aussi. ");
		editor.setEditable(false);
		editor.setPrefSize(400, 400);
		editor.setWrapText(true);
		editor.setFont(new Font(20));


		retourBtn = new Button("Retour");
		retourBtn.setOnAction((event)->{
			app.gotoView("Menu");
		});

		retourBtn.setPrefSize(100,30);


		VBox box = new VBox(t,editor,retourBtn);
		box.setAlignment(Pos.CENTER);//居中对齐
		box.setSpacing(10);
		getPane().setCenter(box);

	}

	@Override
	public void miseAJour() {

	}

	/*@Override
	public void redimension() {
		// TODO Auto-generated method stub

	}*/

}
