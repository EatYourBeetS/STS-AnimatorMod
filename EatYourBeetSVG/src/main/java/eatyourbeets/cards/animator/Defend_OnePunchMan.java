package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper2;

public class Defend_OnePunchMan extends Defend
{
    public static final String ID = Register(Defend_OnePunchMan.class.getSimpleName());

    public Defend_OnePunchMan()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);

        SetHealing(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.GainBlock(this.block);

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper2.StackPower(new EnergizedBluePower(p, 1));
        }
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