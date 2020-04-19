package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class DeenergizedPower extends CommonPower {
        public static final String POWER_ID = CreateFullID(DeenergizedPower.class);

        public DeenergizedPower(AbstractPlayer owner, int amount) {
            super(owner, POWER_ID);

            this.amount = amount;
            this.type = PowerType.DEBUFF;

            updateDescription();
        }

        public void onEnergyRecharge()
        {
            this.flash();
            GameActions.Bottom.SpendEnergy(amount, true);
            GameActions.Bottom.RemovePower(player, player, this);
        }

        @Override
        public void updateDescription() {
            description = FormatDescription(0, amount);
        }
}