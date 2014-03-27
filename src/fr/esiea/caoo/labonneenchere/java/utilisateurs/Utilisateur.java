package fr.esiea.caoo.labonneenchere.java.utilisateurs;

import java.util.Date;
import java.util.List;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.alertes.AlerteEnchereAnnulee;
import fr.esiea.caoo.labonneenchere.java.alertes.AlerteNouvelleOffre;
import fr.esiea.caoo.labonneenchere.java.alertes.AlertePrixReserve;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;
import fr.esiea.caoo.labonneenchere.java.encheres.Objet;

public class Utilisateur implements Acheteur, Vendeur {

	private String login, nom, prenom, lastMsgAlerte;
	private Systeme sys;

	public Utilisateur(String login, String nom, String prenom) {
		// TODO Auto-generated constructor stub

		this.login = login;
		this.nom = nom;
		this.prenom = prenom;
		lastMsgAlerte = null;

		sys = Systeme.getInstance();

	}

	@Override
	public Enchere creerEnchere(Objet o, Date d) {
		// TODO Auto-generated method stub
		Enchere e = new Enchere(login, o, d);
		if (!sys.getEncheres().contains(e)) {
			sys.ajoutEnchere(e);
		}
		return e;

	}

	@Override
	public void publierEnchere(Enchere e) {
		// TODO Auto-generated method stub
		int index;
		// On verifie qu'on est bien l'auteur de l'enchère et que l'enchère fait
		// bien parti du système
		List<Enchere> encheres = sys.getEncheres();
		if (login.equals(e.getVendeur())
		        && ((index = encheres.indexOf(e)) != -1)) {
			if (encheres.get(index).getEtat() != EtatEnchere.PUBLIEE) {
				encheres.get(index).setEtat(EtatEnchere.PUBLIEE);
				sys.getEnchere(e).addAlerte(new AlerteNouvelleOffre(login));
			}
		}

	}

	@Override
	public void annulerEnchere(Enchere e) {
		// TODO Auto-generated method stub
		int index;
		// On verifie qu'on est bien l'auteur de l'enchère et que l'enchère fait
		// bien parti du système
		List<Enchere> encheres = sys.getEncheres();
		if (login.equals(e.getVendeur())
		        && ((index = encheres.indexOf(e)) != -1)) {
			encheres.get(index).setEtat(EtatEnchere.ANNULEE);
		}
	}

	@Override
	public boolean setPrixMinimum(Enchere e, double prix) {
		// TODO Auto-generated method stub
		int index;
		// On verifie qu'on est bien l'auteur de l'enchère et que l'enchère fait
		// bien parti du système
		List<Enchere> encheres = sys.getEncheres();
		if (login.equals(e.getVendeur())
		        && ((index = encheres.indexOf(e)) != -1)) {
			return encheres.get(index).setPrixMin(prix);
		}

		return false;
	}

	@Override
	public boolean setPrixReserve(Enchere e, double prix) {
		// TODO Auto-generated method stub
		int index;
		// On verifie qu'on est bien l'auteur de l'enchère et que l'enchère fait
		// bien parti du système
		List<Enchere> encheres = sys.getEncheres();
		if (login.equals(e.getVendeur())
		        && ((index = encheres.indexOf(e)) != -1)) {
			return encheres.get(index).setPrixReserve(prix);
		}

		return false;
	}

	@Override
	public boolean emettreOffre(Enchere e, double offre) {
		// TODO Auto-generated method stub
		int index;
		// On verifie que l'enchère fait bien parti du système
		List<Enchere> encheres = sys.getEncheres();
		if ((index = encheres.indexOf(e)) != -1) {
			return encheres.get(index).nouvelleOffre(login, offre);
		}

		return false;

	}

	// méthode reçevant les messages générés par les alertes
	public void receptMessage(String str) {
		lastMsgAlerte = str;
		System.out.println("@" + login + " : " + lastMsgAlerte);
	}

	public String getLastMsgAlerte() {
		// TODO Auto-generated method stub
		return lastMsgAlerte;
	}

	public boolean addAlerteNouvelleOffre(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).addAlerte(new AlerteNouvelleOffre(login));
	}

	public boolean addAlertePrixReserve(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).addAlerte(new AlertePrixReserve(login));
	}

	public boolean addAlerteEnchereAnnulee(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).addAlerte(new AlerteEnchereAnnulee(login));
	}

	public boolean delAlerteNouvelleOffre(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).delAlerte(new AlerteNouvelleOffre(login));
	}

	public boolean delAlertePrixReserve(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).delAlerte(new AlertePrixReserve(login));
	}

	public boolean delAlerteEnchereAnnulee(Enchere e) {
		// TODO Auto-generated method stub
		return sys.getEnchere(e).delAlerte(new AlerteEnchereAnnulee(login));
	}

	public int delAllAlerte(Enchere e) {
		return sys.getEnchere(e).delAlerte(login);
	}

	public String getLogin() {
		return login;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Utilisateur) {
			Utilisateur user = (Utilisateur) obj;
			// un utilisateur est le même s'il a le meme login, login doit être
			// unique
			return (login.equals(user.login));
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (login.hashCode());
	}

	public boolean prixDeReserveAtteint(Enchere e) {
		sys.getEnchere(e);
		// TODO Auto-generated method stub
		return false;
	}

}
