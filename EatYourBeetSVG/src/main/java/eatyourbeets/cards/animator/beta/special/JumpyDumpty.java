package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

        Initialize(9, 0, 1, 5);
        SetUpgrade(3, 0, 1, 0);
        SetAffinity_Red(1, 0, 1);
        SetAutoplay(true);
        SetExhaust(true);
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
                        GameActions.Bottom.MakeCardInDrawPile(new JumpyDumpty());
                        GameActions.Bottom.StackPower(new SelfDamagePower(p, secondaryValue));
                    }

                });

        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}