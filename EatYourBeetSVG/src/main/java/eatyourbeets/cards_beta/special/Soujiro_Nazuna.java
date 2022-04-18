package eatyourbeets.cards_beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards_beta.LogHorizon.Soujiro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Nazuna extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Nazuna.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Nazuna()
    {
        super(DATA);

        Initialize(0, 0, 6, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DrawReduction(1);
    }
}