package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class YuzuruOtonashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuzuruOtonashi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self);

    public YuzuruOtonashi()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ExhaustFromHand(name, p.hand.size(), false)
                .SetOptions(true, true, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                       GameActions.Bottom.GainTemporaryHP(magicNumber);
                    }
                });
    }

    @Override
    public void triggerOnExhaust() {
        GameActions.Bottom.MakeCardInHand(this.makeStatEquivalentCopy());
    }
}