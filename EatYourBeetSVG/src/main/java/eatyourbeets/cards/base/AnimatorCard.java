package eatyourbeets.cards.base;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimatorCard extends EYBCard
{
    private static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    private static final Color synergyGlowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR;

    private final List<TooltipInfo> customTooltips = new ArrayList<>();

    public Synergy synergy;
    public boolean anySynergy;

    protected static String Register(Class<? extends AnimatorCard> type, EYBCardBadge... badges)
    {
        return RegisterCard(type, GR.Animator.CreateID(type.getSimpleName()), badges);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, type, GR.Enums.Cards.THE_ANIMATOR, rarity, target);
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
        if (synergy != null)
        {
            return synergy.Name;
        }

        return null;
    }
}