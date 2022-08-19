package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.BiyorigoPower;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class).SetPower(2, CardRarity.RARE);

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 2, 0);

        SetSeries(CardSeries.Katanagatari);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainThorns(magicNumber);
        GameActions.Bottom.GainArtifact(secondaryValue);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, 1));
    }
}