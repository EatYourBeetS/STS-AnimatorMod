package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.JUtils;

public abstract class AnimatorCard extends EYBCard
{
    protected static final AbstractPlayer.PlayerClass PLAYER_CLASS = GR.Animator.PlayerClass;

    public CardSeries series;

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return RegisterCardData(type, GR.Animator.CreateID(type.getSimpleName()), GR.Animator)
        .SetColor(GR.Animator.CardColor).SetMetadataSource(GR.Animator.CardData);
    }

    protected AnimatorCard(EYBCardData cardData)
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

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    public void SetSeries(CardSeries series)
    {
        this.series = series;
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Animator.PlayerClass;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AnimatorCard copy = (AnimatorCard) super.makeStatEquivalentCopy();
        copy.series = series;
        return copy;
    }

    @Override
    public final void use(AbstractPlayer p1, AbstractMonster m1)
    {
        JUtils.LogWarning(this, "AnimatorCard.use() should not be called");
        final CardUseInfo info = new CardUseInfo(this, m1);

        OnUse(p1, m1, info);
        OnLateUse(p1, m1, info);
    }

    @Override
    public ColoredString GetBottomText()
    {
        return (series == null) ? null : new ColoredString(series.LocalizedName, Settings.CREAM_COLOR);
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.Animator.CardColor)
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
        return (color == GR.Animator.CardColor ? ANIMATOR_IMAGES.CARD_ENERGY_ORB_ANIMATOR : ANIMATOR_IMAGES.CARD_ENERGY_ORB_COLORLESS).Texture();
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