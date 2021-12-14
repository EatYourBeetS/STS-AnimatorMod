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

        SetAffinity_Green(2, 0, 2);
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

        if (m != null && GameUtilities.IsAttacking(m.intent)) {
            GameActions.Bottom.CreateThrowingKnives(1);
        }
    }
}