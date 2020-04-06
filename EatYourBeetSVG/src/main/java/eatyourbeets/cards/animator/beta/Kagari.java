package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.RARE).SetColor(CardColor.COLORLESS);
    private static final int THORNS_BASE = 1;

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 3, 10);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
        {
            GameActions.Bottom.ReduceStrength(enemy, magicNumber, true);
        }


        GameActions.Bottom.StackPower(new KagariPower(p, secondaryValue));
    }

    public static class KagariPower extends AnimatorPower
    {
        private int thornsBase;

        public KagariPower(AbstractPlayer owner, int maxThorns)
        {
            super(owner, Kagari.DATA);

            this.thornsBase = THORNS_BASE;
            this.amount = maxThorns;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (GameUtilities.GetPowerAmount(ThornsPower.POWER_ID) < amount &&
                    info.owner != null && info.type == DamageInfo.DamageType.NORMAL)
            {
                GameActions.Top.GainThorns(thornsBase);
                updateDescription();
            }

            return damageAmount;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, thornsBase, amount);
        }
    }
}