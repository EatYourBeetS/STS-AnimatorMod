package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.OnTargetBlockBreakAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Ara extends AnimatorCard
{
    public static final String ID = CreateFullID(Ara.class.getSimpleName());

    public Ara()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        DamageAction damageAction1 = new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        DamageAction damageAction2 = (new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        GameActionsHelper.AddToBottom(new OnTargetBlockBreakAction(m, damageAction1, new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber)));
        GameActionsHelper.AddToBottom(new OnTargetBlockBreakAction(m, damageAction2, new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber)));
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