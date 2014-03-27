package fr.esiea.caoo.labonneenchere.java.alertes;

public interface Observable {
	public boolean addAlerte(Alerte a);

	public void updateAlerte(String login);

	public int delAlerte(String login);

	public boolean delAlerte(Alerte a);
}
