package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LizardPriest extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LizardPriest.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public LizardPriest()
    {
        super(DATA);

        Initialize(0, 7, 0, 3);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    protected float GetInitialBlock()
    {
        return super.GetInitialBlock() + (GameUtilities.HasOrb(Earth.ORB_ID) ? secondaryValue : 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (isSynergizing)
        {
            GameActions.Bottom.GainInspiration(1);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetFilter(c -> GameUtilities.CanRetain(c) && GameUtilities.HasLightAffinity(c))
        .SetMessage(GR.Common.Strings.HandSelection.Retain)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameUtilities.Retain(c);
            }
        });
    }
}