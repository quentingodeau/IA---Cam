package jeux.cam.modele;

import iia.jeux.modele.CoupJeu;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * NB: ne peut être un canter, simple coup si
 *
 * @author godeau & pannirselvame
 */
public class MultipleCoupCam implements CoupJeu {

    public class Pair {

        Position position;
        Character prise;

        public Pair(Position position, Character prise) {
            this.position = position;
            this.prise = prise;
        }

        @Override
        public Pair clone() {
            return new Pair(position, null);
        }

        public Position getPosition() {
            return position;
        }

        public Character getPrise() {
            return prise;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public void setPrise(Character prise) {
            this.prise = prise;
        }
    }

    protected Position depart;
    protected List<Pair> coups;

    public MultipleCoupCam(Position depart, List<Pair> coups) {
        this.depart = depart;
        this.coups = coups;
    }

    public MultipleCoupCam(Position depart) {
        this(depart, new LinkedList<Pair>());
    }

    public MultipleCoupCam(int ligneDep, int colonneDep) {
        this(new Position(ligneDep, colonneDep));
    }

    public MultipleCoupCam(String strCoup) {
        this(strCoup.split("-"));
    }

    public MultipleCoupCam(String tabCoup[]) {
        this(new Position(tabCoup[0]));
        for (int i = 1; i < tabCoup.length; i++) {
            add(new Position(tabCoup[i]));
        }
    }

    @Override
    public MultipleCoupCam clone() {
        MultipleCoupCam multipleCoupCam = new MultipleCoupCam(this.depart);
        for (Pair pair : coups) {
            multipleCoupCam.add(pair.clone());
        }
        return multipleCoupCam;
    }

    public void setDepart(Position depart) {
        this.depart = depart;
    }

    public Position getDepart() {
        return depart;
    }

    public List<Pair> getCoups() {
        return coups;
    }

    public boolean add(Position position) {
        return add(new Pair(position, null));
    }

    public boolean add(Pair pair) {
        return coups.add(pair);
    }

    @Override
    public String toString() {
        String str = depart.toString();
        for (Pair p : coups) {
            str += "-" + p.getPosition();
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultipleCoupCam) {
            MultipleCoupCam coupCam = (MultipleCoupCam) obj;
            boolean equals = this.depart.equals(coupCam.depart);

            Iterator<Pair> itCoupCam = coupCam.coups.iterator();
            Iterator<Pair> itThis = this.coups.iterator();

            while (equals && itThis.hasNext() && itCoupCam.hasNext()) {
                equals = itThis.next().position.equals(itCoupCam.next().position);
            }

            equals = equals && (!itThis.hasNext() && !itCoupCam.hasNext());

            return equals;

        } else if (obj instanceof SimpleCoupCam) {
            SimpleCoupCam coupCam = (SimpleCoupCam) obj;
            return this.coups.size() == 1 && this.depart.equals(coupCam.getDepart()) && this.coups.iterator().next().position.equals(coupCam.arriver);
        } else if (obj instanceof String) {
            return this.equals(new MultipleCoupCam((String) obj));
        } else {
            return super.equals(obj);
        }
    }

}
