package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ElricAlphonseAlt;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorCard
{
    public static final String ID = Register_Old(ElricAlphonse.class);
    static
    {
        GetStaticData(ID).InitializePreview(new ElricAlphonseAlt(), true);
    }

    public ElricAlphonse()
    {
        super(ID, 0, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new ElricAlphonseAlt()).SetOptions(upgraded, false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (GameUtilities.GetPowerAmount(p, IntellectPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainIntellect(1);
        }
    }
}