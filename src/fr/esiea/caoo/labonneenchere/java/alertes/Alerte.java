package fr.esiea.caoo.labonneenchere.java.alertes;

import fr.esiea.caoo.labonneenchere.java.encheres.Enchere;


public abstract class Alerte {
	protected String login;
	private String typeAlerte;

	public Alerte(String login, String typeAlerte) {
		// TODO Auto-generated constructor stub
		this.login = login;
		this.typeAlerte = typeAlerte;
	}

	public abstract void update(Enchere e, String loginPourMessage);

	public String getLogin() {
		return login;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Alerte) {
			Alerte a = (Alerte) obj;
			return (login.equals(a.login) && typeAlerte.equals(a.typeAlerte));
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (login.hashCode() ^ typeAlerte.hashCode());
	}
}
