package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard {
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public ToukaNishikujou() {
        super(DATA);

        Initialize(0, 10, 8,9);
        SetUpgrade(0, 0, -2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, 1)
        .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(__ -> {
            int numThrowingKnives = player.currentBlock / magicNumber;

            if (numThrowingKnives > 0) {
                GameActions.Bottom.CreateThrowingKnives(numThrowingKnives);
            }

            if (HasSynergy())
            {
                GameActions.Bottom.Cycle(name, 1)
                .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
            }
        });
    }
}