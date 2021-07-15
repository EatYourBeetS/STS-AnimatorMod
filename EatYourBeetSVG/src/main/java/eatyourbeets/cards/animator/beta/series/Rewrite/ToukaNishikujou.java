package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ToukaNishikujou()
    {
        super(DATA);

        Initialize(0, 10, 8, 9);
        SetUpgrade(0, 0, -2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, 1)
        .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(() ->
        {
            int throwingKnives = player.currentBlock / magicNumber;
            if (throwingKnives > 0)
            {
                GameActions.Bottom.CreateThrowingKnives(throwingKnives);
            }

            if (HasSynergy())
            {
                GameActions.Bottom.Cycle(name, 1)
                .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
            }
        });
    }
}