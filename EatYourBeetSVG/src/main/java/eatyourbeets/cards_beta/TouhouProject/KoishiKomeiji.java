package eatyourbeets.cards_beta.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class KoishiKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KoishiKomeiji.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public KoishiKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new KoishiPower(p, magicNumber));
    }

    public static class KoishiPower extends AnimatorPower
    {
        private int autoPlayCounter;

        public KoishiPower(AbstractCreature owner, int amount)
        {
            super(owner, KoishiKomeiji.DATA);
            this.amount = amount;
            autoPlayCounter = amount; //Don't autoplay cards the turn you play this card
            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            autoPlayCounter = 0;
            GameActions.Bottom.GainEnergy(amount);
        }

        @Override
        public void onCardDraw(AbstractCard card)
        {
            super.onCardDraw(card);

            if (autoPlayCounter < amount)
            {
                autoPlayCounter++;
                this.flash();
                GameActions.Top.PlayCard(card, player.hand, null)
                .SpendEnergy(true, true)
                .AddCondition(AbstractCard::hasEnoughEnergy);
            }
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

