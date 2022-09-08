package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.SpiritPoop;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.card.HideCardEffect;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.relics.animator.RollingCubes;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class AnimatorCardRewardReroll extends GUIElement
{
    protected final ArrayList<GUI_Button> rerollButtons;
    protected final ActionT1<AbstractCard> onCardReroll;
    protected final ActionT1<AbstractCard> onCardAdded;

    protected RollingCubes rollingCubes;
    protected RewardItem rewardItem;
    protected float interactableTime;
    protected boolean canReroll;

    public AnimatorCardRewardReroll(ActionT1<AbstractCard> onCardAdded, ActionT1<AbstractCard> onCardReroll)
    {
        this.onCardReroll = onCardReroll;
        this.onCardAdded = onCardAdded;
        this.rerollButtons = new ArrayList<>();
    }

    public void Open(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        this.rewardItem = rItem;
        this.rollingCubes = GameUtilities.GetRelic(RollingCubes.ID);
        this.isActive = canReroll = (rollingCubes != null && rollingCubes.CanActivate(rItem));

        if (rerollButtons.isEmpty())
        {
            final int max = Affinity.Basic().length;
            for (int i = 0; i < max; i++)
            {
                final Affinity affinity = Affinity.Basic()[i];
                final GUI_Button button = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), 0, 0)
                .SetForeground(GR.Common.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetDimensions(AbstractCard.IMG_WIDTH * 0.55f, AbstractCard.IMG_HEIGHT * 0.175f)
                .SetColor(Color.GRAY)
                .SetText(GR.Animator.Strings.Rewards.Reroll + " [" + affinity.GetTooltip().id + "]", true)
                .SetOnClick(affinity, (a, __) -> Reroll(a))
                .SetPosition(ScreenW(0.5f) + (AbstractCard.IMG_WIDTH * 0.6f * Mathf.GetOffsetFromCenter(i, max)), ScreenH(0.8f));
                rerollButtons.add(button);
            }
        }
    }

    public void Close()
    {
        SetActive(false);
        canReroll = false;
    }

    public void Reroll(Affinity affinity)
    {
        final ArrayList<AbstractCard> replacement = rollingCubes.Reroll(rewardItem, affinity);
        if (replacement.size() > 0)
        {
            SFX.Play(SFX.CARD_SELECT);

            for (int i = 0; i < replacement.size(); i++)
            {
                final AbstractCard toAdd = replacement.get(i);
                final AbstractCard toRemove = rewardItem.cards.get(i);

                if (toRemove.cardID.equals(HigakiRinne.DATA.ID))
                {
                    SFX.Play(HigakiRinneAction.GetRandomSFX(), 0.6f, 1.6f);
                    GameEffects.TopLevelList.SpawnRelic(new SpiritPoop(), InputHelper.mX, InputHelper.mY);
                }

                GameEffects.TopLevelList.Add(new ExhaustCardEffect(toRemove));
                GameEffects.TopLevelList.Add(new HideCardEffect(toRemove));
                OnCardReroll(toRemove);

                GameUtilities.CopyVisualProperties(toAdd, toRemove);
                rewardItem.cards.set(i, toAdd);
                OnCardAdded(toAdd);
            }
        }

        SetActive(rollingCubes.CanReroll());
        canReroll = isActive;
    }

    @Override
    public void Update()
    {
        if (AbstractDungeon.dynamicBanner != null)
        {
            AbstractDungeon.dynamicBanner.y = ScreenH(0.8f);
        }

        if (canReroll && rewardItem.cards.size() > 0)
        {
//            final float time = GR.UI.Time();
//            boolean cardHovered = false;
//            for (AbstractCard c : rewardItem.cards)
//            {
//                if (c.hb.hovered)
//                {
//                    cardHovered = true;
//                    interactableTime = time;
//                    break;
//                }
//            }

            for (GUI_Button button : rerollButtons)
            {
//                if (time > (interactableTime + 0.25f))
//                {
//                    button.interactable = true;
//                }
//
//                if (cardHovered)
//                {
//                    button.interactable = false;
//                }

                button.SetPosition(button.hb.cX, rewardItem.cards.get(0).current_y + (AbstractCard.IMG_HEIGHT * 0.615f)).Update();
            }
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (canReroll)
        {
            for (GUI_Button button : rerollButtons)
            {
                button.TryRender(sb);
            }
        }
    }

    private void OnCardReroll(AbstractCard card)
    {
        if (onCardReroll != null)
        {
            onCardReroll.Invoke(card);
        }
    }

    private void OnCardAdded(AbstractCard card)
    {
        if (onCardAdded != null)
        {
            onCardAdded.Invoke(card);
        }
    }
}
