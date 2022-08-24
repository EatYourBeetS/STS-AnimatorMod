package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.EYBCardPopupAction;
import eatyourbeets.utilities.RotatingList;
import eatyourbeets.utilities.TupleT2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EYBCardData implements OnAddingToCardRewardListener, OnReceiveRewardsListener
{
    private static final ArrayList<TupleT2<EYBCardData, ActionT1<EYBCardData>>> postInitialize = new ArrayList<>();

    private ActionT2<EYBCardData, ArrayList<RewardItem>> modifyRewards;
    private FuncT2<Boolean, EYBCardData, CardGroup> obtainableAsReward;
    private Constructor<? extends EYBCard> constructor;

    public final Class<? extends EYBCard> type;
    public final CardStrings Strings;

    public AbstractResources Resources;
    public EYBCardMetadata Metadata;
    public Object Shared;
    public String ImagePath;
    public String ID;
    public String SharedID;
    public AbstractCard.CardType CardType;
    public int BaseCost;
    public int MaxCopies;

    public final ArrayList<EYBCardPopupAction> popupActions = new ArrayList<>();
    public final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    public AbstractCard.CardRarity CardRarity;
    public AbstractCard.CardColor CardColor;
    public EYBCardTarget CardTarget;
    public EYBAttackType AttackType;
    public CardSeries Series;
    public EYBCard tempCard = null;

    private TextureAtlas.AtlasRegion cardIcon = null;

    public EYBCardData(Class<? extends EYBCard> type, String cardID, AbstractResources resources)
    {
        this(type, cardID, GR.GetCardStrings(cardID), resources);

        this.ImagePath = GR.GetCardImage(cardID);
    }

    public EYBCardData(Class<? extends EYBCard> type, String cardID, CardStrings strings, AbstractResources resources)
    {
        this.ID = this.SharedID = cardID;
        this.MaxCopies = -1;
        this.Strings = EYBCardText.ProcessCardStrings(strings);
        this.Resources = resources;
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

    public EYBCard CreateNewInstance(boolean upgrade) throws RuntimeException
    {
        final EYBCard card = CreateNewInstance();
        if (upgrade && card.canUpgrade())
        {
            card.upgrade();
        }

        return card;
    }

    public EYBCard CreateNewInstance() throws RuntimeException
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
            throw new RuntimeException(e);
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
        if (card instanceof EYBCard)
        {
            return AddPreview((EYBCard) card, showUpgrade);
        }

        throw new RuntimeException("Only instances of EYBCard are supported for previews. (" + ID + ":" + (card == null ? "null" : card.cardID) + ")");
    }

    public EYBCardData AddPreview(EYBCard card, boolean showUpgrade)
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
        final int length = "eatyourbeets.cards.".length() + Resources.Prefix.length() + 1;
        final String name = type.getPackage().getName();
        final CardSeries series = CardSeries.GetByName(name.substring(length + ((name.charAt(length) == 'b') ? "cards_beta.series." : "series.").length()), false);
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

    public EYBCardData SetMetadataSource(Map<String, EYBCardMetadata> data)
    {
        Metadata = data.get(ID);

        return this;
    }

    public EYBCardData SetTypeAndRarity(AbstractCard.CardType type, AbstractCard.CardRarity rarity)
    {
        CardType = type;
        CardRarity = rarity;

        if (MaxCopies == -1 && type != AbstractCard.CardType.STATUS && type != AbstractCard.CardType.CURSE)
        {
            switch (rarity)
            {
                case COMMON: return SetMaxCopies(3);
                case UNCOMMON: return SetMaxCopies(3);
                case RARE: return SetMaxCopies(2);
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
        return SetAttack(cost, rarity, EYBAttackType.Normal, EYBCardTarget.Normal);
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity, EYBAttackType attackType)
    {
        return SetAttack(cost, rarity, attackType, EYBCardTarget.Normal);
    }

    public EYBCardData SetAttack(int cost, AbstractCard.CardRarity rarity, EYBAttackType attackType, EYBCardTarget target)
    {
        SetTypeAndRarity(AbstractCard.CardType.ATTACK, rarity);

        CardTarget = target;
        AttackType = attackType;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetSkill(int cost, AbstractCard.CardRarity rarity)
    {
        return SetSkill(cost, rarity, EYBCardTarget.Normal);
    }

    public EYBCardData SetSkill(int cost, AbstractCard.CardRarity rarity, EYBCardTarget target)
    {
        SetTypeAndRarity(AbstractCard.CardType.SKILL, rarity);

        CardTarget = target;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetPower(int cost, AbstractCard.CardRarity rarity)
    {
        SetTypeAndRarity(AbstractCard.CardType.POWER, rarity);

        CardTarget = EYBCardTarget.Self;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetCurse(int cost, EYBCardTarget target, boolean special)
    {
        SetTypeAndRarity(AbstractCard.CardType.CURSE, special ? AbstractCard.CardRarity.SPECIAL : AbstractCard.CardRarity.CURSE);

        CardColor = AbstractCard.CardColor.CURSE;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetStatus(int cost, AbstractCard.CardRarity rarity, EYBCardTarget target)
    {
        SetTypeAndRarity(AbstractCard.CardType.STATUS, rarity);

        CardColor = AbstractCard.CardColor.COLORLESS;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData ObtainableAsReward(FuncT2<Boolean, EYBCardData, CardGroup> callback)
    {
        this.obtainableAsReward = callback;

        return this;
    }

    public EYBCardData ModifyRewards(ActionT2<EYBCardData, ArrayList<RewardItem>> callback)
    {
        this.modifyRewards = callback;

        return this;
    }

    public EYBCardData PostInitialize(ActionT1<EYBCardData> callback)
    {
        postInitialize.add(new TupleT2<>(this, callback));

        return this;
    }

    public EYBCardData AddPopupAction(EYBCardPopupAction action)
    {
        popupActions.add(action);

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

    public EYBCard GetFirstCopy(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (c.cardID.equals(ID))
            {
                return (EYBCard)c;
            }
        }

        return null;
    }

    public int GetTotalCopies(CardGroup group)
    {
        int result = 0;
        for (AbstractCard c : group.group)
        {
            if (c.cardID.equals(ID))
            {
                result += 1;
            }
        }

        return result;
    }

    public boolean IsCard(AbstractCard card)
    {
        return card != null && ID.equals(card.cardID);
    }

    public boolean IsInGroup(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (c.cardID.equals(ID))
            {
                return true;
            }
        }

        return false;
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

    @Override
    public boolean ShouldCancel()
    {
        return obtainableAsReward != null && !obtainableAsReward.Invoke(this, AbstractDungeon.player.masterDeck);
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards, boolean normalRewards)
    {
        if (normalRewards && modifyRewards != null)
        {
            modifyRewards.Invoke(this, rewards);
        }
    }
}
