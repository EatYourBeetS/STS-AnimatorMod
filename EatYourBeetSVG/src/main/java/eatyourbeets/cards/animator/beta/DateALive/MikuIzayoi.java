package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MikuIzayoi extends AnimatorCard {
    public static final EYBCardData DATA = Register(MikuIzayoi.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public MikuIzayoi() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.eatyourbeets.cards.animator.beta.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);
    }
}