package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class SougenEsaka extends AnimatorCard {
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public SougenEsaka() {
        super(DATA);

        Initialize(0, 5, 1);
        SetUpgrade(0, 1, 2);
        SetScaling(0,1,0);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.Add(new RandomCardUpgrade());

        if (HasSynergy())
        {
            GameActions.Bottom.GainBlur(magicNumber);
        }
    }
}