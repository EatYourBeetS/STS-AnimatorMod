package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Noah extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noah.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Piercing)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(1, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(2, 0, 1);
        SetHitCount(3);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetHaste(true);
    }

    @Override
    protected float GetInitialDamage()
    {
        if (!CheckPrimaryCondition(false) ^ auxiliaryData.form == 1) {
            return super.GetInitialDamage() + GetXValue();
        }
        return super.GetInitialDamage();
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && CheckPrimaryCondition(false))
        {
            GameUtilities.GetIntent(m).AddStrength(-CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true));
        }
    }

    @Override
    public int GetXValue() {
        return CheckPrimaryCondition(false) ? CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true) : CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f));
        GameActions.Bottom.StackAffinityPower(auxiliaryData.form == 1 ? Affinity.Light : Affinity.Dark , magicNumber, true);

        int amount = GetXValue();
        if (amount > 0) {
            if (CheckPrimaryCondition(true)) {
                TrySpendAffinity(Affinity.Light, GetXValue());
                if (auxiliaryData.form == 1) {
                    GameActions.Bottom.ApplyShackles(TargetHelper.Enemies(), amount);
                }
                else {
                    GameActions.Bottom.StackPower(new DelayedDamagePower(p, amount));
                }

            }
            else {
                TrySpendAffinity(Affinity.Dark, GetXValue());
                if (auxiliaryData.form == 1) {
                    GameActions.Bottom.StackPower(new DelayedDamagePower(p, amount));
                }
                else {
                    GameActions.Bottom.ApplyShackles(TargetHelper.Enemies(), amount);
                }

            }
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Dark, true);
    }
}