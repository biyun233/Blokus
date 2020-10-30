package Vue;

import java.util.HashMap;

import Modele.Jeu;
import Modele.Plateau;
import Patterns.Observateur;
import blokus.Framework;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App implements Observateur{

	Jeu jeu;
	Plateau plateau;
	Plateau[] plateauPiece;
	private final Stage stage;
	private final Scene scene;
	private final Pane pane;
	private final Group root;
	private final HashMap<String,View> viewMap;
	private final ObjectProperty<View> currentView;
	OnLaunch onLaunch;
	OnFinish onFinish;
	OnExit onExit;

	public App(Jeu j, Stage stage) {
		this.jeu = j;
		plateau = jeu.plateau;
		plateauPiece = jeu.plateauPiece;
		this.stage = stage;
		root = new Group();
		pane = new StackPane(root);
		scene = new Scene(pane);
		stage.setScene(scene);

		viewMap = new HashMap<>();
		currentView = new SimpleObjectProperty<View>();
		initFramework();
		initApp();

	}
	private final void initFramework() {
		Framework.app = this;
	}

	private final void initApp() {
		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (event)->{
			if(onExit != null && !onExit.handle()) {
				event.consume();
			}
		});

		currentView.addListener((o,ov,nv)->{
			if(ov != null) {
				ov.onLeave();
				pane.getChildren().remove(ov.getPane());
			}
			if(nv != null) {
				pane.getChildren().add(nv.getPane());
				nv.onEnter();
			}
		});


	}


	public Stage getStage() {
		return stage;
	}

	public Scene getScene() {
		return scene;
	}

	public String getTitle() {
		return stage.getTitle();
	}

	public void setTitle(String title) {
		stage.setTitle(title);
	}
	public StringProperty titleProperty() {
		return stage.titleProperty();
	}

	public double largeurCase() {
		return getWidth() / plateau.taille() ;
	}

	public double hauteurCase() {
		return getHeight()/plateau.taille();
	}

	public double getWidth() {
		return pane.getMinWidth();
	}

	public void setWidth(double width) {
		pane.setMinWidth(width);
	}

	public DoubleProperty widthProperty() {
		return pane.minWidthProperty();
	}

	public double getHeight() {
		return pane.getMinHeight();
	}

	public void setHeight(double height) {
		pane.setMinHeight(height);
	}

	public DoubleProperty HeightProperty() {
		return pane.minHeightProperty();
	}

	public View getView(String name) {
		return viewMap.get(name);
	}

	public void regView(String name, View view) {
		viewMap.put(name, view);
	}

	public void unregView(String name) {
		viewMap.remove(name);
	}

	public View getCurrentView() {
		return currentView.get();
	}

	public ReadOnlyObjectProperty<View> currentViewProperty(){
		return currentView;
	}

	public void gotoView(String name) {
		View view = viewMap.get(name);
		if(view != null) {
			currentView.set(view);
		}
	}
	void launch() {
		if(onLaunch != null) {
			onLaunch.handle();
		}
		for(View view: viewMap.values()) {
			view.onLaunch();
		}
		stage.requestFocus();
		stage.show();
		
		
		/*stage.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				currentView.get().redimension();
				System.out.println("largeur = " + stage.getWidth());
				System.out.println("hauteur = " + stage.getHeight());
			}
		});
		
		stage.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				currentView.get().redimension();
				System.out.println("largeur = " + stage.getWidth());
				System.out.println("hauteur = " + stage.getHeight());
			}
		});*/
	}

	public void finish() {
		for(View view: viewMap.values()) {
			view.onFinish();
		}
		if(onFinish != null) {
			onFinish.handle();
		}
	}

	public void exit() {
		Platform.exit();
	}

	public static interface OnLaunch{
		void handle();
	}

	public static interface OnFinish{
		void handle();
	}

	public static interface OnExit{
		boolean handle();
	}

	@Override
	public void miseAJour() {


	}


}
