package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
import eatyourbeets.powers.affinity.*;
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
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public final EYBCardAffinities PlayerAffinities = new EYBCardAffinities(null);
    public final ForcePower Force;
    public final AgilityPower Agility;
    public final IntellectPower Intellect;
    public final BlessingPower Blessing;
    public final CorruptionPower Corruption;

    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Button info_button;
    protected final ArrayList<EYBCardAffinityRow> rows = new ArrayList<>();
    protected RotatingList<String> tooltipMessages;
    protected EYBCardTooltip tooltip;
    protected Vector2 savedPosition;

    protected AbstractCard currentSynergy = null;
    protected AnimatorCard lastCardPlayed = null;

    public EYBCardAffinitySystem()
    {
        Powers.add(Force = new ForcePower());
        Powers.add(Agility = new AgilityPower());
        Powers.add(Intellect = new IntellectPower());
        Powers.add(Blessing = new BlessingPower());
        Powers.add(Corruption = new CorruptionPower());

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f), Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
        .SetColor(Colors.White(0.75f));

        tooltipMessages = new RotatingList<>(GR.Animator.Strings.Tutorial.GetStrings());
        tooltip = new EYBCardTooltip("Animator Info", "");
        UpdateTooltip(false);

        info_button = new GUI_Button(GR.Common.Images.Panel_Rounded.Texture(), new RelativeHitbox(hb, Scale(60f), Scale(30f), Scale(120f), Scale(20f), false))
        .SetFont(EYBFontHelper.CardTooltipFont, 1f)
        .SetColor(new Color(0.1f, 0.1f, 0.1f, 0.6f))
        .SetTextColor(new Color(1f, 1f, 0.8f, 1f))
        .SetText("info")
        .SetTooltip(tooltip, false)
        .SetOnClick(() -> UpdateTooltip(true));

        final Affinity[] types = Affinity.Basic();
        for (int i = 0; i < types.length; i++)
        {
            rows.add(new EYBCardAffinityRow(this, types[i], i));
        }

        rows.add(new EYBCardAffinityRow(this, Affinity.Sealed, types.length));
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
        return PlayerAffinities;
    }

    public boolean TryUseAffinity(Affinity affinity, int requirement)
    {
        if (affinity == Affinity.Star)
        {
            for (Affinity a : Affinity.Basic())
            {
                if (PlayerAffinities.GetRequirement(a) < requirement)
                {
                    return false;
                }
            }

            for (Affinity a : Affinity.Basic())
            {
                PlayerAffinities.SetRequirement(a, PlayerAffinities.GetRequirement(a) - requirement);
            }
            return true;
        }
        else
        {
            final int amount = PlayerAffinities.GetRequirement(affinity);
            if (amount < requirement)
            {
                return false;
            }

            PlayerAffinities.SetRequirement(affinity, amount - requirement);
            return true;
        }
    }

    public void AddTempAffinity(Affinity affinity, int amount)
    {
        PlayerAffinities.Get(affinity).requirement += amount;
    }

    public int GetAffinityLevel(Affinity affinity)
    {
        if (affinity == Affinity.Star)
        {
            EYBCardAffinity min = JUtils.FindMin(JUtils.Filter(PlayerAffinities.List, ac -> ac.type.ID >= 0), af -> af.level);
            return min != null ? min.level : 0;
        }
        return PlayerAffinities.GetLevel(affinity, false);
    }

    public EYBCardAffinityRow GetRow(Affinity affinity)
    {
        for (EYBCardAffinityRow row : rows)
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
        return p == null ? 0 : p.amount;
    }

    @Override
    public void OnStartOfTurn()
    {
        PlayerAffinities.Set(Affinity.Sealed, 1);

        for (EYBCardAffinityRow row : rows)
        {
            row.OnStartOfTurn();
        }
    }

    public int GetLastAffinityLevel(Affinity affinity)
    {
        return lastCardPlayed == null ? 0 : GameUtilities.GetAffinityLevel(lastCardPlayed, affinity, true);
    }

    public AnimatorCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        currentSynergy = null;
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    block = ApplyScaling(p, card, block);
                }
            }
        }

        return block;
    }

    public float ModifyDamage(float damage, EYBCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    damage = ApplyScaling(p, card, damage);
                }
            }
        }

        return damage;
    }

    public float ApplyScaling(Affinity affinity, EYBCard card, float base)
    {
        if (affinity == Affinity.Star)
        {
            for (AbstractAffinityPower p : Powers)
            {
                if (p.amount > 0)
                {
                    base = ApplyScaling(p, card, base);
                }
            }

            return base;
        }

        return ApplyScaling(GetPower(affinity), card, base);
    }

    public float ApplyScaling(AbstractAffinityPower power, EYBCard card, float base)
    {
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinity, true) * power.amount * 0.33f);
    }

    public void Seal(EYBCardAffinities affinities, boolean manual)
    {
        if (affinities.sealed)
        {
            return;
        }

        if (manual)
        {
            final int seal = PlayerAffinities.GetLevel(Affinity.Sealed);
            if (seal <= 0)
            {
                return;
            }
            PlayerAffinities.Set(Affinity.Sealed, seal - 1);
        }

        affinities.sealed = true;

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

            GetRow(a).Seal(affinities);
        }

        CombatStats.OnAffinitySealed(affinities, manual);
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        PlayerAffinities.Star = null;
        PlayerAffinities.List.clear();
        CombatStats.onStartOfTurn.Subscribe(this);

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

        for (EYBCardAffinityRow row : rows)
        {
            row.Initialize();
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

        final EYBCardAffinities handAffinities = GetPlayerAffinities();
        for (EYBCardAffinityRow row : rows)
        {
            row.Update(PlayerAffinities, hoveredCard, draggingCard);
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
        info_button.ShowTooltip(!draggingCard);
        info_button.Update();

        if (hoveredCard != null && !draggingCard
         && InputManager.RightClick.IsJustPressed() && GameUtilities.CanAcceptInput(true)
         && PlayerAffinities.GetLevel(Affinity.Sealed) > 0 && hoveredCard.affinities.GetLevel(Affinity.General) > 0)
        {
            if (hoveredCard.affinities.sealed)
            {
                GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3, GR.Animator.Strings.Misc.CannotSeal, true));
            }
            else
            {
                SFX.Play(SFX.RELIC_ACTIVATION, 0.75f, 0.85f, 0.95f);
                GameActions.Bottom.SealAffinities(hoveredCard, true);
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

        for (EYBCardAffinityRow t : rows)
        {
            t.Render(sb);
        }
    }

    private void UpdateTooltip(boolean next)
    {
        tooltip.description = next ? tooltipMessages.Next(true) : tooltipMessages.Current(false);
        tooltip.description = GR.Animator.Strings.Tutorial.ClickToCycle(tooltipMessages.GetIndex() + 1, tooltipMessages.GetInnerList().size()) + tooltip.description;
    }
}