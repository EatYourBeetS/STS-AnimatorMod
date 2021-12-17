package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import pinacolada.resources.GR;
import pinacolada.ui.GUIElement;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class PCLCardRewardScreen extends GUIElement
{
    public static final PCLCardRewardScreen Instance = new PCLCardRewardScreen();

    public final PCLCardRewardBonus rewardBundle = new PCLCardRewardBonus();
    public final PCLCardRewardInfo cardBadgeLegend = new PCLCardRewardInfo();
    public final PCLCardRewardReroll purgingStoneUI = new PCLCardRewardReroll(rewardBundle::Add, rewardBundle::Remove);

    public void Open(ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        if (PCLGameUtilities.InBattle(true) || cards == null || rItem == null)
        {
            Close();
            return;
        }

        GR.UI.CardAffinities.Open(AbstractDungeon.player.masterDeck.group);
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