package pinacolada.cards.pcl.series.GoblinSlayer;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class ApprenticeCleric extends PCLCard
{
    public static final PCLCardData DATA = Register(ApprenticeCleric.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public ApprenticeCleric()
    {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 3, 1, 0);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.GainInvocation(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        //GameActions.Bottom.GainSupercharge(secondaryValue);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        PCLActions.Bottom.IncreaseScaling(p.hand, BaseMod.MAX_HAND_SIZE, PCLAffinity.Light, 1)
        .SetFilter(c -> (PCLGameUtilities.HasRedAffinity(c) || PCLGameUtilities.HasOrangeAffinity(c) || PCLGameUtilities.HasGreenAffinity(c)) && buffs.getOrDefault(c.uuid, 0) < magicNumber)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLJUtils.IncrementMapElement(buffs, c.uuid, 1);
            }
        });
    }
}