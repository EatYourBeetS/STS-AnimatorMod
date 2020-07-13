package eatyourbeets.cards.animator.beta.Rewrite;

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
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class AkaneSenri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AkaneSenri.class).SetPower(3, CardRarity.RARE);

    public AkaneSenri()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 4, 0);

        SetSynergy(Synergies.Rewrite);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        GameActions.Bottom.StackPower(new AkaneSenriPower(p, magicNumber, secondaryValue));

        for (AbstractCard card : player.hand.group)
        {
            if (card instanceof EYBCard)
            {
                ((EYBCard) card).SetHaste(true);
            }
        }
    }

    public static class AkaneSenriPower extends AnimatorPower
    {
        public AkaneSenriPower(AbstractPlayer owner, int defaultCountdown, int amount)
        {
            super(owner, AkaneSenri.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            GameActions.Bottom.SpendEnergy(1, false)
            .AddCallback(() ->
            {
                AbstractOrb darkOrb = new Dark();
                GameActions.Bottom.ChannelOrb(darkOrb, true);

                for (int i = 0; i < player.hand.size(); i++)
                {
                    for (int j = 0; j < amount; j++)
                    {
                        darkOrb.onStartOfTurn();
                        darkOrb.onEndOfTurn();
                    }
                }
            });

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}