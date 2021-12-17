package pinacolada.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class DamageNextTurnPower extends PCLPower {
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

            PCLActions.Bottom.DealDamageToRandomEnemy(amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING);

            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
}