package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.ElricAlphonse_Alt;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.affinity.animatorClassic.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ElricAlphonse extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ElricAlphonse.class).SetSeriesFromClassPackage().SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new ElricAlphonse_Alt(), true);
    }

    public ElricAlphonse()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
        
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

        GameActions.Bottom.MakeCardInDiscardPile(new ElricAlphonse_Alt()).SetUpgrade(upgraded, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.GetPowerAmount(p, IntellectPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainIntellect(1, true);
        }
    }
}