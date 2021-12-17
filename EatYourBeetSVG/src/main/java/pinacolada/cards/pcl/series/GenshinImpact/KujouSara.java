package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.utilities.PCLActions;

public class KujouSara extends PCLCard
{
    public static final PCLCardData DATA = Register(KujouSara.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public KujouSara()
    {
        super(DATA);

        Initialize(0, 9, 2, 20);
        SetUpgrade(0, 0, 1);
        SetAffinity_Green(1, 0 ,2);
        SetAffinity_Orange(1, 0, 2);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), PCLAffinity.Green, 1).SetFilter(c -> c.uuid != uuid);

        PCLActions.Last.DiscardFromHand(name, player.hand.size() - 1, true)
                .AddCallback(cards -> {
            if (cards.size() > 0) {
                PCLActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), cards.size() * magicNumber);
            }
        });

        if (IsStarter() && info.TryActivateLimited()) {
            PCLActions.Bottom.Callback(() -> PCLTriggerablePower.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
        }
    }
}