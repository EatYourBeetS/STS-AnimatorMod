package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SuzuneAmano extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuzuneAmano.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage();

    public SuzuneAmano()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Delayed.Callback(() ->
        {
            if (!player.hand.contains(this))
            {
                return;
            }

            int minHealth = Integer.MAX_VALUE;
            AbstractMonster enemy = null;

            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (m.currentHealth < minHealth)
                {
                    minHealth = m.currentHealth;
                    enemy = m;
                }
            }

            if (enemy != null && player.hand.contains(this))
            {
                GameActions.Top.AutoPlay(this, player.hand, enemy);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.ExhaustFromPile(name, 1, p.hand, p.drawPile)
        .SetOptions(false, false, false);
    }
}