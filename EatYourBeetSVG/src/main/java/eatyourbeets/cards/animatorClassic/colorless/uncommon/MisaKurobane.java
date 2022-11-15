package eatyourbeets.cards.animatorClassic.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.MisaKurobane_Yusarin;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MisaKurobane extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MisaKurobane.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Charlotte);
    static
    {
        DATA.AddPreview(new MisaKurobane_Yusarin(), false);
    }

    public MisaKurobane()
    {
        super(DATA);

        Initialize(0, 0,1);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(1);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrb(new Fire());
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.MakeCardInDiscardPile(new MisaKurobane_Yusarin());
    }
}