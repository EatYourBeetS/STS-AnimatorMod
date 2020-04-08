package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.animator.special.DarknessAdrenaline;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 3);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.ReduceStrength(enemy, magicNumber, true);
        }


        GameActions.Bottom.StackPower(new KagariPower(p, 1, secondaryValue));
    }

    public static class KagariPower extends AnimatorPower
    {
        private final int numEarth;

        public KagariPower(AbstractPlayer owner, int amount, int numEarth)
        {
            super(owner, Kagari.DATA);

            this.amount = amount;
            this.numEarth = numEarth;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                flash();
                GameActions.Top.ReducePower(this, 1);

                for(int i=0; i<amount; i++)
                {
                    GameActions.Bottom.ChannelOrb(new Earth(), true);
                }
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, numEarth);
        }
    }
}