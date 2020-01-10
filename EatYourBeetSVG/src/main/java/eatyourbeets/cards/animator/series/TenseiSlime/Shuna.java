package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Shuna extends AnimatorCard
{
    public static final String ID = Register(Shuna.class, EYBCardBadge.Synergy, EYBCardBadge.Drawn);

    public Shuna()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 2, 2);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.GainBlock(block);

        if (HasSynergy())
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}