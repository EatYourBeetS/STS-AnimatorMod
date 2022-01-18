package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.utilities.PCLActions;

public class Krusty extends PCLCard
{
    public static final PCLCardData DATA = Register(Krusty.class)
            .SetSkill(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Krusty()
    {
        super(DATA);

        Initialize(0, 17, 2, 2);
        SetUpgrade(0, 4, 1, 0);

        SetAutoplay(true);

        SetAffinity_Red(1, 0, 2);
        SetAffinity_Orange(1,0,0);
        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainMight(secondaryValue);
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), PCLAffinity.Red, secondaryValue).SetFilter(c -> c.uuid != uuid);
        cooldown.ProgressCooldownAndTrigger(null);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Last.Exhaust(this);
        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(PCLAffinity.Red, false));
    }
}