package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.DrawCards;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActionsHelper2;
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
                GameActionsHelper2.StackPower(new SupportDamagePower(p, supportDamage));
            }
        }

        GameActionsHelper2.AddToTop(new DrawCards(magicNumber)
        .AddCallback(m, (enemy, cards) ->
        {
            for (int i = 0; i < cards.size(); i++)
            {
                GameActionsHelper2.SFX("ATTACK_FIRE");
                GameActionsHelper2.DealDamage(this, (AbstractCreature) enemy, AbstractGameAction.AttackEffect.NONE)
                        .SetOptions(true, true);
            }
        }));
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