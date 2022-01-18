package pinacolada.cards.pcl.enchantments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardPreview;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.relics.pcl.UsefulBox;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public abstract class Enchantment extends PCLCard implements Hidden
{
    public static final String ID = GR.PCL.CreateID(Enchantment.class.getSimpleName());
    private static final ArrayList<Enchantment> cards = new ArrayList<>();

    public int index;
    public int uses = 1;
    public boolean requiresTarget = false;
    public boolean refreshEachTurn = true;
    public PowerTriggerConditionType triggerConditionType = PowerTriggerConditionType.Affinity;
    public PCLEnchantableRelic relic;

    private final Color borderColor;

    public static PCLCardData RegisterInternal(Class<? extends PCLCard> type)
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
            cards.add(new Enchantment4());
            cards.add(new Enchantment5());
            cards.add(new Enchantment6());
            cards.add(new Enchantment7());

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
                    result.auxiliaryData.form = upgradeIndex;
                    result.upgrade();
                }

                return result;
            }
        }

        throw new IndexOutOfBoundsException("Enchantment not found at index: " + index);
    }

    protected Enchantment(PCLCardData cardData, int index)
    {
        super(cardData);

        this.index = index;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new UsefulBox(this);
        this.portraitImg = new AdvancedTexture(GR.GetTexture(GR.GetCardImage(ID), true));
        this.showTypeText = false;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (upgraded && auxiliaryData.form > 0)
        {
            return PCLJUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form - 1], args);
        }
        else
        {
            return PCLJUtils.Format(cardData.Strings.DESCRIPTION, args);
        }
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

    public int GetPowerCost()
    {
        return secondaryValue;
    }

    public boolean CanUsePower(int cost)
    {
        return true;
    }

    public void PayPowerCost(int cost)
    {
    }

    public PCLAffinity[] GetAffinityList() {return PCLAffinity.Extended();}

    public abstract int GetMaxUpgradeIndex();
    public abstract void UsePower(AbstractMonster m);

    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public PCLCardPreview GetCardPreview()
    {
        return upgraded ? null : super.GetCardPreview();
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return super.GetCardBanner().SetColor(borderColor);
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        return super.GetPortraitFrame().SetColor(borderColor);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        return GetCard(index, auxiliaryData.form);
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