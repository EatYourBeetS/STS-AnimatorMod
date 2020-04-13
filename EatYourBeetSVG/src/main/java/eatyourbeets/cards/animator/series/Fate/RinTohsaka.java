package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class RinTohsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RinTohsaka.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public RinTohsaka()
    {
        super(DATA);

        Initialize(0, 5, 0, 1);
        SetUpgrade(0, 1, 0, 1);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Fate);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryArtifact(secondaryValue);

        if (HasSynergy() && p.filledOrbCount() > 0 && CombatStats.TryActivateSemiLimited(cardID))
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
    }
}