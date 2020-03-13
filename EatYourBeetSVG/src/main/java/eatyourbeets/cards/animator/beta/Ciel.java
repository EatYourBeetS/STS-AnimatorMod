package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.MarkedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    private static final Lu LU = new Lu();

    public static final EYBCardData DATA = Register(Ciel.class).SetSkill(2, CardRarity.COMMON);
    static
    {
        DATA.AddPreview(LU, true);
    }

    public Ciel()
    {
        super(DATA);

        Initialize(0, 8, 4, 8);
        SetUpgrade(0, 3, 0, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block).SetVFX(true, false);
        GameActions.Bottom.StackPower(new MarkedPower(m, magicNumber));

        if (HasSynergy())
        {
            for (AbstractCard card : GameUtilities.GetAllCopies(LU))
            {
                card.damage = (card.baseDamage += secondaryValue);
                card.flash();
            }
        }
    }
}