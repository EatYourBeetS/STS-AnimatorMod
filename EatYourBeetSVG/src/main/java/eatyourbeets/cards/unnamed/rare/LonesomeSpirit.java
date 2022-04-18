package eatyourbeets.cards.unnamed.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class LonesomeSpirit extends UnnamedCard
{
    public static final int POWER_ACTIVATION_CAP = 4;
    public static final EYBCardData DATA = Register(LonesomeSpirit.class)
            .SetPower(2, CardRarity.RARE);

    public LonesomeSpirit()
    {
        super(DATA);

        Initialize(0, 0, 1, POWER_ACTIVATION_CAP);
        SetCostUpgrade(-1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new LonesomeSpiritPower(p, 1));
    }

    public static class LonesomeSpiritPower extends UnnamedPower
    {
        public int strPool;
        public int dexPool;

        public LonesomeSpiritPower(AbstractCreature owner, int amount)
        {
            super(owner, LonesomeSpirit.DATA);

            strPool = dexPool = amount * POWER_ACTIVATION_CAP;

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, strPool, dexPool);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            if (IsSolo() && strPool > 0)
            {
                GameActions.Bottom.GainStrength(1);
                ReduceAmounts(1, 0);
                flashWithoutSound();
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            if (IsSolo() && dexPool > 0)
            {
                GameActions.Bottom.GainDexterity(1);
                ReduceAmounts(0, 1);
                flashWithoutSound();
            }
        }

        private void ReduceAmounts(int strength, int dexterity)
        {
            strPool -= strength;
            dexPool -= dexterity;
            updateDescription();

            if (strPool <= 0 && dexPool <= 0)
            {
                RemovePower();
            }
        }

        @Override
        protected ColoredString GetPrimaryAmount(Color c)
        {
            return new ColoredString(strPool, Colors.Orange(c.a));
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(dexPool, Colors.LightGreen(c.a));
        }
    }
}