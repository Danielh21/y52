/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import battleship.interfaces.Ship;
import java.util.Iterator;

/**
 *
 * @author Daniel
 */
public class TestHelp implements Iterator<Ship> {

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Ship next() {
        Ship n = new Ship() {

            @Override
            public int size() {
                return 2;
            }
        };
        return n;
    }
    
}
