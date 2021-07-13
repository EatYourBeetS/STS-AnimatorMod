package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Defend_TenSura extends Defend
{
    public static final String ID = Register(Defend_TenSura.class).ID;

    public Defend_TenSura()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3);
        SetUpgrade(0, 1);

        SetSeries(CardSeries.TenSura);
        SetAffinity_Green(1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);
    }
}