package fr.esiea.caoo.labonneenchere.java.alertes;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;


public class AlerteEnchereAnnulee extends Alerte {
	public AlerteEnchereAnnulee(String login) {
		super(login, "Ench�reAnnul�e");
	}

	@Override
	public void update(Enchere e, String loginPourMessage) {
		// TODO Auto-generated method stub
		if (e.getEtat() == EtatEnchere.ANNULEE) {
			Systeme.getInstance()
			        .getUtilisateurByLogin(login)
			        .receptMessage(
			                "L'ench�re a �t� annul�e par " + loginPourMessage);
		}

	}
}
