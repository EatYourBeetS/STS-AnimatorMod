package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Nazuna extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Nazuna.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);

    public Soujiro_Nazuna()
    {
        super(DATA);

        Initialize(0, 0, 6, 1);
        SetUpgrade(0, 0, 1);

        SetSeries(CardSeries.LogHorizon);
        SetAffinity(0, 1, 0, 1, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1, true));
    }
}