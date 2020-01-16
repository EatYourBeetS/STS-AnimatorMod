package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.unnamed.UnnamedImages;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

public class SingleCardViewPopupPatches
{
    private static final FieldInfo<AbstractCard> cardField = JavaUtilities.GetField("card", SingleCardViewPopup.class);
    private static final AnimatorImages Images = GR.Animator.Images;

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle")
    public static class SingleCardViewPopup_RenderTitle
    {
        @SpireInsertPatch(rloc = 0, localvars = {"card"})
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
        {
            EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
            if (c != null && !c.isFlipped)
            {
                c.renderInSingleCardPopup(sb, false);
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class SingleCardViewPopup_RenderFrame
    {
        @SpireInsertPatch(rloc = 0, localvars = {"card"})
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
        {
            EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
            if (c != null)
            {
                c.renderInSingleCardPopup(sb, true);

                if (c.rarity == AbstractCard.CardRarity.SPECIAL)
                {
                    Texture tmpImg;
                    switch (card.type)
                    {
                        case ATTACK:
                            tmpImg = Images.Textures.CARD_FRAME_ATTACK_SPECIAL_L;
                            break;

                        case POWER:
                            tmpImg = Images.Textures.CARD_FRAME_POWER_SPECIAL_L;
                            break;

                        case SKILL:
                        default:
                            tmpImg = Images.Textures.CARD_FRAME_SKILL_SPECIAL_L;
                            break;
                    }

                    //renderHelper(card, sb, sb.getColor(), tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, card.drawScale);
                    //renderDynamicFrameMethod.invoke(__instance, sb, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, tOffset, tWidth);
                    sb.draw(tmpImg, (float) Settings.WIDTH / 2.0F - 512.0F, (float) Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);

                    return SpireReturn.Return(null);
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBack")
    public static class SingleCardViewPopup_RenderCardBack
    {
        @SpirePrefixPatch
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb)
        {
            AbstractCard card = cardField.Get(__instance);

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
                    //tmpImg = AnimatorResources.CARD_BG_ATTACK_L;
                    break;

                case POWER:
                    tmpImg = ImageMaster.CARD_POWER_BG_GRAY_L;
                    //tmpImg = AnimatorResources.CARD_BG_POWER_L;
                    break;

                default:
                    tmpImg = ImageMaster.CARD_SKILL_BG_GRAY_L;
                    //tmpImg = AnimatorResources.CARD_BG_SKILL_L;
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
        private static final Texture Animator_OrbB = new Texture(GR.Animator.Images.ORB_B_PNG);
        private static final TextureAtlas.AtlasRegion Unnamed_Orb2B = UnnamedImages.ORB_2_ATLAS.findRegion(UnnamedImages.ORB_2B_PNG);

        @SpirePrefixPatch
        public static SpireReturn Method(SingleCardViewPopup __instance, SpriteBatch sb)
        {
            Texture tmpImg;
            float tOffset;
            float tWidth;

            AbstractCard card = cardField.Get(__instance);

            if (card.isLocked || card.isFlipped || !card.isSeen)
            {
                return SpireReturn.Continue();
            }
            else if (card instanceof UnnamedCard)
            {
                UnnamedCard c = (UnnamedCard) card;
                if (c.masteryCost >= 0)
                {
                    renderHelper(sb, Settings.WIDTH / 2.0F + 266.0F * Settings.scale, Settings.HEIGHT / 2.0F + 382.0F * Settings.scale, Unnamed_Orb2B);
                    FontHelper.renderFontCentered(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(c.masteryCost),
                            1228.0F * Settings.scale, Settings.HEIGHT / 2.0F + 390.0F * Settings.scale, Color.WHITE);
                }
            }
            else if (card instanceof AnimatorCard_UltraRare)
            {
                if (card.cost > -2)
                {
                    sb.draw(Animator_OrbB, (float) Settings.WIDTH / 2.0F - 82.0F - 270.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F - 82.0F + 380.0F * Settings.scale, 82.0F, 82.0F, 164.0F, 164.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 164, 164, false, false);
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

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

    private static void renderHelper(SpriteBatch sb, float x, float y, TextureAtlas.AtlasRegion img)
    {
        if (img != null)
        {
            sb.draw(img, x + img.offsetX - (float) img.originalWidth / 2.0F, y + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, Settings.scale, Settings.scale, 0.0F);
        }
    }
}
