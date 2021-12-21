package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.pcl.colorless.MamizouFutatsuiwa;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class MamizouFutatsuiwa_Daruma extends PCLCard
{
    public static final PCLCardData DATA = Register(MamizouFutatsuiwa_Daruma.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetMultiformData(3, false, false, false, true)
            .SetSeries(MamizouFutatsuiwa.DATA.Series);
    public static final int INDEX_BLOCK = 0;
    public static final int INDEX_BLUR = 1;
    public static final int INDEX_PLATED_ARMOR = 2;

    public static MamizouFutatsuiwa_Daruma GetRandomCard()
    {
        return new MamizouFutatsuiwa_Daruma(rng.random(0, 2), 0);
    }

    public MamizouFutatsuiwa_Daruma()
    {
        this(0, 0);
    }

    public MamizouFutatsuiwa_Daruma(int form, int timesUpgraded)
    {
        super(DATA);

        Initialize(0, 2, 1, 1);
        SetUpgrade(0, 2, 1, 1);

        SetEthereal(true);
        SetPurge(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        this.cardText.OverrideDescription(PCLJUtils.Format(rawDescription, cardData.Strings.EXTENDED_DESCRIPTION[form]), true);
        switch (auxiliaryData.form) {
            case INDEX_BLUR:
                SetAffinity_Green(1);
                break;
            case INDEX_PLATED_ARMOR:
                SetAffinity_Orange(1);
                break;
            default:
                SetAffinity_Red(1);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return auxiliaryData.form == 0 ? super.GetBlockInfo() : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        switch (auxiliaryData.form) {
            case INDEX_BLUR:
                PCLActions.Bottom.GainBlur(magicNumber);
                break;
            case INDEX_PLATED_ARMOR:
                PCLActions.Bottom.GainPlatedArmor(secondaryValue);
                break;
            default:
                PCLActions.Bottom.GainBlock(block);
        }

    }
}