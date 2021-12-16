package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.subscribers.OnCardMovedSubscriber;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import patches.energyPanel.EnergyPanelPatches;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: Move to a different folder, make this a Screen
public class ControllableCardPile implements OnPhaseChangedSubscriber, OnPurgeSubscriber, OnCardMovedSubscriber
{
    public static EYBCardTooltip TOOLTIP;
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
        TOOLTIP = new EYBCardTooltip(GR.Animator.Strings.Combat.ControlPile, GR.Animator.Strings.Combat.ControlPileDescription);
        cardButton = new GUI_Button(GR.Common.Images.ControllableCardPile.Texture(), hb)
                .SetBorder(GR.Common.Images.ControllableCardPileBorder.Texture(), Color.WHITE)
                .SetText("")
                .SetFont(FontHelper.energyNumFontBlue, 1f)
                .SetOnClick(() -> {
                    if (currentCard != null && currentCard.CanUse()) {
                        currentCard.Select();
                    }
                })
                .SetOnRightClick(() -> {
                   if (GameUtilities.InBattle()) {
                       CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                       for (ControllableCard c : subscribers) {
                           if (c.CanUse()) {
                               cardGroup.addToBottom(c.card);
                           }
                       }
                       if (cardGroup.size() > 0) {
                           GameActions.Top.SelectFromPile("", 1, cardGroup)
                                   .SetOptions(false, false)
                                   .AddCallback(cards -> {
                                       if (cards.size() > 0) {
                                           ControllableCard co = JUtils.Find(subscribers, c -> c.card == cards.get(0));
                                           if (co != null) {
                                               SetCurrentCard(co);
                                           }
                                       }
                                   });
                       }
                   }
                });
        ;
    }

    public void Clear()
    {
        CombatStats.onPhaseChanged.Subscribe(this);
        CombatStats.onPurge.Subscribe(this);
        CombatStats.onCardMoved.Subscribe(this);
        EnergyPanelPatches.Pile = this;
        subscribers.clear();
        currentCard = null;
    }

    public ControllableCard Add(AbstractCard card)
    {
        ControllableCard controller = new ControllableCard(this, card);
        subscribers.add(controller);
        RefreshCard(controller);
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
        ControllableCard chosen = JUtils.Find(subscribers, c -> c.card == card);
        return chosen != null && chosen.CanUse();
    }

    public boolean Contains(AbstractCard card)
    {
        return card != null && JUtils.Find(subscribers, c -> c.card == card) != null;
    }

    public int GetUsableCount() {
        return JUtils.Count(subscribers, ControllableCard::CanUse);
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        RefreshCards();
    }

    public void Update(EnergyPanel panel)
    {
        isHidden = !GameUtilities.InBattle() && subscribers.size() == 0;
        hb.update();
        if (!isHidden) {
            RefreshCards();
            cardButton.SetText(String.valueOf(GetUsableCount()));
            cardButton.Update();
            showPreview = hb.hovered && !AbstractDungeon.isScreenUp;

            if (showPreview)
            {
                GR.UI.AddPostRender(this::PostRender);
                if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
                    SelectNextCard();
                }

                if (TOOLTIP != null) {
                    EYBCardTooltip.QueueTooltip(TOOLTIP, hb.x, hb.y + TOOLTIP_OFFSET_Y);
                }
            }
        }
    }

    public void Render(EnergyPanel panel, SpriteBatch sb)
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
            SetCurrentCard(JUtils.Find(subscribers, ControllableCard::CanUse));
        }

        if (currentCard != null) {
            currentCard.card.target_x = hb.x + OFFSET_X;
            currentCard.card.target_y = hb.y + OFFSET_Y;
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
        GameActions.Last.Callback(this::RefreshCards);
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        GameActions.Last.Callback(this::RefreshCards);
    }
}
