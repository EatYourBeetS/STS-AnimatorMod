package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;


public class TomoeKashiwaba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TomoeKashiwaba.class).SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public TomoeKashiwaba()
    {
        super(DATA);

        Initialize(8, 4, 0);
        SetUpgrade(0, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return null;
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (upgraded)
        {
            GameActions.Bottom.GainAgility(1);
        }
        else
        {
            GameUtilities.RetainPower(AffinityType.Green);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (p.drawPile.size() > 0)
        {
            AbstractCard topCard = p.drawPile.getTopCard();
            if (HasSynergy(topCard) && CombatStats.TryActivateSemiLimited(cardID))
            {
                GameActions.Top.GainBlock(block);
                GameActions.Top.Draw(topCard);
            }
        }
    }
}
