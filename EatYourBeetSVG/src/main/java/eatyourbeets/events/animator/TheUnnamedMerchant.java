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
    public static final String ID = CreateFullID(TheUnnamedMerchant.class);

    public TheUnnamedMerchant()
    {
        super(ID, new EventStrings(), "Merchant.png");

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
            SetText(text.Introduction());
            SetOption(text.ContinueOption()).AddCallback(this::ProgressPhase);
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

            SetText(merchantLine);

            boolean hasMedallion = AbstractDungeon.player.hasRelic(AncientMedallion.ID);
            boolean hasEnoughGold = AbstractDungeon.player.gold >= buyingPrice;

            if (hasMedallion)
            {
                SetOption(text.SellMedallionOption(sellingPrice)).AddCallback(this::SellMedallion);
            }
            else
            {
                SetOption(text.SellMedallionLockedOption()).SetDisabled(true);
            }

            if (hasEnoughGold)
            {
                SetOption(text.BuyMedallionOption(buyingPrice)).AddCallback(this::BuyMedallion);
            }
            else
            {
                SetOption(text.BuyMedallionLockedOption()).SetDisabled(true);
            }

            haggleEnabled = haggleEnabled && (hasEnoughGold || hasMedallion);
            if (haggleEnabled)
            {
                SetOption(text.HaggleOption()).AddCallback(this::Haggle);
            }

            SetOption(text.LeaveOption()).AddCallback(this::OpenMap);
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
            if ((Math.abs(sellingPrice - buyingPrice) > 20) && AbstractDungeon.miscRng.randomBoolean())
            {
                sellingPrice += AbstractDungeon.miscRng.random(8, 16);
                buyingPrice -= AbstractDungeon.miscRng.random(8, 16);
                merchantLine = text.HaggleSuccess();
            }
            else
            {
                sellingPrice -= AbstractDungeon.miscRng.random(8, 16);
                buyingPrice += AbstractDungeon.miscRng.random(8, 16);
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
            SetText(text.Farewell());
            SetOption(text.LeaveOption()).AddCallback(this::OpenMap);
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

        public String LeaveOption()
        {
            return GetOption(3);
        }

        public String SellMedallionLockedOption()
        {
            return GetOption(4);
        }

        public String BuyMedallionLockedOption()
        {
            return GetOption(5);
        }

        public String ContinueOption()
        {
            return GetOption(6);
        }
    }
}