package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class Essence_Egnaro extends PCLCard
{
    public static final PCLCardData DATA = Register(Essence_Egnaro.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Essence_Egnaro()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1);

        SetAutoplay(true);
        SetExhaust(true);

        this.assetUrl = eatyourbeets.cards.animator.special.Essence_Egnaro.DATA.ImagePath;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(assetUrl, true));
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(1);
        PCLActions.Bottom.GainTemporaryHP(this.magicNumber);
    }
}