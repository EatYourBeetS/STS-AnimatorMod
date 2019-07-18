package eatyourbeets.relics.UnnamedReign;

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
import eatyourbeets.effects.RemoveRelicEffect;
import eatyourbeets.interfaces.AllowedUnnamedReignRelic;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.relics.AnimatorRelic;
import patches.RelicObtainedPatches;

public class AncientMedallion extends AnimatorRelic implements AllowedUnnamedReignRelic
{
    public static final String ID = CreateFullID(AncientMedallion.class.getSimpleName());

    private static final int HEAL_AMOUNT = 4;

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
        if (counter > 0)
        {
            return DESCRIPTIONS[0] + (HEAL_AMOUNT * counter) + DESCRIPTIONS[1];
        }
        else
        {
            return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1];
        }
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

    public static void OnRelicReceived(AbstractRelic relic, RelicObtainedPatches.Trigger trigger)
    {
        if (trigger == RelicObtainedPatches.Trigger.Equip && relic instanceof AncientMedallion)
        {
            AbstractPlayer p = AbstractDungeon.player;
            AncientMedallion ancientMedallion = (AncientMedallion) p.getRelic(AncientMedallion.ID);
            if (ancientMedallion != null)
            {
                if (ancientMedallion != relic)
                {
                    int equipCounter = ((AncientMedallion) relic).equipCounter;
                    AbstractDungeon.effectsQueue.add(new RemoveRelicEffect(ancientMedallion, relic, equipCounter));
                    ancientMedallion.equipCounter = equipCounter;
                    ancientMedallion.flash();
                }

                ancientMedallion.onManualEquip();
            }
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
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = false;

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