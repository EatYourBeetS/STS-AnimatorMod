package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Yusa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yusa.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self);

    public Yusa()
    {
        super(DATA);

        Initialize(0, 5, 1, 3);
        SetUpgrade(0, 2, 0, 1);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Scry(secondaryValue);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        GameActions.Bottom.MakeCardInDiscardPile(new Madness());
    }
}