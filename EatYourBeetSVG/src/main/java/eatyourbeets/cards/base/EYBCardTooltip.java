package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class EYBCardTooltip
{
    public static final Color BASE_COLOR = new Color(1f, 0.9725f, 0.8745f, 1f);
    public static final float CARD_TIP_PAD = 12f * Settings.scale;
    public static final float BOX_EDGE_H = 32f * Settings.scale;
    public static final float SHADOW_DIST_Y = 14f * Settings.scale;
    public static final float SHADOW_DIST_X = 9f * Settings.scale;
    public static final float BOX_BODY_H = 64f * Settings.scale;
    public static final float TEXT_OFFSET_X = 22f * Settings.scale;
    public static final float HEADER_OFFSET_Y = 12f * Settings.scale;
    public static final float ORB_OFFSET_Y = -8f * Settings.scale;
    public static final float BODY_OFFSET_Y = -20f * Settings.scale;
    public static final float BOX_W = 360f * Settings.scale;
    public static final float BODY_TEXT_WIDTH = 320f * Settings.scale;
    public static final float TIP_DESC_LINE_SPACING = 26f * Settings.scale;
    public static final float POWER_ICON_OFFSET_X = 40f * Settings.scale;

    private final static ArrayList EMPTY_LIST = new ArrayList();

    private final static FieldInfo<String> _body = JUtils.GetField("BODY", TipHelper.class);
    private final static FieldInfo<String> _header = JUtils.GetField("HEADER", TipHelper.class);
    private final static FieldInfo<ArrayList> _card = JUtils.GetField("card", TipHelper.class);
    private final static FieldInfo<ArrayList> _keywords = JUtils.GetField("KEYWORDS", TipHelper.class);
    private final static FieldInfo<ArrayList> _powerTips = JUtils.GetField("POWER_TIPS", TipHelper.class);
    private final static FieldInfo<Boolean> _renderedTipsThisFrame = JUtils.GetField("renderedTipThisFrame", TipHelper.class);

    private static final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    private static final ArrayList<EYBCardTooltip> genericTooltips = new ArrayList<>();
    private static EYBCardTooltip maxCopiesTooltip;
    private static boolean inHand;
    private static EYBCard card;
    private static EYBRelic relic;
    private static AbstractCreature creature;
    private static Vector2 genericTipPos = new Vector2(0, 0);

    public AbstractPlayer.PlayerClass playerClass;
    public TextureRegion icon;
    public String id;
    public String title;
    public ColoredString subText;
    public String description;
    public float iconMulti_W = 1;
    public float iconMulti_H = 1;
    public boolean canRender = true;
    public boolean isKeyword = true;
    public Boolean hideDescription = null;

    public EYBCardTooltip(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public EYBCardTooltip(Keyword keyword)
    {
        this.title = keyword.PROPER_NAME;
        this.description = keyword.DESCRIPTION;
    }

    public static boolean CanRenderTooltips()
    {
        return !_renderedTipsThisFrame.Get(null);
    }

    public static void CanRenderTooltips(boolean canRender)
    {
        _renderedTipsThisFrame.Set(null, !canRender);

        if (!canRender)
        {
            _body.Set(null, null);
            _header.Set(null, null);
            _card.Set(null, null);
            _keywords.Set(null, EMPTY_LIST);
            _powerTips.Set(null, EMPTY_LIST);
            tooltips.clear();
            creature = null;
            relic = null;
            card = null;
        }
    }

    private static boolean TryRender()
    {
        final boolean canRender = CanRenderTooltips();
        if (canRender)
        {
            CanRenderTooltips(false);
        }

        return canRender;
    }

    public static void QueueTooltip(EYBCardTooltip tooltip, Hitbox hb, boolean forceRender)
    {
        float x = hb.x;
        float y = hb.cY;
        x += (x < Settings.WIDTH * 0.75f) ? (hb.width) : -(BOX_W + (Settings.scale * 10f));
        y += (y < Settings.HEIGHT * 0.9f) ? (Settings.scale * 40f) : -(Settings.scale * 50f);
        QueueTooltip(tooltip, x, y, forceRender);
    }

    public static void QueueTooltip(EYBCardTooltip tooltip, boolean forceRender)
    {
        float x = InputHelper.mX;
        float y = InputHelper.mY;
        x += (x < Settings.WIDTH * 0.75f) ? (Settings.scale * 40f) : -(BOX_W + (Settings.scale * 40f));
        y += (y < Settings.HEIGHT * 0.9f) ? (Settings.scale * 40f) : -(Settings.scale * 50f);
        QueueTooltip(tooltip, x, y, forceRender);
    }

    public static void QueueTooltip(EYBCardTooltip tooltip, float x, float y, boolean forceRender)
    {
        if (forceRender || TryRender())
        {
            genericTooltips.add(tooltip);
            genericTipPos.x = x;
            genericTipPos.y = y;
        }
    }

    public static void QueueTooltips(ArrayList<EYBCardTooltip> tips, float x, float y, boolean forceRender)
    {
        if (forceRender || TryRender())
        {
            genericTooltips.addAll(tips);
            genericTipPos.x = x;
            genericTipPos.y = y;
        }
    }

    public static void QueueTooltips(AbstractCreature source)
    {
        if (TryRender())
        {
            creature = source;
        }
    }

    public static void QueueTooltips(EYBCard source)
    {
        if (TryRender())
        {
            card = source;
        }
    }

    public static void QueueTooltips(EYBRelic source)
    {
        if (TryRender())
        {
            relic = source;
        }
    }

    public static void RenderAll(SpriteBatch sb)
    {
        if (card != null)
        {
            RenderFromCard(sb);
            card = null;
        }
        if (relic != null)
        {
            RenderFromRelic(sb);
            relic = null;
        }
        if (creature != null)
        {
            RenderFromCreature(sb);
            creature = null;
        }

        if (genericTooltips.size() > 0)
        {
            RenderGeneric(sb);
            genericTooltips.clear();
        }
    }

    public static void RenderFromCard(SpriteBatch sb)
    {
        if (card == null)
        {
            return;
        }

        int totalHidden = 0;
        inHand = AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(card);
        tooltips.clear();
        card.GenerateDynamicTooltips(tooltips);

        for (EYBCardTooltip tip : card.tooltips)
        {
            if (tip.canRender && !tooltips.contains(tip))
            {
                tooltips.add(tip);
            }
        }

        final boolean alt = Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
        for (int i = 0; i < tooltips.size(); i++)
        {
            EYBCardTooltip tip = tooltips.get(i);
            if (StringUtils.isNotEmpty(tip.id))
            {
                if (tip.hideDescription == null)
                {
                    tip.hideDescription = GR.Animator.Config.HideTipDescription(tip.id);
                }

                if (!inHand && alt && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i))
                {
                    GR.Animator.Config.HideTipDescription(tip.id, (tip.hideDescription ^= true), true);
                }
            }

            if (tip.hideDescription == null)
            {
                tip.hideDescription = false;
            }
        }

        float x;
        float y;
        if (card.isPopup)
        {
            x = 0.78f * Settings.WIDTH;
            y = 0.85f * Settings.HEIGHT;
        }
        else
        {
            x = card.current_x;
            if (card.current_x < (float) Settings.WIDTH * 0.7f)
            {
                x += AbstractCard.IMG_WIDTH / 2f + CARD_TIP_PAD;
            }
            else
            {
                x -= AbstractCard.IMG_WIDTH / 2f + CARD_TIP_PAD + BOX_W;
            }

            y = card.current_y - BOX_EDGE_H;
            float size = 0;
            for (EYBCardTooltip tip : tooltips)
            {
                if (tip.hideDescription || StringUtils.isEmpty(tip.description))
                {
                    if (!inHand)
                    {
                        size += 0.2f;
                    }
                }
                else
                {
                    size += 1f;
                }
            }

            if (size > 3f && card.current_y < Settings.HEIGHT * 0.5f && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD)
            {
                float steps = (tooltips.size() - 3) * 0.4f;
                float multi = 1f - (card.current_y / (Settings.HEIGHT * 0.5f));

                y += AbstractCard.IMG_HEIGHT * (0.5f + JUtils.Round(multi * steps, 3));
            }
            else
            {
                y += AbstractCard.IMG_HEIGHT * 0.5f;
            }
        }

        for (int i = 0; i < tooltips.size(); i++)
        {
            EYBCardTooltip tip = tooltips.get(i);
            if (inHand && (tip.hideDescription || StringUtils.isEmpty(tip.description)))
            {
                continue;
            }

            y -= tip.Render(sb, x, y, i) + BOX_EDGE_H * 3.15f;
        }

        final EYBCardPreview preview = card.GetCardPreview();
        if (preview != null)
        {
            preview.Render(sb, card, card.upgraded || GameUtilities.CanShowUpgrades(false));
        }

        if (card.isPopup && !Settings.isEndless && card.cardData != null && card.cardData.MaxCopies > 0)
        {
            if (maxCopiesTooltip == null)
            {
                maxCopiesTooltip = new EYBCardTooltip(GR.Animator.Strings.Misc.MaxCopiesHeader, GR.Animator.Strings.Misc.MaxCopiesDescription);
            }

            EYBFontHelper.CardTooltipFont.getData().setScale(0.9f);
            maxCopiesTooltip.Render(sb, Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.9f, -1);
            RenderHelpers.ResetFont(EYBFontHelper.CardTooltipFont);

            final String text = "x" + card.cardData.MaxCopies;
            RenderHelpers.WriteCentered(sb, RenderHelpers.GetSmallTextFont(card, 1), text,
            Settings.WIDTH * 0.025f + (BOX_W * 1.125f), Settings.HEIGHT * 0.9f, Colors.Cream(1));
        }
    }

    public static void RenderFromRelic(SpriteBatch sb)
    {
        if (relic == null)
        {
            return;
        }

        float x;
        float y;
        if ((float) InputHelper.mX >= 1400.0F * Settings.scale)
        {
            x = InputHelper.mX - (350 * Settings.scale);
            y = InputHelper.mY - (50 * Settings.scale);
        }
        else if (CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.RELIC_VIEW)
        {
            x = 180 * Settings.scale;
            y = 0.7f * Settings.HEIGHT;
        }
        else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP && relic.tips.size() > 2 && !AbstractDungeon.player.hasRelic(relic.relicId))
        {
            x = InputHelper.mX + (60 * Settings.scale);
            y = InputHelper.mY + (180 * Settings.scale);
        }
        else if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(relic.relicId))
        {
            x = InputHelper.mX + (60 * Settings.scale);
            y = InputHelper.mY - (30 * Settings.scale);
        }
        else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            x = 360 * Settings.scale;
            y = InputHelper.mY + (50 * Settings.scale);
        }
        else
        {
            x = InputHelper.mX + (50 * Settings.scale);
            y = InputHelper.mY + (50 * Settings.scale);
        }

        for (int i = 0; i < relic.tips.size(); i++)
        {
            final EYBCardTooltip tip = relic.tips.get(i);
            if (tip.hideDescription == null)
            {
                tip.hideDescription = !StringUtils.isEmpty(tip.id) && GR.Animator.Config.HideTipDescription(tip.id);
            }

            if (!tip.hideDescription)
            {
                y -= tip.Render(sb, x, y, i) + BOX_EDGE_H * 3.15f;
            }
        }
    }

    public static void RenderFromCreature(SpriteBatch sb)
    {
        if (creature == null)
        {
            return;
        }

        final float TIP_X_THRESHOLD = (Settings.WIDTH * 0.5f); // 1544.0F * Settings.scale;
        final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
        final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

        tooltips.clear();
        for (AbstractPower p : creature.powers)
        {
            if (p instanceof EYBClickablePower || p instanceof InvisiblePower)
            {
                continue;
            }

            final EYBCardTooltip tip = new EYBCardTooltip(p.name, p.description);
            if (p.region48 != null)
            {
                tip.icon = p.region48;
            }
            else if (p instanceof EYBPower)
            {
                tip.icon = ((EYBPower) p).powerIcon;
            }

            if (tip.icon == null && p.img != null)
            {
                tip.SetIcon(p.img, 6);
            }

            tooltips.add(tip);
        }

        float x;
        float y = creature.hb.cY + RenderHelpers.CalculateAdditionalOffset(tooltips, creature.hb.cY);
        if ((creature.hb.cX + creature.hb.width * 0.5f) < TIP_X_THRESHOLD)
        {
            x = creature.hb.cX + (creature.hb.width / 2.0F) + TIP_OFFSET_R_X;
        }
        else
        {
            x = creature.hb.cX - (creature.hb.width / 2.0F) + TIP_OFFSET_L_X;
        }

        final float original_y = y;
        final float offset_x = (x > TIP_X_THRESHOLD) ? BOX_W : -BOX_W;
        float offset = 0.0F;

        float offsetChange;
        for (int i = 0; i < tooltips.size(); i++)
        {
            EYBCardTooltip tip = tooltips.get(i);
            offsetChange = RenderHelpers.GetTooltipHeight(tip) + BOX_EDGE_H * 3.15F;
            if ((offset + offsetChange) >= (Settings.HEIGHT * 0.7F))
            {
                offset = 0.0F;
                y = original_y;
                x += offset_x;
            }

            if (tip.hideDescription == null)
            {
                tip.hideDescription = !StringUtils.isEmpty(tip.id) && GR.Animator.Config.HideTipDescription(tip.id);
            }

            y -= tip.Render(sb, x, y, i) + BOX_EDGE_H * 3.15f;
            offset += offsetChange;
        }
    }

    public static void RenderGeneric(SpriteBatch sb)
    {
        float x = genericTipPos.x;
        float y = genericTipPos.y;
        for (int i = 0; i < genericTooltips.size(); i++)
        {
            final EYBCardTooltip tip = genericTooltips.get(i);
            if (tip.hideDescription == null)
            {
                tip.hideDescription = !StringUtils.isEmpty(tip.id) && GR.Animator.Config.HideTipDescription(tip.id);
            }

            if (!tip.hideDescription)
            {
                y -= tip.Render(sb, x, y, i) + BOX_EDGE_H * 3.15f;
            }
        }
    }

    public boolean Is(EYBCardTooltip tooltip)
    {
        return tooltip != null && id.equals(tooltip.id);
    }

    public float Render(SpriteBatch sb, float x, float y, int index)
    {
        if (hideDescription == null)
        {
            JUtils.LogWarning(this, "hideDescription was null, why?");
            hideDescription = !StringUtils.isEmpty(id) && GR.Animator.Config.HideTipDescription(id);
        }

        BitmapFont descriptionFont = card == null ? FontHelper.tipBodyFont : EYBFontHelper.CardTooltipFont;

        final float textHeight = RenderHelpers.GetSmartHeight(playerClass, descriptionFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING);
        final float h = (hideDescription || StringUtils.isEmpty(description)) ? (-40f * Settings.scale) : (-textHeight - 7f * Settings.scale);

        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

        if (icon != null)
        {
            // To render it on the right: x + BOX_W - TEXT_OFFSET_X - 28 * Settings.scale
            renderTipEnergy(sb, icon, x + TEXT_OFFSET_X, y + ORB_OFFSET_Y, 28 * iconMulti_W, 28 * iconMulti_H);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, JUtils.Capitalize(title), x + TEXT_OFFSET_X * 2.5f, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, JUtils.Capitalize(title), x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }

        if (!StringUtils.isEmpty(description))
        {
            if (card != null && StringUtils.isNotEmpty(id) && !inHand && index >= 0)
            {
                FontHelper.renderFontRightTopAligned(sb, descriptionFont, "Alt+" + (index + 1), x + BODY_TEXT_WIDTH * 1.07f, y + HEADER_OFFSET_Y * 1.33f, Settings.PURPLE_COLOR);
            }
            else if (subText != null)
            {
                FontHelper.renderFontRightTopAligned(sb, descriptionFont, subText.text, x + BODY_TEXT_WIDTH * 1.07f, y + HEADER_OFFSET_Y * 1.33f, subText.color);
            }

            if (!hideDescription)
            {
                RenderHelpers.WriteSmartText(playerClass, sb, descriptionFont, description, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);
            }
        }

        return h;
    }

    public EYBCardTooltip SetIconSizeMulti(float w, float h)
    {
        this.iconMulti_W = w;
        this.iconMulti_H = h;

        return this;
    }

    public EYBCardTooltip SetIcon(AbstractBlight blight)
    {
        return SetIcon(blight.img, 4);
    }

    public EYBCardTooltip SetIcon(AbstractRelic relic)
    {
        return SetIcon(relic.img, 4);
    }

    public EYBCardTooltip SetIcon(TextureRegion region)
    {
        this.icon = region;

        return this;
    }

    public EYBCardTooltip SetIcon(TextureRegion region, int div)
    {
        this.icon = RenderHelpers.GetCroppedRegion(region, div);

        return this;
    }

    public EYBCardTooltip SetIcon(Texture texture, int div)
    {
        this.icon = RenderHelpers.GetCroppedRegion(texture, div);

        return this;
    }

    public EYBCardTooltip SetText(String title, String description)
    {
        if (title != null)
        {
            this.title = title;
        }
        if (description != null)
        {
            this.description = description;
        }

        return this;
    }

    public EYBCardTooltip ShowText(boolean value)
    {
        this.canRender = value;

        return this;
    }

    public EYBCardTooltip SetKeyword(boolean value)
    {
        this.isKeyword = value;

        return this;
    }

    public void renderTipEnergy(SpriteBatch sb, TextureRegion region, float x, float y, float width, float height)
    {
        sb.setColor(Color.WHITE);
        sb.draw(region.getTexture(), x, y, 0f, 0f,
                width, height, Settings.scale, Settings.scale, 0f,
                region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                region.getRegionHeight(), false, false);
    }

    public String GetTitleOrIcon()
    {
        return (id != null) ? "[" + id + "]" : title;
    }

    @Override
    public String toString()
    {
        return GetTitleOrIcon();
    }
}