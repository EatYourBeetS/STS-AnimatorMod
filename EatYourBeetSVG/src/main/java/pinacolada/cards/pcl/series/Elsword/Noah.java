package pinacolada.cards.pcl.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
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

        Initialize(3, 0, 1);
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
        return super.GetInitialDamage() + magicNumber * PCLGameUtilities.GetOrbCount(auxiliaryData.form == 1 ? Lightning.ORB_ID : Dark.ORB_ID);
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
        return Math.max(PCLGameUtilities.GetAffinityCount(PCLAffinity.Light), PCLGameUtilities.GetAffinityCount(PCLAffinity.Dark));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d
                .SetDamageEffect(c -> PCLGameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.PURPLE, Color.LIGHT_GRAY, Color.VIOLET, Color.BLUE).duration * 0.6f));

        int amount = GetXValue();
        boolean isPrimary = CheckPrimaryCondition(true);
        if (amount > 0) {
            PCLAffinity spendAffinity = (auxiliaryData.form == 0 ^ isPrimary) ? PCLAffinity.Light : PCLAffinity.Dark;
            TrySpendAffinity(spendAffinity, GetXValue());
            if (isPrimary) {
                PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), amount);
            }
            else {
                PCLActions.Bottom.AddAffinity(auxiliaryData.form == 1 ? PCLAffinity.Light : PCLAffinity.Dark, amount);
            }
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return auxiliaryData.form == 1 ? PCLGameUtilities.GetAffinityCount(PCLAffinity.Light) > PCLGameUtilities.GetAffinityCount(PCLAffinity.Dark) :
                PCLGameUtilities.GetAffinityCount(PCLAffinity.Dark) > PCLGameUtilities.GetAffinityCount(PCLAffinity.Light);
    }
}