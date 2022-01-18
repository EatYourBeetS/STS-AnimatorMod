package pinacolada.blights.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import pinacolada.blights.PCLBlight;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public abstract class AbstractGlyphBlight extends PCLBlight
{
    public static final String ID = CreateFullID(AbstractGlyphBlight.class);
    public static final float RENDER_SCALE = 0.8f;

    public final int ascensionRequirement;
    public final int ascensionStep;
    public final int baseAmount;
    public final int baseAmountStep;
    public int cacheMinimumLevel;

    public AbstractGlyphBlight(String ID, int ascensionRequirement, int ascensionStep)
    {
        this(ID, ascensionRequirement, ascensionStep, 1, 1, 0);
    }

    public AbstractGlyphBlight(String ID, int ascensionRequirement, int ascensionStep, int baseAmount, int baseAmountStep)
    {
        this(ID, ascensionRequirement, ascensionStep, baseAmount, baseAmountStep, 0);
    }

    public AbstractGlyphBlight(String ID, int ascensionRequirement, int ascensionStep, int baseAmount, int baseAmountStep, int counter)
    {
        super(ID);
        this.outlineImg = GR.GetTexture(GR.GetBlightOutlineImage(ID));
        this.ascensionRequirement = ascensionRequirement;
        this.ascensionStep = Math.max(1, ascensionStep);
        this.baseAmount = baseAmount;
        this.baseAmountStep = baseAmountStep;
        this.counter = counter;
        this.scale = RENDER_SCALE; // Because they end up looking larger in game than in the character select screen
        updateDescription();
    }

    public void addAmount(int amount)
    {
        this.counter += amount;
    }

    public void setAmount(int amount) {this.counter = amount;}

    public void reset()
    {
        this.counter = 0;
    }

    public int GetPotency() {
        return baseAmount + this.counter * (AbstractDungeon.actNum - 1) * baseAmountStep;
    }

    public int GetMinimumLevel(int ascensionLevel) {
        cacheMinimumLevel = 0; //Math.max(0,(ascensionLevel - ascensionRequirement + ascensionStep) / ascensionStep);
        return cacheMinimumLevel;
    }

    public String GetUpdatedDescription() {
        return FormatDescription(0, PCLGameUtilities.InGame() ? GetPotency() : baseAmount, baseAmountStep);
    }

    public String GetAscensionTooltipDescription(int ascensionLevel) {
        return PCLJUtils.Format(strings.DESCRIPTION[2], description, ascensionLevel, cacheMinimumLevel);
    }

    public String GetLockedTooltipDescription() {
        return PCLJUtils.Format(strings.DESCRIPTION[3], ascensionRequirement);
    }

    public void update() {
        super.update();
        if (this.isDone) {
            if (this.hb.hovered && AbstractDungeon.topPanel.potionUi.isHidden) {
                this.scale = Settings.scale;
            } else {
                this.scale = MathHelper.scaleLerpSnap(RENDER_SCALE, Settings.scale);
            }
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GetUpdatedDescription();
        super.updateDescription();
    }

    // Screw outlines

    @Override
    public void renderOutline(Color c, SpriteBatch sb, boolean inTopPanel) {
    }

    @Override
    public void renderOutline(SpriteBatch sb, boolean inTopPanel) {
    }
}