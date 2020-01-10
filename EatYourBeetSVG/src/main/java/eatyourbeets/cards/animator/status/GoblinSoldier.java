package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.Synergies;

public class GoblinSoldier extends AnimatorCard_Status
{
    public static final String ID = Register(GoblinSoldier.class);

    public GoblinSoldier()
    {
        super(ID, 1, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0, 0, 2);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }
}