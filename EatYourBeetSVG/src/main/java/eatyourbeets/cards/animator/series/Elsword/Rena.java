package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Rena extends AnimatorCard
{
    public static final String ID = Register_Old(Rena.class);
    static
    {
        GetStaticData(ID).InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public Rena()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 3, 0, 2);
        SetUpgrade(0, 3);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainBlur(secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.CreateThrowingKnives(1);

        if (HasSynergy())
        {
            GameActions.Bottom.GainBlur(secondaryValue);
        }
    }
}