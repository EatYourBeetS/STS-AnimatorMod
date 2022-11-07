package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class RollingCubes extends AnimatorRelic
{
    private static final FieldInfo<Boolean> _isBoss = JUtils.GetField("isBoss", RewardItem.class);
    private static final CardGroup tempGroup1 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    private static final CardGroup tempGroup2 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    private static final RewardItem fakeRewardItem = new RewardItem(0, true);
    static
    {
        fakeRewardItem.type = RewardItem.RewardType.CARD;
        fakeRewardItem.cards = new ArrayList<>();
    }

    public static final String ID = CreateFullID(RollingCubes.class);
    public static final int MAX_STORED_USES = 3;
    public static final int USES_PER_ELITE = 1;

    private int lastRerollFloor = -1;

    public RollingCubes()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, USES_PER_ELITE, MAX_STORED_USES);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(0);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);

        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            SetCounter(Math.min(MAX_STORED_USES, counter + USES_PER_ELITE));
            flash();
        }
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        return CanReroll() && !GameUtilities.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD
                && !_isBoss.Get(rewardItem) && !JUtils.Any(rewardItem.cards, c -> c.color == AbstractCard.CardColor.COLORLESS);
    }

    public boolean CanReroll()
    {
        return counter > 0 && AbstractDungeon.floorNum != lastRerollFloor;
    }

    public ArrayList<AbstractCard> Reroll(RewardItem rewardItem, Affinity affinity)
    {
        SetCounter(counter - 1);
        lastRerollFloor = AbstractDungeon.floorNum;

        fakeRewardItem.type = RewardItem.RewardType.CARD;
        fakeRewardItem.cards.clear();
        fakeRewardItem.cards.addAll(rewardItem.cards);

        final ArrayList<AbstractCard> pool = GameUtilities.GetAvailableCards(GenericCondition.FromT2((a, c) -> GameUtilities.HasAffinity(c, a), affinity));
        final ArrayList<AbstractCard> replacement = new ArrayList<>();
        for (AbstractCard c : rewardItem.cards)
        {
            final AbstractCard card = RerollCard(fakeRewardItem, c, pool);
            if (card != null)
            {
                fakeRewardItem.cards.add(card);
                replacement.add(card);
                pool.remove(card);
            }
            else
            {
                replacement.add(new QuestionMark());
            }
        }

        return replacement;
    }

    private AbstractCard RerollCard(RewardItem rewardItem, AbstractCard card, ArrayList<AbstractCard> availableCards)
    {
        if (availableCards.size() > 0)
        {
            final CardGroup g1 = AbstractDungeon.srcCommonCardPool;
            final CardGroup g2 = AbstractDungeon.srcUncommonCardPool;

            tempGroup1.clear();
            tempGroup2.clear();

            for (AbstractCard sc : availableCards)
            {
                if (g1.contains(sc))
                {
                    tempGroup1.addToTop(sc);
                }
                else if (g2.contains(sc))
                {
                    tempGroup2.addToTop(sc);
                }
            }

            AbstractDungeon.srcCommonCardPool = tempGroup1;
            AbstractDungeon.srcUncommonCardPool = tempGroup2;

            final AbstractCard reward = GR.Common.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);

            AbstractDungeon.srcCommonCardPool = g1;
            AbstractDungeon.srcUncommonCardPool = g2;

            return reward;
        }

        return GR.Common.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);
    }
}