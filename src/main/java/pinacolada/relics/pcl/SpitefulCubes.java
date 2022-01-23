package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class SpitefulCubes extends AbstractCubes
{
    private static final CardGroup tempGroup1 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    private static final CardGroup tempGroup2 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    private static final CardGroup tempGroup3 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

    public static final String ID = CreateFullID(SpitefulCubes.class);
    public static final int MAX_STORED_USES = 3;
    public static final int USES_PER_ELITE = 3;
    public static final int USES_PER_NORMAL = 0;

    public SpitefulCubes()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID, USES_PER_NORMAL, USES_PER_ELITE, MAX_STORED_USES);
    }

    public AbstractCard GetReward(AbstractCard card, RewardItem rewardItem) {
        final PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c != null && c.series != null)
        {
            final PCLRuntimeLoadout loadout = GR.PCL.Dungeon.GetLoadout(c.series);
            if (loadout != null)
            {
                final CardGroup g1 = AbstractDungeon.srcCommonCardPool;
                final CardGroup g2 = AbstractDungeon.srcUncommonCardPool;
                final CardGroup g3 = AbstractDungeon.srcRareCardPool;

                tempGroup1.clear();
                tempGroup2.clear();
                tempGroup3.clear();

                for (AbstractCard sc : loadout.GetCardPoolInPlay().values())
                {
                    if (g1.contains(sc))
                    {
                        tempGroup1.addToTop(sc);
                    }
                    else if (g2.contains(sc))
                    {
                        tempGroup2.addToTop(sc);
                    }
                    else if (g3.contains(sc))
                    {
                        tempGroup3.addToTop(sc);
                    }
                }

                AbstractDungeon.srcCommonCardPool = tempGroup1;
                AbstractDungeon.srcUncommonCardPool = tempGroup2;
                AbstractDungeon.srcRareCardPool = tempGroup3;

                final AbstractCard reward = GR.PCL.Dungeon.GetRandomRewardCard(rewardItem.cards, true,true);

                AbstractDungeon.srcCommonCardPool = g1;
                AbstractDungeon.srcUncommonCardPool = g2;
                AbstractDungeon.srcRareCardPool = g3;

                return reward;
            }
        }
        return super.GetReward(card, rewardItem);
    }

    @Override
    public void obtain()
    {
        ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            AbstractCubes relic = PCLJUtils.SafeCast(relics.get(i), AbstractCubes.class);
            if (relic != null)
            {
                instantObtain(player, i, true);
                setCounter(relic.counter);
                return;
            }
        }

        super.obtain();
    }
}