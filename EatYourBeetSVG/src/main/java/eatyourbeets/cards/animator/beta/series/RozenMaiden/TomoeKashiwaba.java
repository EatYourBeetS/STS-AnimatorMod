package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;


public class TomoeKashiwaba extends AnimatorCard //TODO
{
    public static final EYBCardData DATA = Register(TomoeKashiwaba.class).SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public TomoeKashiwaba()
    {
        super(DATA);

        Initialize(8, 2, 0);
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
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.GainWillpower(1, upgraded);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (p.drawPile.size() > 0)
        {
            AbstractCard topCard = p.drawPile.getTopCard();
            if (HasSynergy(topCard))
            {
                GameActions.Top.GainBlock(block);
                GameActions.Top.Draw(topCard);
            }
        }
    }
}
