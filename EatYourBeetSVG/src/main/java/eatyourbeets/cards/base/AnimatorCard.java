package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.utilities.ColoredString;

import java.util.ArrayList;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR;
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
    }

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    public boolean HasSynergy()
    {
        return Synergies.WouldSynergize(this);
    }

    public boolean HasSynergy(AbstractCard other)
    {
        return Synergies.WouldSynergize(this, other);
    }

    public void SetSpellcaster()
    {
        SetTag(SPELLCASTER, true);
    }

    public void SetMartialArtist()
    {
        SetTag(MARTIAL_ARTIST, true);
    }

    public void SetShapeshifter()
    {
        SetTag(SHAPESHIFTER, true);
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

        if (HasSynergy())
        {
            this.glowColor = synergyGlowColor;
        }
        else
        {
            this.glowColor = defaultGlowColor;
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AnimatorCard copy = (AnimatorCard) super.makeStatEquivalentCopy();
        copy.synergy = synergy;
        return copy;
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
        return (synergy == null) ? null : new ColoredString(synergy.Name, Settings.CREAM_COLOR);
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
    protected Texture GetCardBanner()
    {
        if (rarity == CardRarity.SPECIAL)
        {
            return IMAGES.CARD_BANNER_SPECIAL.Texture();
        }

        return null;
    }

    @Override
    protected Texture GetPortraitFrame()
    {
        if (rarity == CardRarity.SPECIAL)
        {
            switch (type)
            {
                case ATTACK: return IMAGES.CARD_FRAME_ATTACK_SPECIAL.Texture();
                case POWER: return IMAGES.CARD_FRAME_POWER_SPECIAL.Texture();
                default: return IMAGES.CARD_FRAME_SKILL_SPECIAL.Texture();
            }
        }

        return null;
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