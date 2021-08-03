package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class AncientMedallion extends AnimatorRelic implements OnEquipUnnamedReignRelicListener, OnRelicObtainedSubscriber
{
    public static final String ID = CreateFullID(AncientMedallion.class);
    public static final int HEAL_AMOUNT = 4;
    public static final int MULTI_UPGRADE = 2;

    private int equipEffects;
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

        this.equipEffects = counter;
    }

    @Override
    public int getPrice()
    {
        return 250;
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, HEAL_AMOUNT * Math.max(1, counter));
    }

    public void onManualEquip()
    {
        if (counter <= 0)
        {
            SetCounter(equipEffects);
        }

        if (UpgradeRandomCards())
        {
            OpenUpgradeSelection();
        }
        else
        {
            equipEffects -= 1;
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
                equipEffects -= 1;
                if (equipEffects > 0)
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
            player.relics.remove(this);
        }
        else
        {
            description = getUpdatedDescription();
            initializeTips();
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        player.heal(HEAL_AMOUNT * counter, true);
        flash();
    }

    public void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        if (trigger == OnRelicObtainedSubscriber.Trigger.Equip && relic instanceof AncientMedallion)
        {
            if (this != relic)
            {
                GameEffects.Queue.RemoveRelic(relic)
                .AddCallback(((AncientMedallion) relic).equipEffects, (c, r) ->
                {
                    flash();
                    equipEffects = c;
                    AddCounter(c);
                    onManualEquip();
                });
            }
            else if (player.getRelic(ID) == this) // First relic with same ID
            {
                onManualEquip();
            }
        }
    }

    private boolean UpgradeRandomCards()
    {
        RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
        for (AbstractCard c : player.masterDeck.group)
        {
            if (c.canUpgrade())
            {
                upgradableCards.Add(c);
            }
        }

        int upgraded = 0;
        if (upgradableCards.Size() > 0)
        {
            upgraded += 1;
            AbstractCard card = upgradableCards.Retrieve(rng);
            card.upgrade();
            player.bottledCardUpgradeCheck(card);
            GameEffects.TopLevelList.ShowCardBriefly(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2f + AbstractCard.IMG_WIDTH / 2f + 20f * Settings.scale, (float) Settings.HEIGHT / 2f);
        }

        if (upgradableCards.Size() > 0)
        {
            upgraded += 1;
            AbstractCard card = upgradableCards.Retrieve(rng);
            card.upgrade();
            player.bottledCardUpgradeCheck(card);
            GameEffects.TopLevelList.ShowCardBriefly(card.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2f - AbstractCard.IMG_WIDTH / 2f - 20f * Settings.scale, (float) Settings.HEIGHT / 2f);
        }

        if (upgraded > 0)
        {
            GameEffects.TopLevelList.Add(new UpgradeShineEffect((float) Settings.WIDTH / 2f, (float) Settings.HEIGHT / 2f));
        }

        return upgradableCards.Size() > 0;
    }

    private String GetGridSelectMessage()
    {
        return CampfireSmithEffect.TEXT[0];
    }

    private void OpenUpgradeSelection()
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : player.masterDeck.group)
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

            player.bottledCardUpgradeCheck(c);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            awaitingInput = false;

            GameEffects.Queue.Add(new UpgradeShineEffect((float) Settings.WIDTH / 2f, (float) Settings.HEIGHT / 2f));
            GameEffects.Queue.ShowCardBriefly(c.makeStatEquivalentCopy());

            return true;
        }

        return false;
    }

    private void TriggerEvent()
    {
        if (event)
        {
            GR.Common.Dungeon.EnterUnnamedReign();
            event = false;
        }
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {

    }
}