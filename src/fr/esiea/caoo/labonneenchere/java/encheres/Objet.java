package fr.esiea.caoo.labonneenchere.java.encheres;

public class Objet {

	private String id, desc;

	public Objet(String identifiant, String description) {
		// TODO Auto-generated constructor stub
		id = identifiant;
		desc = description;
	}

	public String getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Objet) {
			Objet o = (Objet) obj;
			return (id.equals(id) && desc.equals(o.desc));
		}

		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode() ^ desc.hashCode();
	}

}
