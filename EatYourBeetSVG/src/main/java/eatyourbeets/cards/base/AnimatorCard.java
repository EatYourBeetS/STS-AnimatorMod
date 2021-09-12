package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RotatingList;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = new Color(1, 0.843f, 0, 0.25f);

    // Optional
    protected AnimatorCardCooldown cooldown;
    protected DrawPileCardPreview drawPileCardPreview;
    protected Color borderIndicatorColor;

    public static final AnimatorImages IMAGES = GR.Animator.Images;
    public CardSeries series;

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return RegisterCardData(type, GR.Animator.CreateID(type.getSimpleName())).SetColor(GR.Animator.CardColor);
    }

    protected AnimatorCard(EYBCardData cardData)
    {
        super(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());

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

    public boolean HasSynergy()
    {
        return CombatStats.Affinities.IsSynergizing(this) || WouldSynergize();
    }

    public boolean HasSynergy(AbstractCard other)
    {
        return CombatStats.Affinities.IsSynergizing(this) || WouldSynergize(other);
    }

    public boolean HasDirectSynergy(AbstractCard other)
    {
        return CombatStats.Affinities.HasDirectSynergy(this, other);
    }

    public boolean WouldSynergize()
    {
        return CombatStats.Affinities.WouldSynergize(this);
    }

    public boolean WouldSynergize(AbstractCard other)
    {
        return CombatStats.Affinities.WouldSynergize(this, other);
    }

    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return false;
    }

    public void SetSeries(CardSeries series)
    {
        this.series = series;
    }

    public DrawPileCardPreview SetDrawPileCardPreview(ActionT2<RotatingList<AbstractCard>, AbstractMonster> findCards)
    {
        return this.drawPileCardPreview = new DrawPileCardPreview(findCards)
        .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    public DrawPileCardPreview SetDrawPileCardPreview(FuncT1<Boolean, AbstractCard> findCard)
    {
        return this.drawPileCardPreview = new DrawPileCardPreview(findCard)
        .RequireTarget(target == CardTarget.ENEMY || target == CardTarget.SELF_AND_ENEMY);
    }

    public AnimatorCardCooldown SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        return this.cooldown = new AnimatorCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (cooldown != null)
        {
            cooldown.ProgressCooldownAndTrigger(null);
        }
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        this.glowColor = defaultGlowColor;
        this.borderIndicatorColor = null;

        if (CheckSpecialCondition(false))
        {
            this.glowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR;
            this.borderIndicatorColor = glowColor;
        }

        if (HasSynergy())
        {
            this.glowColor = synergyGlowColor;
        }
    }

    @Override
    public void hover()
    {
        super.hover();

        if (player != null && player.hand.contains(this))
        {
            if (hb.justHovered)
            {
                triggerOnGlowCheck();
            }
            
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
            for (AbstractCard c : player.hand.group)
            {
                c.transparency = 0.35f;
            }
        }

        super.unhover();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AnimatorCard copy = (AnimatorCard) super.makeStatEquivalentCopy();
        copy.series = series;
        return copy;
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (drawPileCardPreview != null && drawPileCardPreview.enabled)
        {
            drawPileCardPreview.Update(this, m);
        }
    }

    @Override
    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        super.Render(sb, hovered, selected, library);

        if (!library && drawPileCardPreview != null && drawPileCardPreview.enabled)
        {
            drawPileCardPreview.Render(sb);
        }
    }

    @Override
    public final void use(AbstractPlayer p1, AbstractMonster m1)
    {
        JUtils.LogWarning(this, "AnimatorCard.use() should not be called");
        final CardUseInfo info = new CardUseInfo(this);

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
                case ATTACK: return IMAGES.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return IMAGES.CARD_BACKGROUND_POWER.Texture();
                default: return IMAGES.CARD_BACKGROUND_SKILL.Texture();
            }
        }
        else if (color == CardColor.COLORLESS)
        {
            switch (type)
            {
                case ATTACK: return IMAGES.CARD_BACKGROUND_ATTACK_UR.Texture();
                case POWER: return IMAGES.CARD_BACKGROUND_POWER_UR.Texture();
                default: return IMAGES.CARD_BACKGROUND_SKILL_UR.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return (color == GR.Animator.CardColor ? IMAGES.CARD_ENERGY_ORB_ANIMATOR : IMAGES.CARD_ENERGY_ORB_COLORLESS).Texture();
    }

    @Override
    protected AdvancedTexture GetCardBorderIndicator()
    {
        return borderIndicatorColor == null ? null : new AdvancedTexture(IMAGES.CARD_BORDER_INDICATOR.Texture(), borderIndicatorColor);
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture(IMAGES.CARD_BANNER_GENERIC.Texture(), GetRarityColor(false));
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    @Override
    public ColoredString GetSecondaryValueString()
    {
        return cooldown != null ? cooldown.GetSecondaryValueString() : super.GetSecondaryValueString();
    }
}