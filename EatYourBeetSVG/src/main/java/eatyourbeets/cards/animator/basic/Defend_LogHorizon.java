package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Defend_LogHorizon extends Defend
{
    public static final String ID = Register(Defend_LogHorizon.class).ID;

    public Defend_LogHorizon()
    {
        super(ID, 0, CardTarget.SELF);

        Initialize(0, 2);
        SetUpgrade(0, 1);

        SetSynergy(Synergies.LogHorizon);
        SetAffinity_Blue(1);
    }

    @Override
    public boolean HasDirectSynergy(AbstractCard other)
    {
        return other.hasTag(SPELLCASTER) || super.HasDirectSynergy(other);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(this.block);
    }
}