package fr.esiea.caoo.labonneenchere.java;

import java.util.ArrayList;
import java.util.List;

import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.utilisateurs.Utilisateur;

/* La classe sytème contient la liste des enchères et est un singleton
 * car un seul système permet de gérer toutes les enchères
 */
public class Systeme {
	private static Systeme SINGLETON = null;
	private static List<Enchere> encheres;
	private static List<Utilisateur> utilisateurs;

	private Systeme() {
		encheres = new ArrayList<Enchere>();
		utilisateurs = new ArrayList<Utilisateur>();
	}

	public static Systeme getInstance() {
		if (SINGLETON == null) {
			synchronized (Systeme.class) {
				if (SINGLETON == null) {
					SINGLETON = new Systeme();
				}
			}
		}
		return SINGLETON;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public Enchere getEnchere(Enchere e) {
		int index;
		if ((index = encheres.indexOf(e)) != -1) {
			return encheres.get(index);
		} else {
			return null;
		}
	}

	public void ajoutEnchere(Enchere enchere) {
		encheres.add(enchere);

	}

	public void nouvelUtilisateur(Utilisateur u) {
		utilisateurs.add(u);
	}

	public Utilisateur getUtilisateurByLogin(String login) {
		// TODO Auto-generated method stub
		for (Utilisateur u : utilisateurs) {
			if (u.getLogin().equals(login)) {
				return u;
			}
		}
		return null;
	}

}
