package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class Move_UltimateCrystalAttack extends AbstractMove
{
    private final int blockAmount;

    public Move_UltimateCrystalAttack(int damage, int block)
    {
        this.blockAmount = block + GetBonus(block, 0.25f);
        this.damageInfo = new DamageInfo(owner, damage + GetBonus(damage, 0.25f));
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base);
    }

    public void ExecuteInternal(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);

        if (this.damageInfo.output < 30)
        {
            owner.useFastAttackAnimation();

            GameActions.Bottom.Add(new DamageAction(target, this.damageInfo, AbstractGameAction.AttackEffect.SMASH));
        }
        else
        {
            owner.useSlowAttackAnimation();

            GameActions.Bottom.VFX(new ViceCrushEffect(target.hb.cX, target.hb.cY), 0.5F);
            GameActions.Bottom.Add(new DamageAction(target, this.damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        GameActions.Bottom.GainBlock(owner, blockAmount);
    }
}