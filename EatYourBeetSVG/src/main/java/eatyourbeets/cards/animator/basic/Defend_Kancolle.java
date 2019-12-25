package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Defend_Kancolle extends Defend
{
    public static final String ID = Register(Defend_Kancolle.class.getSimpleName());

    public Defend_Kancolle()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 5);
        SetUpgrade(0, 3);

        SetHealing(true);
        SetSynergy(Synergies.Kancolle);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainGold(magicNumber);
        }
    }
}