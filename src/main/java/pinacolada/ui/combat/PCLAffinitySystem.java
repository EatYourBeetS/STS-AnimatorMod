package pinacolada.ui.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Ftue;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.ui.hitboxes.RelativeHitbox;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class PCLAffinitySystem extends GUIElement
{
    public static final int SCALING_DIVISION = 1;
    public final ArrayList<AbstractPCLAffinityPower> Powers = new ArrayList<>();
    public AffinityCounts AffinityCounts = new AffinityCounts();
    public PCLAffinityMeter AffinityMeter;

    protected final DraggableHitbox hb;
    protected final GUI_Image dragAmount_image;
    protected final GUI_Image draggable_icon;
    protected final GUI_Button info_icon;
    protected final ArrayList<PCLAffinityRow> rows = new ArrayList<>();
    protected final RotatingList<String> tooltipTitles = new RotatingList<>();
    protected final RotatingList<String> tooltipDescriptions = new RotatingList<>();
    protected GUI_Ftue ftue;
    protected PCLCardTooltip tooltip;
    protected Vector2 amountsSavedPosition;

    protected PCLAffinity currentAffinitySynergy = null;
    protected AbstractCard currentSynergy = null;
    protected PCLCard lastCardPlayed = null;

    public PCLAffinitySystem()
    {
        for (PCLAffinity affinity : PCLAffinity.Extended()) {
            Powers.add(affinity.GetPower());
        }

        hb = new DraggableHitbox(ScreenW(0.0366f), ScreenH(0.425f), Scale(80f),  Scale(40f), true);
        hb.SetBounds(hb.width * 0.6f, Settings.WIDTH - (hb.width * 0.6f), ScreenH(0.35f), ScreenH(0.85f));

        AffinityMeter = new PCLAffinityMeter(this);

        dragAmount_image = new GUI_Image(GR.PCL.Images.Panel_Rounded.Texture(), hb)
        .SetColor(0.05f, 0.05f, 0.05f, 0.5f);
        draggable_icon = new GUI_Image(GR.PCL.Images.Draggable.Texture(), new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(40f), Scale(20f), false))
        .SetColor(Colors.White(0.75f));
        info_icon = new GUI_Button(ImageMaster.INTENT_UNKNOWN, new RelativeHitbox(hb, Scale(40f), Scale(40f), Scale(100f), Scale(20f), false))
                .SetText("");
        // TODO add FTUE with dropdown and images

        final PCLAffinity[] types = PCLAffinity.Extended();
        for (int i = 0; i < types.length; i++)
        {
            rows.add(new PCLAffinityRow(this, types[i], i));
        }

        tooltip = new PCLCardTooltip("", "");
        tooltip.subText = new ColoredString("", Settings.PURPLE_COLOR);

    }

    public AffinityCounts AddAffinity(PCLAffinity affinity, int amount)
    {
        return AffinityCounts.Add(affinity, amount);
    }

    public AffinityCounts AddAffinities(PCLCardAffinities affinities)
    {
        return AffinityCounts.Add(affinities);
    }

    public boolean CheckAffinityLevels(PCLAffinity[] affinities, int amount, boolean addStar) {
        return CheckAffinityLevels(affinities, amount, addStar, false);
    }

    public boolean CheckAffinityLevels(PCLAffinity[] affinities, int amount, boolean addStar, boolean requireAll) {
        for (PCLAffinity affinity : affinities) {
            if (GetAffinityLevel(affinity, addStar) >= amount) {
                return true;
            }
            else if (requireAll) {
                return false;
            }
        }
        return requireAll;
    }

    public void Flash(PCLAffinity affinity) {
        PCLAffinityRow row = GetRow(affinity);
        if (row != null) {
            row.Flash();
        }
    }

    public int GetAffinityLevel(PCLAffinity affinity, boolean addStar) {
        int base = AffinityCounts.GetAmount(affinity);
        return PCLCombatStats.OnTrySpendAffinity(affinity, base, false);
    }

    public PCLCardAffinities GetCardAffinities(Iterable<AbstractCard> cards, AbstractCard ignored)
    {
        final PCLCardAffinities affinities = new PCLCardAffinities(null);
        for (AbstractCard c : cards)
        {
            PCLCard card = PCLJUtils.SafeCast(c, PCLCard.class);
            if (card != ignored && card != null)
            {
                affinities.Add(card.affinities, 1);
            }
        }

        return affinities;
    }

    public PCLCardAffinities GetHandAffinities(AbstractCard ignored)
    {
        return player == null ? new PCLCardAffinities(null) : GetCardAffinities(player.hand.group, ignored);
    }

    public int GetHandAffinityLevel(PCLAffinity affinity, AbstractCard ignored)
    {
        return GetHandAffinities(ignored).GetLevel(affinity, false);
    }

    public PCLAffinityRow GetRow(PCLAffinity affinity)
    {
        for (PCLAffinityRow row : rows)
        {
            if (row.Type == affinity)
            {
                return row;
            }
        }

        return null;
    }

    public PCLAffinity GetLastAffinitySynergy() {
        return currentSynergy != null ? currentAffinitySynergy : null;
    }

    public boolean IsSynergizing(AbstractCard card)
    {
        return card != null && currentSynergy != null && currentSynergy.uuid == card.uuid;
    }

    public AbstractPCLAffinityPower GetPower(PCLAffinity affinity)
    {
        for (AbstractPCLAffinityPower p : Powers)
        {
            if (p.affinity.equals(affinity))
            {
                return p;
            }
        }

        return null;
    }

    public int GetPowerAmount(PCLAffinity affinity)
    {
        final AbstractPCLAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.amount;
    }

    public int GetPowerLevel(PCLAffinity affinity)
    {
        final AbstractPCLAffinityPower p = GetPower(affinity);
        return p == null ? 0 : p.GetEffectiveLevel();
    }

    public void OnStartOfTurn()
    {
        for (PCLAffinityRow row : rows)
        {
            row.OnStartOfTurn();
        }
        AffinityMeter.OnStartOfTurn();
    }

    public int GetLastAffinityLevel(PCLAffinity affinity)
    {
        return lastCardPlayed == null ? 0 : lastCardPlayed.affinities.GetLevel(affinity);
    }

    public PCLCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public AffinityCounts SpendAffinity(PCLAffinity affinity, int amount)
    {
        return AffinityCounts.Spend(affinity, amount);
    }

    public boolean TrySynergize(AbstractCard card)
    {
        if (WouldMatch(card))
        {
            currentSynergy = card;
            currentAffinitySynergy = AffinityMeter.GetCurrentAffinity();
            return true;
        }

        currentSynergy = null;
        currentAffinitySynergy = null;
        return false;
    }

    public boolean CanActivateSynergyBonus(PCLCardAffinity affinity)
    {
        return affinity != null && affinity.level > 0 && CanActivateSynergyBonus(affinity.type);
    }

    public boolean CanActivateSynergyBonus(PCLAffinity affinity)
    {
        return affinity.ID >= 0 && GetRow(affinity).Power.IsEnabled();
    }

    public void OnNotSynergy(PCLCard card) {
        AffinityMeter.OnNotMatch(card);
    }

    public void OnSynergy(PCLCard card)
    {
        AffinityMeter.OnMatch(card);
        int star = card.affinities.Star != null ? card.affinities.Star.level : 0;
        if (star > 0) {
            for (PCLAffinityRow row : rows)
            {
                if (CanActivateSynergyBonus(row.Type)) {
                    row.ActivateSynergyBonus(card);
                }
            }
        }
        else {
            for (PCLCardAffinity affinity : card.affinities.List)
            {
                if (CanActivateSynergyBonus(affinity))
                {
                    final PCLAffinityRow row = GetRow(affinity.type);
                    if (row != null)
                    {
                        row.ActivateSynergyBonus(card);
                    }
                }
            }
        }

    }

    public void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = PCLJUtils.SafeCast(card, PCLCard.class);
        currentSynergy = null;
        currentAffinitySynergy = null;
    }

    public boolean WouldMatch(AbstractCard card)
    {
        for (OnSynergyCheckSubscriber s : PCLCombatStats.onSynergyCheck.GetSubscribers())
        {
            if (s.OnSynergyCheck(card, null))
            {
                return true;
            }
        }

        final PCLCard a = PCLJUtils.SafeCast(card, PCLCard.class);
        if (a != null) {
            return AffinityMeter.HasMatch(a);
        }
        return false;
    }

    public boolean WouldSynergize(AbstractCard card, AbstractCard other)
    {
        for (OnSynergyCheckSubscriber s : PCLCombatStats.onSynergyCheck.GetSubscribers())
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

        final PCLCard a = PCLJUtils.SafeCast(card, PCLCard.class);
        final PCLCard b = PCLJUtils.SafeCast(other, PCLCard.class);
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
        return GetSynergies(c1, c2).GetLevel(PCLAffinity.General, false) > 0;
    }

    public PCLCardAffinities GetSynergies(AbstractCard current, AbstractCard previous)
    {
        final PCLCardAffinities synergies = new PCLCardAffinities(null);
        final PCLCard a = PCLJUtils.SafeCast(current, PCLCard.class);
        final PCLCard b = PCLJUtils.SafeCast(previous, PCLCard.class);
        if (a == null || b == null)
        {
            return synergies;
        }

        for (PCLAffinity affinity : PCLAffinity.Extended())
        {
            int lv_a = a.affinities.GetLevel(affinity);
            int lv_b = b.affinities.GetLevel(affinity);
            if ((lv_a > 1 && lv_b > 0) || (lv_b > 1 && lv_a > 0))
            {
                synergies.Add(affinity, lv_a);
            }
        }

        synergies.SetStar(a.affinities.GetLevel(PCLAffinity.Star));

        return synergies;
    }

    public float ModifyBlock(float block, PCLCard card)
    {
        if (card.type != AbstractCard.CardType.ATTACK || card.cardData.BlockScalingAttack)
        {
            for (AbstractPCLAffinityPower p : Powers)
            {
                block = ApplyScaling(p, card, block);
            }
        }

        return block;
    }

    public float ModifyDamage(float damage, PCLCard card)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            for (AbstractPCLAffinityPower p : Powers)
            {
                damage = ApplyScaling(p, card, damage);
            }
        }

        return damage;
    }

    public float ModifyMagicNumber(float magicNumber, PCLCard card) {
        if (card.cardData.CanScaleMagicNumber) {
            for (AbstractPCLAffinityPower p : Powers)
            {
                magicNumber = ApplyScaling(p, card, magicNumber);
            }
        }
        return magicNumber;
    }

    public float ApplyScaling(PCLAffinity affinity, PCLCard card, float base)
    {
        if (affinity == PCLAffinity.Star)
        {
            for (AbstractPCLAffinityPower p : Powers)
            {
                base = ApplyScaling(p, card, base);
            }

            return base;
        }

        return ApplyScaling(GetPower(affinity), card, base);
    }

    public float ApplyScaling(AbstractPCLAffinityPower power, PCLCard card, float base)
    {
        return base + MathUtils.ceil(card.affinities.GetScaling(power.affinity, true) * power.GetEffectiveScaling());
    }

    // ====================== //
    //  GUI Rendering/Update  //
    // ====================== //

    public void Initialize()
    {
        AffinityCounts = new AffinityCounts();

        PCLJUtils.LogInfo(this, "Initialized PCL Affinity System.");

        if (amountsSavedPosition != null)
        {
            final DraggableHitbox hb = (DraggableHitbox) dragAmount_image.hb;
            amountsSavedPosition.x = hb.target_cX / (float) Settings.WIDTH;
            amountsSavedPosition.y = hb.target_cY / (float) Settings.HEIGHT;
            if (amountsSavedPosition.dst2(GR.PCL.Config.AffinitySystemPosition.Get()) > Mathf.Epsilon)
            {
                PCLJUtils.LogInfo(this, "Saved PCL affinity panel position.");
                GR.PCL.Config.AffinitySystemPosition.Set(amountsSavedPosition.cpy(), true);
            }
        }

        for (PCLAffinityRow row : rows)
        {
            row.Initialize();
        }

        AffinityMeter.Initialize();

        if (tooltipDescriptions.Count() == 0 || tooltipTitles.Count() == 0) {
            for (String tip : GR.PCL.Strings.Tutorial.TutorialItems()) {
                tooltipDescriptions.Add(tip);
            }
            tooltipTitles.Add(GR.Tooltips.Affinity_General.title);
            tooltipTitles.Add(GR.Tooltips.Match.title);
            tooltipTitles.Add(GR.Tooltips.Match.title);

            SetTipIndex(0);
        }

    }

    public void Update()
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        if (amountsSavedPosition == null)
        {
            amountsSavedPosition = GR.PCL.Config.AffinitySystemPosition.Get(new Vector2(0.0522f, 0.43f)).cpy();
            hb.SetPosition(ScreenW(amountsSavedPosition.x), ScreenH(amountsSavedPosition.y));
        }

        boolean draggingCard = false;
        PCLCard hoveredCard = null;
        if (player.hoveredCard != null)
        {
            if ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode)
            {
                draggingCard = true;
            }
            if (player.hoveredCard instanceof PCLCard)
            {
                hoveredCard = (PCLCard)player.hoveredCard;
            }
        }

        final AffinityCounts previewAffinities = new AffinityCounts(AffinityCounts);
        //final EYBCardAffinities synergies = GetSynergies(hoveredCard, lastCardPlayed);
        for (PCLAffinityRow row : rows)
        {
            row.Update(previewAffinities, hoveredCard, null, draggingCard);
        }

        AffinityMeter.Update();

        for (int i = 0; i < Powers.size(); i++)
        {
            Powers.get(i).update(i);
        }

        dragAmount_image.Update();
        draggable_icon.Update();
        info_icon.Update();

        boolean isHovered = dragAmount_image.hb.hovered || info_icon.hb.hovered || PCLJUtils.Any(rows, PCLAffinityRow::IsHovered);
        dragAmount_image.SetColor(0.05f, 0.05f, 0.05f, isHovered ? 0.5f : 0.05f);
        draggable_icon.SetColor(Colors.White(isHovered ? 0.75f : 0.1f));

        if (!draggingCard && info_icon.hb.hovered)
        {
            PCLCardTooltip.QueueTooltip(tooltip);
            if (PCLHotkeys.cycle.isJustPressed()) {
                CycleTips();
            }
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (player == null || player.hand == null || AbstractDungeon.overlayMenu.energyPanel.isHidden)
        {
            return;
        }

        dragAmount_image.Render(sb);
        draggable_icon.Render(sb);
        info_icon.Render(sb);

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, GR.PCL.Strings.Combat.Experience, info_icon.hb.cX + Scale(92), info_icon.hb.y, 1f, Colors.Blue(1f));
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, GR.PCL.Strings.Combat.Uses, info_icon.hb.cX + Scale(156), info_icon.hb.y, 1f, Colors.Blue(1f));

        for (PCLAffinityRow t : rows)
        {
            t.Render(sb);
        }

        AffinityMeter.Render(sb);
    }

    public void CycleTips() {
        tooltipTitles.Next(true);
        UpdateTipContent();
    }

    public void SetTipIndex(int index) {
        tooltipTitles.SetIndex(index);
        UpdateTipContent();
    }

    protected void UpdateTipContent() {
        tooltipDescriptions.SetIndex(tooltipTitles.GetIndex());
        tooltip.description = tooltipDescriptions.Current();
        tooltip.title = tooltipTitles.Current();
        tooltip.subText.SetText(GR.PCL.Strings.Misc.PressKeyToCycle(PCLHotkeys.cycle.getKeyString()) + " (" + (tooltipDescriptions.GetIndex() + 1) + "/" + tooltipDescriptions.Count() + ")");
    }

    public static class AffinityCounts {
        public int[] Counts = new int[PCLAffinity.Extended().length];

        public AffinityCounts() {
        }

        public AffinityCounts(AffinityCounts counts) {
            Counts = counts.Counts.clone();
        }

        public AffinityCounts(PCLCardAffinities affinities) {
            if (affinities.Star != null) {
                for (PCLAffinity a : PCLAffinity.Extended()) {
                    Counts[a.ID] = affinities.Star.level;
                }
            }
            else {
                for (PCLCardAffinity affinity : affinities.List) {
                    Counts[affinity.type.ID] = affinity.level;
                }
            }

        }

        public AffinityCounts Add(PCLAffinity affinity, int amount) {
            int actualAmount = PCLCombatStats.OnGainAffinity(affinity, amount, true);
            if (PCLAffinity.Star.equals(affinity) || PCLAffinity.General.equals(affinity)) {
                for (PCLAffinity a : PCLAffinity.Extended()) {
                    Counts[a.ID] += actualAmount;
                }
            }
            else {
                Counts[affinity.ID] += actualAmount;
            }
            return this;
        }

        public AffinityCounts Add(AffinityCounts counts) {
            for (int i = 0; i < Counts.length; i++) {
                Counts[i] += PCLCombatStats.OnGainAffinity(PCLAffinity.Extended()[i], counts.Counts[i], true);
            }
            return this;
        }

        public AffinityCounts Add(PCLCardAffinities affinities) {
            if (affinities.Star != null) {
                int actualAmount = PCLCombatStats.OnGainAffinity(PCLAffinity.Star, affinities.Star.level, true);
                for (PCLAffinity a : PCLAffinity.Extended()) {
                    Counts[a.ID] += actualAmount;
                }
            }
            else {
                for (PCLCardAffinity affinity : affinities.List) {
                    Counts[affinity.type.ID] += PCLCombatStats.OnGainAffinity(affinity.type, affinity.level, true);
                }
            }

            return this;
        }

        public AffinityCounts Add(PCLCardAffinities affinities, int amount) {
            if (affinities.Star != null) {
                int actualAmount = PCLCombatStats.OnGainAffinity(PCLAffinity.Star, amount, true);
                for (PCLAffinity a : PCLAffinity.Extended()) {
                    Counts[a.ID] += actualAmount;
                }
            }
            else {
                for (PCLCardAffinity affinity : affinities.List) {
                    int actualAmount = PCLCombatStats.OnGainAffinity(affinity.type, amount, true);
                    Counts[affinity.type.ID] += actualAmount;
                }
            }

            return this;
        }

        public AffinityCounts Spend(PCLAffinity affinity, int amount) {
            if (PCLAffinity.Star.equals(affinity) || PCLAffinity.General.equals(affinity)) {
                for (PCLAffinity a : PCLAffinity.Extended()) {
                    Counts[a.ID] -= amount;
                    if (Counts[a.ID] < 0) {
                        Counts[a.ID] = 0;
                    }
                }
            }
            else {
                Counts[affinity.ID] -= amount;
                if (Counts[affinity.ID] < 0) {
                    Counts[affinity.ID] = 0;
                }
            }
            return this;
        }

        public int GetAmount(PCLAffinity affinity)
        {
            if (PCLAffinity.Star.equals(affinity))
            {
                return Arrays.stream(Counts).min().getAsInt();
            }
            else if (PCLAffinity.General.equals(affinity))
            {
                return Arrays.stream(Counts).max().getAsInt();
            }
            else
            {
                return Counts[affinity.ID];
            }
        }
    }
}