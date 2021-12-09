package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.*;
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

        SetAffinity_Green(1, 0, 2);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
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

        if (CombatStats.TryActivateLimited(cardID))
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.MakeCardInDrawPile(GameUtilities.Imitate(this))
                        .AddCallback(card ->
                        {
                            card.isEthereal = true;
                            if (upgraded)
                            {
                                GameUtilities.SetCardTag(card, HASTE, true);
                            }
                        });
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL)
        .forEach(d -> d.SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()).duration));

        if (TrySpendAffinity(Affinity.Green)) {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}