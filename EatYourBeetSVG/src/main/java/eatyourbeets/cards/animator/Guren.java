package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Guren extends AnimatorCard
{
    public static final String ID = Register(Guren.class.getSimpleName(), EYBCardBadge.Special);

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(0, 0,3);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActionsHelper.AddToTop(new GurenAction(m, this.magicNumber));
        }

        int amount = p.exhaustPile.size();
        if (amount > 0 && EffectHistory.TryActivateSemiLimited(this.cardID))
        {
            GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, amount), amount);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }
}