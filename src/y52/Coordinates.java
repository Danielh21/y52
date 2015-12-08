/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.Random;

/**
 *
 * @author Daniel
 */
class Coordinates {
    
    public final int x;
    public final int y;
    public int pre;
    Random gen = new Random();
            
    public Coordinates(int x, int y, int pre) {
        this.x = x;
        this.y = y;
        this.pre = pre;
    }

    public Coordinates() {
        x = gen.nextInt(10);
        y = gen.nextInt(10);
        pre=1;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPre() {
        return pre;
    }
    
}
