package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Shigure extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shigure.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing);

    public Shigure()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(2, 0, 1, 0);
        SetScaling(0, 1, 0);

        SetSeries(CardSeries.OwariNoSeraph);
        SetAffinity(0, 1, 0, 1, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy ->
        {
            GameEffects.List.Add(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()));
            GameActions.Top.ApplyPoison(player, enemy, magicNumber);
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.Cycle(name, 1);
        }
    }
}