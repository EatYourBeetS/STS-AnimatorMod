package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class DolaCouronne extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaCouronne.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public DolaCouronne()
    {
        super(DATA);

        Initialize(0, 9, 2, 7);
        SetUpgrade(0, 1, -1);

        SetSynergy(Synergies.NoGameNoLife);
        SetAlignment(0, 1, 1, 1, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainBlock(secondaryValue);
            GameActions.Bottom.GainArtifact(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true);
    }
}