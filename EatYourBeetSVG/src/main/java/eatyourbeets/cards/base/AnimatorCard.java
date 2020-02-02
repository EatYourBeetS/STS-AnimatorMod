package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.ColoredString;

import java.util.ArrayList;

public abstract class AnimatorCard extends EYBCard
{
    protected static final AnimatorImages IMAGES = GR.Animator.Images;
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final Color synergyGlowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR;
    protected AnimatorCardCooldown cooldown;

    public Synergy synergy;
    public boolean anySynergy;

    protected static String Register(Class<? extends AnimatorCard> type)
    {
        return RegisterCard(type, GR.Animator.CreateID(type.getSimpleName()));
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, EYBAttackType attackType)
    {
        this(id, cost, rarity, attackType, false);
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, EYBAttackType attackType, CardTarget target)
    {
        this(GetStaticData(id), id, AnimatorResources.GetCardImage(id), cost, CardType.ATTACK, GR.Animator.CardColor, rarity, target);

        SetAttackType(attackType);
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, EYBAttackType attackType, boolean isAoE)
    {
        this(GetStaticData(id), id, AnimatorResources.GetCardImage(id), cost, CardType.ATTACK, GR.Animator.CardColor, rarity, isAoE ? CardTarget.ALL_ENEMY : CardTarget.ENEMY);

        SetMultiDamage(isAoE);
        SetAttackType(attackType);
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, CardType type, CardTarget target)
    {
        this(GetStaticData(id), id, AnimatorResources.GetCardImage(id), cost, type, GR.Animator.CardColor, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(GetStaticData(id), id, AnimatorResources.GetCardImage(id), cost, type, color, rarity, target);
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

    public void SetSynergy(Synergy synergy)
    {
        SetSynergy(synergy, false);
    }

    public void SetSynergy(Synergy synergy, boolean shapeshifter)
    {
        this.synergy = synergy;
        this.anySynergy = shapeshifter;
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
        copy.anySynergy = anySynergy;
        return copy;
    }

    @Override
    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        super.GenerateDynamicTooltips(dynamicTooltips);

        if (this.anySynergy)
        {
            dynamicTooltips.add(GR.Tooltips.Shapeshifter);
        }
        else if (this instanceof MartialArtist)
        {
            dynamicTooltips.add(GR.Tooltips.MartialArtist);
        }
        else if (this instanceof Spellcaster)
        {
            dynamicTooltips.add(GR.Tooltips.Spellcaster);
        }
    }

    @Override
    public ColoredString GetHeaderText()
    {
        if (synergy == null)
        {
            return null;
        }

        return new ColoredString(synergy.Name, Settings.CREAM_COLOR);
    }

    @Override
    public ColoredString GetBottomText()
    {
        if (anySynergy)
        {
            return new ColoredString(GR.Tooltips.Shapeshifter.title, new Color(1f, 1f, 0.8f, transparency));
        }
        else if (this instanceof Spellcaster)
        {
            return new ColoredString(GR.Tooltips.Spellcaster.title, new Color(0.9f, 0.9f, 1.0f, transparency));
        }
        else if (this instanceof MartialArtist)
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