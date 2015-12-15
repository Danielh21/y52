/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y52;

import java.util.Random;
import java.util.ArrayList;
import battleship.interfaces.Position;
/**
 *
 * @author Daniel
 */
class Coordinates {
    
    public  int x;
    public  int y;
    public int pre;
    public final int upperBound=9;
    public final int lowerBound=0;
    Random gen = new Random();
    public Coordinates [][] board2;
            
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
    
    public  ArrayList<Position> getNabours(Coordinates coor){
         ArrayList<Position>  nabours = new ArrayList<>();
         
         //Coor = x=5, y=0.
         
         
         if(coor.x!=upperBound){
             nabours.add(new Position(coor.x+1, coor.y));
         }
         
         if(coor.x!=lowerBound){
              nabours.add(new Position(coor.x-1, coor.y));
         }
         
         if(coor.y!=upperBound){
             nabours.add(new Position(coor.x, coor.y+1));
         }
         
         if(coor.y!=lowerBound){
             nabours.add(new Position(coor.x, coor.y-1));
         }
         
         return nabours;
    }
    
}
