package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.EndurancePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Stockpile extends AnimatorCard {
    public static final EYBCardData DATA = Register(Stockpile.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Stockpile() {
        super(DATA);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 1);
        SetAffinity_Orange(2, 0, 0);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .SetMessage(RetainCardsAction.TEXT[0])
                .SetFilter(c -> !c.isEthereal)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        GameUtilities.Retain(card);
                    }

                    EndurancePower.MakeEarthCard();

                });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}