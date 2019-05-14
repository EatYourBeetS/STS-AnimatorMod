package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class SirTouchMe extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(SirTouchMe.class.getSimpleName());

    public SirTouchMe()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(6,4,6);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, p, new JuggernautPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.secondaryValue), this.secondaryValue);
        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeBlock(2);
        }
    }
}