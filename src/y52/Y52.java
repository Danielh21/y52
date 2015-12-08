/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package y52;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class Y52 implements PlayerFactory<BattleshipsPlayer>
{

    public Y52(){}
    
    
    @Override
    public BattleshipsPlayer getNewInstance()
    {
        return new RP();
    }

    @Override
    public String getID()
    {
        return "Y52";
    }

    @Override
    public String getName()
    {
        return "Cherry and Daniel";
    }
    
}
