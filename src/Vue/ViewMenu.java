package Vue;
import static blokus.Framework.app;

import Modele.Jeu;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ViewMenu extends View{
	public ViewMenu(Jeu j) {
		super(j);
	}

	private Button jouerBtn;
	private Button aideBtn;
	private Button quitBtn;

	@Override
	public void onLaunch() {
		jouerBtn = new Button("Jouer");
		jouerBtn.setOnAction((event)->{
			app.gotoView("Parametre");
		});
		jouerBtn.setPrefSize(100,30);
		aideBtn = new Button("Aide");
		aideBtn.setOnAction((event)->{
			app.gotoView("Aide");
		});
		aideBtn.setPrefSize(100,30);
		quitBtn = new Button("Quitter");
		quitBtn.setOnAction((event)->{
			app.exit();
		});
		quitBtn.setPrefSize(100,30);
		VBox box = new VBox(jouerBtn,aideBtn,quitBtn);
		box.setAlignment(Pos.CENTER);//居中对齐
		box.setSpacing(20);
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
