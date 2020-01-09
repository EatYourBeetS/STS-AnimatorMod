package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yusarin;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class MisaKurobane extends AnimatorCard
{
    public static final String ID = Register(MisaKurobane.class);

    public MisaKurobane()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0,1);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.Charlotte);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Yusarin(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Fire(), true);
        GameActions.Bottom.Draw(this.magicNumber);

        if (upgraded)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Yusarin());
        }
        else
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Yusarin());
        }
    }
}