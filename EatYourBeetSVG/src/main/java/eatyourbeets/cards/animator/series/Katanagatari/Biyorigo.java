package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.BiyorigoPower;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Biyorigo.class).SetPower(2, CardRarity.RARE);

    public Biyorigo()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 2, 0);

        SetSeries(CardSeries.Katanagatari);
        SetAffinity(2, 2, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainThorns(magicNumber);
        GameActions.Bottom.GainArtifact(secondaryValue);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, 1));
    }
}