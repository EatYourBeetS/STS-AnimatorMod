package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class JumpyDumpty extends PCLCard {
    public static final PCLCardData DATA = Register(JumpyDumpty.class).SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Fire, eatyourbeets.cards.base.EYBCardTarget.Random).SetSeries(CardSeries.GenshinImpact);

    public JumpyDumpty() {
        super(DATA);

        Initialize(9, 0, 1, 3);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Light(0,0,2);
        SetAutoplay(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        AbstractMonster priorityTarget;
        RandomizedList<AbstractMonster> priorityTargets = new RandomizedList<>();
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            this.Refresh(mo);
            if ((mo.currentBlock > 0 && mo.currentBlock <= damage) || mo.currentHealth <= damage) {
                priorityTargets.Add(mo);
            }
        }
        priorityTarget = priorityTargets.Retrieve(rng);
        if (priorityTarget == null) {
            priorityTarget = PCLGameUtilities.GetRandomEnemy(true);
        }

        if (priorityTarget != null) {
            this.Refresh(priorityTarget);
            PCLActions.Bottom.VFX(new ExplosionSmallEffect(priorityTarget.hb.cX, priorityTarget.hb.cY), 0.1F);
            AbstractMonster finalPriorityTarget = priorityTarget;
            PCLActions.Bottom.DealCardDamage(this, priorityTarget, AttackEffects.NONE).forEach(d -> d
                    .AddCallback(finalPriorityTarget.currentBlock, (initialBlock, target) ->
                    {
                        if (PCLGameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0)) {
                            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                                PCLActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
                                PCLActions.Bottom.ApplyBurning(player, player, secondaryValue);
                            }
                        }

                    }));

        }

        PCLActions.Bottom.ChannelOrb(new Fire());
    }
}