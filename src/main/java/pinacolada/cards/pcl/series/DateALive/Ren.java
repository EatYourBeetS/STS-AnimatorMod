package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Ren extends PCLCard
{
    public static final PCLCardData DATA = Register(Ren.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Dark), true));
    public static final int THRESHOLD = 3;


    public Ren()
    {
        super(DATA);

        Initialize(0, 2, 3, 2);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetEthereal(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.Callback(() -> {
           if (PCLGameUtilities.GetOrbCount(Dark.ORB_ID) == 0) {
               PCLActions.Bottom.ChannelOrb(new Dark()).AddCallback(() -> DoAction(m));
           }
           else {
               PCLActions.Bottom.TriggerOrbPassive(1, false, false).SetFilter(o -> Dark.ORB_ID.equals(o.ID)).AddCallback(() -> DoAction(m));
           }
        });
    }

    protected void DoAction(AbstractMonster m) {
        int darkCount = PCLGameUtilities.GetOrbCount(Dark.ORB_ID);
        PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(m), darkCount * magicNumber);

        if (darkCount >= THRESHOLD && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.ObtainAffinityToken(PCLAffinity.Dark, upgraded);
        }
    }
}