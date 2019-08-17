package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.Synergy;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.utilities.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingleCardViewPopupPatches
{
    private static Field cardField;
    private static Method renderDynamicFrameMethod;

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle")
    public static class CardHeaderSingleView
    {
        @SpireInsertPatch(rloc = 0, localvars = {"card"})
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
        {
            AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
            if (c != null)
            {
                Synergy synergy = c.GetSynergy();

                if (synergy != null && !c.isFlipped)
                {
                    BitmapFont.BitmapFontData fontData = FontHelper.SCP_cardTitleFont_small.getData();

                    float originalScale = fontData.scaleX;
                    float scaleMulti = 0.8f;

                    int length = synergy.NAME.length();
                    if (length > 20)
                    {
                        scaleMulti -= 0.02f * (length - 20);
                        if (scaleMulti < 0.5f)
                        {
                            scaleMulti = 0.5f;
                        }
                    }

                    fontData.setScale(scaleMulti);
                    Color textColor = Settings.CREAM_COLOR.cpy();

                    float xPos = (float)Settings.WIDTH / 2.0F + (10 * Settings.scale);
                    float yPos = (float)Settings.HEIGHT / 2.0F + ((338.0F + 55) * Settings.scale);

                    FontHelper.renderRotatedText(sb, FontHelper.SCP_cardTitleFont_small, synergy.NAME,
                            xPos, yPos, 0.0F, 0,
                            c.angle, true, textColor);

                    fontData.setScale(originalScale);
                }

            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class SingleCardViewPopup_RenderFrame
    {
        @SpirePrefixPatch
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb) throws InvocationTargetException, IllegalAccessException
        {
            AbstractCard card = (AbstractCard) cardField.get(__instance);
            Texture tmpImg;
            float tOffset;
            float tWidth;

            if (!(card instanceof AnimatorCard) || card.rarity != AbstractCard.CardRarity.SPECIAL)
            {
                return SpireReturn.Continue();
            }

            label36:
            switch (card.type)
            {
                case ATTACK:
                    tWidth = AbstractCard.typeWidthAttack;
                    tOffset = AbstractCard.typeOffsetAttack;
                    tmpImg = Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL_L;
                    break;
//                    switch (card.rarity)
//                    {
////                        case SPECIAL:
////                            tmpImg = Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL_L;
////                            break label36;
//                        case COMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_COMMON_L;
//                            break label36;
//                        case UNCOMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_UNCOMMON_L;
//                            break label36;
//                        case RARE:
//                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_RARE_L;
//                            break label36;
//                        default:
//                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_COMMON_L;
//                            break label36;
//                    }

                case POWER:
                    tWidth = AbstractCard.typeWidthPower;
                    tOffset = AbstractCard.typeOffsetPower;
                    tmpImg = Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL_L;
                    break;
//                    switch (card.rarity)
//                    {
//                        case SPECIAL:
//                            tmpImg = Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL_L;
//                            break label36;
//                        case COMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_POWER_COMMON_L;
//                            break label36;
//                        case UNCOMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_POWER_UNCOMMON_L;
//                            break label36;
//                        case RARE:
//                            tmpImg = ImageMaster.CARD_FRAME_POWER_RARE_L;
//                            break label36;
//                        default:
//                            tmpImg = ImageMaster.CARD_FRAME_POWER_COMMON_L;
//                            break label36;
//                    }

                case SKILL:
                default:
                    tWidth = AbstractCard.typeWidthSkill;
                    tOffset = AbstractCard.typeOffsetSkill;
                    tmpImg = Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL_L;
                    break;
//                    switch (card.rarity)
//                    {
//                        case SPECIAL:
//                            tmpImg = Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL_L;
//                            break label36;
//                        case COMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
//                            break label36;
//                        case UNCOMMON:
//                            tmpImg = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L;
//                            break label36;
//                        case RARE:
//                            tmpImg = ImageMaster.CARD_FRAME_SKILL_RARE_L;
//                            break label36;
//                        default:
//                            tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
//                            break label36;
//                    }
            }

            //renderHelper(card, sb, sb.getColor(), tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, card.drawScale);
            sb.draw(tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);

            //renderDynamicFrameMethod.invoke(__instance, sb, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, tOffset, tWidth);

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBack")
    public static class SingleCardViewPopup_RenderCardBack
    {
        @SpirePrefixPatch
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb) throws IllegalAccessException
        {
            AbstractCard card = (AbstractCard) cardField.get(__instance);

            if (!(card instanceof AnimatorCard_UltraRare))
            {
                return SpireReturn.Continue();
            }

            TextureAtlas.AtlasRegion tmpImg;
            float tOffset;
            float tWidth;

            switch (card.type)
            {
                case ATTACK:
                    tmpImg = ImageMaster.CARD_ATTACK_BG_GRAY_L;
                    //tmpImg = Resources_Animator.CARD_BG_ATTACK_L;
                    break;

                case POWER:
                    tmpImg = ImageMaster.CARD_POWER_BG_GRAY_L;
                    //tmpImg = Resources_Animator.CARD_BG_POWER_L;
                    break;

                default:
                    tmpImg = ImageMaster.CARD_SKILL_BG_GRAY_L;
                    //tmpImg = Resources_Animator.CARD_BG_SKILL_L;
                    break;
            }

            if (tmpImg != null)
            {
                Color tmp = sb.getColor();
                sb.setColor(AnimatorCard_UltraRare.RENDER_COLOR2);
                renderHelper(sb, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, tmpImg);
                sb.setColor(tmp);
                //renderHelper(card, sb, AnimatorCard_UltraRare.RENDER_COLOR2, tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, card.drawScale);
                //sb.draw(tmpImg, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
                //sb.draw(tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
            }

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCost")
    public static class SingleCardViewPopup_RenderCost
    {
        private static final Texture ORB_TEXTURE = new Texture(Resources_Animator_Images.ORB_B_PNG);

        @SpirePrefixPatch
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb) throws IllegalAccessException
        {
            AbstractCard card = (AbstractCard) cardField.get(__instance);
            Texture tmpImg;
            float tOffset;
            float tWidth;

            if (!(card instanceof AnimatorCard_UltraRare))
            {
                return SpireReturn.Continue();
            }

            if (!card.isLocked && card.isSeen)
            {
                if (card.cost > -2)
                {
                    sb.draw(ORB_TEXTURE, (float) Settings.WIDTH / 2.0F - 82.0F - 270.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F - 82.0F + 380.0F * Settings.scale, 82.0F, 82.0F, 164.0F, 164.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 164, 164, false, false);
                }

                Color c;
                if (card.isCostModified)
                {
                    c = Settings.GREEN_TEXT_COLOR;
                }
                else
                {
                    c = Settings.CREAM_COLOR;
                }

                switch (card.cost)
                {
                    case -2:
                        break;
                    case -1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, "X", 666.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                    case 0:
                    default:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(card.cost), 668.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                    case 1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(card.cost), 674.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                }
            }

            return SpireReturn.Return(null);
        }
    }

    private static void renderHelper(SpriteBatch sb, float x, float y, TextureAtlas.AtlasRegion img)
    {
        if (img != null)
        {
            sb.draw(img, x + img.offsetX - (float) img.originalWidth / 2.0F, y + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, Settings.scale, Settings.scale, 0.0F);
        }
    }

    static
    {
        try
        {
            renderDynamicFrameMethod = SingleCardViewPopup.class.getDeclaredMethod("renderDynamicFrame", SpriteBatch.class, float.class, float.class, float.class, float.class);
            renderDynamicFrameMethod.setAccessible(true);
            cardField = SingleCardViewPopup.class.getDeclaredField("card");
            cardField.setAccessible(true);
        }
        catch (NoSuchFieldException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }
}
