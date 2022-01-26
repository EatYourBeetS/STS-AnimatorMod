package pinacolada.cards.pcl.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;

public class Defend extends PCLCard
{
    public static final PCLCardData DATA = Register(Defend.class).SetSkill(1, CardRarity.BASIC, PCLCardTarget.None);

    public Defend()
    {
        super(DATA);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }
}