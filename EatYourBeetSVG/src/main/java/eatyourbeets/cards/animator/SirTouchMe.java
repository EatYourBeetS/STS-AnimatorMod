package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class SirTouchMe extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(SirTouchMe.class.getSimpleName());

    public SirTouchMe()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(9,9,6);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new JuggernautPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            upgradeBlock(3);
        }
    }
}