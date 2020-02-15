package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.relics.animator.CursedGlyph;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class BundledRelicContainer extends GUIElement
{
    private static final CursedGlyph CURSED_GLYPH = new CursedGlyph();

    private final AnimatorStrings.Rewards REWARDS = GR.Animator.Strings.Rewards;
    private final ArrayList<BundledRelic> bundledRelics;
    private RewardItem rewardItem;

    public BundledRelicContainer()
    {
        this(null);
    }

    public BundledRelicContainer(RewardItem rewardItem)
    {
        this.rewardItem = rewardItem;
        this.bundledRelics = new ArrayList<>();
    }

    public void Open(RewardItem rewardItem, ArrayList<AbstractCard> cards)
    {
        this.rewardItem = rewardItem;
        this.bundledRelics.clear();

        for (AbstractCard card : cards)
        {
            Add(card);
        }
    }

    public void Close()
    {
        rewardItem = null;
        bundledRelics.clear();
    }

    public void Update()
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            bundledRelic.Render(sb);
        }
    }

    public void OnCardObtained(AbstractCard hoveredCard)
    {
        for (BundledRelic bundledRelic : bundledRelics)
        {
            if (bundledRelic.card == hoveredCard)
            {
                bundledRelic.Acquired();
            }
        }
    }

    public void Add(AbstractCard card)
    {
        BundledRelic bundledRelic = GetBundle(card);
        if (bundledRelic != null)
        {
            bundledRelics.add(bundledRelic);
        }
    }

    public void Remove(AbstractCard card)
    {
        for (int i = 0; i < bundledRelics.size(); i++)
        {
            if (bundledRelics.get(i).card == card)
            {
                bundledRelics.remove(i);
                return;
            }
        }
    }

    private BundledRelic GetBundle(AbstractCard card)
    {
        if (card instanceof AnimatorCard_UltraRare)
        {
            return GetCursedRelicBundle(card);
        }

        if (card instanceof AnimatorCard)
        {
            for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
            {
                if (series.promoted && series.bonus < 6)
                {
                    if (series.Cards.containsKey(card.cardID) && GameUtilities.GetMasterDeckInstance(card.cardID) == null)
                    {
                        if (series.bonus % 2 == 0)
                        {
                            return GetGoldBundle(card, series.bonus >= 4 ? 24 : 12);
                        }
                        else
                        {
                            return GetMaxHPBundle(card, series.bonus >= 4 ? 2 : 1);
                        }
                    }
                }
            }
        }

        return null;
    }

    private BundledRelic GetCursedRelicBundle(AbstractCard card)
    {
        return new BundledRelic(card, c -> GameEffects.Queue.ObtainRelic(new CursedGlyph()))
                .SetIcon(CURSED_GLYPH.img, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.52f)
                .SetText(REWARDS.CursedRelic, Settings.RED_TEXT_COLOR, -AbstractCard.RAW_W * 0.10f, -AbstractCard.RAW_H * 0.54f);
    }

    private BundledRelic GetGoldBundle(AbstractCard card, int gold)
    {
        return new BundledRelic(card, this::ReceiveGold).SetAmount(gold)
                .SetIcon(ImageMaster.UI_GOLD, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.545f)
                .SetText(REWARDS.GoldBonus(gold), Color.WHITE, -AbstractCard.RAW_W * 0.165f, -AbstractCard.RAW_H * 0.54f);
    }

    private BundledRelic GetMaxHPBundle(AbstractCard card, int maxHP)
    {
        return new BundledRelic(card, this::ReceiveMaxHP).SetAmount(maxHP)
                .SetIcon(ImageMaster.TP_HP, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.545f)
                .SetText(REWARDS.MaxHPBonus(maxHP), Color.WHITE, -AbstractCard.RAW_W * 0.165f, -AbstractCard.RAW_H * 0.54f);
    }

    private void ReceiveGold(BundledRelic bundle)
    {
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
        {
            if (series.Cards.containsKey(bundle.card.cardID))
            {
                CardCrawlGame.sound.play("GOLD_GAIN");
                AbstractDungeon.player.gainGold(bundle.amount);
                series.bonus += 1;

                TheMissingPiece.RefreshDescription();
                JavaUtilities.Log(this, "Obtained Gold Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }

    private void ReceiveMaxHP(BundledRelic bundle)
    {
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
        {
            if (series.Cards.containsKey(bundle.card.cardID))
            {
                AbstractDungeon.player.increaseMaxHp(bundle.amount, true);
                series.bonus += 1;

                TheMissingPiece.RefreshDescription();
                JavaUtilities.Log(this, "Obtained Max HP Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }
}
