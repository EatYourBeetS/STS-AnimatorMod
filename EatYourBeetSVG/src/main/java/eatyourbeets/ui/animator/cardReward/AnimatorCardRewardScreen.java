package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class AnimatorCardRewardScreen extends GUIElement
{
    protected final AnimatorCardRewardBonus rewardBundle = new AnimatorCardRewardBonus();
    protected final AnimatorCardRewardInfo cardBadgeLegend = new AnimatorCardRewardInfo();
    protected final AnimatorCardRewardAlignments cardAlignments = new AnimatorCardRewardAlignments();
    protected final AnimatorCardRewardBanish purgingStoneUI = new AnimatorCardRewardBanish(rewardBundle::Add, rewardBundle::Remove);

    public void Open(ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        if (GameUtilities.InBattle())
        {
            Close();
            return;
        }

        rewardBundle.Open(rItem, cards);
        purgingStoneUI.Open(rItem, cards);
        cardAlignments.Open();
        cardBadgeLegend.Open();
    }

    public void Close()
    {
        cardBadgeLegend.Close();
        cardAlignments.Close();
        rewardBundle.Close();
        purgingStoneUI.Close();
    }

    public void Update()
    {
        purgingStoneUI.TryUpdate();
        rewardBundle.TryUpdate();
        cardAlignments.TryUpdate();
        cardBadgeLegend.TryUpdate();
    }

    public void PreRender(SpriteBatch sb)
    {
        cardAlignments.TryRender(sb);
        cardBadgeLegend.TryRender(sb);
        purgingStoneUI.TryRender(sb);
    }

    public void Render(SpriteBatch sb)
    {
        rewardBundle.TryRender(sb);
    }

    public void OnCardObtained(AbstractCard hoveredCard)
    {
        rewardBundle.OnCardObtained(hoveredCard);
    }
}