package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yusarin;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MisaKurobane extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MisaKurobane.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new Yusarin(), false);
    }

    public MisaKurobane()
    {
        super(DATA);

        Initialize(0, 0,1);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Fire(), true);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.MakeCardInDiscardPile(new Yusarin());
    }
}