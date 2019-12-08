package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ElricAlphonseAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(ElricAlphonseAlt.class.getSimpleName(), EYBCardBadge.Synergy);

    public ElricAlphonseAlt()
    {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0,0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper2.ChannelOrb(new Lightning(), true);
        GameActionsHelper2.GainPlatedArmor(this.magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper2.GainOrbSlots(1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}