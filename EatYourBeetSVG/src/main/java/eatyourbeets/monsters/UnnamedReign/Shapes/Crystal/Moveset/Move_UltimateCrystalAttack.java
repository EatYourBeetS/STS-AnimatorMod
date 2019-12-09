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
            GameActionsHelper_Legacy.AddToBottom(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SMASH));
        }
        else
        {
            owner.useSlowAttackAnimation();
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damageInfo, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        GameActionsHelper_Legacy.GainBlock(owner, blockAmount);
    }
}