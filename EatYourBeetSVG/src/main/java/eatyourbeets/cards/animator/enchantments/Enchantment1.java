package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.CardEffectChoice;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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

        Initialize(0, 0, 1, 3);
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
            upgradeMagicNumber(2);
        }
    }

    @Override
    public boolean CanUsePower(int cost)
    {
        return !GameUtilities.InStance(NeutralStance.STANCE_ID) || (IsStanceSpecific() && super.CanUsePower(cost));
    }

    @Override
    public void PayPowerCost(int cost)
    {
        if (GameUtilities.InStance(NeutralStance.STANCE_ID) && IsStanceSpecific()) {
            super.PayPowerCost(cost);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID)
                .AddCallback((stance) ->
                {
                    if (stance != null && !stance.ID.equals(NeutralStance.STANCE_ID) && stance instanceof EYBStance)
                    {
                        if (IsStanceSpecific() && !stance.ID.equals(GetStanceID())) {
                            GameActions.Bottom.ChangeStance(GetStanceID());
                        }
                        else {
                            GameActions.Bottom.StackAffinityPower(((EYBStance) stance).affinity, magicNumber, auxiliaryData.form == 7);
                        }
                    }
                });
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