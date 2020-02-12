package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ciel extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ciel.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Lu(), false);
    }

    public Ciel()
    {
        super(DATA);

        Initialize(0, 8, 3, 8);
        SetUpgrade(0, 2, 1, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new VigorPower(p, magicNumber));

        if (HasSynergy())
        {
            for (AbstractCard card : GameUtilities.GetAllCopies(cardData.defaultPreview))
            {
                card.damage = (card.baseDamage += secondaryValue);
                card.flash();
            }
        }
    }
}