package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Kagari extends AnimatorCard {
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    public Kagari() {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1, 1);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.StackPower(new KagariPower(p, magicNumber, secondaryValue));
    }

    public static class KagariPower extends AnimatorPower {
        private int thornsBase;
        private int thornsIncrease;

        public KagariPower(AbstractPlayer owner, int thornsBase, int thornsIncrease) {
            super(owner, Kagari.DATA);

            this.thornsBase = thornsBase;
            this.thornsIncrease = thornsIncrease;
            this.amount = thornsBase;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            this.amount = thornsBase;
            updateDescription();
        }

        @Override
        public void atEndOfRound()
        {
            this.amount = thornsBase;
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (damageAmount > 0 && damageAmount < this.owner.currentHealth &&
                    info.owner != null && info.type == DamageInfo.DamageType.NORMAL && info.type != DamageInfo.DamageType.HP_LOSS)
            {
                GameActions.Top.GainThorns(amount);
                amount += thornsIncrease;
                updateDescription();
            }

            return damageAmount;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, thornsIncrease);
        }
    }
}