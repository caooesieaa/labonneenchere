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

public class EnchereTest {

	private Utilisateur u1, u2, u3;
	private Objet o1, o2, o3;
	private Enchere e1, e2, e3, e4;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		u1 = new Utilisateur("UnLogin", "HONXA", "Cécile");
		Systeme.getInstance().nouvelUtilisateur(u1);
		u2 = new Utilisateur("UnAutreLogin", "BOUT", "Karrie");
		Systeme.getInstance().nouvelUtilisateur(u2);
		u3 = new Utilisateur("Sacha", "TOUILLE", "Sacha");
		Systeme.getInstance().nouvelUtilisateur(u3);
		o1 = new Objet("Barbecue", "Haut de gamme");
		o2 = new Objet("Télévision", "Ecran flexible OLED");
		o3 = new Objet("Couette", "Tiens chaud, très chaud");

		e1 = u1.creerEnchere(o1, new Date(2014, 7, 10, 20, 00));

		e2 = u1.creerEnchere(o2, new Date(2014, 5, 29, 12, 10));

		e3 = u2.creerEnchere(o2, new Date(2014, 12, 21));

		e4 = u2.creerEnchere(o3, new Date(2014, 4, 28));

	}

	@Test
	public void testEmettreOffre() {
		// test d'émission d'offre en fonction de l'état de l'enchère

		Systeme sys = Systeme.getInstance();
		e1 = sys.getEnchere(e1);
		e2 = sys.getEnchere(e2);

		assertTrue(e1.getEtat() == EtatEnchere.CREEE);
		assertFalse(u2.emettreOffre(e1, 11));

		// On ne peut émettre d'offre que sur une enchère publiée
		u1.publierEnchere(e2);
		assertTrue(e2.getEtat() == EtatEnchere.PUBLIEE);
		assertTrue(u2.emettreOffre(e2, 10));

		e1.setEtat(EtatEnchere.ANNULEE);
		assertTrue(e1.getEtat() == EtatEnchere.ANNULEE);
		assertFalse(u2.emettreOffre(e1, 1));

		e2.setEtat(EtatEnchere.TERMINEE);
		assertTrue(e2.getEtat() == EtatEnchere.TERMINEE);
		assertFalse(u2.emettreOffre(e2, 1));

	}

	@Test
	public void testPrixMinimum() {
		e3 = Systeme.getInstance().getEnchere(e3);
		e3.setEtat(EtatEnchere.CREEE);
		assertTrue(u2.setPrixMinimum(e3, 10));
		assertTrue(e3.getPrixMin() == 10);
		u2.publierEnchere(e3);
		assertTrue(e3.getEtat() == EtatEnchere.PUBLIEE);
		assertFalse(u1.emettreOffre(e3, 5));
		assertTrue(u1.emettreOffre(e3, 35));

	}

	@Test
	public void testPrixDeReserve() {
		e3 = Systeme.getInstance().getEnchere(e3);
		e3.setEtat(EtatEnchere.CREEE);
		// ##############
		assertTrue(u2.setPrixReserve(e3, 25.60));
		assertTrue(e3.getPrixReserve() == 25.60);
		u2.publierEnchere(e3);
		u1.emettreOffre(e3, 23);
		assertFalse(e3.prixDeReserveAtteint());
		u1.emettreOffre(e3, 26);
		assertTrue(e3.prixDeReserveAtteint());
	}

	@Test
	public void testAnnulerEnchere() {
		e3 = Systeme.getInstance().getEnchere(e3);
		e3.setEtat(EtatEnchere.CREEE);

		assertTrue(u2.setPrixReserve(e3, 25));
		u2.publierEnchere(e3);
		u1.emettreOffre(e3, 30);

		// tentative d'annulation d'une enchère dont une offre dépasse le prix
		// de réserve
		u2.annulerEnchere(e3);
		assertFalse(e3.getEtat() == EtatEnchere.ANNULEE);

		e4 = Systeme.getInstance().getEnchere(e4);
		assertTrue(u2.setPrixReserve(e4, 10));
		u2.publierEnchere(e4);
		u1.emettreOffre(e4, 5);
		u3.emettreOffre(e4, 9.5);

		// annulation d'une echère dont aucun offre ne dépasse le prix de
		// réserve
		u2.annulerEnchere(e4);
		assertTrue(e4.getEtat() == EtatEnchere.ANNULEE);

	}

}
