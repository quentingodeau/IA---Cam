package jeux.cam.modele;

import iia.jeux.modele.CoupJeu;
import java.util.Collection;

/**
 *
 * @author godeau & pannirselvame
 */
public class SimpleCoupCam implements CoupJeu {
    Position depart;
    Position arriver;
    Character prise;

    public SimpleCoupCam(Position depart, Position arriver) {
        this.depart = depart;
        this.arriver = arriver;
        this.prise = null;
    }

    public SimpleCoupCam(Position depart, int ligneArr, int colonneArr) {
        this(depart, new Position(ligneArr, colonneArr));
    }

    public SimpleCoupCam(int ligneDep, int colonneDep, int ligneArr, int colonneArr) {
        this(new Position(ligneDep, colonneDep), new Position(ligneArr, colonneArr));
    }

    public SimpleCoupCam(String strCoup) {
        this(strCoup.split("-"));
    }

    public SimpleCoupCam(String tabCoup[]) {
        this(new Position(tabCoup[0]), new Position(tabCoup[1]));
    }
    
    @Override
    public CoupJeu clone() {
        return new SimpleCoupCam(depart, arriver);
    }

    public Position getDepart() {
        return depart;
    }

    public void setDepart(Position depart) {
        this.depart = depart;
    }

    public Position getArriver() {
        return arriver;
    }

    public void setArriver(Position arriver) {
        this.arriver = arriver;
    }

    public Character getPrise() {
        return prise;
    }

    public void setPrise(Character prise) {
        this.prise = prise;
    }

    @Override
    public String toString() {
        return depart + "-" + arriver;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleCoupCam) {
            return this.depart.equals(((SimpleCoupCam) obj).depart) && this.arriver.equals(((SimpleCoupCam) obj).arriver);
        } else if (obj instanceof MultipleCoupCam) {
            MultipleCoupCam coupCam = (MultipleCoupCam) obj;
            Collection<MultipleCoupCam.Pair> coups = coupCam.getCoups();
            return coups.size() == 1 && coupCam.getDepart().equals(this.depart) && coups.iterator().next().getPosition().equals(this.arriver);
        } else if (obj instanceof String) {
            return this.equals(new SimpleCoupCam(((String) obj)));
        } else {
            return super.equals(obj);
        }
    }
}
