package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class YunYun extends AnimatorCard
{
    public static final String ID = CreateFullID(YunYun.class.getSimpleName());

    public YunYun()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(10, 0);

        this.isMultiDamage = true;

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        int newCost = AbstractDungeon.player.hand.getAttacks().size();
        if (AbstractDungeon.player.hand.contains(this))
        {
            newCost = Math.max(0, newCost - 1);
        }

        this.setCostForTurn(newCost);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));

        ArrayList<AbstractMonster> enemies = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < enemies.size(); ++i)
        {
            AbstractMonster enemy = enemies.get(i);
            if (!enemy.isDeadOrEscaped())
            {
                GameActionsHelper.AddToBottom(new VFXAction(new LightningEffect(enemy.drawX, enemy.drawY)));
                GameActionsHelper.AddToBottom(new DamageAction(enemy, new DamageInfo(p, this.multiDamage[i], this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
            }
        }

        //GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}