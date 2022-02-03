package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.cards.pcl.series.MadokaMagica.SayakaMiki;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class SayakaMiki_Oktavia extends PCLCard
{
    public static final PCLCardData DATA = Register(SayakaMiki_Oktavia.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public SayakaMiki_Oktavia()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void triggerOnPurge() {
        PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public int GetXValue() {
        return PCLJUtils.Count(player.hand.group, c -> c.type == CardType.CURSE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < 1 + GetXValue(); i++) {
            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(),magicNumber);
            PCLActions.Bottom.ApplyFreezing(TargetHelper.Enemies(),secondaryValue);
        }
    }
}
