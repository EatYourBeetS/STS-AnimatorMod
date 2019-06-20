package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingleCardViewPopupPatches
{
    private static Field cardField;
    private static Method renderDynamicFrameMethod;

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

            if (!(card instanceof AnimatorCard))
            {
                return SpireReturn.Continue();
            }

            label36:
            switch (card.type)
            {
                case ATTACK:
                    tWidth = AbstractCard.typeWidthAttack;
                    tOffset = AbstractCard.typeOffsetAttack;
                    switch (card.rarity)
                    {
                        case SPECIAL:
                            tmpImg = Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL_L;
                            break label36;
                        case COMMON:
                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_COMMON_L;
                            break label36;
                        case UNCOMMON:
                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_UNCOMMON_L;
                            break label36;
                        case RARE:
                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_RARE_L;
                            break label36;
                        default:
                            tmpImg = ImageMaster.CARD_FRAME_ATTACK_COMMON_L;
                            break label36;
                    }

                case POWER:
                    tWidth = AbstractCard.typeWidthPower;
                    tOffset = AbstractCard.typeOffsetPower;
                    switch (card.rarity)
                    {
                        case SPECIAL:
                            tmpImg = Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL_L;
                            break label36;
                        case COMMON:
                            tmpImg = ImageMaster.CARD_FRAME_POWER_COMMON_L;
                            break label36;
                        case UNCOMMON:
                            tmpImg = ImageMaster.CARD_FRAME_POWER_UNCOMMON_L;
                            break label36;
                        case RARE:
                            tmpImg = ImageMaster.CARD_FRAME_POWER_RARE_L;
                            break label36;
                        default:
                            tmpImg = ImageMaster.CARD_FRAME_POWER_COMMON_L;
                            break label36;
                    }

                case SKILL:
                default:
                    tWidth = AbstractCard.typeWidthSkill;
                    tOffset = AbstractCard.typeOffsetSkill;
                    switch (card.rarity)
                    {
                        case SPECIAL:
                            tmpImg = Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL_L;
                            break label36;
                        case COMMON:
                            tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
                            break label36;
                        case UNCOMMON:
                            tmpImg = ImageMaster.CARD_FRAME_SKILL_UNCOMMON_L;
                            break label36;
                        case RARE:
                            tmpImg = ImageMaster.CARD_FRAME_SKILL_RARE_L;
                            break label36;
                        default:
                            tmpImg = ImageMaster.CARD_FRAME_SKILL_COMMON_L;
                            break label36;
                    }
            }

            sb.draw(tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);

            renderDynamicFrameMethod.invoke(__instance, sb, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, tOffset, tWidth);

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
            Texture tmpImg;
            float tOffset;
            float tWidth;

            if (!(card instanceof AnimatorCard_UltraRare))
            {
                return SpireReturn.Continue();
            }

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
                sb.draw(tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
                sb.setColor(tmp);
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

//    @SpireInsertPatch(rloc = 0, localvars={"card"})
//    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
//    {
//        if (card instanceof AnimatorCard){
//            // render header
//        }
//    }

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
