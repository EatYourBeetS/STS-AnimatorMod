package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class JumpyDumpty extends AnimatorCard {
    public static final EYBCardData DATA = Register(JumpyDumpty.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.Random).SetSeries(CardSeries.GenshinImpact);

    public JumpyDumpty() {
        super(DATA);

        Initialize(12, 0, 1, 5);
        SetUpgrade(4, 0, 0, 0);
        SetAffinity_Red(1, 0, 2);
        SetAutoplay(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        AbstractMonster priorityTarget;
        RandomizedList<AbstractMonster> priorityTargets = new RandomizedList<>();
        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
            if ((mo.currentBlock > 0 && mo.currentBlock <= damage) || mo.currentHealth <= damage) {
                priorityTargets.Add(mo);
            }
        }
        priorityTarget = priorityTargets.Retrieve(rng);
        if (priorityTarget == null) {
            priorityTarget = GameUtilities.GetRandomEnemy(true);
        }

        if (priorityTarget != null) {
            GameActions.Bottom.VFX(new ExplosionSmallEffect(priorityTarget.hb.cX, priorityTarget.hb.cY), 0.1F);
            GameActions.Bottom.DealDamage(this, priorityTarget, AttackEffects.NONE)
                    .AddCallback(priorityTarget.currentBlock, (initialBlock, target) ->
                    {
                        if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0)) {
                            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                                GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
                                GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.FIRE);
                            }
                        }

                    });

            GameActions.Bottom.ApplyBurning(p, priorityTarget, magicNumber);
        }

    }
}