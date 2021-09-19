package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Exhume extends AnimatorCard {
    public static final EYBCardData DATA = Register(Exhume.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None);

    public Exhume() {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {

        GameActions.Bottom.FetchFromPile(name, magicNumber, p.discardPile)
                .SetOptions(true, true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}