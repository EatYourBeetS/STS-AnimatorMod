package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yusa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yusa.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new FakeAbstractCard(new Insight()), false);
    }

    public Yusa()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Scry(secondaryValue);
        GameActions.Bottom.ExhaustFromPile(name, magicNumber, p.discardPile);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Insight());
        }
    }
}