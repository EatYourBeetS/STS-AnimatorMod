package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Akatsuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Akatsuki.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Akatsuki()
    {
        super(DATA);

        Initialize(11, 0, 2, 4);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(2, 0, 1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && !GameUtilities.IsAttacking(enemy.intent))
        {
            amount += secondaryValue;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Top.MakeCardInHand(this)
                .SetUpgrade(false, true)
                .AddCallback(card ->
                {
                    GameUtilities.ModifyCostForCombat(card, 0, false);
                    GameUtilities.ModifyDamage(card, 0, false);
                    card.purgeOnUse = true;
                    card.isEthereal = true;
                });
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL)
        .SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()).duration);
    }
}