package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Demiurge extends AnimatorCard
{
    public static final String ID = CreateFullID(Demiurge.class.getSimpleName());

    public Demiurge()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0,6);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainEnergy(upgraded ? 3 : 2);
        GameActionsHelper.DamageTarget(p, p, this.magicNumber, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.ModifyMagicNumber(this, 3);
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
        //if (TryUpgrade())
        //{
        //    upgradeMagicNumber(-2);
        //}
    }
}