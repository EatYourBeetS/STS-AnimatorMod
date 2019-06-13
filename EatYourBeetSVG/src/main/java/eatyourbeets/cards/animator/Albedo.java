package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import patches.AbstractEnums;

public class Albedo extends AnimatorCard
{
    public static final String ID = CreateFullID(Albedo.class.getSimpleName());

    public Albedo()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(12,0);

        //AddExtendedDescription();

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        this.tags.remove(AbstractEnums.CardTags.LOYAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, p, new EnchantedArmorPower(p, this.damage), this.damage);

        if (HasActiveSynergy())
        {
            this.tags.add(AbstractEnums.CardTags.LOYAL);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}