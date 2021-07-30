package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.beta.BountyMap;

public class ThePharmacy extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(ThePharmacy.class);

    public ThePharmacy()
    {
        super(ID, new EventStrings(),"BubuPharmacy.png");

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Offering());
        RegisterPhase(2, new Bounty());
        RegisterPhase(3, new Offering());
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
        private final int price = 40;


        @Override
        protected void OnEnter()
        {
            if (merchantLine == null)
            {
                merchantLine = text.Offering();
            }

            AddText(merchantLine);

            boolean hasEnoughGold = (player.gold >= price);
            boolean hasBounty = (player.hasRelic(BountyMap.ID));

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

            AbstractPotion potion = AbstractDungeon.returnRandomPotion(false);
            if (player.hasRelic("Sozu")) {
                player.getRelic("Sozu").flash();
            } else {
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
            AddOption(text.RefuseBountyOption()).AddCallback(() -> ChangePhase(2));
        }

        private void AcceptBounty()
        {
            BountyMap relic = new BountyMap();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
            ProgressPhase();
        }
    }

    private static class Accepted extends EYBEventPhase<ThePharmacy, ThePharmacy.EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Accepted());
            AddOption(EYBEvent.COMMON_STRINGS.Continue()).AddCallback(() -> ChangePhase(2));
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

        public final String RefuseBountyOption()
        {
            return GetOption(4);
        }
    }
}