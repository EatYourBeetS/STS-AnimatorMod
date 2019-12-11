package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class ItamiYouji extends AnimatorCard
{
    public static final String ID = Register(ItamiYouji.class.getSimpleName(), EYBCardBadge.Synergy);

    public ItamiYouji()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(2,0,4, 1);

        SetPiercing(true);
        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (HasActiveSynergy())
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
                .SetOptions(true, true);
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}