package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.UnnamedResources_Images;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

public class AbstractCardPatches
{
    private static FieldInfo<Boolean> DarkenField = JavaUtilities.GetPrivateField("darken", AbstractCard.class);
    private static TextureAtlas.AtlasRegion Orb2A = UnnamedResources_Images.ORB_2_ATLAS.findRegion(UnnamedResources_Images.ORB_2A_PNG);

    @SpirePatch(clz=AbstractCard.class, method="renderEnergy")
    public static class AbstractCardPatches_renderEnergy
    {
        @SpirePostfixPatch
        public static void Method(AbstractCard __instance, SpriteBatch sb)
        {
            UnnamedCard card = JavaUtilities.SafeCast(__instance, UnnamedCard.class);
            if (card != null && card.masteryCost > -2 && !DarkenField.Get(card) && !card.isLocked && card.isSeen)
            {
                Color costColor = Color.WHITE.cpy();

                if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(card) && !PlayerStatistics.Void.CanUse(card))
                {
                    costColor = new Color(1.0F, 0.3F, 0.3F, 1.0F); //AbstractCard.ENERGY_COST_RESTRICTED_COLOR;
                }

                costColor.a = card.transparency;

                FontHelper.cardEnergyFont_L.getData().setScale(card.drawScale);
                renderHelper(sb, Orb2A, card.current_x, card.current_y, card);
                FontHelper.renderRotatedText(sb, FontHelper.cardEnergyFont_L, card.getMasteryCostString(), card.current_x, card.current_y,
                        138.0F * card.drawScale * Settings.scale, 190.0F * card.drawScale * Settings.scale,
                        card.angle, false, costColor);
            }
        }

        private static void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY, AbstractCard card)
        {
            sb.setColor(Color.WHITE);
            sb.draw(img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F,
                    img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY,
                    img.packedWidth, img.packedHeight, card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
        }
    }
}
