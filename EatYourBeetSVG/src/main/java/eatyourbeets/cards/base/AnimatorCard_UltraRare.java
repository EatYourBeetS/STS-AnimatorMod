package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.ultrarare.Azami;
import eatyourbeets.cards.animator.ultrarare.Cthulhu;
import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimatorCard_UltraRare extends AnimatorCard implements Hidden
{
    private static final Map<String, AnimatorCard_UltraRare> cards = new HashMap<>();
    private static final Color RENDER_COLOR = new Color(0.4f, 0.4f, 0.4f, 1);

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetMaxCopies(1);
    }

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

            cards.put(Azami.DATA.ID, new Azami());
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
        _renderColor.Set(this, RENDER_COLOR);

        super.renderCardBg(sb, x, y);

        _renderColor.Set(this, temp);
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return ANIMATOR_IMAGES.CARD_ENERGY_ORB_ANIMATOR.Texture();
    }

//    @Override
//    protected ColoredTexture GetCardBanner()
//    {
//        return new ColoredTexture(IMAGES.CARD_BANNER_ULTRARARE.Texture());
//    }
}