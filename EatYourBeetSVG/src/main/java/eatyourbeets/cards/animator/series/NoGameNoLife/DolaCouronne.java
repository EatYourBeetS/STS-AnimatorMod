package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DolaCouronne extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaCouronne.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public DolaCouronne()
    {
        super(DATA);

        Initialize(0, 7, 2, 3);
        SetUpgrade(0, 1, -1);

        SetAffinity_Green(1);
        SetAffinity_Orange(1);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true).AddCallback(cards -> {
            for (AbstractCard c : cards) {
                if (GameUtilities.HasBlueAffinity(c)) {
                    GameActions.Bottom.GainBlock(secondaryValue);
                }
            }
        });
    }
}