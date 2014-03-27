package fr.esiea.caoo.labonneenchere.java.alertes;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;


public class AlerteNouvelleOffre extends Alerte {
	// cette alerte permet aussi bien de pr�venir un acheteur qu'une offre
	// sup�rieure a �t� soumise, et de pr�venir l'auteur de l'annonce qu'une
	// nouvelle offre a �t� soumise
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
			                "Une nouvelle offre a �t� soumise par "
			                        + loginPourMessage);
		}

	}

}
