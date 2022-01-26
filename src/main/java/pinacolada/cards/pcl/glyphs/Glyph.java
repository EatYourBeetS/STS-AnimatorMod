package pinacolada.cards.pcl.glyphs;

import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.resources.GR;

import java.util.ArrayList;

public abstract class Glyph extends PCLCard_Curse implements Hidden
{
    public static final String ID = GR.PCL.CreateID(Glyph.class.getSimpleName());
    private static final ArrayList<Glyph> cards = new ArrayList<>();

    public static PCLCardData RegisterInternal(Class<? extends PCLCard> type)
    {
        return Register(type).SetCurse(-2, PCLCardTarget.None, true);
    }

    public static ArrayList<Glyph> GetCards()
    {
        if (cards.isEmpty())
        {
            cards.add(new Glyph01());
            cards.add(new Glyph02());
            cards.add(new Glyph03());
            cards.add(new Glyph04());
            cards.add(new Glyph05());
            cards.add(new Glyph06());
            cards.add(new Glyph07());
            cards.add(new Glyph08());
            cards.add(new Glyph09());
        }

        return cards;
    }

    public static Glyph GetCard(int index, int upgradeLevel)
    {
        ArrayList<Glyph> glyphs = GetCards();
        if (index >= 0 && index < glyphs.size()) {
            Glyph result = glyphs.get(index);
            for (int i = 0; i < upgradeLevel; i++) {
                result.upgrade();
            }
            return result;
        }

        throw new IndexOutOfBoundsException("Glyph not found at index: " + index);
    }

    protected Glyph(PCLCardData cardData)
    {
        super(cardData, false);

        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true));
        this.showTypeText = false;
        this.maxUpgradeLevel = -1;
        this.canUpgrade = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return null;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return null;
    }

    public void AtEndOfTurnEffect(boolean isPlayer) {}

    public void AtStartOfBattleEffect() {}

    public void AtStartOfTurnEffect() {}

}