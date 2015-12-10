/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;
import java.util.Comparator;
/**
 *
 * @author Daniel
 */
public class ComparatorCoordinates implements Comparator<Coordinates> {

    @Override
    public int compare(Coordinates o1, Coordinates o2) {
        if(o1.getPre() > o2.getPre()){
                return -1;
                }
                if(o1.getPre() < o2.getPre()){
                     return 1;
                }
                return 0;
            
    }
    
}
