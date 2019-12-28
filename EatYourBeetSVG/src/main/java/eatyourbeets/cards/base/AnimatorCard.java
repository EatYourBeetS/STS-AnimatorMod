package eatyourbeets.cards.base;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.resources.AnimatorResources_Images;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimatorCard extends EYBCard
{
    private final List<TooltipInfo> customTooltips = new ArrayList<>();

    public Synergy synergy;
    public boolean anySynergy;

    protected static String Register(Class<? extends AnimatorCard> type, EYBCardBadge... badges)
    {
        return RegisterCard(type, AnimatorResources.CreateID(type.getSimpleName()), badges);
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

    protected AnimatorCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, type, AbstractEnums.Cards.THE_ANIMATOR, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(staticCardData.get(id), id, AnimatorResources.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected AnimatorCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);

        if (this instanceof AnimatorCard_UltraRare)
        {
            setBannerTexture(AnimatorResources_Images.BANNER_SPECIAL2_PNG, AnimatorResources_Images.BANNER_SPECIAL2_P_PNG);
        }
        else if (rarity == CardRarity.SPECIAL)
        {
            setBannerTexture(AnimatorResources_Images.BANNER_SPECIAL_PNG, AnimatorResources_Images.BANNER_SPECIAL_P_PNG);
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
    public AbstractCard makeStatEquivalentCopy()
    {
        AnimatorCard copy = (AnimatorCard) super.makeStatEquivalentCopy();
        copy.synergy = synergy;
        copy.anySynergy = anySynergy;
        return copy;
    }
}