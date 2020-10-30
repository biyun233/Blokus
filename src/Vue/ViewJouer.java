package Vue;
import static blokus.Framework.app;

import java.util.EmptyStackException;
import java.util.LinkedList;

import Controleur.ControleurMediateur;
import Controleur.JoueurHumain;
import Controleur.JoueurIA;
import Modele.Jeu;
import Modele.Plateau;
import Modele.Position;
import Structures.Point;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewJouer extends View {

    public ViewJouer(Jeu j) {
        super(j);
    }
    ViewParametre vp;
    ControleurMediateur c;
    double largeurCase, hauteurCase;
    double largeurCasePiece, hauteurCasePiece;
    double largeurCaseAffiche, hauteurCaseAffiche;
    public int joueurCourant = jeu.joueurCourant;
    public VBox Score;
    private Button jouerBtn;
    private Button retourBtn;
    private Button quitBtn;
    private Button recommencerBtn;
    private Button sauvegardeBtn;
    private Button miroirBtn;
    private Button tourneBtn;
    private Button annulBtn;
    private Button refaireBtn;
    public Button[] joueur;
    public HBox JoueurBtn ;
    public Label[] joueur_score;
    public boolean load = false;

    private Canvas canPlateau;
    private Canvas canPiece;
    private Canvas canAffiche;
    private Canvas canScore;

    private AnchorPane panePlateau;
    private AnchorPane panePiece;
    private AnchorPane paneAffiche;
    private AnchorPane paneScore;

    private int affiche = 0;
    VBox gPlateau ;
    VBox gAffiche ;
    VBox gPiece;

    @Override
    public void onLaunch() {
        vp = (ViewParametre) app.getView("Parametre");
        c = new ControleurMediateur(jeu,this,vp);


        canPlateau = new Canvas(450, 400);
        panePlateau = new AnchorPane(canPlateau);
        panePlateau.setPrefSize(450, 400);
        joueur = new Button[4];
        joueur_score = new Label[4];

        jouerBtn = new Button("Jouer");
        jouerBtn.setOnAction((event)->{
            c.h.initHistorique();
            if(load==true) {
                this.modify(jeu);
                c.jeu.load(jeu);
            }
            if(jouerBtn.getText().equalsIgnoreCase("Jouer")) {
                jouerBtn.setText("Pause");
                jeu.enCours = true;
            }
            else if(jouerBtn.getText().equalsIgnoreCase("Pause")) {
                jouerBtn.setText("Jouer");
                jeu.enCours=false;
            }
            load = false;
            miseAJour();

        });

        sauvegardeBtn = new Button("Sauvegarder");
        sauvegardeBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                TextInputDialog in = new TextInputDialog("default");

                in.setTitle("Sauvegarde");
                in.setContentText("Saisir le nom du fichier : ");
                in.setHeaderText("Confirmez votre sauvegarde !");
                Button ok = (Button)in.getDialogPane().lookupButton(ButtonType.OK);
                ok.setText("OK");
                ok.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(in.getEditor().getText());
                        c.h.save(jeu, in.getEditor().getText());
                    }

                });

                Button cancel = (Button)in.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("Annuler");
                cancel.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Annuler");

                    }

                });
                in.show();


            }

        });

        recommencerBtn = new Button("Recommencer");
        recommencerBtn.setOnAction((event)->{
            jeu.recommencer();
            this.modify(jeu);
            jouerBtn.setText("Jouer");
            jeu.enCours = false ;
            joueurCourant = jeu.joueurCourant;
            c.joueurCourant = jeu.joueurCourant;
            affiche=0;
            miseAJour();
        });

        annulBtn = new Button("Annuler");
        annulBtn.setOnAction((event)->{
            try {
                c.annuler();
                jeu.enCours = false;
                jouerBtn.setText("Jouer");
            }catch(EmptyStackException e) {
                System.out.println("je ne peux pas annuler!");
            }
        });

        refaireBtn = new Button("Refaire");
        refaireBtn.setOnAction((event)->{
            try {
                c.refaire();
                jeu.enCours = false;
                jouerBtn.setText("Jouer");
            }catch(EmptyStackException e) {
                System.out.println("je ne peux pas refaire!");
            }
        });

        retourBtn = new Button("Retour");
        retourBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Dialog<ButtonType> dialog = new Dialog<ButtonType>();
                dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
                Button yes = (Button)dialog.getDialogPane().lookupButton(ButtonType.YES);
                yes.setText("Oui");
                Button no = (Button)dialog.getDialogPane().lookupButton(ButtonType.NO);
                no.setText("Non");

                yes.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        jeu.recommencer();
                        modify(jeu);
                        jouerBtn.setText("Jouer");
                        joueurCourant = jeu.joueurCourant;
                        c.joueurCourant = jeu.joueurCourant;
                        c.h.futur.clear();
                        c.h.passe.clear();
                        app.gotoView("Parametre");
                        affiche = 0;
                        miseAJour();
                    }
                });
                no.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();

                    }
                });

                dialog.setContentText("Voulez vous aller à la page Paramètre ?");
                dialog.show();

            }
        });
        quitBtn = new Button("Quit");
        quitBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Dialog<ButtonType> dialog = new Dialog<ButtonType>();
                dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
                Button yes = (Button)dialog.getDialogPane().lookupButton(ButtonType.YES);
                yes.setText("Oui");
                Button no = (Button)dialog.getDialogPane().lookupButton(ButtonType.NO);
                no.setText("Non");

                yes.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        app.exit();
                    }
                });
                no.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();
                    }
                });

                dialog.setContentText("Voulez vous terminer le jeu ?");
                dialog.show();

            }
        });
        miroirBtn = new Button("Miroir");
        miroirBtn.setOnAction((event)->{
            try {
                jeu.plateauAffiche.Miroir();
                jeu.pieceCourant.Miroir();
                miseAJour();
            }catch(NullPointerException e) {
                System.out.println("Selectionnez une piece!");
            }
        });

        tourneBtn = new Button("Tourner");
        tourneBtn.setOnAction((event)->{
            try {
                jeu.plateauAffiche.retationGauche();
                jeu.pieceCourant.retationGauche();
                miseAJour();
            }catch(NullPointerException e) {
                System.out.println("Selectionnez une piece pour tourner!");
            }
        });

        HBox recomSauveBtn = new HBox();
        recomSauveBtn.getChildren().addAll(recommencerBtn,sauvegardeBtn);
        recomSauveBtn.setSpacing(20);
        recomSauveBtn.setAlignment(Pos.CENTER);

        HBox actionBtn = new HBox();
        actionBtn.getChildren().addAll(miroirBtn,tourneBtn);
        actionBtn.setSpacing(20);
        actionBtn.setAlignment(Pos.CENTER);

        HBox annulrefaire = new HBox();
        annulrefaire.getChildren().addAll(annulBtn,refaireBtn);
        annulrefaire.setSpacing(20);
        annulrefaire.setAlignment(Pos.CENTER);

        HBox pageBtn = new HBox();
        pageBtn.getChildren().addAll(retourBtn,quitBtn);
        pageBtn.setSpacing(20);
        pageBtn.setAlignment(Pos.CENTER);

        HBox JoueurBtn = new HBox();
        joueur[0] = new Button("Joueur1");
        joueur[0].setOnAction((event)->{
            joueurCourant = 0;
            miseAJour();
        });
        joueur[1] = new Button("Joueur2");
        joueur[1].setOnAction((event)->{
            joueurCourant = 1;
            miseAJour();
        });
        joueur[2] = new Button("Joueur3");
        joueur[2].setOnAction((event)->{
            joueurCourant = 2;
            miseAJour();
        });
        joueur[3] = new Button("Joueur4");
        joueur[3].setOnAction((event)->{
            joueurCourant = 3;
            miseAJour();
        });

        JoueurBtn.getChildren().addAll(joueur[0],joueur[1],joueur[2],joueur[3]);


        Score = new VBox();
        Label score = new Label("Score");

        Score.getChildren().add(score);
        Score.setPadding(new Insets(20));
        Score.setSpacing(10.0);
        score.setAlignment(Pos.TOP_CENTER);

        for(int i=0;i<4;i++) {
            joueur_score[i] = new Label();
        }
        Score.getChildren().addAll(joueur_score[0],joueur_score[1],joueur_score[2],joueur_score[3]);

        canPiece = new Canvas(450, 200);
        panePiece = new AnchorPane(canPiece);
        panePiece.setPrefSize(450, 200);

        canAffiche = new Canvas(200,200);
        paneAffiche = new AnchorPane(canAffiche);
        paneAffiche.setPrefSize(200,200);

        canScore = new Canvas(200,200);
        paneScore = new AnchorPane(canScore);
        paneScore.getChildren().add(Score);

        gPlateau = new VBox();
        gPiece = new VBox();
        gAffiche = new VBox();

        BorderPane Pane = new BorderPane();

        gPlateau.getChildren().add(panePlateau);
        gPiece.getChildren().addAll(JoueurBtn,panePiece);
        gAffiche.getChildren().addAll(jouerBtn,paneScore,actionBtn,paneAffiche,annulrefaire,recomSauveBtn,pageBtn);
        gAffiche.setSpacing(20);
        gAffiche.setAlignment(Pos.CENTER);

        getPane().setPadding(new Insets(10));

        Pane.setTop(gPlateau);
        Pane.setBottom(gPiece);
        getPane().setLeft(Pane);
        getPane().setRight(gAffiche);

        jeu.ajouteObservateur(this);
        miseAJour();


        canPlateau.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    c.initAffiche();
                    c.clicSouris(e.getX(), e.getY());
                    modify(c.jeu);
                    //jeu.pieceCourant = null;
                }catch(ArrayIndexOutOfBoundsException exception) {
                    System.out.println("Plateau selectionnez une piece!");
                }catch(NullPointerException exception) {
                    System.out.println("null pointer!");
                }
            }
        });

        canPiece.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try {
                    c.selectPiece(e.getX(), e.getY());
                }catch(NullPointerException event) {
                    System.out.println("Selectionnez une piece!");
                }
            }
        });

        canAffiche.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                c.PieceAffiche(e.getX(), e.getY());
                //Get cursor forcement
                canAffiche.requestFocus();
            }
        });

        canAffiche.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                canAffiche.requestFocus();
            }
        });

        canAffiche.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch(event.getCode().getName()) {
                    case "Z":
                        jeu.plateauAffiche.Miroir();
                        jeu.pieceCourant.Miroir();
                        miseAJour();
                        break;
                    case "S":
                        jeu.plateauAffiche.Miroir();
                        jeu.pieceCourant.Miroir();
                        miseAJour();
                        break;
                    case "Q":
                        jeu.plateauAffiche.retationGauche();
                        jeu.pieceCourant.retationGauche();
                        miseAJour();
                        break;
                    case "D":
                        jeu.plateauAffiche.retationDroite();
                        jeu.pieceCourant.retationDroite();
                        miseAJour();
                        break;

                }
            }

        });

        canAffiche.setOnDragDetected(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                canAffiche.startFullDrag();
            }
        });


        canAffiche.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {

            @Override
            public void handle(MouseDragEvent e) {
                c.PieceAffiche(e.getX(), e.getY());
                canAffiche.setDisable(true);
            }
        });


        canPlateau.setOnMouseDragOver(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //reset Color
                miseAJour();
                modify(c.jeu);
                //Render l'indicateur
                double x = event.getX();
                double y = event.getY();
                GraphicsContext gPlateau = canPlateau.getGraphicsContext2D();
                int l = (int) (y / hauteurCase);
                int c = (int) (x / largeurCase);

                Position posPlateau = new Position(l,c);
                Position posPiece = new Position(jeu.PosPieceL,jeu.PosPieceC);

                //Create a list to save the places where we should render
                LinkedList<Point> list = new LinkedList<>();
                //Rechercher les position de chaque carrés d'une pièce


                for(int i = 0; i<5 ;i++) {
                    for(int j = 0; j<5;j++) {
                        if(jeu.pieceCourant.carres[i][j]) {
                            Point p = new Point(i-posPiece.l,j-posPiece.c);
                            list.push(p);
                        }
                    }
                }
                if (jeu.placerPossible(posPlateau, posPiece, jeu.pieceCourant)) {

                    while(!list.isEmpty()){
                        Point p = list.pop();
                        gPlateau.setFill(Color.BLACK);
                        c = posPlateau.c + p.getY();
                        l = posPlateau.l + p.getX();
                        gPlateau.fillRect(c*largeurCase, l*hauteurCase, largeurCase, hauteurCase);
                    }

                }
                else {

                    while(!list.isEmpty()){
                        Point p = list.pop();
                        gPlateau.setFill(Color.RED);
                        c = posPlateau.c + p.getY();
                        l = posPlateau.l + p.getX();
                        gPlateau.fillRect(c*largeurCase, l*hauteurCase, largeurCase, hauteurCase);
                    }
                }
            }
        });


        canPlateau.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {

            @Override
            public void handle(MouseDragEvent e) {
                try {
                    c.initAffiche();
                    c.clicSouris(e.getX(), e.getY());
                    modify(c.jeu);
                }catch(ArrayIndexOutOfBoundsException exception) {
                    System.out.println("select une piece!");
                }catch(NullPointerException exception) {
                    System.out.println("drag null pointer!");
                }
            }
        });
        canPlateau.setOnMouseDragExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //如果把已经选到了的piece拖动到canPlateau以外canPlateau会重置
                miseAJour();


            }

        });

        getPane().setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {

            @Override
            public void handle(MouseDragEvent event) {
                canAffiche.setDisable(false);

            }

        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                c.tictac();
            }
        }.start();
    }


    @Override
    public void miseAJour() {
        if(!jeu.enCoursJ[0]&&!jeu.enCoursJ[1]&&!jeu.enCoursJ[2]&&!jeu.enCoursJ[3]&& affiche ==0) {
            int max = 0;
            int i_max = 0;
            if(vp.nbJoueur==4) {
                for(int i=0;i<4;i++) {
                    if(jeu.Score[i]>=max) {
                        max = jeu.Score[i];
                        i_max = i;
                    }
                }
            }
            if(vp.nbJoueur==2) {
                for(int i=0;i<2;i++) {
                    if((jeu.Score[i]+jeu.Score[i+2])>=max) {
                        max = jeu.Score[i];
                        i_max = i;
                    }
                }

            }
            Dialog<ButtonType> dia = new Dialog<ButtonType>();
            dia.getDialogPane().getButtonTypes().add(ButtonType.YES);
            Button yes = (Button)dia.getDialogPane().lookupButton(ButtonType.YES);
            yes.setText("OK");

            yes.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dia.close();
                }
            });
            dia.setContentText("Fin du jeu!  " + vp.nom[i_max] + "  a gagné!");
            dia.show();
            affiche =1;
        }
        double lignes = plateau.taille();
        double colonnes = plateau.taille();
        largeurCase = largeurPlateau() / colonnes;
        hauteurCase = hauteurPlateau() / lignes;

        GraphicsContext gPlateau = canPlateau.getGraphicsContext2D();
        gPlateau.clearRect(0, 0, largeurPlateau(), hauteurPlateau());
        // Grille
        for (int i=0; i<lignes;i++) {
            for (int j=0; j<colonnes;j++) {
                gPlateau.setFill(Color.LIGHTGRAY);
                gPlateau.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);

            }
        }
        for (int i=0; i<lignes+1;i++) {
            gPlateau.strokeLine(0, i*hauteurCase, largeurPlateau(), i*hauteurCase);
        }
        for (int i=0; i<colonnes+1;i++) {
            gPlateau.strokeLine(i*largeurCase, 0, i*largeurCase, hauteurPlateau());
        }
        // Coups
        draw(hauteurCase, largeurCase, plateau, gPlateau);

        //canPiece
        largeurCasePiece = largeurPiece() / 23;
        hauteurCasePiece = hauteurPiece() / 12;

        GraphicsContext gPiece = canPiece.getGraphicsContext2D();
        line(gPiece,largeurPiece(),hauteurPiece());
        draw(hauteurCasePiece, largeurCasePiece, plateauPiece[joueurCourant], gPiece);


        //canAffiche
        largeurCaseAffiche = largeurAffiche() / 5;
        hauteurCaseAffiche = hauteurAffiche() / 5;
        GraphicsContext gAffiche = canAffiche.getGraphicsContext2D();

        line(gAffiche,largeurAffiche(),hauteurAffiche());
        draw(hauteurCaseAffiche, largeurCaseAffiche, plateauAffiche, gAffiche);

        //canScore
        GraphicsContext gScore = canScore.getGraphicsContext2D();
        line(gScore,largeurAffiche(),hauteurAffiche());

        if(vp.nbJoueur==4) {
            joueur_score[0].setText(joueur[0].getText() + ": " + jeu.Score[0]);
            joueur_score[1].setText(joueur[1].getText() + ": " + jeu.Score[1]);
            joueur_score[2].setText(joueur[2].getText() + ": " + jeu.Score[2]);
            joueur_score[3].setText(joueur[3].getText() + ": " + jeu.Score[3]);
        }
        if(vp.nbJoueur==2) {
            joueur_score[0].setText(joueur[0].getText() + ": " + (jeu.Score[0]+jeu.Score[2]));
            joueur_score[1].setText(joueur[1].getText() + ": " + (jeu.Score[1]+jeu.Score[3]));

        }

        bouton(joueurCourant);
    }

    void bouton(int jc) {
        Color color = null;
        BorderStroke[] bos;
        bos = new BorderStroke[4];
        Border[] bo;
        bo = new Border[4];
        switch (joueurCourant) {
            case 0:
                color = Color.DARKOLIVEGREEN;
                break;
            case 1:
                color = Color.DARKSLATEBLUE;
                break;
            case 2:
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.INDIANRED;
                break;
        }
        BackgroundFill bgf = new BackgroundFill(color, new CornerRadii(20), new Insets(10));
        Background bg = new Background(bgf);
        bos[0] = new BorderStroke(Color.DARKOLIVEGREEN, BorderStrokeStyle.SOLID,null,new BorderWidths(5),new Insets(10));
        bo[0] = new Border(bos);
        bos[1] = new BorderStroke(Color.DARKSLATEBLUE, BorderStrokeStyle.SOLID,null,new BorderWidths(5),new Insets(10));
        bo[1] = new Border(bos);
        bos[2] = new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID,null,new BorderWidths(5),new Insets(10));
        bo[2] = new Border(bos);
        bos[3] = new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID,null,new BorderWidths(5),new Insets(10));
        bo[3] = new Border(bos);
        for(int i = 0;i<4;i++) {
            joueur[i].setBorder(bo[i]);
            joueur[i].setBackground(null);
        }
        joueur[jc].setBackground(bg);
    }

    void draw(double hauteur, double largeur, Plateau p, GraphicsContext g) {
        Color color = null;

        for (int i=0; i<p.p.length; i++) {
            for (int j=0; j<p.p[0].length; j++) {
                if(p==plateau) {
                    switch (p.valeur(i, j)) {
                        case -1:
                            break;
                        case 0:
                            g.setFill(Color.DARKOLIVEGREEN);
                            g.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);
                            break;
                        case 1:
                            g.setFill(Color.DARKSLATEBLUE);
                            g.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);
                            break;
                        case 2:
                            g.setFill(Color.YELLOW);
                            g.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);
                            break;
                        case 3:
                            g.setFill(Color.INDIANRED);
                            g.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);
                            break;
                        case 8:
                            g.setFill(Color.LIGHTGREEN);
                            g.fillRect(j*largeurCase, i*hauteurCase, largeurCase, hauteurCase);
                            break;

                    }
                }
                if(p==plateauPiece[joueurCourant]) {
                    switch (joueurCourant) {
                        case 0:
                            color = Color.DARKOLIVEGREEN;
                            break;
                        case 1:
                            color = Color.DARKSLATEBLUE;
                            break;
                        case 2:
                            color = Color.YELLOW;
                            break;
                        case 3:
                            color = Color.INDIANRED;
                            break;
                    }
                    switch (p.valeur(i, j)) {
                        case -1:
                            break;
                        case -2:
                            g.setFill(Color.LIGHTGREY);
                            g.fillRect(j*largeur, i*hauteur, largeur, hauteur);
                            g.strokeLine(j*largeur, i*hauteur, (j+1)*largeur, i*hauteur);
                            g.strokeLine(j*largeur, i*hauteur, j*largeur, (i+1)*hauteur);
                            g.strokeLine(j*largeur, (i+1)*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            g.strokeLine((j+1)*largeur, i*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            break;
                        case -3:
                            g.setFill(Color.ANTIQUEWHITE);
                            g.fillRect(j*largeur, i*hauteur, largeur, hauteur);
                            g.strokeLine(j*largeur, i*hauteur, (j+1)*largeur, i*hauteur);
                            g.strokeLine(j*largeur, i*hauteur, j*largeur, (i+1)*hauteur);
                            g.strokeLine(j*largeur, (i+1)*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            g.strokeLine((j+1)*largeur, i*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            break;
                        default:
                            g.setFill(color);
                            g.fillRect(j*largeur, i*hauteur, largeur, hauteur);
                            g.strokeLine(j*largeur, i*hauteur, (j+1)*largeur, i*hauteur);
                            g.strokeLine(j*largeur, i*hauteur, j*largeur, (i+1)*hauteur);
                            g.strokeLine(j*largeur, (i+1)*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            g.strokeLine((j+1)*largeur, i*hauteur, (j+1)*largeur, (i+1)*hauteur);
                            break;

                    }

                }
                if(p==plateauAffiche && p.valeurB(i, j)) {
                    switch (joueurCourant) {
                        case 0:
                            color = Color.DARKOLIVEGREEN;
                            break;
                        case 1:
                            color = Color.DARKSLATEBLUE;
                            break;
                        case 2:
                            color = Color.YELLOW;
                            break;
                        case 3:
                            color = Color.INDIANRED;
                            break;
                    }
                    g.setFill(color);
                    g.fillRect(j*largeur, i*hauteur, largeur, hauteur);
                    g.strokeLine(j*largeur, i*hauteur, (j+1)*largeur, i*hauteur);
                    g.strokeLine(j*largeur, i*hauteur, j*largeur, (i+1)*hauteur);
                    g.strokeLine(j*largeur, (i+1)*hauteur, (j+1)*largeur, (i+1)*hauteur);
                    g.strokeLine((j+1)*largeur, i*hauteur, (j+1)*largeur, (i+1)*hauteur);
                }
            }
        }
    }

    double largeurPlateau() {
        return canPlateau.getWidth();
    }

    double hauteurPlateau() {
        return canPlateau.getHeight();
    }

    double largeurPiece() {
        return canPiece.getWidth();
    }

    double hauteurPiece() {
        return canPiece.getHeight();
    }

    double largeurAffiche() {
        return canAffiche.getWidth();
    }

    double hauteurAffiche() {
        return canAffiche.getHeight();
    }

    public double largeurCase() {
        return largeurCase;
    }

    public double hauteurCase() {
        return hauteurCase;
    }

    public double largeurCasePiece() {
        return largeurCasePiece;
    }

    public double hauteurCasePiece() {
        return hauteurCasePiece;
    }


    public double largeurCaseAffiche() {
        return largeurCaseAffiche;
    }

    public double hauteurCaseAffiche() {
        return hauteurCaseAffiche;
    }



}
