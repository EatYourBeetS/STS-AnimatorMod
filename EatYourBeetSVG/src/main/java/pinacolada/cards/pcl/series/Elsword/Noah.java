package pinacolada.cards.pcl.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Noah extends PCLCard
{
    public static final PCLCardData DATA = Register(Noah.class)
            .SetAttack(1, CardRarity.RARE, PCLAttackType.Piercing)
            .SetMaxCopies(2)
            .SetMultiformData(2,false)
            .SetSeriesFromClassPackage();

    public Noah()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(1, 0);

        SetAffinity_Light(1);
        SetAffinity_Dark(1, 0, 1);
        SetHitCount(3);
    }

    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();

        SetHaste(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            SetAffinity_Light(1, 0, 1);
            SetAffinity_Dark(1);
            LoadImage("_Light");
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
        else {
            SetAffinity_Light(1);
            SetAffinity_Dark(1, 0, 1);
            LoadImage(null);
            this.cardText.OverrideDescription(null, true);
        }
        return super.SetForm(form, timesUpgraded);
    };

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
            PCLGameUtilities.GetPCLIntent(m).AddStrength(-PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true));
        }
    }

    @Override
    public int GetXValue() {
        return CheckPrimaryCondition(false) ? PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) : PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d
                .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f));
        PCLActions.Bottom.StackAffinityPower(auxiliaryData.form == 1 ? PCLAffinity.Light : PCLAffinity.Dark , magicNumber, true);

        int amount = GetXValue();
        if (amount > 0) {
            if (CheckPrimaryCondition(true)) {
                TrySpendAffinity(PCLAffinity.Light, GetXValue());
                if (auxiliaryData.form == 1) {
                    PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), amount);
                }
                else {
                    PCLActions.Bottom.StackPower(new DelayedDamagePower(p, amount));
                }

            }
            else {
                TrySpendAffinity(PCLAffinity.Dark, GetXValue());
                if (auxiliaryData.form == 1) {
                    PCLActions.Bottom.StackPower(new DelayedDamagePower(p, amount));
                }
                else {
                    PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), amount);
                }

            }
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Light, true) > PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Dark, true);
    }
}