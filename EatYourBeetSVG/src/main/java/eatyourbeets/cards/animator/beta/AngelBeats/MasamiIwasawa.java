package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class MasamiIwasawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MasamiIwasawa.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.Self);

    public MasamiIwasawa()
    {
        super(DATA);

        Initialize(0, 15, 2, 2);
        SetUpgrade(0, 3, 1, 0);

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.MakeCardInDrawPile(new Dazed())
        .SetDestination(CardSelection.Top)
        .Repeat(secondaryValue);

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Draw(magicNumber);
        }
    }
}