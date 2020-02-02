package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.ScarUpgradeAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorCard
{
    public static final String ID = Register(Scar.class);

    public Scar()
    {
        super(ID, 1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

        Initialize(12, 0, 0, 30);
        SetUpgrade(4, 0);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(__ -> CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.3F))
        .SetPiercing(true, true);

        if (p.masterDeck.size() >= secondaryValue && EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Add(new ScarUpgradeAction());
        }
    }
}