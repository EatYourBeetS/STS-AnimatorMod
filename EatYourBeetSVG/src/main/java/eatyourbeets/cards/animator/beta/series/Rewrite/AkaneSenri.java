package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class AkaneSenri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AkaneSenri.class).SetPower(3, CardRarity.RARE);

    public AkaneSenri()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 2);
        SetEthereal(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new AkaneSenriPower(p, secondaryValue));

        RandomizedList<AbstractCard> randomizedList = new RandomizedList<>();
        randomizedList.AddAll(player.drawPile.group);

        for (int i=0; i<magicNumber; i++)
        {
            AbstractCard card = randomizedList.Retrieve(rng);

            if (card != null && card instanceof EYBCard && !card.hasTag(HASTE))
            {
                ((EYBCard) card).SetHaste(true);
            }
            else
            {
                break;
            }
        }
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