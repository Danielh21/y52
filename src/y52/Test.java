/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.ArrayList;
import battleship.interfaces.Board;
/**
 *
 * @author Daniel
 */
public class Test {
    
    public static void main(String[] args) {
        RP rP = new RP();
        
        ArrayList<Coordinates> Contains = new ArrayList<>();
        rP.setAllXY(Contains);
        
        for (Coordinates Contain : Contains) {
            System.out.println(Contain.x + "," + Contain.y + " Pre: " + Contain.pre);
        }
        
        
                
    }
    
}
