package eatyourbeets.cards.animator.enchantments;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardPreview;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class Enchantment extends AnimatorCard implements Hidden
{
    public static final String ID = GR.Animator.CreateID(Enchantment.class.getSimpleName());
    private static final ArrayList<Enchantment> allCards = new ArrayList<>();
    private static final ArrayList<Enchantment> lv1Cards = new ArrayList<>();
    private static final ArrayList<Enchantment> lv2Cards = new ArrayList<>();

    public int level;

    public int index;
    public boolean requiresTarget;
    public EnchantableRelic relic;

    private final Color borderColor;

    public static EYBCardData RegisterInternal(Class<? extends AnimatorCard> type)
    {
        return Register(type).SetPower(-2, CardRarity.SPECIAL).SetImagePath(GR.GetCardImage(ID));
    }

    //Overridable methods
    public abstract void OnObtain();
    public abstract void OnStartOfBattle();

    public static ArrayList<Enchantment> GetAllCards()
    {
        GetLv1Cards();
        GetLv2Cards();

        return allCards;
    }

    public static ArrayList<Enchantment> GetLv1Cards()
    {
        if (lv1Cards.isEmpty())
        {
            lv1Cards.add(new DreamCatcherEnchantment());
            lv1Cards.add(new MissingPieceEnchantment());
            lv1Cards.add(new MagicCoinEnchantment());
            lv1Cards.add(new AstrolabeEnchantment());
            lv1Cards.add(new TinyHouseEnchantment());
            lv1Cards.add(new QuestionMarkEnchantment());
            lv1Cards.add(new PandorasBoxEnchantment());
            lv1Cards.add(new PeacePipeEnchantment());
            lv1Cards.add(new SingingBowlEnchantment());
            lv1Cards.add(new LeesWaffleEnchantment());

            allCards.addAll(lv1Cards);
        }

        return lv1Cards;
    }

    public static ArrayList<Enchantment> GetLv2Cards()
    {
        if (lv2Cards.isEmpty())
        {
            lv2Cards.add(new PrayerWheelEnchantment());
            lv2Cards.add(new RacePieceEnchantment());
            lv2Cards.add(new StarCompassEnchantment());
            lv2Cards.add(new AngelWingsEnchantment());
            lv2Cards.add(new AncientTomeEnchantment());
            lv2Cards.add(new RollingCubesEnchantment());
            lv2Cards.add(new EmptyCageEnchantment());
            lv2Cards.add(new OrreryEnchantment());
            lv2Cards.add(new DollysMirrorEnchantment());
            lv2Cards.add(new UnnamedGiftEnchantment());
            allCards.addAll(lv2Cards);
        }

        return lv2Cards;
    }

    public static Enchantment GetCard(int index)
    {
        for (Enchantment e : GetAllCards())
        {
            if (e.index == index)
            {
                Enchantment result = (Enchantment) e.makeCopy();

                return result;
            }
        }

        throw new IndexOutOfBoundsException("Enchantment not found at index: " + index);
    }

    protected Enchantment(EYBCardData cardData, int level, int index)
    {
        super(cardData);

        this.index = index;
        this.level = level;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new LivingPicture(this);
        this.portraitForeground = new AdvancedTexture(relic.img, null);
        this.portraitForeground.pos.scale = 2;
    }

    protected Enchantment(EYBCardData cardData, int level, int index, AbstractRelic relicForImg)
    {
        super(cardData);

        this.index = index;
        this.level = level;
        this.borderColor = new Color(0.7f, 0.8f, 0.9f, 1f);
        this.cropPortrait = false;
        this.relic = new LivingPicture(this);
        this.portraitForeground = new AdvancedTexture(relicForImg.img, null);
        this.portraitForeground.pos.scale = 2;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        if (upgraded && auxiliaryData.form > 0)
        {
            return JUtils.Format(cardData.Strings.EXTENDED_DESCRIPTION[auxiliaryData.form - 1], args);
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

    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public EYBCardPreview GetCardPreview()
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
        return GetCard(index);
    }
}