package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.List;
import java.util.Map;

public class Marielle extends PCLCard
{
    public static final PCLCardData DATA = Register(Marielle.class)
            .SetSkill(2, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Marielle()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Orange(1);
        SetAffinity_Light(0,0,1);

        SetAffinityRequirement(PCLAffinity.General, 8);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        final Map<CardType, List<AbstractCard>> map = PCLJUtils.Group(p.drawPile.group, c -> c.type);

        int i = 0;
        for (CardType t : map.keySet())
        {
            final CardGroup group = PCLGameUtilities.CreateCardGroup(map.get(t));
            PCLActions.Bottom.Motivate(group)
            .AddCallback(i, (index, c) ->
            {
                final float offsetX = (Settings.WIDTH * 0.12f) + index * AbstractCard.IMG_WIDTH * 0.4f;
                final float offsetY = (Settings.HEIGHT * 0.33f) + index * AbstractCard.IMG_HEIGHT * 0.1f;
                if (c != null)
                {
                    PCLGameEffects.TopLevelList.ShowCardBriefly(c.makeStatEquivalentCopy(), offsetX, offsetY);
                }
            });

            i += 1;
        }

        if (info.IsSynergizing)
        {
            PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                PCLActions.Bottom.GainTemporaryHP(magicNumber);
                PCLActions.Bottom.ObtainAffinityToken(PCLGameUtilities.GetRandomElement(PCLAffinity.Basic(), PCLCard.rng), upgraded);
            });
        }
    }
}