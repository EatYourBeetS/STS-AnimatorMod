package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = new Color(1, 0.843f, 0, 0.25f);
    protected AnimatorCardCooldown cooldown;

    public static final AnimatorImages IMAGES = GR.Animator.Images;
    public static final CardTags SHAPESHIFTER = GR.Enums.CardTags.SHAPESHIFTER;
    public static final CardTags MARTIAL_ARTIST = GR.Enums.CardTags.MARTIAL_ARTIST;
    public static final CardTags SPELLCASTER = GR.Enums.CardTags.SPELLCASTER;
    public Synergy synergy;

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

        if (cardData.Synergy != null)
        {
            SetSynergy(cardData.Synergy);
        }
    }

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    public boolean HasSynergy()
    {
        return Synergies.IsSynergizing(this) || WouldSynergize();
    }

    public boolean HasSynergy(AbstractCard other)
    {
        return Synergies.IsSynergizing(this) || WouldSynergize(other);
    }

    public boolean HasDirectSynergy(AbstractCard other)
    {
        return Synergies.HasTagSynergy(this, other);
    }

    public boolean WouldSynergize()
    {
        return Synergies.WouldSynergize(this);
    }

    public boolean WouldSynergize(AbstractCard other)
    {
        return Synergies.WouldSynergize(this, other);
    }

    public void SetShapeshifter()
    {
        SetTag(SHAPESHIFTER, true);
        affinities.SetStar(1);
    }

    public void SetSynergy(Synergy synergy)
    {
        this.synergy = synergy;
    }

    public void SetCooldown(int baseCooldown, int cooldownUpgrade, ActionT1<AbstractMonster> onCooldownCompleted)
    {
        cooldown = new AnimatorCardCooldown(this, baseCooldown, cooldownUpgrade, onCooldownCompleted);
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

        this.glowColor = HasSynergy() ? synergyGlowColor : defaultGlowColor;
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
        AnimatorCard copy = (AnimatorCard) super.makeStatEquivalentCopy();
        copy.synergy = synergy;
        return copy;
    }

    @Override
    public final void use(AbstractPlayer p1, AbstractMonster m1)
    {
        JUtils.LogWarning(this, "AnimatorCard.use() should not be called");
        boolean isSynergizing = Synergies.IsSynergizing(this);
        OnUse(p1, m1, isSynergizing);
        OnLateUse(p1, m1, isSynergizing);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }

    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
    public ColoredString GetBottomText()
    {
        return (synergy == null) ? null : new ColoredString(synergy.LocalizedName, Settings.CREAM_COLOR);
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
        if (color == GR.Animator.CardColor)
        {
            return IMAGES.CARD_ENERGY_ORB_A.Texture();
        }

        return null;
    }

    @Override
    protected ColoredTexture GetCardBanner()
    {
        return new ColoredTexture(IMAGES.CARD_BANNER_GENERIC.Texture(), GetRarityColor(false));
    }

    @Override
    protected ColoredTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new ColoredTexture(IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new ColoredTexture(IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new ColoredTexture(IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    @Override
    public ColoredString GetSecondaryValueString()
    {
        if (cooldown != null)
        {
            return cooldown.GetSecondaryValueString();
        }

        return super.GetSecondaryValueString();
    }
}