package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.ArrayUtils;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_EnterStance;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.*;
import pinacolada.utilities.PCLActions;

public class Enchantment1 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment1.class);
    public static final int INDEX = 1;

    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    private static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    public Enchantment1()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 8);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form < 7)
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(3);
        }
        else {
            upgradeSecondaryValue(6);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return PCLCombatStats.MatchingSystem.CheckAffinityLevels(GetAffinityList(), cost, true);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        PCLActions.Bottom.TryChooseSpendAffinity(name, cost, GetAffinityList());
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        String stanceID = GetStanceID();
        if (stanceID != null) {
            PCLActions.Bottom.ChangeStance(stanceID);
        }
        else if (auxiliaryData.form == 7) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(WisdomStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(EnduranceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(InvocationStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(DesecrationStance.STANCE_ID));
            }

            choices.Select(1, m);
        }
        else {
            PCLActions.Bottom.ChangeStance(PCLStance.GetRandomStance());
        }
    }

    protected PCLAffinity[] GetAffinityList() {
        switch(auxiliaryData.form) {
            case 1:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Red);
            case 2:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Green);
            case 3:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Blue);
            case 4:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Orange);
            case 5:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Light);
            case 6:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Dark);
        }
        return PCLAffinity.Extended();
    }

    protected String GetStanceID() {
        switch(auxiliaryData.form) {
            case 1:
                return MightStance.STANCE_ID;
            case 2:
                return VelocityStance.STANCE_ID;
            case 3:
                return WisdomStance.STANCE_ID;
            case 4:
                return EnduranceStance.STANCE_ID;
            case 5:
                return InvocationStance.STANCE_ID;
            case 6:
                return DesecrationStance.STANCE_ID;
        }
        return null;
    }
}