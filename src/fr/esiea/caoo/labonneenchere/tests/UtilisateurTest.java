package fr.esiea.caoo.labonneenchere.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;
import fr.esiea.caoo.labonneenchere.java.encheres.Objet;
import fr.esiea.caoo.labonneenchere.java.utilisateurs.Utilisateur;

public class UtilisateurTest {

	private Utilisateur u1, u2;
	private Objet o1, o2;
	private Enchere e1, e2;

	@Before
	public void setUp() {
		u1 = new Utilisateur("UnLogin", "HONXA", "Cécile");
		Systeme.getInstance().nouvelUtilisateur(u1);
		u2 = new Utilisateur("UnAutreLogin", "BOUT", "Karrie");
		Systeme.getInstance().nouvelUtilisateur(u2);
		o1 = new Objet("Parapluie", "Résiste à la grêle et aux pluies fortes");
		o2 = new Objet("Matelas", "Confortable et moelleux");
		e1 = u1.creerEnchere(o1, new Date(2014, 7, 10, 20, 00));
		e2 = u1.creerEnchere(o2, new Date(2013, 5, 29, 12, 10));
		u1.publierEnchere(e2);

	}

	@Test
	public void testVendeur() throws InterruptedException {

		e1 = Systeme.getInstance().getEnchere(e1);
		e2 = Systeme.getInstance().getEnchere(e2);

		assertTrue(e1.getEtat() == EtatEnchere.CREEE);

		assertTrue(e2.getEtat() == EtatEnchere.PUBLIEE);

	}

	@Test
	public void testAcheteur() {

		// créateur de l'enchère qui tente d'émettre une offre
		assertFalse(u1.emettreOffre(e2, 10.50));

		// enchère non publiée
		assertFalse(u2.emettreOffre(e1, 9));

		// enchère publiée
		assertTrue(u2.emettreOffre(e2, 11));

	}
}
