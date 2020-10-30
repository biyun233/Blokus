package Controleur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Modele.Historique;
import Modele.Jeu;
import Modele.Piece;
import Modele.Position;
import Vue.ViewJouer;
import Vue.ViewParametre;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ControleurMediateur {
    public Jeu jeu;
    ViewJouer vjouer;
    ViewParametre vpara;
    public int joueurCourant;
    final int lenteurAttente = 50;
    int decompte;
    public Historique h;
    public String fichier;

    public ControleurMediateur(Jeu j, ViewJouer vj, ViewParametre vp) {
        jeu = j;
        vjouer = vj;
        vpara = vp;
        h = new Historique();
        h.add(jeu);
    }

    public void setDimension(int n) {
        jeu = new Jeu(n);
        vjouer.modify(jeu);
        vpara.modify(jeu);
        for(int i=0;i<4;i++) {
            vpara.joueurs[i].modify(jeu);
        }
        vjouer.onLaunch();

    }

    public void save() {
        h.save(jeu,"0");
    }

    public void load(String fichier) {
        this.jeu = h.load(fichier);
        //System.out.println("joueurcourant " + jeu.joueurCourant);
        vjouer.modify(jeu);
        joueurCourant = jeu.joueurCourant;
        //vpara.modify(jeu);
        vjouer.joueurCourant = jeu.joueurCourant;
        vjouer.miseAJour();
        vjouer.load = true;
    }

    public void annuler() {
        jeu = h.annuler();
        joueurCourant = jeu.joueurCourant;
        vjouer.modify(jeu);
        vpara.modify(jeu);
        for(int i=0;i<4;i++) {
            vpara.joueurs[i].modify(jeu);
        }
        vjouer.joueurCourant = jeu.joueurCourant;
        vjouer.miseAJour();
    }

    public void refaire() {
        jeu = (Jeu) copyObject(h.refaire());
        joueurCourant = jeu.joueurCourant;
        vjouer.modify(jeu);
        vpara.modify(jeu);
        for(int i=0;i<4;i++) {
            vpara.joueurs[i].modify(jeu);
        }
        vjouer.joueurCourant = jeu.joueurCourant;
        vjouer.miseAJour();
    }

    Object copyObject(Object src) {
        Object dest = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(src);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            try {
                dest = new ObjectInputStream(bais).readObject();
            } catch (ClassNotFoundException e) {
                System.err.println("Class non trouvee");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }


    public void modifScore(int nb) {
        vjouer.Score.getChildren().clear();
        if(nb == 4)
            vjouer.Score.getChildren().addAll(vjouer.joueur_score[0],vjouer.joueur_score[1],vjouer.joueur_score[2],vjouer.joueur_score[3]);
        else if(nb == 2)
            vjouer.Score.getChildren().addAll(vjouer.joueur_score[0],vjouer.joueur_score[1]);
    }

    public void setNom(TextField t,int order) {
        t.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                t.setText(newValue);
                vpara.nom[order] = newValue;
            }
        });
    }

    public void setNomCharge(TextField t) {
        t.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                t.setText(newValue);
                fichier = newValue;
            }
        });
    }

    public void valide(int order) {
        vjouer.joueur[order].setText(vpara.nom[order]);
        vjouer.joueur_score[order].setText(vpara.nom[order]+ ": " + jeu.Score[order]);
        if(vpara.nbJoueur==2) {
            vjouer.joueur[order+2].setText(vpara.nom[order]);
            vjouer.joueur_score[order].setText(vpara.nom[order]+ ": " + (jeu.Score[order]+jeu.Score[order+2]));
        }

    }
    public void redimensionnement() {
        vjouer.miseAJour();
    }

    public void clicSouris(double x, double y) {
        int l = (int) (y / vjouer.hauteurCase());
        int c = (int) (x / vjouer.largeurCase());

        Jeu j = jeu;
        Position posPlateau = new Position(l,c);
        Position posPiece = new Position(jeu.PosPieceL,jeu.PosPieceC);
        Piece piece = jeu.pieceCourant;
        if (vpara.joueurs[joueurCourant].jeu(posPlateau,posPiece,piece)) {
            jeu.plateauPiece[vjouer.joueurCourant].enlevePiece(jeu.pieceCourant.getNum());
            vjouer.joueurCourant = jeu.joueurCourant;
            h.add(j);
            vjouer.miseAJour();
            changeJoueur();
            vjouer.jeu.pieceCourant = null;
        }
        else {
            jeu.setSelected(piece.getNum());
            jeu.plateauAffiche.PlacerPiece(jeu.pieceCourant);
            vjouer.miseAJour();
        }

    }

    public void selectPiece(double x, double y) {
        int l = (int) (y / vjouer.hauteurCasePiece());
        int c = (int) (x / vjouer.largeurCasePiece());

        if(jeu.plateauPiece[jeu.joueurCourant].valeur(l,c)>=0 && vjouer.joueurCourant==jeu.joueurCourant) {
            jeu.setSelected(jeu.plateauPiece[jeu.joueurCourant].valeur(l,c));
            jeu.plateauPiece[jeu.joueurCourant].select(jeu.pieceCourant.getNum());
            jeu.plateauAffiche.PlacerPiece(jeu.pieceCourant);
            vjouer.miseAJour();
        }
        jeu.plateauPiece[jeu.joueurCourant].unselect(jeu.pieceCourant.getNum());
    }

    public void PieceAffiche(double x, double y) {
        int l = (int) (y / vjouer.hauteurCaseAffiche());
        int c = (int) (x / vjouer.largeurCaseAffiche());

        jeu.setPieceL(l);
        jeu.setPieceC(c);
    }

    public void initAffiche() {
        jeu.plateauAffiche.initPlateauAffiche();
        vjouer.miseAJour();
    }
    void changeJoueur() {
        joueurCourant = (joueurCourant + 1) % vpara.joueurs.length;
        decompte = lenteurAttente;
    }

    public boolean choisirNiveau(String niveau){
        switch(niveau){
            case "Robot simple": return vpara.joueurs[joueurCourant].tempsEcoule();
            case "Robot Intelligent": return vpara.joueurs[joueurCourant].tempsEcoulePiecePrefere();
            case "Robot Excellent": return vpara.joueurs[joueurCourant].tempsEcouleMechant();
            default: return false;
        }
    }

    public void tictac() {
        Jeu j = jeu;

        if (jeu.enCours()) {
            if (decompte == 0) {
                if( choisirNiveau(vpara.dif[joueurCourant])) {//num() pour joueurCourant change
                    vjouer.joueurCourant = jeu.joueurCourant;
                    h.add(j);
                    vjouer.miseAJour();
                    changeJoueur();
                }
                else {
                    if(vpara.joueurs[joueurCourant].jeu.enCoursJ[joueurCourant]) {
                        System.out.println("On vous attend, joueur " + joueurCourant);
                        decompte = lenteurAttente;
                    }
                    else{
                        vpara.joueurs[joueurCourant].jeu.setupNextJoueur();
                        vjouer.joueurCourant = jeu.joueurCourant;
                        vjouer.miseAJour();
                        changeJoueur();
                    }

                }
            }
            else{
                decompte--;
            }
        }
    }
}
