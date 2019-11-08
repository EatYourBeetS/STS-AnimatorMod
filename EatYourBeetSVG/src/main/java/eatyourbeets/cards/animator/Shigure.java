package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;

public class Shigure extends AnimatorCard
{
    public static final String ID = Register(Shigure.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Exhaust);

    public Shigure()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4,0, 3, 2);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GainSupportDamage(AbstractDungeon.player, this.secondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, magicNumber), magicNumber);

        if (HasActiveSynergy())
        {
            GainSupportDamage(p, this.secondaryValue);
        }
    }

    private void GainSupportDamage(AbstractPlayer p, int amount)
    {
        GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, amount), amount);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        }
    }
}