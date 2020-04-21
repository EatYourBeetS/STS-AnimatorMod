package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class TempHPNextTurnPower extends AnimatorPower {
    public static final String POWER_ID = CreateFullID(TempHPNextTurnPower.class);

    public TempHPNextTurnPower(AbstractPlayer owner, int amount) {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = FormatDescription(0, amount);
    }

    public void atStartOfTurn()
    {
        flash();
        GameActions.Bottom.GainTemporaryHP(amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }
 }