package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.subscribers.OnEquipUnnamedReignRelicSubscriber;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

public class AncientMedallion extends AnimatorRelic implements OnEquipUnnamedReignRelicSubscriber, OnRelicObtainedSubscriber
{
    public static final String ID = CreateFullID(AncientMedallion.class.getSimpleName());

    private static final int HEAL_AMOUNT = 4;
    private static final int MULTI_UPGRADE = 2;

    private int equipCounter;
    private boolean event;
    private boolean awaitingInput;

    public AncientMedallion()
    {
        this(1);
    }

    public AncientMedallion(boolean event)
    {
        this(1);

        this.event = event;
    }

    public AncientMedallion(int counter)
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);

        this.equipCounter = counter;
    }

    @Override
    public int getPrice()
    {
        return 250;
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], HEAL_AMOUNT * Math.max(1, counter));
    }

    public void onManualEquip()
    {
        if (counter <= 0)
        {
            setCounter(equipCounter);
        }

        if (UpgradeRandomCards())
        {
            OpenUpgradeSelection();
        }
        else
        {
            equipCounter -= 1;
            TriggerEvent();
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (awaitingInput)
        {
            if (UpdateSelection())
            {
                equipCounter -= 1;
                if (equipCounter > 0)
                {
                    onManualEquip();
                }
                else
                {
                    TriggerEvent();
                }
            }
        }
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        if (counter <= 0)
        {
            AbstractDungeon.player.relics.remove(this);
        }
        else
        {
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        AbstractDungeon.player.heal(HEAL_AMOUNT * counter, true);
        this.flash();
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (trigger == OnRelicObtainedSubscriber.Trigger.Equip && relic instanceof AncientMedallion)
        {
            if (this != relic)
            {
                this.equipCounter = ((AncientMedallion) relic).equipCounter;
                this.flash();

                GameEffects.Queue.RemoveRelic(relic).AddCallback(equipCounter, (c, r) -> setCounter(counter + (int)c));
            }

            this.onManualEquip();
        }
    }

    private boolean UpgradeRandomCards()
    {
        RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.canUpgrade())
            {
                upgradableCards.Add(c);
            }
        }

        int upgraded = 0;
        if (upgradableCards.Count() > 0)
        {
            upgraded += 1;
            AbstractCard card1 = upgradableCards.Retrieve(AbstractDungeon.cardRandomRng);
            card1.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card1);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card1.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
        }

        if (upgradableCards.Count() > 0)
        {
            upgraded += 1;
            AbstractCard card1 = upgradableCards.Retrieve(AbstractDungeon.cardRandomRng);
            card1.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card1);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card1.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
        }

        if (upgraded > 0)
        {
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }

        return upgradableCards.Count() > 0;
    }

    private String GetGridSelectMessage()
    {
        return CardCrawlGame.languagePack.getUIString("CampfireSmithEffect").TEXT[0];
    }

    private void OpenUpgradeSelection()
    {
        AbstractPlayer p = AbstractDungeon.player;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (c.canUpgrade())
            {
                group.addToTop(c);
            }
        }

        if (group.size() > 0)
        {
            awaitingInput = true;
        }
        else
        {
            awaitingInput = false;
            return;
        }

        if (!AbstractDungeon.isScreenUp)
        {
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), true, false, false, false);
        }
        else
        {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(group, 1, GetGridSelectMessage(), true, false, false, false);
        }
    }

    private boolean UpdateSelection()
    {
        AbstractCard preview = AbstractDungeon.gridSelectScreen.upgradePreviewCard;
        if (preview != null && !preview.tags.contains(GR.Enums.CardTags.TEMPORARY) && preview.canUpgrade())
        {
            preview.upgrade();
            preview.displayUpgrades();
            preview.tags.add(GR.Enums.CardTags.TEMPORARY);
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            for (int i = 0; i < MULTI_UPGRADE; i++)
            {
                if (c.canUpgrade())
                {
                    c.upgrade();
                }
            }

            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = false;

            GameEffects.Queue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            GameEffects.Queue.ShowCardBriefly(c.makeStatEquivalentCopy());

            return true;
        }

        return false;
    }

    private void TriggerEvent()
    {
        if (event)
        {
            TheUnnamedReign.EnterDungeon();

            event = false;
        }
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

    }
}