package eatyourbeets.events.animator;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.events.base.EYBEventPhase;
import eatyourbeets.events.base.EYBEventStrings;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamedMerchant extends EYBEvent
{
    public static final EventStrings STRINGS = new EventStrings();
    public static final String ID = CreateFullID(TheUnnamedMerchant.class);

    public TheUnnamedMerchant()
    {
        super(ID, STRINGS, "Merchant.png");

        RegisterPhase(0, new Introduction());
        RegisterPhase(1, new Offering());
        RegisterPhase(2, new Farewell());
        ProgressPhase();
    }

    private static class Introduction extends EYBEventPhase<TheUnnamedMerchant, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Introduction());
            AddContinueOption();
        }
    }

    private static class Offering extends EYBEventPhase<TheUnnamedMerchant, EventStrings>
    {
        private String merchantLine;
        private boolean haggleEnabled;
        private int sellingPrice;
        private int buyingPrice;

        @Override
        protected void OnEnter()
        {
            if (merchantLine == null)
            {
                merchantLine = text.Offering();
                haggleEnabled = true;
                sellingPrice = 90;
                buyingPrice = 180;
            }

            AddText(merchantLine);

            boolean hasMedallion = AbstractDungeon.player.hasRelic(AncientMedallion.ID);
            boolean hasEnoughGold = AbstractDungeon.player.gold >= buyingPrice;

            if (hasMedallion)
            {
                AddOption(text.SellMedallionOption(sellingPrice)).AddCallback(this::SellMedallion);
            }
            else
            {
                AddOption(text.SellMedallionLockedOption()).SetDisabled(true);
            }

            if (hasEnoughGold)
            {
                AddOption(text.BuyMedallionOption(buyingPrice)).AddCallback(this::BuyMedallion);
            }
            else
            {
                AddOption(text.BuyMedallionLockedOption()).SetDisabled(true);
            }

            haggleEnabled = haggleEnabled && (hasEnoughGold || hasMedallion);
            if (haggleEnabled)
            {
                AddOption(text.HaggleOption()).AddCallback(this::Haggle);
            }

            AddLeaveOption();
        }

        private void SellMedallion()
        {
            AncientMedallion medallion = GameUtilities.GetRelic(AncientMedallion.class);
            if (medallion != null)
            {
                medallion.setCounter(medallion.counter - 1);
                GameEffects.List.Add(new RainingGoldEffect(sellingPrice));
                player.gainGold(sellingPrice);
            }

            MakeNewOffer();
        }

        private void BuyMedallion()
        {
            player.loseGold(buyingPrice);

            AncientMedallion relic = new AncientMedallion();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);

            MakeNewOffer();
        }

        private void Haggle()
        {
            if ((Math.abs(sellingPrice - buyingPrice) > 20) && RNG.randomBoolean())
            {
                sellingPrice += RNG.random(8, 16);
                buyingPrice -= RNG.random(8, 16);
                merchantLine = text.HaggleSuccess();
            }
            else
            {
                sellingPrice -= RNG.random(8, 16);
                buyingPrice += RNG.random(8, 16);
                merchantLine = text.HaggleFail();
                haggleEnabled = false;
            }

            Refresh();
        }

        private void MakeNewOffer()
        {
            sellingPrice = (int)Math.max(1, sellingPrice * 0.75);
            buyingPrice = (int)Math.max(1, buyingPrice * 1.2);
            merchantLine = text.Offering();
            Refresh();
        }
    }

    private static class Farewell extends EYBEventPhase<TheUnnamedMerchant, EventStrings>
    {
        @Override
        protected void OnEnter()
        {
            AddText(text.Farewell());
            AddLeaveOption();
        }
    }

    private static class EventStrings extends EYBEventStrings
    {
        public String Introduction()
        {
            return GetDescription(0);
        }

        public String Offering()
        {
            return GetDescription(1);
        }

        public String HaggleSuccess()
        {
            return GetDescription(2);
        }

        public String HaggleFail()
        {
            return GetDescription(3);
        }

        public String Farewell()
        {
            return GetDescription(4);
        }

        public String SellMedallionOption(int gold)
        {
            return GetOption(0, gold);
        }

        public String BuyMedallionOption(int gold)
        {
            return GetOption(1, gold);
        }

        public String HaggleOption()
        {
            return GetOption(2);
        }

        public String SellMedallionLockedOption()
        {
            return GetOption(3);
        }

        public String BuyMedallionLockedOption()
        {
            return GetOption(4);
        }
    }
}