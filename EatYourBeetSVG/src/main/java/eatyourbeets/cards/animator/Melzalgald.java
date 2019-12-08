package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Melzalgald extends AnimatorCard
{
    public static final String ID = Register(Melzalgald.class.getSimpleName());

    public Melzalgald()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(21, 0);

        SetExhaust(true);
        SetSynergy(Synergies.OnePunchMan, true);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new MelzalgaldAlt_1(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActionsHelper.MakeCardInHand(new MelzalgaldAlt_1(), 1, upgraded);
        GameActionsHelper.MakeCardInHand(new MelzalgaldAlt_2(), 1, upgraded);
        GameActionsHelper.MakeCardInHand(new MelzalgaldAlt_3(), 1, upgraded);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}