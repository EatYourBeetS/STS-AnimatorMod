package eatyourbeets.cards.animatorClassic.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.AinzPower;
import eatyourbeets.utilities.GameActions;

public class Ainz extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Ainz.class).SetPower(7, CardRarity.RARE);

    public Ainz()
    {
        super(DATA);

        Initialize(0, 0, AinzPower.CHOICES);
        SetCostUpgrade(-1);

        SetHealing(true);
        SetSeries(CardSeries.Overlord);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.cost > 0)
        {
            this.updateCost(-1);

            GameActions.Bottom.GainRandomAffinityPower(1, false);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new AinzPower(p, 1));
    }
}