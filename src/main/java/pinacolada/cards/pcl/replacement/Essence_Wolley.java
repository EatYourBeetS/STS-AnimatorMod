package pinacolada.cards.pcl.replacement;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class Essence_Wolley extends PCLCard
{
    public static final PCLCardData DATA = Register(Essence_Wolley.class)
            .SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Essence_Wolley()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Star(1);

        SetAutoplay(true);
        SetExhaust(true);

        this.assetUrl = eatyourbeets.cards.animator.special.Essence_Wolley.DATA.ImagePath;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(assetUrl, true));
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Draw(this.magicNumber);
        PCLActions.Bottom.DiscardFromHand(this.name, 1, false)
                .SetOptions(false, false, false);
    }
}