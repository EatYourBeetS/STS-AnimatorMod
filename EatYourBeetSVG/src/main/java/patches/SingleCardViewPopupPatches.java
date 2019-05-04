package patches;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.AnimatorCard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingleCardViewPopupPatches
{
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class SingleCardViewPopup_RenderCardBack
    {
        private static Field cardField;
        private static Method renderDynamicFrameMethod;

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
                            tmpImg = AnimatorResources.CARD_FRAME_ATTACK_SPECIAL_L;
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
                            tmpImg = AnimatorResources.CARD_FRAME_POWER_SPECIAL_L;
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
                            tmpImg = AnimatorResources.CARD_FRAME_SKILL_SPECIAL_L;
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
}
