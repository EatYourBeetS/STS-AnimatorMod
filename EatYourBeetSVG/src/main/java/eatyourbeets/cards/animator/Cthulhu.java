package eatyourbeets.cards.animator;

import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.interfaces.markers.Hidden;

public abstract class Cthulhu extends AnimatorCard_UltraRare implements Hidden
{
    protected Cthulhu(String id, int cost, CardType type, CardTarget target)
    {
        super(id, cost, type, target);
    }
}