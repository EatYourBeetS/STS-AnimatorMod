package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(8, 0, 2, 0);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (HasSynergy() ? magicNumber : 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.Add(new PutOnDeckAction(p, p, 1, false));
    }
}

