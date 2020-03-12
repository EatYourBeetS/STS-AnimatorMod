package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class MioTakamiya extends AnimatorCard implements Hidden {
    public static final EYBCardData DATA = Register(MioTakamiya.class).SetSkill(3, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public MioTakamiya() {
        super(DATA);

        Initialize(0, 15, 0);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);
    }
}