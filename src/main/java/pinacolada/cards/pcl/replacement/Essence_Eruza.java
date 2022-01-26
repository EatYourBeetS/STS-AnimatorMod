package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class Essence_Eruza extends PCLCard
{
    public static final PCLCardData DATA = Register(Essence_Eruza.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Essence_Eruza()
    {
        super(DATA);

        Initialize(0, 3, 0);
        SetUpgrade(0, 1, 0);

        SetAffinity_Star(1);

        SetAutoplay(true);
        SetExhaust(true);

        this.assetUrl = eatyourbeets.cards.animator.special.Essence_Eruza.DATA.ImagePath;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(assetUrl, true));
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainEnergy(1);
        PCLActions.Bottom.GainBlock(block);
    }
}