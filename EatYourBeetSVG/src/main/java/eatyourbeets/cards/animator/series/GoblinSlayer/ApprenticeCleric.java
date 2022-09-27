package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
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

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(1, 1, 0);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            final int amount = CombatStats.Affinities.GetPowerAmount(Affinity.Light) * secondaryValue;
            if (amount > 0)
            {
                GameActions.Bottom.RecoverHP(amount);
                GameActions.Bottom.Flash(this);
            }
        }
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
                GameActions.Top.IncreaseScaling(c, Affinity.Light, magicNumber);
                GameActions.Top.SealAffinities(c, false);
            }
        });
    }
}