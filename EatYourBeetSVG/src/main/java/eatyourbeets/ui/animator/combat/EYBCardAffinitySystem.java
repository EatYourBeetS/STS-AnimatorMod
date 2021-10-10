package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.*;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class EYBCardAffinitySystem extends GUIElement implements OnStartOfTurnSubscriber
{
    public final ArrayList<AbstractAffinityPower> Powers = new ArrayList<>();
    public final EYBCardAffinities BonusAffinities = new EYBCardAffinities(null);
    public final ForcePower Force;
    public final AgilityPower Agility;
    public final IntellectPower Intellect;
    public final WillpowerPower Willpower;
    public final BlessingPower Blessing;
    public final CorruptionPower Corruption;

    protected final DraggableHitbox hb;
    protected final GUI_Image dragPanel_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Image info_icon;
    protected final ArrayList<EYBCardAffinityRow> rows = new ArrayList<>();
    protected EYBCardTooltip tooltip;
    protected Vector2 savedPosition;

    protected AbstractCard currentSynergy = null;
    protected AnimatorCard lastCardPlayed = null;

    public EYBCardAffinitySystem()
    {
        Powers.add(Force = new ForcePower());
        Powers.add(Agility = new AgilityPower());
        Powers.add(Intellect = new IntellectPower());
        Powers.add(Willpower = new WillpowerPower());
        Powers.add(Blessing = new BlessingPower());
        Powers.add(Corruption = new CorruptionPower());

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        dragPanel_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);

        draggable_icon = new GUI_Image(GR.Common.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
        .SetColor(Colors.White(0.75f));

        tooltip = new EYBCardTooltip(GR.Tooltips.Affinity_General.title, GR.Animator.Strings.Tutorial.AffinityPanel);
        info_icon = new GUI_Image(ImageMaster.INTENT_UNKNOWN, new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(100f), Scale(20f), false))
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f)
        .SetForegroundTexture(ImageMaster.INTENT_UNKNOWN, Colors.White(0.75f), 0.9f);

        final Affinity[] types = Affinity.Basic();
        for (int i = 0; i < types.length; i++)
        {
            rows.add(new EYBCardAffinityRow(this, types[i], i));
        }

        rows.add(new EYBCardAffinityRow(this, Affinity.General, types.length));
    }

    public EYBCardAffinities GetAffinities(Iterable<AbstractCard> cards, AbstractCard ignored)
    {
        final EYBCardAffinities affinities = new EYBCardAffinities(null);
        for (AbstractCard c : cards)
        {
            EYBCard card = JUtils.SafeCast(c, EYBCard.class);
            if (card != ignored && card != null)
            {
                affinities.Add(card.affinities, 1);
            }
        }

        return affinities;
    }

    public EYBCardAffinities GetHandAffinities(AbstractCard ignored)
    {
        return player == null ? BonusAffinities : GetAffinities(player.hand.group, ignored).Add(BonusAffinities);
    }

    public int GetHandAffinityLevel(Affinity affinity, AbstractCard ignored)
    {
        return GetHandAffinities(ignored).GetLevel(affinity, false);
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

    public boolean IsSynergizing(AbstractCard card)
    {
        return card != null && currentSynergy != null && currentSynergy.uuid == card.uuid;
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

    public int GetPowerThreshold(Affinity affinity)
    {
        final AbstractAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.GetCurrentThreshold();
    }

    @Override
    public void OnStartOfTurn()
    {
        for (EYBCardAffinityRow row : rows)
        {
            row.OnStartOfTurn();
        }
    }

    public AnimatorCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public boolean CanActivateElementalBonus(EYBCardAffinity element)
    {
        return element != null && CanActivateElementalBonus(element.type);
    }

    public boolean CanActivateElementalBonus(Affinity element)
    {
        return element.ID >= 0;
    }

    public void AddMaxActivationsPerTurn(Affinity affinity, int amount)
    {
        final EYBCardAffinityRow row = GetRow(affinity);
    }

    public void OnCardPlayed(AnimatorCard card)
    {
        for (EYBCardAffinity affinity : card.affinities.List)
        {
            if (CanActivateElementalBonus(affinity)) {
                final EYBCardAffinityRow row = GetRow(affinity.type);
                if (row != null) {
                    row.ActivateElementBonus(card, affinity.level);
                }
            }
        }
    }

    public void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        currentSynergy = null;
    }

    public boolean WouldSynergize(AbstractCard card)
    {
        return WouldSynergize(card, lastCardPlayed);
    }

    public boolean WouldSynergize(AbstractCard card, AbstractCard other)
    {
        for (OnSynergyCheckSubscriber s : CombatStats.onSynergyCheck.GetSubscribers())
        {
            if (s.OnSynergyCheck(card, other))
            {
                return true;
            }
        }

        if (card == null || other == null)
        {
            return false;
        }

        final AnimatorCard a = JUtils.SafeCast(card, AnimatorCard.class);
        final AnimatorCard b = JUtils.SafeCast(other, AnimatorCard.class);
        if (a != null)
        {
            return b != null ? (a.HasDirectSynergy(b) || b.HasDirectSynergy(a)) : a.HasDirectSynergy(other);
        }
        else
        {
            return b != null ? b.HasDirectSynergy(card) : HasDirectSynergy(card, other);
        }
    }

    public boolean HasDirectSynergy(AbstractCard c1, AbstractCard c2)
    {
        return GetSynergies(c1, c2).GetLevel(Affinity.General, false) > 0;
    }

    public EYBCardAffinities GetSynergies(AbstractCard current, AbstractCard previous)
    {
        final EYBCardAffinities synergies = new EYBCardAffinities(null);
        final EYBCard a = JUtils.SafeCast(current, EYBCard.class);
        final EYBCard b = JUtils.SafeCast(previous, EYBCard.class);
        if (a == null || b == null)
        {
            return synergies;
        }

        for (Affinity affinity : Affinity.Basic())
        {
            int lv_a = a.affinities.GetLevel(affinity);
            int lv_b = b.affinities.GetLevel(affinity);
            if ((lv_a > 1 && lv_b > 0) || (lv_b > 1 && lv_a > 0))
            {
                synergies.Add(affinity, lv_a);
            }
        }

        synergies.SetStar(a.affinities.GetLevel(Affinity.Star));

        return synergies;
    }

    public float ModifyBlock(float block, EYBCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK || card.cardData.BlockScalingAttack)
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
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinity, true) * power.amount * 0.5f);
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        BonusAffinities.Star = null;
        BonusAffinities.List.clear();
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
                hoveredCard = (EYBCard)player.hoveredCard;
            }
        }

        final EYBCardAffinities handAffinities = GetHandAffinities(hoveredCard);
        final EYBCardAffinities synergies = GetSynergies(hoveredCard, lastCardPlayed);
        for (EYBCardAffinityRow row : rows)
        {
            row.Update(handAffinities, hoveredCard, synergies, draggingCard);
        }

        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }

        dragPanel_image.Update();
        draggable_icon.Update();
        info_icon.Update();

        if (!draggingCard && info_icon.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip);
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
        info_icon.Render(sb);

        for (EYBCardAffinityRow t : rows)
        {
            t.Render(sb);
        }
    }
}