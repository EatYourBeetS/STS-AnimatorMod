package eatyourbeets.cards.animator.series.GoblinSlayer;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ApprenticeCleric extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(2);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (GameUtilities.GetPowerAmount(Affinity.Light) <= secondaryValue)
        {
            GameActions.Bottom.GainBlessing(1);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlessing(1, false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.IncreaseScaling(p.drawPile, BaseMod.MAX_HAND_SIZE, Affinity.Light, 1)
        .SetFilter(c ->
        {
            final EYBCardAffinities a = GameUtilities.GetAffinities(c);
            if (a != null && (a.GetLevel(Affinity.Red, true) > 0 || a.GetLevel(Affinity.Green, true) > 0))
            {
                return a.GetScaling(Affinity.Light, false) < 2;
            }

            return false;
        })
        .SetSelection(CardSelection.Random, magicNumber)
        .AddCallback(cards ->
        {
            for (int i = 0; i < cards.size(); i++)
            {
                final float x = AbstractCard.IMG_WIDTH * 0.5f * i;
                GameEffects.List.ShowCopy(cards.get(i), (Settings.WIDTH * 0.2f) + x, Settings.HEIGHT * 0.4f);
            }
        });
    }
}