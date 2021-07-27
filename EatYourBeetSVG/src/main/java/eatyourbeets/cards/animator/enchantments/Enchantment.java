package eatyourbeets.cards.animator.enchantments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.EnchantableRelic;
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
    public boolean requiresTarget;
    public EnchantableRelic relic;

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
                    result.cardData.Strings.UPGRADE_DESCRIPTION = result.cardData.Strings.EXTENDED_DESCRIPTION[upgradeIndex - 1];
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

        this.index = index;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new LivingPicture(this);
        this.portraitForeground = new ColoredTexture(relic.img, null);
        this.portraitForeground.scale = 2;
    }

    public int GetPowerCost()
    {
        return secondaryValue;
    }

    public boolean CanUsePower(int cost)
    {
        return player.energy.energy >= cost;
    }

    public void PayPowerCost(int cost)
    {
        player.energy.use(cost);
    }

    public abstract int GetMaxUpgradeIndex();
    public abstract void UsePower(AbstractMonster m);

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

    public ArrayList<Enchantment> GetUpgrades()
    {
        ArrayList<Enchantment> result = new ArrayList<>();
        for (int i = 1; i <= GetMaxUpgradeIndex(); i++)
        {
            result.add(GetCard(index, i));
        }

        return result;
    }
}