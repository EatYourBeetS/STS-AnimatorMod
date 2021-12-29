package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.stances.MightStance;
import pinacolada.stances.WisdomStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class SakuyaOhtori extends PCLCard
{
    public static final PCLCardData DATA = Register(SakuyaOhtori.class).SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    public SakuyaOhtori()
    {
        super(DATA);

        Initialize(0, 3, 2, 1);
        SetUpgrade(0, 2, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i=0; i<magicNumber; i++)
        {
            PCLActions.Bottom.GainBlock(block);
        }

        if (PCLGameUtilities.InStance(MightStance.STANCE_ID))
        {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
            PCLActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), magicNumber);
        }
        else
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
            Dark dark = null;

            for (AbstractOrb orb : player.orbs)
            {
                dark = PCLJUtils.SafeCast(orb, Dark.class);
                if (PCLJUtils.SafeCast(orb, Dark.class) != null)
                {
                    break;
                }
            }

            if (dark != null)
            {
                dark.evokeAmount *= 2;
            }
        }
    }
}