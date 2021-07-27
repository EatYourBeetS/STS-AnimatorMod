package eatyourbeets.cards.animator.enchantments;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;

import java.util.ArrayList;

public abstract class Enchantment extends AnimatorCard implements Hidden
{
    public static final String ID = GR.Animator.CreateID(Enchantment.class.getSimpleName());
    private static final ArrayList<Enchantment> cards = new ArrayList<>();

    public int index;
    public int upgradeIndex;
    public LivingPicture relic;

    private final Color borderColor;

    public static EYBCardData RegisterAura(Class<? extends AnimatorCard> type)
    {
        return Register(type).SetPower(-2, CardRarity.SPECIAL).SetImagePath(GR.GetCardImage(ID));
    }

    public static ArrayList<Enchantment> GetCards()
    {
        if (cards.isEmpty())
        {
            cards.add(new Enchantment1());
            cards.add(new Enchantment2());
            cards.add(new Enchantment3());
        }

        return cards;
    }

    public static Enchantment GetCard(int index, int upgradeIndex)
    {
        for (Enchantment e : GetCards())
        {
            if (e.index == index)
            {
                Enchantment result = (Enchantment) e.makeCopy();
                if (upgradeIndex > 0)
                {
                    result.upgradeIndex = upgradeIndex;
                    result.upgrade();
                }

                return result;
            }
        }

        throw new IndexOutOfBoundsException("Enchantment not found at index: " + index);
    }

    protected Enchantment(EYBCardData cardData, int index)
    {
        super(cardData);

        this.relic = new LivingPicture(index);
        this.index = index;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.portraitForeground = new ColoredTexture(relic.img, null);
        this.portraitForeground.scale = 2;
    }

    @Override
    protected ColoredTexture GetCardBanner()
    {
        return super.GetCardBanner().SetColor(borderColor);
    }

    @Override
    protected ColoredTexture GetPortraitFrame()
    {
        return super.GetPortraitFrame().SetColor(borderColor);
    }
}