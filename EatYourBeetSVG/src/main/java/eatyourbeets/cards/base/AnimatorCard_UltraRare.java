package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.ultrarare.Cthulhu;
import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.RenderHelpers;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatorCard_UltraRare extends AnimatorCard implements Hidden
{
    private static final Map<String, AnimatorCard_UltraRare> cards = new HashMap<>();
    private static final Color RENDER_COLOR = new Color(0.2f, 0.2f, 0.2f, 1);

    protected AnimatorCard_UltraRare(EYBCardData data)
    {
        super(data);

        SetUnique(true, false);
    }

    public static Map<String, AnimatorCard_UltraRare> GetCards()
    {
        if (cards.isEmpty())
        {
            for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
            {
                EYBCardData data = loadout.GetUltraRare();
                if (data != null)
                {
                    AbstractCard card = data.CreateNewInstance();
                    if (card instanceof AnimatorCard_UltraRare)
                    {
                        cards.put(card.cardID, (AnimatorCard_UltraRare) card);
                    }
                    else
                    {
                        throw new RuntimeException("AnimatorLoadout.GetUltraRare() should return an instance of AnimatorCard_UltraRare");
                    }
                }
            }

            cards.put(Cthulhu.DATA.ID, new Cthulhu());
            cards.put(HolyGrail.DATA.ID, new HolyGrail());
            cards.put(SummoningRitual.DATA.ID, new SummoningRitual());
        }

        return cards;
    }

    public static EYBCardData GetCardData(AnimatorLoadout loadout)
    {
        return loadout == null ? Cthulhu.DATA : loadout.GetUltraRare();
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

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        final Color temp = _renderColor.Get(this);
        RENDER_COLOR.a = this.transparency;
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        RenderHelpers.DrawGrayscale(sb, () -> {
            RenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), RENDER_COLOR, transparency, popUpMultiplier);
            return true;});
    }

//    @Override
//    protected ColoredTexture GetCardBanner()
//    {
//        return new ColoredTexture(IMAGES.CARD_BANNER_ULTRARARE.Texture());
//    }
}