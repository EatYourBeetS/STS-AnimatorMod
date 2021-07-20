package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Naotsugu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Naotsugu.class).SetAttack(3, CardRarity.COMMON, EYBAttackType.Normal);

    public Naotsugu()
    {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY)
        .AddCallback(e ->
        {
            AbstractCard best = null;
            int maxBlock = e.lastDamageTaken;
            for (AbstractCard c : player.hand.group)
            {
                if (c.block > 0 && c.block < maxBlock)
                {
                    if (ForceStance.IsActive())
                    {
                        GameActions.Top.PlayCard(c, player.hand, (AbstractMonster) e)
                        .SetDuration(Settings.ACTION_DUR_MED, true);
                    }
                    else if (best == null || best.block > c.block)
                    {
                        best = c;
                    }
                }
            }

            if (best != null)
            {
                GameActions.Top.PlayCard(best, player.hand, (AbstractMonster) e);
            }
        });
    }
}