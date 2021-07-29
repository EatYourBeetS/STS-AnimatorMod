package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Aisha extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Aisha.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeries(CardSeries.Elsword);

    public Aisha()
    {
        super(DATA);

        Initialize(2, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseDamage(this, player.filledOrbCount() * secondaryValue, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE).SetVFX(true, false)
            .SetDamageEffect(enemy ->
            {
                GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
            });
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainIntellect(2);
            GameActions.Bottom.GainCorruption(2);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean use)
    {
        return CombatStats.OrbsEvokedThisTurn().size() > 0 && (use ? CombatStats.TryActivateSemiLimited(cardID) : CombatStats.CanActivateSemiLimited(cardID));
    }
}