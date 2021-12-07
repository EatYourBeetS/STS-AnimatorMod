package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.MightStance;
import eatyourbeets.utilities.GameActions;

public class Naotsugu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Naotsugu.class)
            .SetAttack(3, CardRarity.COMMON, EYBAttackType.Normal)
            .SetSeriesFromClassPackage();

    public Naotsugu()
    {
        super(DATA);

        Initialize(9, 1);
        SetUpgrade(3, 1);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Red, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY)
                .forEach(d -> d.AddCallback(e ->
        {
            AbstractCard best = null;
            int maxBlock = e.lastDamageTaken;
            for (AbstractCard c : player.hand.group)
            {
                if (c.block > 0 && c.block < maxBlock)
                {
                    if (MightStance.IsActive() || TrySpendAffinity(Affinity.Red))
                    {
                        GameActions.Top.PlayCard(c, player.hand, (AbstractMonster) e)
                        .SetDuration(Settings.ACTION_DUR_MED, true);
                    }
                    else if (best == null || best.block > c.block)
                    {
                        best = c;
                    }
                }
            }

            if (best != null)
            {
                GameActions.Top.PlayCard(best, player.hand, (AbstractMonster) e);
            }
        }));
    }
}