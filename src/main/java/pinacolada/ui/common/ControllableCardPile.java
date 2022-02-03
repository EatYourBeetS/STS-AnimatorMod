package pinacolada.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.interfaces.subscribers.OnCardMovedSubscriber;
import pinacolada.interfaces.subscribers.OnPurgeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class ControllableCardPile implements OnPhaseChangedSubscriber, OnPurgeSubscriber, OnCardMovedSubscriber
{
    public static PCLCardTooltip TOOLTIP;
    public static final float OFFSET_X = AbstractCard.IMG_WIDTH * 0.85f;
    public static final float OFFSET_Y = -AbstractCard.IMG_WIDTH * 0.1f;
    public static final float TOOLTIP_OFFSET_Y = AbstractCard.IMG_HEIGHT * 0.75f;
    public static final float SCALE = 0.65f;
    public static final float HOVER_TIME_OUT = 0.4F;

    public final ArrayList<ControllableCard> subscribers = new ArrayList<>();
    public boolean isHidden = false;

    protected ControllableCard currentCard;
    protected boolean showPreview;
    protected final GUI_Button cardButton;
    private final Hitbox hb = new Hitbox(144f * Settings.scale, 288 * Settings.scale, 96 * Settings.scale, 96f * Settings.scale);

    public ControllableCardPile()
    {
        TOOLTIP = new PCLCardTooltip(GR.PCL.Strings.Combat.ControlPile, GR.PCL.Strings.Combat.ControlPileDescription);
        cardButton = new GUI_Button(GR.PCL.Images.ControllableCardPile.Texture(), hb)
                .SetBorder(GR.PCL.Images.ControllableCardPileBorder.Texture(), Color.WHITE)
                .SetText("")
                .SetFont(FontHelper.energyNumFontBlue, 1f)
                .SetOnClick(() -> {
                    if (!AbstractDungeon.isScreenUp && currentCard != null && currentCard.CanUse()) {
                        currentCard.Select();
                    }
                })
                .SetOnRightClick(() -> {
                   if (PCLGameUtilities.InBattle() && !AbstractDungeon.isScreenUp) {
                       CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                       for (ControllableCard c : subscribers) {
                           if (c.CanUse()) {
                               cardGroup.addToBottom(c.card);
                               c.card.drawScale = c.card.targetDrawScale = 0.75f;
                           }
                       }
                       if (cardGroup.size() > 0) {
                           PCLActions.Top.SelectFromPile("", 1, cardGroup)
                                   .SetOptions(false, false)
                                   .AddCallback(cards -> {
                                       if (cards.size() > 0) {
                                           ControllableCard co = PCLJUtils.Find(subscribers, c -> c.card == cards.get(0));
                                           if (co != null) {
                                               SetCurrentCard(co);
                                           }
                                       }
                                   });
                       }
                   }
                });
    }

    public void Clear()
    {
        PCLCombatStats.onPhaseChanged.Subscribe(this);
        PCLCombatStats.onPurge.Subscribe(this);
        PCLCombatStats.onCardMoved.Subscribe(this);
        subscribers.clear();
        currentCard = null;
    }

    public ControllableCard Add(AbstractCard card)
    {
        ControllableCard controller = Find(card);
        if (controller == null) {
            controller = new ControllableCard(this, card);
            subscribers.add(controller);
            RefreshCard(controller);
        }
        return controller;
    }

    public void Remove(ControllableCard controller) {
        subscribers.remove(controller);
        RefreshCards();
    }

    public boolean SetCurrentCard(ControllableCard controller) {
        if (controller != null && controller.CanUse()) {
            currentCard = controller;
            return true;
        }
        return false;
    }

    public void SelectNextCard() {
        RefreshCards();
        if (currentCard != null) {
            int startingIndex = subscribers.indexOf(currentCard);
            int index = startingIndex;
            index = (index + 1) % subscribers.size();
            while (index != startingIndex && !SetCurrentCard(subscribers.get(index))) {
                index = (index + 1) % subscribers.size();
            }
        }
    }

    public boolean CanUse(AbstractCard card)
    {
        ControllableCard chosen = PCLJUtils.Find(subscribers, c -> c.card == card);
        return chosen != null && chosen.CanUse();
    }

    public boolean Contains(AbstractCard card)
    {
        return card != null && Find(card) != null;
    }

    public ControllableCard Find(AbstractCard card) {
        return PCLJUtils.Find(subscribers, c -> c.card == card);
    }

    public int GetUsableCount() {
        return PCLJUtils.Count(subscribers, ControllableCard::CanUse);
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        RefreshCards();
    }

    public void Update()
    {
        isHidden = !PCLGameUtilities.InBattle() || subscribers.size() == 0;
        if (!AbstractDungeon.isScreenUp) {
            hb.update();
        }
        if (!isHidden) {
            RefreshCards();
            cardButton.SetText(String.valueOf(GetUsableCount()));
            cardButton.Update();
            showPreview = hb.hovered && !AbstractDungeon.isScreenUp;

            if (showPreview)
            {
                GR.UI.AddPostRender(this::PostRender);
                if (PCLHotkeys.cycle.isJustPressed()) {
                    SelectNextCard();
                }

                if (TOOLTIP != null) {
                    TOOLTIP.description = GR.PCL.Strings.Combat.ControlPileDescriptionFull(PCLHotkeys.cycle.getKeyString());
                    PCLCardTooltip.QueueTooltip(TOOLTIP, hb.x, hb.y + TOOLTIP_OFFSET_Y);
                }
            }

            if (PCLHotkeys.controlPileSelect.isJustPressed()) {
                cardButton.onLeftClick.Complete(cardButton);
            }
            else if (PCLHotkeys.controlPileChange.isJustPressed()) {
                cardButton.onRightClick.Complete(cardButton);
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (!isHidden)
        {
            sb.setColor(Color.WHITE);
            cardButton.Render(sb);
        }
    }

    public void RefreshCards()
    {
        Iterator<ControllableCard> i = subscribers.iterator();
        while (i.hasNext()) {
            ControllableCard controller = i.next();
            if (!RefreshCard(controller)) {
                if (currentCard == controller) {
                    currentCard = null;
                }
                i.remove();
            }
            else if (!controller.CanUse() && currentCard == controller) {
                currentCard = null;
            }
        }

        if (currentCard == null && subscribers.size() > 0) {
            SetCurrentCard(PCLJUtils.Find(subscribers, ControllableCard::CanUse));
        }

        if (currentCard != null && hb.hovered && !AbstractDungeon.isScreenUp) {
            currentCard.card.current_x = currentCard.card.target_x = hb.x + OFFSET_X;
            currentCard.card.current_y = currentCard.card.target_y = hb.y + OFFSET_Y;
            currentCard.card.drawScale = currentCard.card.targetDrawScale = SCALE;
            currentCard.card.fadingOut = false;
        }
    }

    public void PostRender(SpriteBatch sb)
    {
        if (!isHidden && currentCard != null)
        {
            currentCard.Render(sb);
        }
    }


    protected boolean RefreshCard(ControllableCard c)
    {
        final AbstractCard card = c.card;
        if (card == null) {
            return false;
        }

        c.Update();
        if (c.CanUse())
        {
            if (card.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp)
            {
                card.beginGlowing();
            }
            else
            {
                card.stopGlowing();
            }

            card.triggerOnGlowCheck();
            card.applyPowers();
        }

        return true;
    }

    @Override
    public void OnCardMoved(AbstractCard card, CardGroup source, CardGroup destination) {
        PCLActions.Last.Callback(this::RefreshCards);
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        PCLActions.Last.Callback(this::RefreshCards);
    }
}
