package fr.esiea.caoo.labonneenchere.java.encheres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.esiea.caoo.labonneenchere.java.alertes.Alerte;
import fr.esiea.caoo.labonneenchere.java.alertes.Observable;

public class Enchere implements Observable {

	private Objet obj;
	private String loginVendeur;
	private Date dateLimite;
	private double prixMin, prixReserve;
	private EtatEnchere etat;
	private Map<String, Double> offres;
	private Timer timer = null; // permet de gérer l'événement pour terminer
	                            // l'enchère lorsque la date limite est dépassée
	private List<Alerte> listeAlerte;

	public Enchere(String user, Objet objet, Date limite) {
		// TODO Auto-generated constructor stub
		// une nouvelle enchère est en état "créée"
		loginVendeur = user;
		obj = objet;
		dateLimite = limite;
		prixMin = 0;
		prixReserve = 0;
		etat = EtatEnchere.CREEE;
		offres = new HashMap<String, Double>();
		listeAlerte = new ArrayList<Alerte>();
	}

	public boolean setEtat(EtatEnchere e) {
		// On peut seulement changer l'état d'une commande créée ou publiée,
		if ((etat == EtatEnchere.ANNULEE) || (etat == EtatEnchere.TERMINEE)) {
			return false;
		} else {
			if ((timer == null) && (e == EtatEnchere.PUBLIEE)) {
				// On lance un timer pour surveiller la date afin de terminer
				// l'offre, le timer ne devient actif que lorsque l'offre est
				// publiée
				timer = new Timer();
				timer.schedule(new TimerTaskOnDate(), 1000);
			}

			if (e == EtatEnchere.ANNULEE) {
				// il faut vérifier que aucune offre de cette enchère n'a
				// atteint de prix de réserve
				List<Double> listePrix = new ArrayList<Double>(offres.values());
				int taille;
				if ((taille = listePrix.size()) > 0) {
					Collections.sort(listePrix);
					if (listePrix.get(taille - 1) >= prixReserve) {
						// prix de reserve atteint
						return false;
					}
				}
			}

			if (((e == EtatEnchere.TERMINEE) || (e == EtatEnchere.ANNULEE))
			        && (timer != null)) {
				// On désactive le timer lorsque l'offre devient
				// terminée/annulée
				timer.cancel();
			}

			etat = e;
			updateAlerte(loginVendeur);
			return true;
		}

	}

	// tâche exécutée par le timer
	class TimerTaskOnDate extends TimerTask {
		@Override
		public void run() {
			if (dateLimite.compareTo(new Date()) < 0) {
				setEtat(EtatEnchere.TERMINEE);
			}
		}
	}

	public boolean setPrixMin(double prix) {
		// On considère qu'une fois l'enchère publiée/annulée/terminée, on ne
		// peut plus modifier les prix
		if ((etat == EtatEnchere.CREEE) && (prix > 0)) {
			prixMin = prix;
			return true;
		} else {
			return false;
		}
	}

	public boolean setPrixReserve(double prix) {
		// On considère qu'une fois l'enchère publiée/annulée/terminée, on ne
		// peut plus modifier les prix
		// le prix de réserve doit être supérieure au prix minimum
		if ((etat == EtatEnchere.CREEE) && (prix > 0) && (prix > prixMin)) {
			prixReserve = prix;
			return true;
		} else {
			return false;
		}

	}

	public double getPrixMin() {
		return prixMin;
	}

	public double getPrixReserve() {
		return prixReserve;
	}

	public String getVendeur() {
		return loginVendeur;
	}

	public EtatEnchere getEtat() {
		return etat;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Enchere) {
			Enchere e = (Enchere) obj;
			return (this.obj.equals(e.obj)
			        && loginVendeur.equals(e.loginVendeur) && dateLimite
			            .equals(e.dateLimite));
		}

		return false;
	}

	@Override
	public int hashCode() {
		return obj.hashCode() ^ loginVendeur.hashCode() ^ dateLimite.hashCode();
	}

	public boolean nouvelleOffre(String acheteur, double offre) {
		// TODO Auto-generated method stub

		// On vérifie que l'emetteur de l'offre n'est pas le vendeur
		if (loginVendeur == acheteur) {
			return false;
		}

		// On vérifie que l'état de l'enchère est "publiée"
		if (etat != EtatEnchere.PUBLIEE) {

			return false;
		}

		// On vérifie que le prix de l'offre > prix minimum
		if (prixMin > offre) {

			return false;
		}

		// on vérifie que l'offre proposée est supérieure à l'offre la plus
		// élevée
		List<Double> listePrix = new ArrayList<Double>(offres.values());
		int taille;
		if ((taille = listePrix.size()) > 0) {
			Collections.sort(listePrix);
			if (listePrix.get(taille - 1) >= offre) {
				return false;
			}
		}

		// A ce stade, toutes les vérifs sont OK, ajout de l'offre

		offres.put(acheteur, offre);
		updateAlerte(acheteur);
		return true;
	}

	public boolean prixDeReserveAtteint() {
		List<Double> listePrix = new ArrayList<Double>(offres.values());
		int taille;
		if ((taille = listePrix.size()) > 0) {
			Collections.sort(listePrix);
			return (listePrix.get(taille - 1) >= prixReserve);
			// prix de reserve atteint

		}
		return false;
	}

	@Override
	public boolean addAlerte(Alerte a) {
		// TODO Auto-generated method stub
		if (etat == EtatEnchere.PUBLIEE) {
			if (!listeAlerte.contains(a)) {
				listeAlerte.add(a);
				return true;
			}
		}

		return false;

	}

	@Override
	public void updateAlerte(String login) {
		// TODO Auto-generated method stub
		for (Alerte a : listeAlerte) {
			a.update(this, login);
		}

	}

	@Override
	public int delAlerte(String login) {
		// TODO Auto-generated method stub
		List<Alerte> listeSuppr = new ArrayList<Alerte>();
		int nbAlertesSuppr = 0;
		for (Alerte a : listeAlerte) {
			if (a.getLogin().equals(login)) {
				listeSuppr.add(a);
				nbAlertesSuppr++;
			}
		}
		listeAlerte.removeAll(listeSuppr);
		return nbAlertesSuppr;

	}

	@Override
	public boolean delAlerte(Alerte a) {
		// TODO Auto-generated method stub

		if (listeAlerte.contains(a)) {
			listeAlerte.remove(a);
			return true;
		}

		return false;

	}
}
