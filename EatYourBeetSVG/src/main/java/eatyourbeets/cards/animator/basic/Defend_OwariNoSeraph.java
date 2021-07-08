package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Defend_OwariNoSeraph extends Defend
{
    public static final String ID = Register(Defend_OwariNoSeraph.class).ID;

    public Defend_OwariNoSeraph()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.OwariNoSeraph);
        SetAffinity_Green(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }
}