package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class AkaneSenri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AkaneSenri.class).SetPower(3, CardRarity.RARE).SetSeriesFromClassPackage();

    public AkaneSenri()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 2);
        SetEthereal(true);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new AkaneSenriPower(p, secondaryValue));
        GameActions.Bottom.ModifyTag(player.drawPile, magicNumber, HASTE, true);
    }

    public static class AkaneSenriPower extends AnimatorPower
    {
        public AkaneSenriPower(AbstractPlayer owner, int amount)
        {
            super(owner, AkaneSenri.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            AbstractOrb darkOrb = new Dark();
            GameActions.Bottom.ChannelOrb(darkOrb);

            int triggerAmount = player.hand.size() / 2;

            for (int i = 0; i < triggerAmount; i++)
            {
                for (int j = 0; j < amount; j++)
                {
                    darkOrb.onStartOfTurn();
                    darkOrb.onEndOfTurn();
                }
            }
            flash();

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}