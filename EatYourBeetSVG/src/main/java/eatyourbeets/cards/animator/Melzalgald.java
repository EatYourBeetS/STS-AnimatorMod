package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Melzalgald extends AnimatorCard
{
    public static final String ID = CreateFullID(Melzalgald.class.getSimpleName());

    public Melzalgald()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(21, 0);

        this.exhaust = true;
        this.tags.add(CardTags.HEALING);

        SetSynergy(Synergies.OnePunchMan, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

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