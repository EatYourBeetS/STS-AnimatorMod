package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YachiyoNanami extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class).SetPower(2, CardRarity.UNCOMMON);

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, magicNumber));
    }

    public static class YachiyoNanamiPower extends AnimatorPower
    {
        public static final int INTELLECT_AMOUNT = 2;

        public YachiyoNanamiPower(AbstractPlayer owner, int amount)
        {
            super(owner, YachiyoNanami.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, INTELLECT_AMOUNT);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            flash();

            GameActions.Bottom.DiscardFromHand(name, 1, false)
            .SetOptions(true, true, true)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    if (GameUtilities.IsCurseOrStatus(cards.get(0)))
                    {
                        GameActions.Bottom.GainIntellect(INTELLECT_AMOUNT);
                    }

                    GameActions.Bottom.GainBlock(amount);
                }
            });
        }
    }
}