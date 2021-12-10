package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.ui.cards.DrawPileCardPreview;
import eatyourbeets.utilities.*;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = new Color(1, 0.843f, 0, 0.25f);
    private static final Color COLORLESS_ORB_COLOR = new Color(0.7f, 0.7f, 0.7f, 1);
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
       this(cardData, 0, 0);
    }

    protected AnimatorCard(EYBCardData cardData, int form, int timesUpgraded)
    {
        super(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget(), form, timesUpgraded);

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
        super(data, id, imagePath, cost, type, color, rarity, target, 0, 0);
    }

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, int form, int timesUpgraded)
    {
        super(data, id, imagePath, cost, type, color, rarity, target, form, timesUpgraded);
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
        if (hasTag(HARMONIC)) {
            if (GameUtilities.IsSameSeries(this,other)) {
                return true;
            }
        }
        return CombatStats.Affinities.HasDirectSynergy(this, other);
    }

    public boolean WouldSynergize()
    {
        return CombatStats.Affinities.WouldMatch(this);
    }

    public boolean WouldSynergize(AbstractCard other)
    {
        return CombatStats.Affinities.WouldSynergize(this, other);
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
        if (color == GR.Animator.CardColor || color == CardColor.COLORLESS)
        {

            switch (type)
            {
                case ATTACK: return isPopup ? IMAGES.CARD_BACKGROUND_ATTACK_L.Texture() : IMAGES.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return isPopup ? IMAGES.CARD_BACKGROUND_POWER_L.Texture() : IMAGES.CARD_BACKGROUND_POWER.Texture();
                default: return isPopup ? IMAGES.CARD_BACKGROUND_SKILL_L.Texture() : IMAGES.CARD_BACKGROUND_SKILL.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return (isPopup ? IMAGES.CARD_ENERGY_ORB_ANIMATOR_L : IMAGES.CARD_ENERGY_ORB_ANIMATOR).Texture();
    }

    @Override
    protected AdvancedTexture GetCardBorderIndicator()
    {
        return borderIndicatorColor == null ? null : new AdvancedTexture(IMAGES.CARD_BORDER_INDICATOR.Texture(), borderIndicatorColor);
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture((isPopup ? IMAGES.CARD_BANNER_L : IMAGES.CARD_BANNER).Texture(), GetRarityColor(false));
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(isPopup ? IMAGES.CARD_FRAME_ATTACK_L.Texture() : IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(isPopup ? IMAGES.CARD_FRAME_POWER_L.Texture() :IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(isPopup ? IMAGES.CARD_FRAME_SKILL_L.Texture() : IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        if (this.color == CardColor.COLORLESS) {
            RenderHelpers.DrawGrayscale(sb, () -> {
                RenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), COLORLESS_ORB_COLOR, transparency, popUpMultiplier);
                return true;});
        }
        else {
            RenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), _renderColor.Get(this), transparency, popUpMultiplier);
        }
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            Texture baseCard = GetCardBackground();
            float popUpMultiplier = isPopup ? 0.5f : 1f;
            Vector2 offset = new Vector2(-baseCard.getWidth() / (isPopup ? 7.7f : 3.85f), baseCard.getHeight() / (isPopup ? 5.3f : 2.64f));
            Texture energyOrb = GetEnergyOrb();
            if (this.color == CardColor.COLORLESS && !(this instanceof AnimatorCard_UltraRare)) {
                RenderHelpers.DrawGrayscale(sb, () -> {RenderHelpers.DrawOnCardAuto(sb, this, energyOrb, offset, energyOrb.getWidth(), energyOrb.getHeight(), COLORLESS_ORB_COLOR, transparency, popUpMultiplier); return true;});
            }
            else {
                RenderHelpers.DrawOnCardAuto(sb, this, energyOrb, offset, energyOrb.getWidth(), energyOrb.getHeight(), _renderColor.Get(this), transparency, popUpMultiplier);
            }

            ColoredString costString = GetCostString();
            if (costString != null)
            {
                BitmapFont font = RenderHelpers.GetEnergyFont(this);
                RenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                RenderHelpers.ResetFont(font);
            }
        }
    }
}