package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameActions;

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

        GameActions.Bottom.GainPlatedArmor(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new EarthenThornsPower(p, magicNumber));
    }
}