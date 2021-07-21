package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private static final Color RENDER_COLOR = new Color(0.4f, 0.4f, 0.4f, 1);

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
        RENDER_COLOR.a = this.transparency;
        switch (type)
        {
            case ATTACK:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_ATTACK_UR.Texture(), x, y);
                break;
            case SKILL:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_SKILL_UR.Texture(), x, y);
                break;
            case POWER:
                RenderHelpers.DrawOnCardCentered(sb, this, RENDER_COLOR, GR.Animator.Images.CARD_BACKGROUND_POWER_UR.Texture(), x, y);
                break;
            default:
                super.renderCardBg(sb, x, y);
                break;
        }
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return IMAGES.CARD_ENERGY_ORB_A.Texture();
    }

//    @Override
//    protected ColoredTexture GetCardBanner()
//    {
//        return new ColoredTexture(IMAGES.CARD_BANNER_ULTRARARE.Texture());
//    }
}