package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Azekura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Azekura.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Azekura()
    {
        super(DATA);

        Initialize(0, 11, 3, 2);
        SetUpgrade(0, 0, -1, 0);

        SetSynergy(Synergies.Katanagatari);
        SetMartialArtist();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainThorns(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainPlatedArmor(secondaryValue);

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            if (c.baseBlock > 0)
            {
                GameUtilities.DecreaseBlock(c, magicNumber, false);
                GameUtilities.Flash(this, Color.RED, false);
            }
        }

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainThorns(secondaryValue);
        }
    }
}