package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Krusty extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Krusty.class)
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
        GameActions.Bottom.GainMight(secondaryValue);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), Affinity.Red, secondaryValue).SetFilter(c -> c.uuid != uuid);
        cooldown.ProgressCooldownAndTrigger(null);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Last.Exhaust(this);
        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Red, false));
    }
}