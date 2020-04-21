package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class PiercingNextTurnPower extends AnimatorPower {
        public static final String POWER_ID = CreateFullID(PiercingNextTurnPower.class);

        public PiercingNextTurnPower(AbstractPlayer owner, int amount) {
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
            int[] damage = DamageInfo.createDamageMatrix(amount, true);

            GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                    .SetPiercing(true, true);
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
}