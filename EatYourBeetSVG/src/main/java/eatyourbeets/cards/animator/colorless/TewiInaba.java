package eatyourbeets.cards.animator.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.Earth;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class TewiInaba extends AnimatorCard
{
    public static final String ID = Register(TewiInaba.class.getSimpleName(), EYBCardBadge.Synergy);

    public TewiInaba()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 2);

        SetExhaust(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.DiscardFromHand(this.name, 1, true);
        GameActions.Bottom.Draw(this.magicNumber);

        if (HasActiveSynergy())
        {
            for (AbstractOrb orb : p.orbs)
            {
                if (Earth.ORB_ID.equals(orb.ID))
                {
                    GameActions.Bottom.Add(new EvokeSpecificOrbAction(orb));
                    GameActions.Bottom.ChannelOrb(new Earth(), true);
                    return;
                }
            }
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
