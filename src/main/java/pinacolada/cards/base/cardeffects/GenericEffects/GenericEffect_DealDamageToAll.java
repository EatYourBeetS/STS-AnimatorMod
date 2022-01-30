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

public class GenericEffect_DealDamageToAll extends GenericEffect
{
    public static final String ID = Register(GenericEffect_DealDamageToAll.class);

    protected final AbstractGameAction.AttackEffect attackEffect;

    public GenericEffect_DealDamageToAll(int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(ID, attackEffect.name(), PCLCardTarget.AoE, amount);
        this.attackEffect = attackEffect;
    }

    @Override
    public String GetText()
    {
        return GR.PCL.Strings.Actions.DealDamageToAll(amount, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
        PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, attackEffect);
    }
}
