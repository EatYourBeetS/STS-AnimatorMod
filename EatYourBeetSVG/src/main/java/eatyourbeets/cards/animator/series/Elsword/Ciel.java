package eatyourbeets.cards.animator.series.Elsword;

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
    public static final EYBCardData DATA = Register(Ciel.class).SetSkill(2, CardRarity.COMMON);
    static
    {
        DATA.AddPreview(new Lu(), true);
    }

    public Ciel()
    {
        super(DATA);

        Initialize(0, 9, 4, 9);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block).SetVFX(true, false);
        GameActions.Bottom.StackPower(new MarkedPower(m, magicNumber));

        if (HasSynergy())
        {
            for (AbstractCard card : GameUtilities.GetAllInBattleCopies(Lu.DATA.ID))
            {
                card.damage = (card.baseDamage += secondaryValue);
                card.flash();
            }
        }
    }
}