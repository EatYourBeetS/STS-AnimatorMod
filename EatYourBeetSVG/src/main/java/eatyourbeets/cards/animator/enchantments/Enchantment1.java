package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
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
        if (IsStanceSpecific()) {
            if (GameUtilities.InStance(GetStanceID())) {
                GameActions.Bottom.ChangeStance(GetStanceID());
            }
            else {
                GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
                        .AddCallback((stance) ->
                        {
                            if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && stance instanceof EYBStance)
                            {
                                GameActions.Bottom.StackAffinityPower(((EYBStance) stance).affinity, magicNumber, false);
                            }
                        });
            }
        }
        else if (auxiliaryData.form == 7) {
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(WillpowerStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(BlessingStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_EnterStance(CorruptionStance.STANCE_ID));
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
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Red);
            case 2:
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Green);
            case 3:
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Blue);
            case 4:
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Orange);
            case 5:
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Light);
            case 6:
                return ArrayUtils.removeElement(Affinity.Basic(), Affinity.Dark);
        }
        return Affinity.Basic();
    }

    protected boolean IsStanceSpecific() {
        return auxiliaryData.form > 0 && auxiliaryData.form < 7;
    }

    protected String GetStanceID() {
        switch(auxiliaryData.form) {
            case 1:
                return ForceStance.STANCE_ID;
            case 2:
                return AgilityStance.STANCE_ID;
            case 3:
                return IntellectStance.STANCE_ID;
            case 4:
                return WillpowerStance.STANCE_ID;
            case 5:
                return BlessingStance.STANCE_ID;
            case 6:
                return CorruptionStance.STANCE_ID;
        }
        return NeutralStance.STANCE_ID;
    }
}