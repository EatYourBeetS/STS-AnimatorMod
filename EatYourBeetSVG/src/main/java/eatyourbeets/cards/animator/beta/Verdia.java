package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Verdia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Verdia.class).SetSkill(3, CardRarity.COMMON);

    public Verdia()
    {
        super(DATA);

        Initialize(0, 12, 2, 2);
        SetUpgrade(0, 1, 1, 1);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainPlatedArmor(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ApplyVulnerable(p, m, secondaryValue);
    }
}