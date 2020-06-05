package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Yusa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yusa.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Yusa()
    {
        super(DATA);

        Initialize(0, 6, 1, 2);
        SetUpgrade(0, 1, 0, 1);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromPile(name, magicNumber, p.discardPile);
        GameActions.Top.Scry(secondaryValue);
        GameActions.Top.GainBlock(block);
    }
}