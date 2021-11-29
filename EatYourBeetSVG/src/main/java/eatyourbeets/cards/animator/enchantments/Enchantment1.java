package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;
import org.apache.commons.lang3.ArrayUtils;

public class Enchantment1 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment1.class);
    public static final int INDEX = 1;

    private static final CardEffectChoice choices = new CardEffectChoice();
    private static final float D_X = CardGroup.DRAW_PILE_X * 1.5f;
    private static final float D_Y = CardGroup.DRAW_PILE_Y * 3.5f;

    public Enchantment1()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 5);
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
            upgradeSecondaryValue(2);
        }
        else {
            upgradeSecondaryValue(1);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return CombatStats.Affinities.CheckAffinityLevels(GetAffinityList(), cost, true);
    }

    @Override
    public void PayPowerCost(int cost)
    {
        GameActions.Bottom.TryChooseSpendAffinity(name, cost, GetAffinityList());
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        String stanceID = GetStanceID();
        if (stanceID != null) {
            GameActions.Bottom.ChangeStance(stanceID);
        }
        else if (auxiliaryData.form == 7) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(MightStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(WisdomStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(EnduranceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(SuperchargeStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(DesecrationStance.STANCE_ID));
            }

            choices.Select(1, m);
        }
        else {
            GameActions.Bottom.ChangeStance(EYBStance.GetRandomStance());
        }
    }

    protected Affinity[] GetAffinityList() {
        switch(auxiliaryData.form) {
            case 1:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Red);
            case 2:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Green);
            case 3:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Blue);
            case 4:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Orange);
            case 5:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Light);
            case 6:
                return ArrayUtils.removeElement(Affinity.Extended(), Affinity.Dark);
        }
        return Affinity.Extended();
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
                return SuperchargeStance.STANCE_ID;
            case 6:
                return DesecrationStance.STANCE_ID;
        }
        return null;
    }
}