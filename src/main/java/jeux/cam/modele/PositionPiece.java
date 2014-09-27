package jeux.cam.modele;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author godeau
 */
public class PositionPiece extends ArrayList<Position> {

    public PositionPiece() {
        super(7);
    }
    
    public PositionPiece(Collection<Position> positions) {
        super(positions);
    }

    public boolean remove(int i, int j) {
        for (int k = 0; k < this.size(); k++) {
            if(this.get(k).equals(i, j)){
                this.remove(k);
                return true;
            }
            
        }
        return false;
    }
}
