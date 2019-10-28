package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.actions.common.PiercingDamageAction;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class Rose extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Rose.class.getSimpleName());

    public Rose()
    {
        super(ID, 3, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(8,0, 2);

        this.baseSecondaryValue = this.secondaryValue = 30;

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DrawCard(p, this.magicNumber);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, BaseMod.MAX_HAND_SIZE, m, this::OnDiscard));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(10);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != null && cards != null)
        {
            AbstractMonster m = (AbstractMonster) state;

            GameActionsHelper.AddToBottom(new RoseDamageAction(m, this, cards.size(), this.damage));
        }
    }

    private class RoseDamageAction extends AnimatorAction
    {
        private final int damage;
        private final int times;
        private final AbstractPlayer p;
        private final Rose rose;
        private final AbstractMonster enemy;

        private AbstractGameAction action;

        private RoseDamageAction(AbstractMonster enemy, Rose rose, int times, int damage)
        {
            this.damage = damage;
            this.enemy = enemy;
            this.rose = rose;
            this.p = AbstractDungeon.player;
            this.times = times;
        }

        @Override
        public void update()
        {
            if (action != null)
            {
                action.update();

                if (action.isDone)
                {
                    if (enemy.currentHealth <= 0 || enemy.isDeadOrEscaped() || enemy.isDying)
                    {
                        int[] damageMatrix = DamageInfo.createDamageMatrix(rose.secondaryValue, true);

                        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
                        {
                            Explosion(m.hb);
                        }

                        GameActionsHelper.DamageAllEnemies(p, damageMatrix, DamageInfo.DamageType.THORNS, AttackEffect.NONE);
                    }
                    else  if (times > 1)
                    {
                        GameActionsHelper.AddToBottom(new RoseDamageAction(enemy, rose, times - 1, damage));
                    }

                    this.isDone = true;
                }
            }
            else
            {
                Explosion(enemy.hb);
                action = new PiercingDamageAction(enemy, new DamageInfo(p, damage, rose.damageTypeForTurn), true);
            }
        }

        private void Explosion(Hitbox hb)
        {
            GameActionsHelper.AddToTop(new VFXAction(new ExplosionSmallEffect(hb.cX + MathUtils.random(-0.05F, 0.05F), hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F));
        }
    }
}