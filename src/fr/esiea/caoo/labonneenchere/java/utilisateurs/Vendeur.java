package fr.esiea.caoo.labonneenchere.java.utilisateurs;

import java.util.Date;

import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.Objet;

public interface Vendeur {

	public Enchere creerEnchere(Objet o, Date d);

	public void publierEnchere(Enchere e);

	public void annulerEnchere(Enchere e);

	public boolean setPrixMinimum(Enchere e, double prix);

	public boolean setPrixReserve(Enchere e, double prix);

}
