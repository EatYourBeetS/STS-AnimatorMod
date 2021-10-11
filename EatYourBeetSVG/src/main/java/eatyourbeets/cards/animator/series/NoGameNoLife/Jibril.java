package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.ShuffleOrbs;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Jibril extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Jibril.class)
            .SetAttack(3, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    private int costReduction = 0;

    public Jibril()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Water(2, 0, 0);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetExhaust(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        costReduction = 0;

        Refresh(null);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (c.type == CardType.POWER) {
            costReduction -= 1;
        }
        GameActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    public void RefreshCost()
    {
        CostModifiers.For(this).Set(costReduction);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return DamageAttribute.Instance.SetCard(this).SetText(new ColoredString("???", Colors.Cream(1f)));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded) {
            GameActions.Bottom.TriggerOrbPassive(player.orbs.size()).SetSequential(true);
        }
        GameActions.Bottom.Callback(new ShuffleOrbs(1)).AddCallback(
                () -> {
                    int totalDamage = 0;
                    for (int i = 0; i < Math.min(magicNumber, player.orbs.size()); i++) {
                        if (GameUtilities.IsValidOrb(player.orbs.get(i))) {
                            totalDamage += player.orbs.get(i).evokeAmount;
                        }
                    }
                    final int[] damageMatrix = DamageInfo.createDamageMatrix(totalDamage, true);
                    GameActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.VIOLET), 0.3f);
                    GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AttackEffects.DARKNESS)
                            .SetPiercing(true, false);
                }
        );
    }
}
