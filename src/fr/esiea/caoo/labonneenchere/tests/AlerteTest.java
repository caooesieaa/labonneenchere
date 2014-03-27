package fr.esiea.caoo.labonneenchere.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import fr.esiea.caoo.labonneenchere.java.Systeme;
import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;
import fr.esiea.caoo.labonneenchere.java.encheres.EtatEnchere;
import fr.esiea.caoo.labonneenchere.java.encheres.Objet;
import fr.esiea.caoo.labonneenchere.java.utilisateurs.Utilisateur;

public class AlerteTest {

	private Utilisateur createurEnchere, acheteurSerieux, sacha, sabri;
	private Objet o1, o2, o3;
	private Enchere e1, e2, e3, e4;

	@Before
	public void setUp() throws Exception {
		createurEnchere = new Utilisateur("CreateurEnchere", "HONXA", "Cécile");
		Systeme.getInstance().nouvelUtilisateur(createurEnchere);
		acheteurSerieux = new Utilisateur("AcheteurSerieux", "BOUT", "Karrie");
		Systeme.getInstance().nouvelUtilisateur(acheteurSerieux);
		sacha = new Utilisateur("Sacha", "TOUILLE", "Sacha");
		Systeme.getInstance().nouvelUtilisateur(sacha);
		sabri = new Utilisateur("Sabri", "COL", "Sabri");
		Systeme.getInstance().nouvelUtilisateur(sabri);
		o1 = new Objet("Table", "Utile pour manger en famille");
		o2 = new Objet("Iphone 7", "Authentique, pas une contrefaçon");
		o3 = new Objet("Coton tige", "Nettoie les oreilles");

		e1 = createurEnchere.creerEnchere(o1, new Date(2014, 7, 10, 20, 00));

		e2 = createurEnchere.creerEnchere(o2, new Date(2014, 5, 29, 12, 10));

		e3 = acheteurSerieux.creerEnchere(o2, new Date(2014, 12, 21));

		e4 = sacha.creerEnchere(o3, new Date(2015, 01, 01));

	}

	@Test
	public void testAlerteEnchereAnnulee() {
		createurEnchere.publierEnchere(e2);
		assertTrue(sacha.addAlerteEnchereAnnulee(e2));
		Systeme.getInstance().getEnchere(e2).setEtat(EtatEnchere.ANNULEE);

		assertEquals(
		        Systeme.getInstance().getUtilisateurByLogin(sacha.getLogin())
		                .getLastMsgAlerte(),
		        "L'enchère a été annulée par CreateurEnchere");

	}

	@Test
	public void testAlerteNouvelleOffre() {
		createurEnchere.publierEnchere(e1);
		acheteurSerieux.emettreOffre(e1, 18);
		assertEquals(
		        Systeme.getInstance()
		                .getUtilisateurByLogin(createurEnchere.getLogin())
		                .getLastMsgAlerte(),
		        "Une nouvelle offre a été soumise par AcheteurSerieux");
	}

	@Test
	public void testAlerteOffreSuperieure() {
		createurEnchere.publierEnchere(e1);
		acheteurSerieux.emettreOffre(e1, 18);
		assertTrue(acheteurSerieux.addAlerteNouvelleOffre(e1));
		sacha.emettreOffre(e1, 21);
		assertEquals(
		        Systeme.getInstance()
		                .getUtilisateurByLogin(acheteurSerieux.getLogin())
		                .getLastMsgAlerte(),
		        "Une nouvelle offre a été soumise par Sacha");
	}

	@Test
	public void testAlertePrixReserve() {
		acheteurSerieux.setPrixReserve(e3, 10);
		acheteurSerieux.publierEnchere(e3);

		assertTrue(createurEnchere.addAlertePrixReserve(e3));
		createurEnchere.emettreOffre(e3, 8);
		sacha.emettreOffre(e3, 11);

		assertEquals("Le Prix de réserve a été atteint par Sacha", Systeme
		        .getInstance()
		        .getUtilisateurByLogin(createurEnchere.getLogin())
		        .getLastMsgAlerte());

	}

	@Test
	public void testDesactivationAlerte() {
		sacha.publierEnchere(e4);
		assertTrue(sabri.addAlerteEnchereAnnulee(e4));
		assertTrue(sabri.addAlerteNouvelleOffre(e4));
		assertTrue(sabri.addAlertePrixReserve(e4));

		assertTrue(sabri.delAlerteEnchereAnnulee(e4));

		assertEquals(2, sabri.delAllAlerte(e4));// retourne le nombre d'alerte
		                                        // annulée, = 2 car on a annulé
		                                        // une enchère toute seule à la
		                                        // ligne précédente

		acheteurSerieux.emettreOffre(e4, 35); // l'utilisateur Sabri ne reçoit
											  // aucune alerte
	}

	//

}
