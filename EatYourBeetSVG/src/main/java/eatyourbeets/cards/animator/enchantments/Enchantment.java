package eatyourbeets.cards.animator.enchantments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.JUtils;

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

    public static EYBCardData RegisterInternal(Class<? extends AnimatorCard> type)
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

            for (Enchantment a : cards)
            {
                for (Enchantment b : Enchantment.GetCard(a.index, 0).GetUpgrades())
                {
                    a.cardData.AddPreview(b, true);
                }
            }
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

        this.index = index;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new LivingPicture(this);
        this.portraitForeground = new ColoredTexture(relic.img, null);
        this.portraitForeground.scale = 2;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (upgraded && upgradeIndex > 0)
        {
            return JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[upgradeIndex - 1], args);
        }
        else
        {
            return JUtils.Format(cardData.Strings.DESCRIPTION, args);
        }
    }

    public int GetPowerCost()
    {
        return secondaryValue;
    }

    public boolean CanUsePower(int cost)
    {
        return EnergyPanel.getCurrentEnergy() >= cost;
    }

    public void PayPowerCost(int cost)
    {
        EnergyPanel.useEnergy(cost);
    }

    public abstract int GetMaxUpgradeIndex();
    public abstract void UsePower(AbstractMonster m);

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return upgraded ? null : super.GetCardPreview();
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

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return GetCard(index, upgradeIndex);
    }

    public ArrayList<Enchantment> GetUpgrades()
    {
        final ArrayList<Enchantment> result = new ArrayList<>();
        for (int i = 1; i <= GetMaxUpgradeIndex(); i++)
        {
            result.add(GetCard(index, i));
        }

        return result;
    }
}