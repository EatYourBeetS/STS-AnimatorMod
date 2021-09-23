package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 3, KagariPower.EARTH_ORBS);
        SetUpgrade(0, 0, 3);
        SetAffinity_Blue(2, 0, 0);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddStrength(-magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
                GameActions.Bottom.ChannelOrbs(Earth::new, EARTH_ORBS);
            }
        }
    }
}