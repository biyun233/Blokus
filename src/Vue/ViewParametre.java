package Vue;
import static blokus.Framework.app;

import Controleur.ControleurMediateur;
import Controleur.Joueur;
import Controleur.JoueurHumain;
import Controleur.JoueurIA;
import Modele.Jeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewParametre extends View{
    public ViewParametre(Jeu j) {
        super(j);
    }

    ViewJouer vj;
    ControleurMediateur c;
    private Canvas can;
    private AnchorPane pane;
    VBox boiteJeu;
    VBox dimension;
    VBox charge;
    private Button retourBtn;
    private Button commencerBtn;
    private Button loadBtn;
    private Button Jeu4Btn;
    private Button Jeu2Btn;
    public ChoiceBox<String>[] Joueur;
    public int nbJoueur=4;
    public String[] dif;
    public Joueur[] joueurs;
    public String[] nom;
    public ToggleGroup group;
    public int dim = 23;
    TextField text0;
    TextField text1;
    TextField text2;
    TextField text3;
    HBox JoueurBtn = new HBox();

    @Override
    public void onLaunch() {
        miseAJour();
    }

    @Override
    public void miseAJour() {
        vj = (ViewJouer) app.getView("Jouer");
        c = new ControleurMediateur(jeu,vj,this);
        nom = new String[4];
        dif = new String[4];
        for(int i=0;i<4;i++) {
            nom[i] = "Joueur" + (i+1);
            dif[i] = "Humain";
        }
        can = new Canvas(600,200);
        pane = new AnchorPane(can);
        joueurs = new Joueur[4];

        joueurs[0] = new JoueurHumain(0, jeu);
        joueurs[1] = new JoueurHumain(1, jeu);
        joueurs[2] = new JoueurHumain(2, jeu);
        joueurs[3] = new JoueurHumain(3, jeu);

        HBox JeuBtn = new HBox();
        Jeu4Btn = new Button("Jeu à 4");
        Jeu4Btn.setOnAction((event)->{
            nbJoueur = 4;
            c.modifScore(4);
            miseAJour();
        });
        Jeu2Btn = new Button("Jeu à 2");
        Jeu2Btn.setOnAction((event)->{
            nbJoueur = 2;
            c.modifScore(2);
            miseAJour();
        });
        JeuBtn.getChildren().addAll(Jeu4Btn,Jeu2Btn);

        retourBtn = new Button("Retour");
        retourBtn.setOnAction((event)->{
            app.gotoView("Menu");
        });
        commencerBtn = new Button("Commencer");
        commencerBtn.setOnAction((event)->{
            if(nbJoueur==4) {
                for(int i=0;i<4;i++) {
                    c.valide(i);
                }
            }
            else {
                for(int i=0;i<2;i++) {
                    c.valide(i);
                }
            }
            app.gotoView("Jouer");
        });

        loadBtn = new Button("Charger");
        loadBtn.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                try {
                    c.load(c.fichier);
                    Dialog<ButtonType> dia = new Dialog<ButtonType>();
                    dia.setContentText("Chargement réussi ");
                    dia.getDialogPane().getButtonTypes().add(ButtonType.YES);

                    Button yes = (Button)dia.getDialogPane().lookupButton(ButtonType.YES);
                    yes.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dia.close();
                        }
                    });
                    yes.setText("Oui");
                    dia.show();
                }catch(NullPointerException e) {
                    Dialog<ButtonType> dia = new Dialog<ButtonType>();
                    dia.setContentText("Chargement non réussi ");
                    dia.getDialogPane().getButtonTypes().add(ButtonType.YES);

                    Button yes = (Button)dia.getDialogPane().lookupButton(ButtonType.YES);
                    yes.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            c.setDimension(dim);
                            dia.close();
                        }
                    });
                    yes.setText("Oui");
                    dia.show();
                }


            }


        });


        Joueur = new ChoiceBox[4];
        for(int i=0;i<4;i++) {
            Joueur[i] = new ChoiceBox<String>(FXCollections.observableArrayList("Humain", "Robot simple", "Robot Intelligent","Robot Excellent"));
            Joueur[i].getSelectionModel().selectFirst();
        }

        Label j0 = new Label("Joueur 1 (vert) :   ");
        if(nbJoueur == 2)
            j0.setText("Joueur 1 (vert et jaune) :   ");
        text0 = new TextField();
        text0.setPromptText("Entrez le nom du joueur :");
        text0.setFocusTraversable(false);
        HBox J0 = new HBox(j0,Joueur[0],text0);
        J0.setSpacing(10);

        Label j1 = new Label("Joueur 2 (bleu) :   ");
        if(nbJoueur == 2)
            j1.setText("Joueur 2 (bleu et rouge) :   ");
        text1 = new TextField();
        text1.setPromptText("Entrez le nom du joueur :");
        text1.setFocusTraversable(false);
        HBox J1 = new HBox(j1,Joueur[1],text1);
        J1.setSpacing(10);

        Label j2 = new Label("Joueur 3 (jaune) : ");
        text2 = new TextField();
        text2.setPromptText("Entrez le nom du joueur :");
        text2.setFocusTraversable(false);
        HBox J2 = new HBox(j2,Joueur[2],text2);
        J2.setSpacing(10);

        Label j3 = new Label("Joueur 4 (rouge) : ");
        text3 = new TextField();
        text3.setPromptText("Entrez le nom du joueur :");
        text3.setFocusTraversable(false);
        HBox J3 = new HBox(j3,Joueur[3],text3);
        J3.setSpacing(10);

        VBox joueurbox = new VBox();
        if(nbJoueur == 4) {
            joueurbox.getChildren().addAll(J0,J1,J2,J3);
        }
        else if(nbJoueur == 2) {
            joueurbox.getChildren().addAll(J0,J1);
        }

        joueurbox.setPadding(new Insets(10));
        joueurbox.setSpacing(20);
        pane.getChildren().add(joueurbox);

        boiteJeu = new VBox();
        boiteJeu.getChildren().addAll(JeuBtn,pane);
        boiteJeu.setAlignment(Pos.CENTER);

        dimension = new VBox();
        Label dim = new Label("Veuillez choisir une dimension : ");
        group = new ToggleGroup();
        RadioButton d1 = new RadioButton("11 * 11");
        d1.setUserData(11);;
        d1.setToggleGroup(group);
        RadioButton d2 = new RadioButton("13 * 13");
        d2.setUserData(13);
        d2.setToggleGroup(group);
        RadioButton d3 = new RadioButton("17 * 17");
        d3.setToggleGroup(group);
        d3.setUserData(17);
        RadioButton d4 = new RadioButton("23 * 23");
        d4.setToggleGroup(group);
        d4.setUserData(23);
        d4.setSelected(true);

        dimension.getChildren().addAll(dim,d1,d2,d3,d4);
        dimension.setSpacing(20);

        charge = new VBox();
        Label charger = new Label("Si vous voulez charger une partie :  ");
        TextField text = new TextField();
        text.setPromptText("Entrez le nom d'un fichier");
        text.setFocusTraversable(false);
        charge.getChildren().addAll(charger,text,loadBtn);
        charge.setSpacing(20);

        HBox box = new HBox(retourBtn,commencerBtn);
        box.setAlignment(Pos.BOTTOM_CENTER);//居中对齐
        box.setSpacing(100);
        getPane().setBottom(box);
        getPane().setTop(boiteJeu);
        getPane().setRight(dimension);
        getPane().setLeft(charge);
        getPane().setPadding(new Insets(50));
        BorderPane.setMargin(dimension, new Insets(50));
        BorderPane.setMargin(charge, new Insets(50));

        GraphicsContext gJeu = can.getGraphicsContext2D();
        line(gJeu,can.getWidth(),can.getHeight());

        for(int i = 0;i < 4;i++) {
            if(nbJoueur==4)
                changeModele(Joueur[i], i);
            else if(nbJoueur==2 && i<2) {
                changeModele(Joueur[i], i);
                changeModele(Joueur[i], i+2);
            }
            else if(nbJoueur==2 && i>=2) {
            }
        }
        login(c);
        c.setNomCharge(text);
        selectDimension();

    }

    void login(ControleurMediateur c) {
        if(nbJoueur==4) {
            c.setNom(text0,0);
            c.setNom(text1,1);
            c.setNom(text2,2);
            c.setNom(text3,3);
        }
        else {
            c.setNom(text0,0);
            c.setNom(text1,1);
        }

    }

    String changeModele(ChoiceBox<String> c, int order) {
        c.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equalsIgnoreCase("Humain")) {
                    joueurs[order] = new JoueurHumain(order, jeu);
                }else {
                    joueurs[order] = new JoueurIA(order, jeu);
                }
                dif[order] = newValue;
            }
        });
        return dif[order];
    }
    public int selectDimension() {
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldVal, Toggle newVal)
            {
                RadioButton temp_rb=(RadioButton)newVal;
                c.setDimension((int)temp_rb.getUserData());
                dim = (int)temp_rb.getUserData();
            }
        });
        return dim;
    }


	/*@Override
	public void redimension() {
		// TODO Auto-generated method stub
		
	}*/
}
