package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.AnimatorAction;
import eatyourbeets.actions.PiercingDamageAction;
import eatyourbeets.actions.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;

public class Rose extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Rose.class.getSimpleName());

    public Rose()
    {
        super(ID, 3, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(7,0, 4);

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
            upgradeDamage(2);
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
        private int damage;
        private int times;
        private AbstractPlayer p;
        private Rose rose;
        private AbstractMonster enemy;

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
                    if (times > 1)
                    {
                        if (enemy.isDeadOrEscaped() || enemy.isDying)
                        {
                            AbstractMonster newTarget = PlayerStatistics.GetRandomEnemy(true);
                            if (newTarget != null)
                            {
                                rose.calculateCardDamage(newTarget);
                                GameActionsHelper.AddToBottom(new RoseDamageAction(newTarget, rose, times - 1, rose.damage));
                            }
                        }
                        else
                        {
                            GameActionsHelper.AddToBottom(new RoseDamageAction(enemy, rose, times - 1, damage));
                        }
                    }

                    this.isDone = true;
                }
            }
            else
            {
                GameActionsHelper.AddToTop(new VFXAction(new ExplosionSmallEffect(enemy.hb.cX + MathUtils.random(-0.05F, 0.05F), enemy.hb.cY + MathUtils.random(-0.05F, 0.05F)), 0.1F));
                action = new PiercingDamageAction(enemy, new DamageInfo(p, damage, rose.damageTypeForTurn), true);
            }
        }
    }
}