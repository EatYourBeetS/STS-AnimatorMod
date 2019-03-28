package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Jibril extends AnimatorCard
{
    public static final String ID = CreateFullID(Jibril.class.getSimpleName());

    public Jibril()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(10,0, 20);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        float multiplier = 1;

        if (mo != null && mo.powers != null)
        {
            for (AbstractPower p : mo.powers)
            {
                if (p.type == AbstractPower.PowerType.DEBUFF)
                {
                    if (p.ID.equals(VulnerablePower.POWER_ID))
                    {
                        multiplier += this.magicNumber * 0.005f;
                    }
                    else
                    {
                        multiplier += this.magicNumber * 0.01f;
                    }
                }
            }
        }

        return super.calculateModifiedCardDamage(player, mo, tmp * multiplier);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(5);
        }
    }
}