package fr.esiea.caoo.labonneenchere.java.alertes;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;


public class AlerteNouvelleOffre extends Alerte {
	// cette alerte permet aussi bien de prévenir un acheteur qu'une offre
	// supérieure a été soumise, et de prévenir l'auteur de l'annonce qu'une
	// nouvelle offre a été soumise
	public AlerteNouvelleOffre(String login) {
		super(login, "NouvelleOffre");
	}

	@Override
	public void update(Enchere e, String loginPourMessage) {
		// TODO Auto-generated method stub
		if ((e.getEtat() == EtatEnchere.PUBLIEE)) {
			Systeme.getInstance()
			        .getUtilisateurByLogin(login)
			        .receptMessage(
			                "Une nouvelle offre a été soumise par "
			                        + loginPourMessage);
		}

	}

}
