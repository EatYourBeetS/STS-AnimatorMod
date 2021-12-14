package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HidekiHinata extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HidekiHinata.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public HidekiHinata()
    {
        super(DATA);

        Initialize(7, 0, 1, 3);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.IsAttacking(enemy.intent))
        {
            return super.ModifyDamage(enemy, amount * 2);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);

        if (!GameUtilities.IsAttacking(m.intent)) {
            GameActions.Bottom.GainSupportDamage(secondaryValue);
        }

        if (CombatStats.ControlPile.Contains(this)) {
            GameActions.Bottom.StackPower(new EnergizedPower(p, 1));
        }
    }
}