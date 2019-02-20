package eatyourbeets.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.Utilities;

import java.util.ArrayList;
import java.util.Random;

public class BundledRelic
{
    public final String cardID;
    public AbstractCard card;

    private final AbstractRelic.RelicTier relicTier;
    private final String relicID;
    private final int chance;

    private int roll;
    private AbstractRelic relic;

    public BundledRelic(String cardID, String relicID, AbstractRelic.RelicTier tier, int chance)
    {
        this.chance = chance;
        this.cardID = cardID;
        this.relicTier = tier;
        this.relicID = relicID;
    }

    public BundledRelic Clone(int roll, AbstractCard card)
    {
        Utilities.Logger.info(cardID + ", " + relicID + ", Rolled: " + roll + " (" + chance + ")");
        BundledRelic bundledRelic = new BundledRelic(cardID, relicID, relicTier, chance);
        bundledRelic.roll = roll;
        bundledRelic.card = card;

        return bundledRelic;
    }

    public void Open()
    {
//        for (BundledRelic b : bundledRelics)
//        {
//            if (b.relic != null && b.relic.relicId.equals(relicID))
//            {
//                return;
//            }
//        }

        relic = null;

        if (roll < chance)
        {
            ArrayList<String> relicPool = GetRelicPool();
            if (relicPool == null || relicPool.contains(relicID))
            {
                for (AbstractRelic r : AbstractDungeon.player.relics)
                {
                    if (r.relicId.equals(relicID))
                    {
                        Utilities.Logger.info(relicID + " Skipped");
                        return;
                    }
                }
                Utilities.Logger.info(relicID + " Created");
                relic = RelicLibrary.getRelic(relicID).makeCopy();
            }
        }
    }

    public void Update(CardRewardScreen screen)
    {
        if (relic != null)
        {
            card.hb.height = AbstractCard.IMG_HEIGHT * 0.6f;
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

            relic.hb.move(relic.currentX, relic.currentY);
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

            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
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
