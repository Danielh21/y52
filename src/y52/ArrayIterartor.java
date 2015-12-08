/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.Iterator;

/**
 *
 * @author Daniel
 */
public class ArrayIterartor implements Iterator{
        
        int index;
    private Object array[][];
        public ArrayIterartor() {
            index =0;
        }
        

        @Override
        public boolean hasNext() {
            return(index < array.length);
        }

        @Override
        public Object next() {
            
            int next = index +1;
            index++;
            return next;
        }
        
    }
