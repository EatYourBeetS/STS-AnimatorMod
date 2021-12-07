package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class JumpyDumpty extends AnimatorCard {
    public static final EYBCardData DATA = Register(JumpyDumpty.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.Random).SetSeries(CardSeries.GenshinImpact);

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
        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
            this.Refresh(mo);
            if ((mo.currentBlock > 0 && mo.currentBlock <= damage) || mo.currentHealth <= damage) {
                priorityTargets.Add(mo);
            }
        }
        priorityTarget = priorityTargets.Retrieve(rng);
        if (priorityTarget == null) {
            priorityTarget = GameUtilities.GetRandomEnemy(true);
        }

        if (priorityTarget != null) {
            this.Refresh(priorityTarget);
            GameActions.Bottom.VFX(new ExplosionSmallEffect(priorityTarget.hb.cX, priorityTarget.hb.cY), 0.1F);
            AbstractMonster finalPriorityTarget = priorityTarget;
            GameActions.Bottom.DealCardDamage(this, priorityTarget, AttackEffects.NONE).forEach(d -> d
                    .AddCallback(finalPriorityTarget.currentBlock, (initialBlock, target) ->
                    {
                        if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0)) {
                            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                                GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
                                GameActions.Bottom.ApplyBurning(player, player, secondaryValue);
                            }
                        }

                    }));

        }

        GameActions.Bottom.ChannelOrb(new Fire());
    }
}