package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class RinTohsaka extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register_Old(RinTohsaka.class);

    public RinTohsaka()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 2, 1);
        SetUpgrade(0, 4, 0);
        SetScaling(1, 0, 0);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainTemporaryArtifact(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.orbs.size() > 0)
        {
            AbstractOrb orb = p.orbs.get(0);
            if (!(orb instanceof EmptyOrbSlot))
            {
                AbstractOrb copy = orb.makeCopy();

                copy.evokeAmount = orb.evokeAmount;
                copy.passiveAmount = orb.passiveAmount;

                GameActions.Bottom.ChannelOrb(copy, true);
            }
        }

        GameActions.Bottom.GainBlock(block);
    }
}