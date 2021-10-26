package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RotatingList;
import eatyourbeets.utilities.TupleT2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EYBCardData
{
    private static final ArrayList<TupleT2<EYBCardData, ActionT1<EYBCardData>>> postInitialize = new ArrayList<>();
    private Constructor<? extends EYBCard> constructor;

    public final Class<? extends EYBCard> type;
    public final CardStrings Strings;

    public EYBCardMetadata Metadata;
    public Object Shared;
    public String ImagePath;
    public String ID;
    public AbstractCard.CardType CardType;
    public int BaseCost;
    public int MaxCopies;

    public int MaxForms = 1;
    public boolean CanToggleFromPopup = false;
    public boolean CanToggleOnUpgrade = false;
    public boolean CanToggleFromAlternateForm = false;
    public boolean UnUpgradedCanToggleForms = false;

    public final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    public AbstractCard.CardRarity CardRarity;
    public AbstractCard.CardColor CardColor;
    public EYBCardTarget CardTarget;
    public EYBAttackType AttackType;
    public CardSeries Series;
    public EYBCard tempCard = null;
    public boolean BlockScalingAttack;
    public boolean CanTriggerSupercharge = true;

    private TextureAtlas.AtlasRegion cardIcon = null;

    public EYBCardData(Class<? extends EYBCard> type, String cardID)
    {
        this(type, cardID, GR.GetCardStrings(cardID));

        this.ImagePath = GR.GetCardImage(cardID);
    }

    public EYBCardData(Class<? extends EYBCard> type, String cardID, CardStrings strings)
    {
        this.ID = cardID;
        this.MaxCopies = -1;
        this.Strings = EYBCardText.ProcessCardStrings(strings);
        this.type = type;
    }

    public static void PostInitialize()
    {
        for (TupleT2<EYBCardData, ActionT1<EYBCardData>> pair : postInitialize)
        {
            pair.V2.Invoke(pair.V1);
        }

        postInitialize.clear();
    }

    public AbstractCard CreateNewInstance(boolean upgrade) throws RuntimeException
    {
        AbstractCard card = CreateNewInstance();
        if (upgrade && card.canUpgrade())
        {
            card.upgrade();
        }

        return card;
    }

    public AbstractCard CreateNewInstance() throws RuntimeException
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

    public EYBCard MakeCopy(boolean upgraded)
    {
        return (EYBCard) (type.isAssignableFrom(Hidden.class) ? CreateNewInstance(upgraded) : CardLibrary.getCopy(ID, upgraded ? 1 : 0, 0));
    }

    public EYBCardData AddPreviews(List<? extends AbstractCard> cards, boolean showUpgrade)
    {
        for (AbstractCard c : cards)
        {
            AddPreview(c, showUpgrade);
        }

        return this;
    }

    public EYBCardData AddPreview(AbstractCard card, boolean showUpgrade)
    {
        if (card instanceof EYBCardBase)
        {
            return AddPreview((EYBCardBase) card, showUpgrade);
        }

        throw new RuntimeException("Only instances of EYBCardBase are supported for previews.");
    }

    public EYBCardData AddPreview(EYBCardBase card, boolean showUpgrade)
    {
        previews.Add(new EYBCardPreview(card, showUpgrade));

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

    public EYBCardPreview GetCardPreview()
    {
        if (previews.Count() > 1)
        {
            EYBCardPreview preview;
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT))
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

    public EYBCardData SetSeriesFromClassPackage()
    {
        final int length = "eatyourbeets.cards.animator.".length();
        final String name = type.getPackage().getName();
        final CardSeries series = CardSeries.GetByName(name.substring(length + ((name.charAt(length) == 'b') ? "beta.series." : "series.").length()), false);
        if (series == null)
        {
            throw new RuntimeException("Couldn't find card series from class package: " + type);
        }

        return SetSeries(series);
    }

    public EYBCardData SetSeries(CardSeries series)
    {
        Series = series;

        return this;
    }

    public EYBCardData SetMaxCopies(int maxCopies)
    {
        MaxCopies = maxCopies;

        return this;
    }

    public EYBCardData SetColor(AbstractCard.CardColor color)
    {
        CardColor = color;

        return this;
    }

    public EYBCardData SetRarity(AbstractCard.CardRarity rarity)
    {
        CardRarity = rarity;

        if (MaxCopies == -1)
        {
            switch (rarity)
            {
                case COMMON: return SetMaxCopies(9);
                case UNCOMMON: return SetMaxCopies(9);
                case RARE: return SetMaxCopies(9);
                default: return SetMaxCopies(0);
            }
        }

        return this;
    }

    public EYBCardData SetImagePath(String imagePath)
    {
        ImagePath = imagePath;

        return this;
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity)
    {
        return SetAttack(cost, rarity, EYBAttackType.Normal, EYBCardTarget.Normal, false, true);
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity, EYBAttackType attackType)
    {
        return SetAttack(cost, rarity, attackType, EYBCardTarget.Normal, false, true);
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity, EYBAttackType attackType, EYBCardTarget target)
    {
        return SetAttack(cost, rarity, attackType, target, false, true);
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity, EYBAttackType attackType, EYBCardTarget target, boolean isBlockScaling, boolean canTriggerSupercharge)
    {
        SetRarity(rarity);

        CardTarget = target;
        CardType = AbstractCard.CardType.ATTACK;
        AttackType = attackType;
        BaseCost = cost;
        BlockScalingAttack = isBlockScaling;
        CanTriggerSupercharge = canTriggerSupercharge;

        return this;
    }

    public EYBCardData SetSkill(int cost, AbstractCard.CardRarity rarity)
    {
        return SetSkill(cost, rarity, EYBCardTarget.Normal);
    }

    public EYBCardData SetSkill(int cost, AbstractCard.CardRarity rarity, EYBCardTarget target)
    {
        SetRarity(rarity);

        CardTarget = target;
        CardType = AbstractCard.CardType.SKILL;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetPower(int cost, AbstractCard.CardRarity rarity)
    {
        SetRarity(rarity);

        CardTarget = EYBCardTarget.Self;
        CardType = AbstractCard.CardType.POWER;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetCurse(int cost, EYBCardTarget target, boolean special)
    {
        SetRarity(special ? AbstractCard.CardRarity.SPECIAL : AbstractCard.CardRarity.CURSE);

        CardColor = AbstractCard.CardColor.CURSE;
        CardType = AbstractCard.CardType.CURSE;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetStatus(int cost, AbstractCard.CardRarity rarity, EYBCardTarget target)
    {
        SetRarity(rarity);

        CardColor = AbstractCard.CardColor.COLORLESS;
        CardType = AbstractCard.CardType.STATUS;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetMultiformData(int maxForms) {
        return SetMultiformData(maxForms,true,false,true,false);
    }

    public EYBCardData SetMultiformData(int maxForms, boolean canToggleFromPopup, boolean canToggleOnUpgrade, boolean canToggleFromAlternateForm, boolean unUpgradedCanToggleForms)
    {
        this.MaxForms = maxForms;
        this.CanToggleFromPopup = canToggleFromPopup;
        this.CanToggleOnUpgrade = canToggleOnUpgrade;
        this.CanToggleFromAlternateForm = canToggleFromAlternateForm;
        this.UnUpgradedCanToggleForms = unUpgradedCanToggleForms;

        return this;
    }

    public EYBCardData PostInitialize(ActionT1<EYBCardData> action)
    {
        postInitialize.add(new TupleT2<>(this, action));

        return this;
    }

    public EYBCardData SetSharedData(Object shared)
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
