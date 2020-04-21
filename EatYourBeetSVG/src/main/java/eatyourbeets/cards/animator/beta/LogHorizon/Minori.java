package eatyourbeets.cards.animator.beta.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Minori extends AnimatorCard {
    public static final EYBCardData DATA = Register(Minori.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Minori() {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainTemporaryHP(magicNumber);

        GameActions.Bottom.Cycle(name, secondaryValue);

        if (HasSynergy())
        {
            GameActions.Bottom.Motivate(secondaryValue);
        }
    }
}