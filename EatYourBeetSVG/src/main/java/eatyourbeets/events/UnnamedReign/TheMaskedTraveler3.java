package eatyourbeets.events.UnnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import patches.AbstractEnums;

public class TheMaskedTraveler3 extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheMaskedTraveler3.class.getSimpleName());

    private static final int PRICE = 150;

    public TheMaskedTraveler3()
    {
        super(ID, "MaskedTraveler.png");

        this.noCardsInRewards = true;

        RegisterPhase(1, this::CreatePhase1, this::HandlePhase1);
        RegisterPhase(2, this::CreatePhase2, this::HandlePhase2);
        ProgressPhase();
    }

    private void CreatePhase1()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[0], true);

        AbstractPlayer p = AbstractDungeon.player;

        if (p.hasRelic(AncientMedallion.ID))
        {
            UpdateDialogOption(0, OPTIONS[0]); // Trade
        }
        else
        {
            UpdateDialogOption(0, OPTIONS[3], true); // Trade
        }

        if (p.gold >= PRICE)
        {
            UpdateDialogOption(1, OPTIONS[1]); // Buy
        }
        else
        {
            UpdateDialogOption(1, OPTIONS[4], true); // Buy
        }

        UpdateDialogOption(2, OPTIONS[2]); // Leave
    }

    private void HandlePhase1(int button)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (button == 0) // Trade
        {
            AncientMedallion medallion = JavaUtilities.SafeCast(p.getRelic(AncientMedallion.ID), AncientMedallion.class);
            if (medallion != null)
            {
                medallion.setCounter(medallion.counter - 1);
                Reward();
            }

            ProgressPhase();
        }
        else if (button == 1) // Buy
        {
            p.loseGold(PRICE);
            Reward();

            ProgressPhase();
        }
        else
        {
            this.openMap();
        }
    }

    private void CreatePhase2()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[1], true);
        UpdateDialogOption(0, OPTIONS[2]); // Leave
    }

    private void HandlePhase2(int button)
    {
        this.openMap();
    }

    private void Reward()
    {
        AbstractDungeon.getCurrRoom().rewards.clear();

        RewardItem rewardItem = new RewardItem(AbstractEnums.Cards.THE_ANIMATOR);
        RandomizedList<AbstractCard> cards = new RandomizedList<>(AbstractDungeon.rareCardPool.group);

        rewardItem.cards.clear();
        for (int i = 0; i < 3; i++)
        {
            AbstractCard card = cards.Retrieve(AbstractDungeon.miscRng).makeCopy();
            card.upgrade();
            rewardItem.cards.add(card);
        }

        AbstractDungeon.getCurrRoom().addCardReward(rewardItem);

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}