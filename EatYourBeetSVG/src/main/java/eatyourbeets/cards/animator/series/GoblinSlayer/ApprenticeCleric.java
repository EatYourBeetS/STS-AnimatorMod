package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ApprenticeCleric extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Light(1, 1, 0);
        SetAffinity_Blue(1);

        SetAffinityRequirement(Affinity.Light, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetRetainOnce(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainBlessing(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromPile(name, 1, player.drawPile)
        .ShowEffect(true, true)
        .SetFilter(c ->
        {
            final EYBCardAffinities a = GameUtilities.GetAffinities(c);
            return (a != null && !a.sealed && (a.GetLevel(Affinity.Red, true) > 0 || a.GetLevel(Affinity.Green, true) > 0));
        })
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Top.IncreaseScaling(c, Affinity.Light, 1);
                GameActions.Top.SealAffinities(c, false);
            }
        });

        if (TryUseAffinity(Affinity.Light))
        {
            GameActions.Bottom.GainBlessing(1);
        }
    }
}