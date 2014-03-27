package fr.esiea.caoo.labonneenchere.java.alertes;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;


public class AlertePrixReserve extends Alerte {
	public AlertePrixReserve(String login) {
		super(login, "PrixReserve");
	}

	@Override
	public void update(Enchere e, String loginPourMessage) {
		// TODO Auto-generated method stub
		if ((e.getEtat() == EtatEnchere.PUBLIEE) && e.prixDeReserveAtteint()
		        && (loginPourMessage != login)) {
			Systeme.getInstance()
			        .getUtilisateurByLogin(login)
			        .receptMessage(
			                "Le Prix de réserve a été atteint par "
			                        + loginPourMessage);
		}

	}
}
