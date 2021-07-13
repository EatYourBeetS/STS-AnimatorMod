package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yusarin;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MisaKurobane extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MisaKurobane.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Charlotte);
    static
    {
        DATA.AddPreview(new Yusarin(), false);
    }

    public MisaKurobane()
    {
        super(DATA);

        Initialize(0, 0,1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);

        SetExhaust(true);
        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ChannelOrb(new Fire());
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.MakeCardInDiscardPile(new Yusarin());
    }
}