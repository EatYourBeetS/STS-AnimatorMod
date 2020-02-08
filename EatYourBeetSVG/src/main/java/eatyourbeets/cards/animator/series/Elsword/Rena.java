package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Rena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rena.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public Rena()
    {
        super(DATA);

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