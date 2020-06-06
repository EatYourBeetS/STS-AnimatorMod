package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class KanadeTachibana extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KanadeTachibana.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None);

    public KanadeTachibana()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.FetchFromPile(name, p.discardPile.size(), p.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0]);

        if (HasSynergy())
        {
            GameActions.Bottom.ExhaustFromHand(name, BaseMod.MAX_HAND_SIZE, false)
            .SetOptions(true, true, true);
        }
    }
}