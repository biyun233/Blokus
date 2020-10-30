package Modele;


public interface HistoriqueInterface {

	public boolean peutAnnuler();
	public boolean peutRefaire();
	public void add(Jeu jeu);
	public Jeu annuler();
	public Jeu refaire();
	public Jeu load(String fichier);
	public void save(Jeu j,String fichier);



}
