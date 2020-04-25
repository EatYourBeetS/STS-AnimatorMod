package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard {
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public ToukaNishikujou() {
        super(DATA);

        Initialize(0, 9, 10,1);
        SetUpgrade(0, 2, -2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainAgility(secondaryValue,upgraded);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(__ -> {
            if (HasSynergy())
            {
                int numThrowingKnives = player.currentBlock / magicNumber;

                if (numThrowingKnives > 0)
                {
                    GameActions.Bottom.CreateThrowingKnives(numThrowingKnives);
                }
            }
        });
    }
}