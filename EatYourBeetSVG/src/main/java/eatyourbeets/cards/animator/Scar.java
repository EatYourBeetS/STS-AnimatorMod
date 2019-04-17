package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.OnTargetDeadAction;
import eatyourbeets.actions.PiercingDamageAction;
import eatyourbeets.actions.ScarAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Scar extends AnimatorCard
{
    public static final String ID = CreateFullID(Scar.class.getSimpleName());

    public Scar()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(12,0);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        PiercingDamageAction damageAction = new PiercingDamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActionsHelper.AddToBottom(new OnTargetDeadAction(m, damageAction, new ScarAction(p, this), false));

//        if (HasActiveSynergy())
//        {
//            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
//        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}