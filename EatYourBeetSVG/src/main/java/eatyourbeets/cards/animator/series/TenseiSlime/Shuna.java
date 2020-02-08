package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Shuna extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shuna.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Shuna()
    {
        super(DATA);

        Initialize(0, 4, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainTemporaryHP(secondaryValue);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainBlock(block);
    }
}