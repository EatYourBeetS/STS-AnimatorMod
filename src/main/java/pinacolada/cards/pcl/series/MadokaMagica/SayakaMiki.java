package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.SayakaMiki_Oktavia;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class SayakaMiki extends PCLCard
{
    public static final PCLCardData DATA = Register(SayakaMiki.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new SayakaMiki_Oktavia(), true);
            });

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Green(1);
        SetAffinity_Blue(1);
        SetSoul(4, 0, SayakaMiki_Oktavia::new);
    }

    @Override
    public int GetXValue() {
        return PCLCombatStats.MatchingSystem.GetAffinityLevel(PCLAffinity.Blue, true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText(GetXValue(), Settings.CREAM_COLOR);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(GetXValue());
        TrySpendAffinity(PCLAffinity.Blue, GetXValue());
        PCLActions.Bottom.ChannelOrb(new Frost());

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
