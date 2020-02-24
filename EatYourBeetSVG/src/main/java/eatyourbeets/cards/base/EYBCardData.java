package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RotatingList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EYBCardData
{
    private Constructor<? extends EYBCard> constructor;

    public final Class<? extends EYBCard> type;
    public final CardStrings Strings;

    public EYBCardMetadata Metadata;
    public String ImagePath;
    public String ID;
    public AbstractCard.CardType CardType;
    public int BaseCost;
    public int MaxCopies;

    public final RotatingList<EYBCardPreview> previews = new RotatingList<>();
    public AbstractCard.CardRarity CardRarity;
    public AbstractCard.CardColor CardColor;
    public EYBCardTarget CardTarget;
    public EYBAttackType AttackType;
    public EYBCard tempCard = null;

    private TextureAtlas.AtlasRegion cardIcon = null;

    public EYBCardData(Class<? extends EYBCard> type, String cardID)
    {
        this.type = type;
        this.Strings = GR.GetCardStrings(cardID);
        this.ImagePath = GR.GetCardImage(cardID);
        this.ID = cardID;
        this.MaxCopies = -1;
    }

    public EYBCardData(Class<? extends EYBCard> type, String cardID, CardStrings strings)
    {
        this.type = type;
        this.Strings = strings;
        this.ID = cardID;
        this.MaxCopies = -1;
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
            throw new RuntimeException(e);
        }
    }

    public void AddPreview(EYBCardBase card, boolean addUpgrade)
    {
        previews.Add(new EYBCardPreview(card, addUpgrade));
    }

    public TextureAtlas.AtlasRegion GetCardIcon()
    {
        if (cardIcon == null)
        {
            Texture texture = GR.GetTexture(ImagePath);
            int h = texture.getHeight();
            int w = texture.getWidth();
            int section = h / 2;
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
        CardRarity = rarity;
        CardTarget = target;
        CardType = AbstractCard.CardType.ATTACK;
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
        CardRarity = rarity;
        CardTarget = target;
        CardType = AbstractCard.CardType.SKILL;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetPower(int cost, AbstractCard.CardRarity rarity)
    {
        CardRarity = rarity;
        CardTarget = EYBCardTarget.Self;
        CardType = AbstractCard.CardType.POWER;
        AttackType = EYBAttackType.None;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetCurse(int cost, EYBCardTarget target)
    {
        CardRarity = AbstractCard.CardRarity.CURSE;
        CardColor = AbstractCard.CardColor.CURSE;
        CardType = AbstractCard.CardType.CURSE;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetStatus(int cost, AbstractCard.CardRarity rarity, EYBCardTarget target)
    {
        CardRarity = rarity;
        CardColor = AbstractCard.CardColor.COLORLESS;
        CardType = AbstractCard.CardType.STATUS;
        AttackType = EYBAttackType.None;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }
}
