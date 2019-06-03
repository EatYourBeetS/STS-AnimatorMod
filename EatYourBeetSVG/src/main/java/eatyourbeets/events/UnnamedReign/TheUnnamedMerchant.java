package eatyourbeets.events.UnnamedReign;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.Utilities;
import eatyourbeets.events.AnimatorEvent;
import eatyourbeets.relics.AncientMedallion;

public class TheUnnamedMerchant extends AnimatorEvent
{
    public static final String ID = CreateFullID(TheUnnamedMerchant.class.getSimpleName());

    private boolean hasMedallion;
    private boolean hasEnoughGold;
    private boolean haggleEnabled;

    private int merchantLine;
    private int sellingPrice;
    private int buyingPrice;

    public TheUnnamedMerchant()
    {
        super(ID, "Merchant.png");

        haggleEnabled = true;
        sellingPrice = 80;
        buyingPrice = 190;
        merchantLine = 1;

        RegisterPhase(1, this::CreatePhase1, this::HandlePhase1);
        RegisterPhase(2, this::CreatePhase2, this::HandlePhase2);
        RegisterPhase(3, this::CreatePhase3, this::HandlePhase3);
        ProgressPhase();
    }

    private void CreatePhase1()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[0], true);
        UpdateDialogOption(0, OPTIONS[6]); // Continue
    }

    private void HandlePhase1(int button)
    {
        ProgressPhase();
    }

    private void CreatePhase2()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[merchantLine], true);

        hasMedallion = AbstractDungeon.player.hasRelic(AncientMedallion.ID);
        hasEnoughGold = AbstractDungeon.player.gold >= buyingPrice;

        if (hasMedallion)
        {
            UpdateDialogOption(0, OPTIONS[0].replace("{0}", String.valueOf(sellingPrice))); // Sell
        }
        else
        {
            UpdateDialogOption(0, OPTIONS[4], true); // Buy
        }

        if (hasEnoughGold)
        {
            UpdateDialogOption(1, OPTIONS[1].replace("{0}", String.valueOf(buyingPrice))); // Buy
        }
        else
        {
            UpdateDialogOption(1, OPTIONS[5], true); // Buy
        }

        haggleEnabled = haggleEnabled && (hasEnoughGold || hasMedallion);
        if (haggleEnabled)
        {
            UpdateDialogOption(2, OPTIONS[2]); // Haggle
            UpdateDialogOption(3, OPTIONS[3]); // Leave
        }
        else
        {
            UpdateDialogOption(2, OPTIONS[3]); // Leave
        }
    }

    private void HandlePhase2(int button)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (button == 0)
        {
            // selling
            AncientMedallion medallion = Utilities.SafeCast(p.getRelic(AncientMedallion.ID), AncientMedallion.class);
            if (medallion != null)
            {
                medallion.setCounter(medallion.counter - 1);
                AbstractDungeon.effectList.add(new RainingGoldEffect(sellingPrice));
                p.gainGold(sellingPrice);
            }

            ProgressPhase();
        }
        else if (button == 1)
        {
            // buying
            p.loseGold(buyingPrice);

            AncientMedallion relic = new AncientMedallion();
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);

            ProgressPhase();
        }
        else if (haggleEnabled && button == 2)
        {
            Haggle();
            ProgressPhase(2);
        }
        else
        {
            this.openMap();
        }
    }

    private void CreatePhase3()
    {
        UpdateBodyText(eventStrings.DESCRIPTIONS[4], true);
        UpdateDialogOption(0, OPTIONS[3]);
    }

    private void HandlePhase3(int button)
    {
        this.openMap();
    }

    private void Haggle()
    {
        if ((Math.abs(sellingPrice - buyingPrice) > 20) && AbstractDungeon.miscRng.randomBoolean())
        {
            sellingPrice += AbstractDungeon.miscRng.random(8, 16);
            buyingPrice -= AbstractDungeon.miscRng.random(8, 16);
            merchantLine = 2;
        }
        else
        {
            sellingPrice -= AbstractDungeon.miscRng.random(8, 16);
            buyingPrice += AbstractDungeon.miscRng.random(8, 16);
            haggleEnabled = false;
            merchantLine = 3;
        }
    }
}