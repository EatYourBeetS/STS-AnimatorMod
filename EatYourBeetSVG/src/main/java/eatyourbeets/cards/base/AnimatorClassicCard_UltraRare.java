package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatorClassicCard_UltraRare extends AnimatorClassicCard
{
    private static final Map<String, AnimatorClassicCard_UltraRare> cards = new HashMap<>();
    private static final Color RENDER_COLOR = new Color(0.4f, 0.4f, 0.4f, 1);

    protected AnimatorClassicCard_UltraRare(EYBCardData cardData)
    {
        super(cardData);

        SetUnique(true, false);
    }

    protected static EYBCardData Register(Class<? extends AnimatorClassicCard> type)
    {
        return AnimatorClassicCard.Register(type).SetMaxCopies(1);
    }

    public static void MarkAsSeen(String cardID)
    {
        if (!IsSeen(cardID))
        {
            UnlockTracker.seenPref.putInteger(cardID, 2);
            UnlockTracker.seenPref.flush();
        }
    }

    public static boolean IsSeen(String cardID)
    {
        return UnlockTracker.seenPref.getInteger(cardID, 0) >= 1;
    }

    public static Map<String, AnimatorClassicCard_UltraRare> GetCards()
    {
        if (cards.isEmpty())
        {
            for (AnimatorCard_UltraRare card : AnimatorCard_UltraRare.GetCards().values())
            {
                EYBCardData data = GameUtilities.GetReplacement(GR.AnimatorClassic.PlayerClass, card.cardID);
                if (data != null)
                {
                    cards.put(data.ID, (AnimatorClassicCard_UltraRare)data.CreateNewInstance());
                }
            }
        }

        return cards;
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        final Color temp = _renderColor.Get(this);
        RENDER_COLOR.a = this.transparency;
        _renderColor.Set(this, RENDER_COLOR);

        super.renderCardBg(sb, x, y);

        _renderColor.Set(this, temp);
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return ANIMATOR_IMAGES.CARD_ENERGY_ORB_ANIMATOR.Texture();
    }
}