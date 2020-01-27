package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class ItamiYouji extends AnimatorCard
{
    public static final String ID = Register(ItamiYouji.class, EYBCardBadge.Synergy);

    public ItamiYouji()
    {
        super(ID, 2, CardRarity.RARE, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(2, 0, 4, 1);
        SetUpgrade(2, 0);

        SetPiercing(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (HasSynergy())
        {
            int supportDamage = secondaryValue * GameUtilities.GetCurrentEnemies(true).size();
            if (supportDamage > 0)
            {
                GameActions.Bottom.StackPower(new SupportDamagePower(p, supportDamage));
            }
        }

        GameActions.Top.Draw(magicNumber)
        .AddCallback(m, (enemy, cards) ->
        {
            for (int i = 0; i < cards.size(); i++)
            {
                GameActions.Bottom.SFX("ATTACK_FIRE");
                GameActions.Bottom.DealDamage(this, (AbstractCreature) enemy, AbstractGameAction.AttackEffect.NONE)
                        .SetPiercing(true, true);
            }
        });
    }
}