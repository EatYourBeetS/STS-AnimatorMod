package eatyourbeets.events.replacement;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.DrugDealer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.MutagenicStrength;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.animator.special.Special_Refrain;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AnimatorDrugDealer extends AbstractImageEvent
{
    public static final String ID = GR.Animator.CreateID(com.megacrit.cardcrawl.events.city.DrugDealer.ID);
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private int screenNum = 0;
    private boolean cardsSelected = false;

    public AnimatorDrugDealer()
    {
        super(NAME, DESCRIPTIONS[0], "images/events/drugDealer.jpg");
        this.imageEventText.setDialogOption(OPTIONS[0], CardLibrary.getCopy(Special_Refrain.DATA.ID));
        if (AbstractDungeon.player.masterDeck.getPurgeableCards().size() >= 2)
        {
            this.imageEventText.setDialogOption(OPTIONS[1]);
        }
        else
        {
            this.imageEventText.setDialogOption(OPTIONS[4], true);
        }

        this.imageEventText.setDialogOption(OPTIONS[2], new MutagenicStrength());
    }

    protected void buttonEffect(int buttonPressed)
    {
        switch (this.screenNum)
        {
            case 0:
                switch (buttonPressed)
                {
                    case 0:
                        AbstractCard jax = new Special_Refrain();
                        logMetricObtainCard("Drug Dealer", "Obtain J.A.X.", jax);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(jax, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.transform();
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        AbstractRelic r;
                        if (!AbstractDungeon.player.hasRelic(MutagenicStrength.ID))
                        {
                            r = new MutagenicStrength();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, r);
                        }
                        else
                        {
                            r = new Circlet();
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, r);
                        }

                        logMetricObtainRelic("Drug Dealer", "Inject Mutagens", r);
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    default:
                        JUtils.LogInfo(this, "ERROR: Unhandled case " + buttonPressed);
                }

                this.screenNum = 1;
                break;
            case 1:
                this.openMap();
        }

    }

    public void update()
    {
        super.update();
        if (!this.cardsSelected)
        {
            ArrayList<String> transformedCards = new ArrayList<>();
            ArrayList<String> obtainedCards = new ArrayList<>();
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2)
            {
                this.cardsSelected = true;
                float displayCount = 0.0F;

                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    card.untip();
                    card.unhover();
                    transformedCards.add(card.cardID);
                    AbstractDungeon.player.masterDeck.removeCard(card);
                    AbstractDungeon.transformCard(card, false, AbstractDungeon.miscRng);
                    AbstractCard c = AbstractDungeon.getTransformedCard();
                    if (AbstractDungeon.screen != CurrentScreen.TRANSFORM && c != null)
                    {
                        obtainedCards.add(c.cardID);
                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(c.makeCopy(), (float) Settings.WIDTH / 3.0F + displayCount, (float) Settings.HEIGHT / 2.0F, false));
                        displayCount += (float) Settings.WIDTH / 6.0F;
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                logMetricTransformCards("Drug Dealer", "Became Test Subject", transformedCards, obtainedCards);
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }
        }
    }

    private void transform()
    {
        if (!AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 2, OPTIONS[5], false, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 2, OPTIONS[5], false, false, false, false);
        }
    }

    static
    {
        eventStrings = GR.GetEventStrings(ID);
        final EventStrings original = CardCrawlGame.languagePack.getEventString(DrugDealer.ID);
        NAME = original.NAME;
        DESCRIPTIONS = original.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
