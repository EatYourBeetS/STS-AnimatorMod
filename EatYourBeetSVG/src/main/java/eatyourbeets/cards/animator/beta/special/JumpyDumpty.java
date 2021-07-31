package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class JumpyDumpty extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JumpyDumpty.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Elemental).SetSeries(CardSeries.GenshinImpact);

    public JumpyDumpty()
    {
        super(DATA);

        Initialize(11, 0, 3, 5);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Red(1, 0, 1);
        SetAutoplay(true);
        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (hasTag(AUTOPLAY))
        {
            AbstractMonster enemy = null;
            int minBlock = Integer.MAX_VALUE;
            int minHealth = Integer.MAX_VALUE;

            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (m.currentBlock > 0 && m.currentBlock < minBlock)
                {
                    minBlock = m.currentBlock;
                    enemy = m;
                }
            }
            if (enemy == null) {
                for (AbstractMonster m : GameUtilities.GetEnemies(true))
                {
                    if (m.currentHealth  < minHealth)
                    {
                        minHealth = m.currentHealth;
                        enemy = m;
                    }
                }
            }

            GameActions.Bottom.PlayCard(this, player.hand, enemy)
                    .SpendEnergy(true)
                    .AddCondition(AbstractCard::hasEnoughEnergy);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
                .AddCallback(m.currentBlock, (initialBlock, target) ->
                {
                    if (GameUtilities.IsDeadOrEscaped(target) || (initialBlock > 0 && target.currentBlock <= 0))
                    {
                        GameActions.Bottom.MakeCardInDrawPile(this.makeStatEquivalentCopy());
                        GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));
                    }

                });

        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}