package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_OwariNoSeraph extends Defend
{
    public static final String ID = Register(Defend_OwariNoSeraph.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Defend_OwariNoSeraph()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyWeak(p, m, this.magicNumber);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.GainBlock(this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}