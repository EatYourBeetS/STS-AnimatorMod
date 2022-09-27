package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.Projectile;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.Collections;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class EYBCardAffinitySystem extends GUIElement implements OnStartOfTurnSubscriber
{
    public static final int BASE_COST = 2;

    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public final ArrayList<EYBCardAffinityRow> Rows = new ArrayList<>();
    public final EYBCardAffinities BaseAffinities = new EYBCardAffinities(null);
    public final EYBCardAffinities CurrentAffinities = new EYBCardAffinities(null);

    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Button info_button;
    protected final GUI_Button lock_button;
    protected RotatingList<String> tooltipMessages;
    protected EYBCardTooltip info_tooltip;
    protected EYBCardTooltip lock_tooltip;
    protected Vector2 savedPosition;
    protected boolean canUseAffinities;
    protected boolean holdingShift;

    public EYBCardAffinitySystem()
    {
        canUseAffinities = true;

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.43f), Scale(80f), Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
        .SetColor(Colors.White(0.75f));

        tooltipMessages = new RotatingList<>(GR.Animator.Strings.Tutorial.GetStrings());
        info_tooltip = new EYBCardTooltip("Animator Info", "");
        lock_tooltip = new EYBCardTooltip("Lock Affinity", "");
        UpdateInfoTooltip(false);

        info_button = new GUI_Button(GR.Common.Images.Panel_Rounded.Texture(), new RelativeHitbox(hb, Scale(60f), Scale(30f), Scale(120f), Scale(20f), false))
        .SetFont(EYBFontHelper.CardTooltipFont, 1f)
        .SetColor(new Color(0.1f, 0.1f, 0.1f, 0.6f))
        .SetTextColor(new Color(1f, 1f, 0.8f, 1f))
        .SetText("info")
        .SetTooltip(info_tooltip, false)
        .SetOnClick(() -> UpdateInfoTooltip(true));

        for (Affinity type : Affinity.Basic())
        {
            Rows.add(new EYBCardAffinityRow(this, type, Rows.size()));
        }

        lock_button = new GUI_Button(GR.Common.Images.Panel_Elliptical_Half_H.Texture(), new RelativeHitbox(hb, 1, 1, 1.55f, -0.5f -(Rows.size() * 0.975f)))
        .SetFont(EYBFontHelper.CardTooltipFont, 1f)
        .SetColor(new Color(0.05f, 0.05f, 0.05f, 1f))
        .SetTooltip(lock_tooltip, false);
        lock_button.SetOnRightClick(() ->
        {
            boolean hold = GR.Animator.Config.HoldShiftToLockAffinities.Toggle(true);
            lock_tooltip.SetText(lock_tooltip.title, GR.Animator.Strings.Affinities.LockMessage(hold));
        });
        lock_button.SetOnClick(() -> UnlockAffinities(!canUseAffinities));

        Rows.add(new EYBCardAffinityRow(this, Affinity.Sealed, Rows.size()));
    }

    public EYBCardAffinities GetAffinities(Iterable<AbstractCard> cards, boolean unsealed)
    {
        final EYBCardAffinities affinities = new EYBCardAffinities(null);
        for (AbstractCard c : cards)
        {
            final EYBCard card = JUtils.SafeCast(c, EYBCard.class);
            if (card != null && (!unsealed || !card.affinities.sealed))
            {
                affinities.Add(card.affinities, 1);
            }
        }

        return affinities;
    }

    public EYBCardAffinities GetPlayerAffinities()
    {
        return CurrentAffinities;
    }

    public boolean TryUseAffinity(Affinity affinity, int amount)
    {
        if (affinity == Affinity.Star)
        {
            for (Affinity a : Affinity.Basic())
            {
                if (CurrentAffinities.GetLevel(a) < amount)
                {
                    return false;
                }
            }

            for (Affinity a : Affinity.Basic())
            {
                CurrentAffinities.Set(a, CurrentAffinities.GetLevel(a) - amount);
            }

            return true;
        }
        else
        {
            final int level = CurrentAffinities.GetLevel(affinity);
            if (level < amount)
            {
                return false;
            }

            CurrentAffinities.Add(affinity, -amount);

            return true;
        }
    }

    public int UseAffinity(Affinity affinity, int amount)
    {
        int totalUsed = 0;
        if (affinity == Affinity.Star)
        {
            for (Affinity a : Affinity.Basic())
            {
                totalUsed += UseAffinity(a, amount);
            }
        }
        else
        {
            final int level = CurrentAffinities.GetLevel(affinity);
            final int toUse = Mathf.Min(level, amount);
            if (toUse > 0)
            {
                CurrentAffinities.Set(affinity, level - toUse);
                totalUsed = toUse;
            }
        }

        return totalUsed;
    }

    public boolean CanUseAffinities()
    {
        return canUseAffinities;
    }

    public int GetRemainingSealUses()
    {
        return CurrentAffinities.GetLevel(Affinity.Sealed);
    }

    public void AddAffinitySealUses(int uses)
    {
        final int remainingUses = GetRemainingSealUses();
        if (remainingUses < 3)
        {
            uses = (Mathf.Clamp(remainingUses + uses, 0, 3));
            CurrentAffinities.Set(Affinity.Sealed, uses);
            BaseAffinities.Set(Affinity.Sealed, uses);
        }
    }

    public void AddAffinity(Affinity affinity, int amount)
    {
        if (affinity == Affinity.Star || affinity == Affinity.General)
        {
            for (Affinity a : Affinity.Basic())
            {
                AddAffinity(a, amount);
            }
        }
        else if (affinity == Affinity.Sealed)
        {
            throw new RuntimeException("Do not use AddAffinity() for Affinity.Sealed, use AddAffinitySealUses() instead.");
        }
        else
        {
            amount = CombatStats.OnAffinityGained(affinity, amount);
            CurrentAffinities.Add(affinity, amount);
            BaseAffinities.Add(affinity, amount);
        }
    }

    public void AddTempAffinity(Affinity affinity, int amount)
    {
        if (affinity == Affinity.Star || affinity == Affinity.General)
        {
            for (Affinity a : Affinity.Basic())
            {
                AddTempAffinity(a, amount);
            }
        }
        else if (affinity == Affinity.Sealed)
        {
            throw new RuntimeException("Do not use AddTempAffinity() for Affinity.Sealed, use AddAffinitySealUses() instead.");
        }
        else
        {
            CurrentAffinities.Add(affinity, amount);
        }
    }

    public int GetUsableAffinity(Affinity affinity)
    {
        if (affinity == Affinity.Star || affinity == Affinity.General)
        {
            EYBCardAffinity min = JUtils.FindMin(JUtils.Filter(CurrentAffinities.List, ac -> ac.type.ID >= 0), af -> af.level);
            return min != null ? min.level : 0;
        }

        return CurrentAffinities.GetLevel(affinity);
    }

    public EYBCardAffinityRow GetRow(Affinity affinity)
    {
        for (EYBCardAffinityRow row : Rows)
        {
            if (row.Type == affinity)
            {
                return row;
            }
        }

        return null;
    }

    public AbstractAffinityPower GetPower(Affinity affinity)
    {
        for (AbstractAffinityPower p : Powers)
        {
            if (p.affinity.equals(affinity))
            {
                return p;
            }
        }

        return null;
    }

    public int GetPowerAmount(Affinity affinity)
    {
        final AbstractAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.GetPlayerClass() == GR.Animator.PlayerClass ? p.GetThresholdLevel() : p.amount;
    }

    @Override
    public void OnStartOfTurn()
    {
        AddAffinitySealUses(1);

        for (EYBCardAffinityRow row : Rows)
        {
            final int value = Mathf.Min(BaseAffinities.GetLevel(row.Type), CurrentAffinities.GetLevel(row.Type));
            CurrentAffinities.Set(row.Type, value);
            BaseAffinities.Set(row.Type, value);

            row.OnStartOfTurn();
        }
    }

    public int GetLastAffinityLevel(Affinity affinity)
    {
        final EYBCard lastCardPlayed = GetLastCardPlayed();
        return lastCardPlayed == null ? 0 : GameUtilities.GetAffinityLevel(lastCardPlayed, affinity, true);
    }

    public EYBCard GetLastCardPlayed()
    {
        return CardSeries.GetLastCardPlayed();
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        return card.type != AbstractCard.CardType.ATTACK ? ApplyScaling(Affinity.Star, card, block) : block;
    }

    public float ModifyDamage(float damage, EYBCard card)
    {
        return card.type == AbstractCard.CardType.ATTACK ? ApplyScaling(Affinity.Star, card, damage) : damage;
    }

    public float ApplyScaling(Affinity affinity, EYBCard card, float base)
    {
        if (affinity == Affinity.Star)
        {
            for (AbstractAffinityPower p : Powers)
            {
                base = p.ApplyScaling(card, base);
            }

            return base;
        }

        return GetPower(affinity).ApplyScaling(card, base);
    }

    public void Seal(EYBCardAffinities affinities, boolean reshuffle)
    {
        if (affinities.sealed)
        {
            return;
        }

        if (reshuffle)
        {
            final int seal = CurrentAffinities.GetLevel(Affinity.Sealed);
            if (seal <= 0)
            {
                return;
            }
            CurrentAffinities.Set(Affinity.Sealed, seal - 1);
        }

        affinities.sealed = true;
        SFX.Play(SFX.RELIC_ACTIVATION, 0.75f, 0.85f, reshuffle ? 0.95f : 0.75f);

        final ArrayList<Affinity> list = new ArrayList<>();
        if (affinities.HasStar())
        {
            Collections.addAll(list, Affinity.Basic());
        }
        else
        {
            for (EYBCardAffinity a : affinities.List)
            {
                if (a.level > 0)
                {
                    list.add(a.type);
                }
            }
        }

        final Hitbox hb = affinities.Card.hb;
        final float offset_y = AbstractCard.IMG_HEIGHT * 0.525f;
        for (int i = 0; i < list.size(); i++)
        {
            final Affinity a = list.get(i);
            final float offset_x = Mathf.GetOffsetFromCenter(i, list.size()) * 36;
            final Hitbox targetHB = CombatStats.Affinities.GetRow(a).image_affinity.hb;
            final Projectile p = new Projectile(a.GetIcon(), 48, 48).SetPosition(hb.cX + offset_x, hb.cY + offset_y);
            GameEffects.TopLevelQueue.Add(VFX.ThrowProjectile(p, CombatStats.Affinities.GetRow(a).image_affinity.hb))
            .SetDuration(Mathf.Abs(targetHB.cX - hb.cX) / (Settings.WIDTH * 0.5f), false);

            GetRow(a).Seal(affinities, reshuffle);
        }

        CombatStats.OnAffinitySealed(affinities, reshuffle);
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        final boolean classic = !GR.Animator.IsSelected();

        BaseAffinities.Clear();
        CurrentAffinities.Clear();

        CombatStats.onStartOfTurn.ToggleSubscription(this, !classic);

        if (savedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) dragPanel_image.hb;
            savedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            savedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (savedPosition.dst2(GR.Animator.Config.AffinitySystemPosition.Get()) > Mathf.Epsilon)
            {
                JUtils.LogInfo(this, "Saved affinity panel position.");
                GR.Animator.Config.AffinitySystemPosition.Set(savedPosition.cpy(), true);
            }
        }

        Powers.clear();
        for (Affinity affinity : Affinity.Basic())
        {
            Powers.add(AbstractAffinityPower.CreatePower(affinity, classic));
        }

        if (classic)
        {
            canUseAffinities = true;
        }
        else
        {
            UnlockAffinities(holdingShift != canUseAffinities);
            holdingShift = false;
        }

        for (EYBCardAffinityRow row : Rows)
        {
            row.Initialize();
            BaseAffinities.SetRequirement(row.Type, BASE_COST);
        }
    }

    public void Update()
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        if (savedPosition == null)
        {
            savedPosition = GR.Animator.Config.AffinitySystemPosition.Get(new Vector2(0.0522f, 0.43f)).cpy();
            hb.SetPosition(ScreenW(savedPosition.x), ScreenH(savedPosition.y));
        }

        boolean draggingCard = false;
        EYBCard hoveredCard = null;
        if (player.hoveredCard != null)
        {
            if ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode)
            {
                draggingCard = true;
            }
            if (player.hoveredCard instanceof EYBCard)
            {
                hoveredCard = (EYBCard) player.hoveredCard;
            }
        }

        for (EYBCardAffinityRow row : Rows)
        {
            row.Update(hoveredCard, draggingCard);
        }

        if (AbstractDungeon.isScreenUp || CardCrawlGame.isPopupOpen)
        {
            return;
        }

        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }

        dragPanel_image.Update();
        draggable_icon.Update();
        info_button.ShowTooltip(!draggingCard).Update();
        lock_button.ShowTooltip(!draggingCard).Update();

        if (!draggingCard && GameUtilities.CanAcceptInput(true))
        {
            if (hoveredCard != null && InputManager.RightClick.IsJustPressed() && CurrentAffinities.GetLevel(Affinity.Sealed) > 0)
            {
                if (GameUtilities.CanSeal(hoveredCard))
                {
                    GameActions.Bottom.SealAffinities(hoveredCard, true);
                }
                else
                {
                    GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3, GR.Animator.Strings.Misc.CannotSeal, true));
                }
            }

            if (InputManager.Shift.IsJustPressed())
            {
                SFX.Play(SFX.UI_CLICK_2, 1.2f, 1.3f, 0.85f);
                UnlockAffinities(!canUseAffinities);
                player.hand.applyPowers();

                if (GR.Animator.Config.HoldShiftToLockAffinities.Get())
                {
                    holdingShift = true;
                }
            }
            else if (holdingShift && InputManager.Shift.IsReleased())
            {
                SFX.Play(SFX.UI_CLICK_2, 0.75f, 0.85f, 0.85f);
                UnlockAffinities(!canUseAffinities);
                player.hand.applyPowers();

                holdingShift = false;
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        dragPanel_image.Render(sb);
        draggable_icon.Render(sb);
        info_button.Render(sb);
        lock_button.Render(sb);

        for (EYBCardAffinityRow t : Rows)
        {
            t.Render(sb);
        }
    }

    private void UnlockAffinities(boolean unlock)
    {
        if (unlock)
        {
            this.lock_button.SetText("USE").SetTextColor(new Color(0.6f, 1f, 0.6f, 1f));
            this.canUseAffinities = true;
        }
        else
        {
            this.lock_button.SetText("LOCK").SetTextColor(new Color(1f, 0.6f, 0.6f, 1f));
            this.canUseAffinities = false;
        }

        lock_tooltip.description = GR.Animator.Strings.Affinities.LockMessage(GR.Animator.Config.HoldShiftToLockAffinities.Get());
    }

    private void UpdateInfoTooltip(boolean next)
    {
        info_tooltip.description = next ? tooltipMessages.Next(true) : tooltipMessages.Current(false);
        info_tooltip.description = GR.Animator.Strings.Tutorial.ClickToCycle(tooltipMessages.GetIndex() + 1, tooltipMessages.GetInnerList().size()) + info_tooltip.description;
    }
}