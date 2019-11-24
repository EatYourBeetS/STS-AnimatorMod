package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;

public class Arpeggio extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Arpeggio.class.getSimpleName(), EYBCardBadge.Synergy);

    public Arpeggio()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 6);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (secondaryValue > 0)
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(secondaryValue));
        }

        GameActionsHelper.GainIntellect(magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ChannelOrb(new Earth(), true);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}