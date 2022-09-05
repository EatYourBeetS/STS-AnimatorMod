package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animatorClassic.AnimatorClassicResources;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AnimatorClassicCard extends EYBCard
{
    protected static final AbstractPlayer.PlayerClass PLAYER_CLASS = GR.Animator.PlayerClass;
    protected static final AbstractCard.CardTags SHAPESHIFTER = GR.Enums.CardTags.SHAPESHIFTER;
    protected static final AbstractCard.CardTags MARTIAL_ARTIST = GR.Enums.CardTags.MARTIAL_ARTIST;
    protected static final AbstractCard.CardTags SPELLCASTER = GR.Enums.CardTags.SPELLCASTER;

    public CardSeries series;

    protected static EYBCardData Register(Class<? extends AnimatorClassicCard> type)
    {
        final EYBCardData data = RegisterCardData(type, GR.AnimatorClassic.CreateID(type.getSimpleName()), GR.AnimatorClassic)
        .SetColor(GR.AnimatorClassic.CardColor).SetMetadataSource(GR.AnimatorClassic.CardData);

        if (!Gdx.files.internal(data.ImagePath).exists())
        {
            data.ImagePath = GR.GetCardImage(GR.Animator.CreateID(data.ID.substring(AnimatorClassicResources.ID.length() + 1)));
            if (data.Metadata == null)
            {
                data.Metadata = new EYBCardMetadata();
                data.Metadata.cropPortrait = false;
            }
        }

        return data;
    }

    protected AnimatorClassicCard(EYBCardData cardData)
    {
        super(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());

        canUpgrade = cardData.CardType != CardType.STATUS && cardData.CardType != CardType.CURSE;

        SetMultiDamage(cardData.CardTarget == EYBCardTarget.ALL);
        SetAttackTarget(cardData.CardTarget);
        SetAttackType(cardData.AttackType);

        if (cardData.Series != null)
        {
             SetSeries(cardData.Series);
        }
    }

    protected AnimatorClassicCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.AnimatorClassic.PlayerClass;
    }

    public void SetSeries(CardSeries series)
    {
        this.series = series;
    }

    public void SetMartialArtist()
    {
        SetTag(MARTIAL_ARTIST, true);
    }

    public void SetSpellcaster()
    {
        SetTag(SPELLCASTER, true);
    }

    public void SetShapeshifter()
    {
        SetTag(SHAPESHIFTER, true);
    }

    public void SetScaling(int intellect, int agility, int force)
    {
        affinities.Get(Affinity.Blue, true).scaling = intellect;
        affinities.Get(Affinity.Green, true).scaling = agility;
        affinities.Get(Affinity.Red, true).scaling = force;
    }

    public boolean HasSynergy()
    {
        return CardSeries.Synergy.IsSynergizing(this) || WouldSynergize();
    }

    public boolean HasSynergy(AbstractCard other)
    {
        return CardSeries.Synergy.IsSynergizing(this) || WouldSynergize(other);
    }

    public boolean HasDirectSynergy(AbstractCard other)
    {
        if (series != null)
        {
            final AnimatorClassicCard card = JUtils.SafeCast(other, AnimatorClassicCard.class);
            if (card != null && series.equals(card.series))
            {
                return true;
            }
        }

        return CardSeries.Synergy.HasTagSynergy(this, other);
    }

    public boolean WouldSynergize()
    {
        return CardSeries.Synergy.WouldSynergize(this);
    }

    public boolean WouldSynergize(AbstractCard other)
    {
        return CardSeries.Synergy.WouldSynergize(this, other);
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        this.glowColor = HasSynergy() ? goldenGlowColor : blueGlowColor;
    }

    @Override
    public void hover()
    {
        super.hover();

        if (player != null && player.hand.contains(this))
        {
            for (AbstractCard c : player.hand.group)
            {
                if (c == this || WouldSynergize(c))
                {
                    c.transparency = 1f;
                }
                else
                {
                    c.transparency = 0.2f;
                }
            }
        }
    }

    @Override
    public void unhover()
    {
        if (hovered && player != null && player.hand.contains(this))
        {
            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                c.transparency = 0.35f;
            }
        }

        super.unhover();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AnimatorClassicCard copy = (AnimatorClassicCard) super.makeStatEquivalentCopy();
        copy.series = series;
        return copy;
    }

    @Override
    public final void use(AbstractPlayer p1, AbstractMonster m1)
    {
        JUtils.LogWarning(this, "AnimatorClassicCard.use() should not be called");
        final CardUseInfo info = new CardUseInfo(this, m1);

        OnUse(p1, m1, info);
        OnLateUse(p1, m1, info);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        super.GenerateDynamicTooltips(dynamicTooltips);

        if (hasTag(SHAPESHIFTER))
        {
            dynamicTooltips.add(GR.Tooltips.Shapeshifter);
        }
        else if (hasTag(MARTIAL_ARTIST))
        {
            dynamicTooltips.add(GR.Tooltips.MartialArtist);
        }
        else if (hasTag(SPELLCASTER))
        {
            dynamicTooltips.add(GR.Tooltips.Spellcaster);
        }
    }

    @Override
    public ColoredString GetHeaderText()
    {
        return (series == null) ? null : new ColoredString(series.LocalizedName, Settings.CREAM_COLOR);
    }

    @Override
    public ColoredString GetBottomText()
    {
        if (hasTag(SHAPESHIFTER))
        {
            return new ColoredString(GR.Tooltips.Shapeshifter.title, new Color(1f, 1f, 0.8f, transparency));
        }
        else if (hasTag(SPELLCASTER))
        {
            return new ColoredString(GR.Tooltips.Spellcaster.title, new Color(0.9f, 0.9f, 1f, transparency));
        }
        else if (hasTag(MARTIAL_ARTIST))
        {
            return new ColoredString(GR.Tooltips.MartialArtist.title, new Color(0.9f, 1f, 0.9f, transparency));
        }

        return null;
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.AnimatorClassic.CardColor)
        {
            switch (type)
            {
                case ATTACK: return ANIMATOR_IMAGES.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return ANIMATOR_IMAGES.CARD_BACKGROUND_POWER.Texture();
                default: return ANIMATOR_IMAGES.CARD_BACKGROUND_SKILL.Texture();
            }
        }
        else if (color == CardColor.COLORLESS || color == CardColor.CURSE)
        {
            switch (type)
            {
                case ATTACK: return ANIMATOR_IMAGES.CARD_BACKGROUND_ATTACK_UR.Texture();
                case POWER: return ANIMATOR_IMAGES.CARD_BACKGROUND_POWER_UR.Texture();
                default: return ANIMATOR_IMAGES.CARD_BACKGROUND_SKILL_UR.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return (color == GR.AnimatorClassic.CardColor ? ANIMATOR_IMAGES.CARD_ENERGY_ORB_ANIMATOR : ANIMATOR_IMAGES.CARD_ENERGY_ORB_COLORLESS).Texture();
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture(ANIMATOR_IMAGES.CARD_BANNER_GENERIC.Texture(), GetRarityColor(false));
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(ANIMATOR_IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(ANIMATOR_IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(ANIMATOR_IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    @Override
    public ColoredString GetSecondaryValueString()
    {
        return cooldown != null ? cooldown.GetSecondaryValueString() : super.GetSecondaryValueString();
    }
}