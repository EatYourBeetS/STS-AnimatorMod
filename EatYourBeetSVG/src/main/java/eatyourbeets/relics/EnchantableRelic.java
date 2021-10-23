package eatyourbeets.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.animator.EnchantmentReward;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public abstract class EnchantableRelic extends AnimatorRelic implements OnReceiveRewardsListener // implements CustomSavable<Integer> NOTE: I do not implement this here because CustomSavable patch does not check abstract classes
{
    public static final int MAX_CHOICES = 3;
    public Enchantment enchantment1;
    public Enchantment enchantment2;
    public static final int MAX_OPTIONS = 100;

    public EnchantableRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, tier, sfx, null);
    }

    public EnchantableRelic(String id, RelicTier tier, LandingSound sfx, Enchantment enchantment)
    {
        super(id, tier, sfx);

        if (enchantment != null)
        {
            ApplyEnchantment(enchantment);
        }
    }

    public Integer onSave()
    {
        int enchantment1 = (this.enchantment1 != null) ? this.enchantment1.index : 0;
        int enchantment2 = (this.enchantment2 != null) ? this.enchantment2.index : 0;

        return  ((enchantment1 * MAX_OPTIONS) + enchantment2);
    }

    public void onLoad(Integer index)
    {
        if (index != null && index > 0)
        {
            JUtils.LogInfo(this, "onLoad:" + index);

            int card1Index = index / MAX_OPTIONS;
            int card2Index = index % MAX_OPTIONS;

            if (card1Index > 0) {
                ApplyEnchantment(Enchantment.GetCard(1, card1Index));
            }
            if (card2Index > 0) {
                ApplyEnchantment(Enchantment.GetCard(2, card2Index));
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        super.renderTip(sb);

        if (enchantment1 != null)
        {
            enchantment1.drawScale = enchantment1.targetDrawScale = 0.8f;
            enchantment1.current_x = enchantment1.target_x = InputHelper.mX + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * EYBCardTooltip.BOX_W);
            enchantment1.current_y = enchantment1.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
            GR.UI.AddPostRender(enchantment1::render);
        }

        if (enchantment2 != null)
        {
            enchantment2.drawScale = enchantment2.targetDrawScale = 0.8f;
            enchantment2.current_x = enchantment2.target_x = InputHelper.mX + ((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? (AbstractCard.IMG_WIDTH * -0.5f) - 0.5f : (AbstractCard.IMG_WIDTH * 0.5f) + 0.5f) + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * EYBCardTooltip.BOX_W);
            enchantment2.current_y = enchantment2.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
            GR.UI.AddPostRender(enchantment2::render);
        }
    }

    public void ApplyEnchantment(Enchantment enchantment)
    {
        if (enchantment.level == 1) {
            this.enchantment1 = enchantment;
        }
        else if (enchantment.level == 2) {
            this.enchantment2 = enchantment;
        }
        RefreshTexture();
    }

    public CardGroup CreateUpgradeGroup()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final WeightedList<Enchantment> possiblePicks;

        if (enchantment1 == null)
        {
            //Level 1
            possiblePicks = Enchantment.GetLv1Cards();
        }
        else
        {
            //Level 2
            possiblePicks = Enchantment.GetLv2Cards();
        }

        for (int i = 0; i < MAX_CHOICES; i++) {
            group.group.add(possiblePicks.Retrieve(rng));
        }

        return group;
    }

    public int GetEnchantmentLevel()
    {
        return enchantment1 == null ? 0 : enchantment2 == null ? enchantment1.level : enchantment2.level;
    }

    public void RefreshTexture()
    {
        setTexture(GR.GetTexture(GR.GetRelicImage(relicId)));
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        if (enchantment1 != null)
        {
            enchantment1.OnStartOfBattle();
        }

        if (enchantment2 != null)
        {
            enchantment2.OnStartOfBattle();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        final EnchantableRelic copy = (EnchantableRelic) super.makeCopy();
        if (enchantment1 != null)
        {
            copy.ApplyEnchantment((Enchantment) enchantment1.makeStatEquivalentCopy());
        }
        if (enchantment2 != null)
        {
            copy.ApplyEnchantment((Enchantment) enchantment2.makeStatEquivalentCopy());
        }

        return copy;
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards) {
        if (GetEnchantmentLevel() < 2) {
            EnchantmentReward.TryAddReward(this, rewards);
        }
    }
}
