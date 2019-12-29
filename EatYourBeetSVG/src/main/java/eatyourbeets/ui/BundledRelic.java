package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.ultrarare.Hero;
import eatyourbeets.effects.player.ObtainRelicEffect;
import eatyourbeets.relics.animator.unnamedReign.AncientMedallion;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.relics.animator.CursedGlyph;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class BundledRelic
{
    public final String cardID;
    public AbstractCard card;

    private static final String[] text = AnimatorResources_Strings.Rewards.TEXT;

    private final AbstractRelic.RelicTier relicTier;
    private final String relicID;
    private final float chance;

    private float roll;
    private AbstractRelic relic;

    public BundledRelic(String cardID, String relicID, AbstractRelic.RelicTier tier, float chance)
    {
        this.chance = chance;
        this.cardID = cardID;
        this.relicTier = tier;
        this.relicID = relicID;
    }

    public BundledRelic Clone(float roll)
    {
        boolean hasHero = false;
        float chance = this.chance;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.getAttacks().group)
        {
            if (c instanceof Hero)
            {
                chance += 16;
                hasHero = true;
            }
        }

        if (GameUtilities.GetActualAscensionLevel() > 6 && !hasHero)
        {
            if (chance < 50)
            {
                chance = 0;
            }
        }

        //JavaUtilities.Logger.info(cardID + ", Rolled: " + roll + " (" + chance + ")");
        BundledRelic bundledRelic = new BundledRelic(cardID, relicID, relicTier, chance);
        bundledRelic.roll = roll;

        return bundledRelic;
    }

    public void Open()
    {
        relic = null;

        if (roll < chance)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                relic = new AncientMedallion();
                relic.flash();
                return;
            }

            if (relicID.equals(CursedGlyph.ID))
            {
                relic = new CursedGlyph();
                relic.flash();
                return;
            }

            ArrayList<String> relicPool = GetRelicPool();
            if (relicPool == null || relicPool.contains(relicID))
            {
                for (AbstractRelic r : AbstractDungeon.player.relics)
                {
                    if (r.relicId.equals(relicID))
                    {
                        JavaUtilities.Logger.info(relicID + " Skipped");
                        return;
                    }
                }

                JavaUtilities.Logger.info(relicID + " Created");
                relic = RelicLibrary.getRelic(relicID).makeCopy();
                relic.flash();
            }
        }
    }

    public void Update(CardRewardScreen screen)
    {
        if (relic != null)
        {
            card.hb.height = AbstractCard.IMG_HEIGHT * 0.6f;
            relic.hb.width = AbstractCard.IMG_WIDTH * 0.8f;
            relic.update();
        }
    }

    public void Render(CardRewardScreen screen, SpriteBatch sb)
    {
        if (relic != null)
        {
            float offset_y = (-(AbstractCard.IMG_HEIGHT * 0.45f) * card.drawScale);
            float offset_x = (-(AbstractCard.IMG_WIDTH * 0.45f) * card.drawScale);

            relic.currentX = card.current_x + offset_x;
            relic.currentY = card.current_y + offset_y;
            relic.scale = card.drawScale;
            relic.render(sb);

            BitmapFont font = FontHelper.buttonLabelFont;
            font.getData().setScale(relic.scale * 0.8f);
            if (relicID.equals(CursedGlyph.ID))
            {
                FontHelper.renderFontLeft(sb, font, text[2], card.current_x + (offset_x * 0.66f), card.current_y + offset_y, Color.RED);
            }
            else
            {
                FontHelper.renderFontLeft(sb, font, text[1], card.current_x + (offset_x * 0.66f), card.current_y + offset_y, Color.WHITE);
            }
            font.getData().setScale(1f);

            relic.hb.move(card.current_x, relic.currentY);
            if (relic.hb.hovered)
            {
                relic.renderTip(sb);
            }
        }
    }

    public void Acquired()
    {
        if (relic != null)
        {
            ArrayList<String> relicPool = GetRelicPool();
            if (relicPool != null)
            {
                relicPool.remove(relic.relicId);
            }

            relic.hb.resize(AbstractRelic.PAD_X, AbstractRelic.PAD_X);
            AbstractDungeon.effectsQueue.add(new ObtainRelicEffect(relic));
        }
    }

    private ArrayList<String> GetRelicPool()
    {
        switch (relicTier)
        {
            case COMMON: return AbstractDungeon.commonRelicPool;

            case UNCOMMON: return AbstractDungeon.uncommonRelicPool;

            case RARE: return AbstractDungeon.rareRelicPool;

            case BOSS: return AbstractDungeon.bossRelicPool;

            case SHOP: return AbstractDungeon.shopRelicPool;

            case DEPRECATED:
            case STARTER:
            case SPECIAL:
                default:
                return null;
        }
    }
}
