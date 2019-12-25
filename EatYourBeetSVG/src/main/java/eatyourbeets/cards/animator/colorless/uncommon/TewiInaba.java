package eatyourbeets.cards.animator.colorless.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.Earth;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class TewiInaba extends AnimatorCard
{
    public static final String ID = Register(TewiInaba.class.getSimpleName(), EYBCardBadge.Discard);

    public TewiInaba()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 2, 2);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Add(new TriggerPassiveAction(1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Add(new EvokeOrbAction(1));

        if (!p.orbs.isEmpty() && Earth.ORB_ID.equals(p.orbs.get(0).ID))
        {
            GameActions.Bottom.Draw(this.magicNumber);
        }
    }
}
