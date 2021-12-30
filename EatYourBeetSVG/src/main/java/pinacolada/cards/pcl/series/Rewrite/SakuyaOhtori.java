package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.resources.GR;
import pinacolada.stances.MightStance;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.UUID;

public class SakuyaOhtori extends PCLCard
{
    public static final PCLCardData DATA = Register(SakuyaOhtori.class).SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Normal).SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public SakuyaOhtori()
    {
        super(DATA);

        Initialize(0, 3, 2, 3);
        SetUpgrade(0, 2, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 1);

        SetHitCount(2);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(hitCount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<hitCount; i++)
        {
            PCLActions.Bottom.GainBlock(block);
        }

        if (PCLGameUtilities.InStance(MightStance.STANCE_ID))
        {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
            buffs = CombatStats.GetCombatData(cardID, null);
            if (buffs == null)
            {
                buffs = new HashMap<>();
                CombatStats.SetCombatData(cardID, buffs);
            }

            PCLActions.Bottom.SelectFromHand(name, 1, false)
                    .SetOptions(false, false, false)
                    .SetMessage(GR.PCL.Strings.HandSelection.GenericBuff)
                    .SetFilter(c -> c instanceof PCLCard && !PCLGameUtilities.IsHindrance(c) && buffs.getOrDefault(c.uuid, 0) < secondaryValue && (c.baseDamage >= 0 || c.baseBlock >= 0))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards)
                        {
                            PCLActions.Bottom.IncreaseScaling(c, PCLAffinity.Blue, secondaryValue);
                            PCLJUtils.IncrementMapElement(buffs, c.uuid, secondaryValue);
                            c.flash();
                        }
                    });

        }
        else
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
            PCLActions.Bottom.ApplyBlinded(TargetHelper.Normal(m), magicNumber);
        }
    }
}