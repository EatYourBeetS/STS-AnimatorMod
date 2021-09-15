package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RoryMercury extends AnimatorCard
{
    public static final EYBCardData DATA = Register(RoryMercury.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public RoryMercury()
    {
        super(DATA);

        Initialize(5, 0, 0);
        SetUpgrade(2, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY);

        GameActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c -> GameUtilities.SetCardTag(c, HASTE, true));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (AbstractMonster enemy : GameUtilities.GetEnemies(true))
        {
            if (!enemy.hasPower(VulnerablePower.POWER_ID))
            {
                return;
            }
        }

        if (info.TryActivateSemiLimited())
        {
            GameActions.Bottom.GainInspiration(1);
        }
    }
}