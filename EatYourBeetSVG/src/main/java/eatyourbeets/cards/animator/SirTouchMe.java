package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.AinzEffects.AinzEffect;
import eatyourbeets.powers.AinzPower;
import eatyourbeets.powers.TemporaryJuggernautPower;
import patches.AbstractEnums;

public class SirTouchMe extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(SirTouchMe.class.getSimpleName());

    public SirTouchMe()
    {
        super(ID, 2, CardType.ATTACK, CardTarget.SELF_AND_ENEMY);

        Initialize(10,10,4);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, p, new JuggernautPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.GainBlock(p, this.block);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            upgradeBlock(4);
        }
    }
}