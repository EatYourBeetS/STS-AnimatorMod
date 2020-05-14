package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.ultrarare.AngelAlter;
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

        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    protected void OnUpgrade()
    {
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.FetchFromPile(name, p.discardPile.size(), p.discardPile)
                .SetOptions(false, true)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        GameActions.Bottom.Add(new RefreshHandLayout());
                    }
                    for (AbstractCard card : p.discardPile.group) {
                        addToBot(new ExhaustSpecificCardAction(card, p.discardPile));
                    }
                });

    }
}