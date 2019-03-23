package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.RoryMercuryPower;

public class RoryMercury extends AnimatorCard
{
    public static final String ID = CreateFullID(RoryMercury.class.getSimpleName());

    public RoryMercury()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(8,0, 30);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RoryMercuryPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(10);
        }
    }
}