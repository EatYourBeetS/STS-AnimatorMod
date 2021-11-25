package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.beta.BountyMap;
import eatyourbeets.relics.animator.beta.BountyMap2;

public class ThePharmacy extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(ThePharmacy.class);

    public static ThePharmacy TryCreate(Random rng)
    {
        if (!AbstractDungeon.player.hasRelic(BountyMap.ID) && AbstractDungeon.floorNum > 5 && AbstractDungeon.floorNum < 45 && rng.randomBoolean(0.12f)) {
            return new ThePharmacy();
        }
        return null;
    }

    public ThePharmacy()
    {
        super(ID, new EventStrings(),IMAGES.BubuPharmacy.Path());
        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Offering());
        RegisterPhase(2, new Bounty());
        RegisterPhase(3, new Accepted());
        RegisterPhase(4, new Accepted2());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Offering extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        private String merchantLine;
        protected static int price = 25;


        @Override
        protected void OnEnter()
        {
            if (merchantLine == null)
            {
                merchantLine = text.Offering();
            }

            AddText(merchantLine);

            boolean hasEnoughGold = (player.gold >= price);
            boolean hasBounty = player.hasRelic(BountyMap.ID) || player.hasRelic(BountyMap2.ID);

            if (!hasBounty) {
                AddOption(text.InquireBountyOption()).AddCallback(this::ProgressPhase);
            }

            if (hasEnoughGold)
            {
                AddOption(text.BuyPotionOption(price)).AddCallback(this::BuyPotion);
            }
            else
            {
                AddOption(text.BuyPotionLockedOption()).SetDisabled(true);
            }
            AddLeaveOption();
        }

        private void BuyPotion()
        {
            player.loseGold(price);
            price += 10;

            AbstractPotion potion = AbstractDungeon.returnRandomPotion(false);
            if (player.hasRelic("Sozu")) {
                player.getRelic("Sozu").flash();
            }
            else {
                player.obtainPotion(potion);
            }
            CardCrawlGame.metricData.addPotionObtainData(potion);
            merchantLine = text.Offering2();
            Refresh();
        }
    }

    private static class Bounty extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Bounty());
            AddOption(text.AcceptBountyOption()).AddCallback(this::AcceptBounty);
            AddOption(text.AcceptBountyOption2()).AddCallback(this::AcceptBounty2);
            AddOption(text.RefuseBountyOption()).AddCallback(() -> ChangePhase(Offering.class));
        }

        private void AcceptBounty()
        {
            BountyMap relic = new BountyMap();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            ProgressPhase();
        }

        private void AcceptBounty2()
        {
            BountyMap2 relic = new BountyMap2();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            ChangePhase(Accepted2.class);
        }
    }

    private static class Accepted extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Accepted());
            AddOption(EYBEvent.COMMON_STRINGS.Continue()).AddCallback(() -> ChangePhase(Offering.class));
        }
    }

    private static class Accepted2 extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Accepted());
            AddOption(EYBEvent.COMMON_STRINGS.Continue()).AddCallback(() -> ChangePhase(Offering.class));
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public final String Introduction()
        {
            return GetDescription(0);
        }

        public final String Offering()
        {
            return GetDescription(1);
        }

        public final String Offering2()
        {
            return GetDescription(2);
        }

        public final String Bounty()
        {
            return GetDescription(3);
        }

        public final String Accepted()
        {
            return GetDescription(4);
        }

        public final String Accepted2()
        {
            return GetDescription(5);
        }

        public final String InquireBountyOption()
        {
            return GetOption(0);
        }

        public final String BuyPotionOption(int gold)
        {
            return GetOption(1, gold);
        }

        public final String BuyPotionLockedOption()
        {
            return GetOption(2);
        }

        public final String AcceptBountyOption()
        {
            return GetOption(3);
        }

        public final String AcceptBountyOption2()
        {
            return GetOption(4);
        }

        public final String RefuseBountyOption()
        {
            return GetOption(5);
        }
    }
}