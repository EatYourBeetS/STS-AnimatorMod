package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_GenshinImpact extends Defend
{
    public static final String ID = Register(Defend_GenshinImpact.class).ID;

    public Defend_GenshinImpact()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6, 0);
        SetUpgrade(0, 3);

        SetSeries(CardSeries.GenshinImpact);
        SetAffinity_Orange(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameUtilities.RetainPower(AffinityType.Orange);
    }
}