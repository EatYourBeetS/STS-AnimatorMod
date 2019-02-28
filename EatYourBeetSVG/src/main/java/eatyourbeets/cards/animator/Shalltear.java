package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.OnTargetBlockBreakAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Shalltear extends AnimatorCard
{
    public static final String ID = CreateFullID(Shalltear.class.getSimpleName());

    public Shalltear()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(11,0, 4);

        tags.add(CardTags.HEALING);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractMonster m2 : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (!m2.isDying && m2.currentHealth > 0 && !m2.halfDead)
            {
                DamageAction damageAction = new DamageAction(m2, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE);

                GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m2.hb.cX, m2.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));
                GameActionsHelper.AddToBottom(new OnTargetBlockBreakAction(m2, damageAction, new HealAction(p, p, this.magicNumber)));

//                if (m2.currentBlock > 0 && m2.currentBlock <= (this.damage * 2))
//                {
//                    AbstractDungeon.actionManager.addToTop(new HealAction(p, p, this.magicNumber));
//                }
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }
}