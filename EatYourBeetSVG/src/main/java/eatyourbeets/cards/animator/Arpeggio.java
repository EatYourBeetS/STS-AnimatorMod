package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Earth;

public class Arpeggio extends AnimatorCard
{
    public static final String ID = Register(Arpeggio.class.getSimpleName(), EYBCardBadge.Synergy);

    public Arpeggio()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        magicNumber = baseMagicNumber + Math.max(0, Math.floorDiv(PlayerStatistics.GetFocus(), 3));
        isMagicNumberModified = (baseMagicNumber != magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
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
        TryUpgrade();
    }
}