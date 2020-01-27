package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class HighElfArcher extends AnimatorCard
{
    public static final String ID = Register(HighElfArcher.class, EYBCardBadge.Synergy);

    public HighElfArcher()
    {
        super(ID, 0, CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(3, 0, 2);
        SetUpgrade(0, 0, 1);

        SetPiercing(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
        .SetPiercing(true, true);

        int agility = 0;
        if (GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID) <= magicNumber)
        {
            agility += 1;
        }
        if (HasSynergy() && EffectHistory.TryActivateLimited(cardID))
        {
            agility += 1;
            GameActions.Bottom.Draw(1);
        }
        if (agility > 0)
        {
            GameActions.Bottom.GainAgility(agility);
        }
    }
}