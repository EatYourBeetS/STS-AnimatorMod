package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class KoishiKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KoishiKomeiji.class).SetPower(2, CardRarity.RARE);

    public KoishiKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetCostUpgrade(-1);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new KoishiPower(p, magicNumber));
    }

    public static class KoishiPower extends AnimatorPower
    {
        private int autoPlayCounter = 0;

        public KoishiPower(AbstractCreature owner, int amount)
        {
            super(owner, KoishiKomeiji.DATA);
            this.amount = amount;
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
        public void onCardDraw(AbstractCard card) {
            super.onCardDraw(card);

            if (autoPlayCounter < amount) {
                autoPlayCounter++;
                this.flash();
                GameActions.Bottom.PlayCard(card, player.hand, null)
                        .SpendEnergy(true)
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

