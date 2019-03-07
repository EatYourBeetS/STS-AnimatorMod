package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class DolaSchwi extends AnimatorCard
{
    public static final String ID = CreateFullID(DolaSchwi.class.getSimpleName());

    public DolaSchwi()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(10,0,1);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        VulnerablePower power = new VulnerablePower(m, this.magicNumber, false);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, power, this.magicNumber));

        int damage = this.damage;
        if (!m.hasPower(VulnerablePower.POWER_ID) && !m.hasPower(ArtifactPower.POWER_ID))
        {
            damage = (int)power.atDamageReceive(damage, DamageInfo.DamageType.NORMAL);
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

        if (HasActiveSynergy())
        {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(4);
            upgradeMagicNumber(1);
        }
    }
}