package eatyourbeets.cards.base;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.damage.DealDamage;
import eatyourbeets.actions.damage.DealDamageToAll;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimatorCard extends EYBCard
{
    private static final String SPELLCASTER_STRING = GR.GetTooltipByID("Spellcaster").title;
    private static final String MARTIAL_ARTIST_STRING = GR.GetTooltipByID("Martial Artist").title;
    private static final String SHAPESHIFTER_STRING = GR.GetTooltipByID("Shapeshifter").title;
    private static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    private static final Color synergyGlowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR;

    private final List<TooltipInfo> customTooltips = new ArrayList<>();

    public Synergy synergy;
    public boolean anySynergy;

    protected static String Register(Class<? extends AnimatorCard> type)
    {
        return RegisterCard(type, GR.Animator.CreateID(type.getSimpleName()));
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, AttackType attackType)
    {
        this(id, cost, rarity, attackType, false);
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, AttackType attackType, boolean isAoE)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, CardType.ATTACK, GR.Animator.CardColor, rarity, isAoE ? CardTarget.ALL_ENEMY : CardTarget.ENEMY);

        SetMultiDamage(isAoE);
        SetAttackType(attackType);
    }

    protected AnimatorCard(String id, int cost, CardRarity rarity, CardType type, CardTarget target)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, type, GR.Animator.CardColor, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);

        assetUrl = imagePath;

        if (this instanceof AnimatorCard_UltraRare)
        {
            setBannerTexture(GR.Animator.Images.BANNER_SPECIAL2_PNG, GR.Animator.Images.BANNER_SPECIAL2_P_PNG);
        }
        else if (rarity == CardRarity.SPECIAL)
        {
            setBannerTexture(GR.Animator.Images.BANNER_SPECIAL_PNG, GR.Animator.Images.BANNER_SPECIAL_P_PNG);
        }
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
    protected String GetHeaderText()
    {
        if (synergy == null)
        {
            return null;
        }

        return synergy.Name;
    }

    public DealDamage DealDamage(AbstractMonster target, AbstractGameAction.AttackEffect effect)
    {
        return GameActions.Bottom.DealDamage(this, target, effect)
        .SetPiercing(attackType.bypassThorns, attackType.bypassBlock);
    }

    public DealDamageToAll DealDamageToALL(AbstractGameAction.AttackEffect effect)
    {
        return GameActions.Bottom.DealDamageToAll(this, effect)
        .SetPiercing(attackType.bypassThorns, attackType.bypassBlock);
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.Animator.CardColor)
        {
            switch (type)
            {
                case ATTACK:
                    return GR.GetTexture(GR.Animator.Images.ATTACK_PNG);
                case SKILL:
                    return GR.GetTexture(GR.Animator.Images.SKILL_PNG);
                case POWER:
                    return GR.GetTexture(GR.Animator.Images.POWER_PNG);
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        if (color == GR.Animator.CardColor)
        {
            return GR.GetTexture(GR.Animator.Images.ORB_B_PNG);
        }

        return null;
    }

    @Override
    public ColoredString GetBottomText()
    {
        if (anySynergy)
        {
            return new ColoredString(SHAPESHIFTER_STRING, new Color(1f, 1f, 0.8f, transparency));
        }
        else if (this instanceof Spellcaster)
        {
            return new ColoredString(SPELLCASTER_STRING, new Color(0.9f, 0.9f, 1.0f, transparency));
        }
        else if (this instanceof MartialArtist)
        {
            return new ColoredString(MARTIAL_ARTIST_STRING, new Color(0.9f, 1f, 0.9f, transparency));
        }

        return null;
    }
}