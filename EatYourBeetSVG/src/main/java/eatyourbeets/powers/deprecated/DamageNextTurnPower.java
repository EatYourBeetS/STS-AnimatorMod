package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class DamageNextTurnPower extends CommonPower {
        public static final String POWER_ID = CreateFullID(DamageNextTurnPower.class);

        public DamageNextTurnPower(AbstractPlayer owner, int amount) {
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

            GameActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING);

            GameActions.Bottom.RemovePower(owner, owner, this);
        }
}