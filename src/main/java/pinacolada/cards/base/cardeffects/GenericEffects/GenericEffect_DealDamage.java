package pinacolada.cards.base.cardeffects.GenericEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_DealDamage extends GenericEffect
{
    public static final String ID = Register(GenericEffect_DealDamage.class);

    protected final AbstractGameAction.AttackEffect attackEffect;

    public GenericEffect_DealDamage(int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(ID, attackEffect.name(), PCLCardTarget.Normal, amount);
        this.attackEffect = attackEffect;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.DealDamage(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.DealDamage(p, m, amount, DamageInfo.DamageType.THORNS, attackEffect);
    }
}
