package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.pcl.ultrarare.Cthulhu;
import pinacolada.cards.pcl.ultrarare.HolyGrail;
import pinacolada.cards.pcl.ultrarare.SummoningRitual;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.utilities.PCLRenderHelpers;

import java.util.HashMap;
import java.util.Map;

public abstract class PCLCard_UltraRare extends PCLCard implements Hidden
{
    private static final Map<String, PCLCard_UltraRare> cards = new HashMap<>();
    private static final Color RENDER_COLOR = new Color(0.2f, 0.2f, 0.2f, 1);

    protected PCLCard_UltraRare(PCLCardData data)
    {
        super(data);

        SetUnique(true, false);
    }

    public static Map<String, PCLCard_UltraRare> GetCards()
    {
        if (cards.isEmpty())
        {
            for (PCLLoadout loadout : GR.PCL.Data.GetEveryLoadout())
            {
                PCLCardData data = loadout.GetUltraRare();
                if (data != null)
                {
                    AbstractCard card = data.CreateNewInstance();
                    if (card instanceof PCLCard_UltraRare)
                    {
                        cards.put(card.cardID, (PCLCard_UltraRare) card);
                    }
                    else
                    {
                        throw new RuntimeException("GetUltraRare() should return an instance of PCLCard_UltraRare");
                    }
                }
            }

            cards.put(Cthulhu.DATA.ID, new Cthulhu());
            cards.put(HolyGrail.DATA.ID, new HolyGrail());
            cards.put(SummoningRitual.DATA.ID, new SummoningRitual());
        }

        return cards;
    }

    public static PCLCardData GetCardData(PCLLoadout loadout)
    {
        return loadout == null ? null : loadout.Series == null ? Cthulhu.DATA : loadout.GetUltraRare();
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
        PCLRenderHelpers.DrawGrayscale(sb, () -> {
            PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), RENDER_COLOR, transparency, popUpMultiplier);
            return true;});
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture((isPopup ?
                GR.PCL.Images.CARD_BANNER_ATTRIBUTE_L: GR.PCL.Images.CARD_BANNER_ATTRIBUTE).Texture(),
                Color.WHITE.cpy());
    }
}