package eatyourbeets.ui.screens.animator.cardReward;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class AnimatorCardRewardScreen extends GUIElement
{
    public static final Logger logger = LogManager.getLogger(AnimatorCardRewardScreen.class.getName());

    protected final BundledRelicContainer rewardBundle = new BundledRelicContainer();
    protected final AnimatorCardBadgeLegend cardBadgeLegend = new AnimatorCardBadgeLegend();
    protected final AnimatorPurgingStoneUI purgingStoneUI = new AnimatorPurgingStoneUI(rewardBundle::Remove);

    public void Open(ArrayList<AbstractCard> cards, RewardItem rItem, String header)
    {
        if (GameUtilities.InBattle())
        {
            Close();
            return;
        }

        purgingStoneUI.Open(rItem, cards);
        rewardBundle.Open(rItem, cards);
        cardBadgeLegend.Open();
    }

    public void Close()
    {
        cardBadgeLegend.Close();
        rewardBundle.Close();
        purgingStoneUI.Close();
    }

    public void Update()
    {
        purgingStoneUI.TryUpdate();
        rewardBundle.TryUpdate();
        cardBadgeLegend.TryUpdate();
    }

    public void PreRender(SpriteBatch sb)
    {
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