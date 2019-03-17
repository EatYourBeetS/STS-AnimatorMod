package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Shalltear extends AnimatorCard
{
    public static final String ID = CreateFullID(Shalltear.class.getSimpleName());

    public Shalltear()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(12,0, 3);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        boolean hasActiveSynergy = HasActiveSynergy();

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m1.hb.cX, m1.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
            GameActionsHelper.DamageTarget(p, m1, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);

            if (hasActiveSynergy)
            {
                GameActionsHelper.ApplyPower(p, m1, new PoisonPower(m1, p, this.magicNumber), this.magicNumber);
            }
        }
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