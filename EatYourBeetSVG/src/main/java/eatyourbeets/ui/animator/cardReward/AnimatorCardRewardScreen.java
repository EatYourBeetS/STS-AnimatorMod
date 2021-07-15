package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class AnimatorCardRewardScreen extends GUIElement
{
    public static final AnimatorCardRewardScreen Instance = new AnimatorCardRewardScreen();

    public final AnimatorCardRewardBonus rewardBundle = new AnimatorCardRewardBonus();
    public final AnimatorCardRewardInfo cardBadgeLegend = new AnimatorCardRewardInfo();
    public final AnimatorCardRewardBanish purgingStoneUI = new AnimatorCardRewardBanish(rewardBundle::Add, rewardBundle::Remove);

    public void Open(ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        if (GameUtilities.InBattle())
        {
            Close();
            return;
        }

        GR.UI.CardAffinities.Open(AbstractDungeon.player.masterDeck.group, null);

        rewardBundle.Open(rItem, cards);
        purgingStoneUI.Open(rItem, cards);
        cardBadgeLegend.Open();
    }

    public void Close()
    {
        GR.UI.CardAffinities.Close();

        cardBadgeLegend.Close();
        rewardBundle.Close();
        purgingStoneUI.Close();
    }

    public void Update()
    {
        GR.UI.CardAffinities.TryUpdate();

        purgingStoneUI.TryUpdate();
        rewardBundle.TryUpdate();
        cardBadgeLegend.TryUpdate();
    }

    public void PreRender(SpriteBatch sb)
    {
        GR.UI.CardAffinities.TryRender(sb);

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