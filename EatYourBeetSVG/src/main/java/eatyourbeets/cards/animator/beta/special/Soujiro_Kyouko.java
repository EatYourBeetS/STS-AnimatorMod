package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Kyouko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Kyouko.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Soujiro_Kyouko()
    {
        super(DATA);

        Initialize(0, 4, 0);
        SetUpgrade(0, 2, 0);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
    }
}