package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.RotatingList;
import eatyourbeets.utilities.TupleT2;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLHotkeys;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PCLCardData
{
    public static final String CARD_DATA_PREFIX = "pinacolada.cards.pcl.";
    private static final ArrayList<TupleT2<PCLCardData, ActionT1<PCLCardData>>> postInitialize = new ArrayList<>();
    private Constructor<? extends PCLCard> constructor;

    public final Class<? extends PCLCard> type;
    public final CardStrings Strings;

    public Object Shared;
    public String ImagePath;
    public String ID;
    public AbstractCard.CardType CardType;
    public int BaseCost;
    public int MaxCopies;
    public boolean cropPortrait;

    public int MaxForms = 1;
    public boolean CanToggleFromPopup = false;
    public boolean CanToggleOnUpgrade = false;
    public boolean CanToggleFromAlternateForm = false;
    public boolean UnUpgradedCanToggleForms = false;

    public final RotatingList<PCLCardPreview> previews = new RotatingList<>();
    public AbstractCard.CardRarity CardRarity;
    public AbstractCard.CardColor CardColor;
    public PCLCardTarget CardTarget;
    public PCLAttackType AttackType;
    public CardSeries Series;
    public PCLCard tempCard = null;
    public boolean BlockScalingAttack;
    public boolean CanScaleMagicNumber = false;
    public boolean CanGrantAffinity = true;
    public boolean IsExpansionCard;

    private TextureAtlas.AtlasRegion cardIcon = null;

    public PCLCardData(Class<? extends PCLCard> type, String cardID)
    {
        this(type, cardID, GR.GetCardStrings(cardID));

        this.ImagePath = GR.GetCardImage(cardID);
    }

    public PCLCardData(Class<? extends PCLCard> type, String cardID, CardStrings strings)
    {
        this.ID = cardID;
        this.MaxCopies = -1;
        this.Strings = PCLCardText.ProcessCardStrings(strings);
        this.type = type;
    }

    public static void PostInitialize()
    {
        for (TupleT2<PCLCardData, ActionT1<PCLCardData>> pair : postInitialize)
        {
            pair.V2.Invoke(pair.V1);
        }

        postInitialize.clear();
    }

    public PCLCard CreateNewInstance(boolean upgrade) throws RuntimeException
    {
        PCLCard card = CreateNewInstance();
        if (upgrade && card.canUpgrade())
        {
            card.upgrade();
        }

        return card;
    }

    public PCLCard CreateNewInstance() throws RuntimeException
    {
        try
        {
            if (constructor == null)
            {
                constructor = type.getConstructor();
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            throw new RuntimeException(ID, e);
        }
    }

    // CardLibrary.getCopy may return an EYBCard if it determines a card should be replaced, so we cannot guarantee it is a PCLCard
    public AbstractCard MakeCopy(boolean upgraded)
    {
        return (type.isAssignableFrom(Hidden.class) ? CreateNewInstance(upgraded) : CardLibrary.getCopy(ID, upgraded ? 1 : 0, 0));
    }

    public PCLCardData AddPreviews(List<? extends AbstractCard> cards, boolean showUpgrade)
    {
        for (AbstractCard c : cards)
        {
            AddPreview(c, showUpgrade);
        }

        return this;
    }

    public PCLCardData AddPreview(AbstractCard card, boolean showUpgrade)
    {
        if (card instanceof PCLCardBase)
        {
            return AddPreview((PCLCardBase) card, showUpgrade);
        }

        throw new RuntimeException("Only instances of PCLCardBase are supported for previews.");
    }

    public PCLCardData AddPreview(PCLCardBase card, boolean showUpgrade)
    {
        previews.Add(new PCLCardPreview(card, showUpgrade));

        return this;
    }

    public TextureAtlas.AtlasRegion GetCardIcon()
    {
        if (cardIcon == null)
        {
            final Texture texture = GR.GetTexture(ImagePath);
            final int h = texture.getHeight();
            final int w = texture.getWidth();
            final int section = h / 2;
            cardIcon = new TextureAtlas.AtlasRegion(texture, (w / 2) - (section / 2), 0, section, section);
        }

        return cardIcon;
    }

    public PCLCardPreview GetCardPreview()
    {
        if (previews.Count() > 1)
        {
            PCLCardPreview preview;
            if (PCLHotkeys.cycle.isJustPressed())
            {
                preview = previews.Next(true);
            }
            else
            {
                preview = previews.Current();
            }

            preview.isMultiPreview = true;
            return preview;
        }
        else
        {
            return previews.Current();
        }
    }

    public PCLCardData SetSeriesFromClassPackage() {
        return SetSeriesFromClassPackage(false);
    }

    public PCLCardData SetSeriesFromClassPackage(boolean isExpansionCard)
    {
        final int length = CARD_DATA_PREFIX.length();
        final String name = type.getPackage().getName();
        final CardSeries series = CardSeries.GetByName(name.substring(length + ((name.charAt(length) == 'b') ? "beta.series." : "series.").length()), false);
        if (series == null)
        {
            throw new RuntimeException("Couldn't find card series from class package: " + type);
        }
        this.IsExpansionCard = isExpansionCard;

        return SetSeries(series);
    }

    public PCLCardData SetSeries(CardSeries series)
    {
        Series = series;

        return this;
    }

    public PCLCardData SetMaxCopies(int maxCopies)
    {
        MaxCopies = maxCopies;

        return this;
    }

    public PCLCardData SetCanGrantAffinity(boolean canGrant) {
        this.CanGrantAffinity = canGrant;

        return this;
    }

    public PCLCardData SetCanScaleMagicNumber(boolean canScale) {
        this.CanScaleMagicNumber = canScale;

        return this;
    }

    public PCLCardData SetColor(AbstractCard.CardColor color)
    {
        CardColor = color;

        return this;
    }

    public PCLCardData SetRarity(AbstractCard.CardRarity rarity)
    {
        CardRarity = rarity;

        if (MaxCopies == -1)
        {
            switch (rarity)
            {
                case COMMON: return SetMaxCopies(5);
                case UNCOMMON: return SetMaxCopies(4);
                case RARE: return SetMaxCopies(3);
                default: return SetMaxCopies(0);
            }
        }

        return this;
    }

    public PCLCardData SetImagePath(String imagePath)
    {
        ImagePath = imagePath;

        return this;
    }

    public PCLCardData SetAttack(int cost, AbstractCard.CardRarity rarity)
    {
        return SetAttack(cost, rarity, PCLAttackType.Normal, PCLCardTarget.Normal, false, false);
    }

    public PCLCardData SetAttack(int cost, AbstractCard.CardRarity rarity, PCLAttackType attackType)
    {
        return SetAttack(cost, rarity, attackType, PCLCardTarget.Normal, false, false);
    }

    public PCLCardData SetAttack(int cost, AbstractCard.CardRarity rarity, PCLAttackType attackType, PCLCardTarget target)
    {
        return SetAttack(cost, rarity, attackType, target, false, false);
    }

    public PCLCardData SetAttack(int cost, AbstractCard.CardRarity rarity, PCLAttackType attackType, PCLCardTarget target, boolean isBlockScaling)
    {
        return SetAttack(cost, rarity, attackType, target, isBlockScaling, false);
    }

    public PCLCardData SetAttack(int cost, AbstractCard.CardRarity rarity, PCLAttackType attackType, PCLCardTarget target, boolean isBlockScaling, boolean canScaleMagicNumber)
    {
        SetRarity(rarity);

        CardTarget = target;
        CardType = AbstractCard.CardType.ATTACK;
        AttackType = attackType;
        BaseCost = cost;
        BlockScalingAttack = isBlockScaling;
        CanScaleMagicNumber = canScaleMagicNumber;

        return this;
    }

    public PCLCardData SetSkill(int cost, AbstractCard.CardRarity rarity)
    {
        return SetSkill(cost, rarity, PCLCardTarget.Normal);
    }

    public PCLCardData SetSkill(int cost, AbstractCard.CardRarity rarity, PCLCardTarget target)
    {
        return SetSkill(cost, rarity, target, false);
    }


    public PCLCardData SetSkill(int cost, AbstractCard.CardRarity rarity, PCLCardTarget target, boolean canScaleMagicNumber)
    {
        SetRarity(rarity);

        CardTarget = target;
        CardType = AbstractCard.CardType.SKILL;
        AttackType = PCLAttackType.None;
        BaseCost = cost;
        CanScaleMagicNumber = canScaleMagicNumber;

        return this;
    }

    public PCLCardData SetPower(int cost, AbstractCard.CardRarity rarity)
    {
        SetRarity(rarity);

        CardTarget = PCLCardTarget.Self;
        CardType = AbstractCard.CardType.POWER;
        AttackType = PCLAttackType.None;
        BaseCost = cost;

        return this;
    }

    public PCLCardData SetCurse(int cost, PCLCardTarget target, boolean special)
    {
        SetRarity(special ? AbstractCard.CardRarity.SPECIAL : AbstractCard.CardRarity.CURSE);

        CardColor = AbstractCard.CardColor.CURSE;
        CardType = AbstractCard.CardType.CURSE;
        AttackType = PCLAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public PCLCardData SetStatus(int cost, AbstractCard.CardRarity rarity, PCLCardTarget target)
    {
        SetRarity(rarity);

        CardColor = AbstractCard.CardColor.COLORLESS;
        CardType = AbstractCard.CardType.STATUS;
        AttackType = PCLAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public PCLCardData SetMultiformData(int maxForms) {
        return SetMultiformData(maxForms,true,false,true,false);
    }

    public PCLCardData SetMultiformData(int maxForms, boolean canToggleFromPopup) {
        return SetMultiformData(maxForms,canToggleFromPopup,!canToggleFromPopup,canToggleFromPopup,false);
    }

    public PCLCardData SetMultiformData(int maxForms, boolean canToggleFromPopup, boolean canToggleOnUpgrade, boolean canToggleFromAlternateForm, boolean unUpgradedCanToggleForms)
    {
        this.MaxForms = maxForms;
        this.CanToggleFromPopup = canToggleFromPopup;
        this.CanToggleOnUpgrade = canToggleOnUpgrade;
        this.CanToggleFromAlternateForm = canToggleFromAlternateForm;
        this.UnUpgradedCanToggleForms = unUpgradedCanToggleForms;

        return this;
    }

    public PCLCardData PostInitialize(ActionT1<PCLCardData> action)
    {
        postInitialize.add(new TupleT2<>(this, action));

        return this;
    }

    public PCLCardData SetSharedData(Object shared)
    {
        Shared = shared;

        return this;
    }

    public <T> T GetSharedData()
    {
        return (T)Shared;
    }

    public boolean IsNotSeen()
    {
        return UnlockTracker.isCardLocked(ID) || !UnlockTracker.isCardSeen(ID);
    }

    public void MarkSeen()
    {
        if (!UnlockTracker.isCardSeen(ID))
        {
            UnlockTracker.markCardAsSeen(ID);
        }
    }
}
