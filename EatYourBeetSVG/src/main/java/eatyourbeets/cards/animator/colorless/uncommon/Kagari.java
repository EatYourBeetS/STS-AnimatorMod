package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 3, KagariPower.EARTH_ORBS);
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

        GameActions.Bottom.StackPower(new KagariPower(p, 1));
    }

    public static class KagariPower extends AnimatorPower
    {
        public static final int EARTH_ORBS = 2;

        public KagariPower(AbstractPlayer owner, int amount)
        {
            super(owner, Kagari.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, EARTH_ORBS);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                flash();
                GameActions.Top.ReducePower(this, 1);

                for (int i = 0; i < EARTH_ORBS; i++)
                {
                    GameActions.Bottom.ChannelOrb(new Earth(), true);
                }
            }
        }
    }
}