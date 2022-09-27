package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
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

        Initialize(5, 0, 4);
        SetUpgrade(2, 0, 0);

        SetAffinity_Red(1, 0, 3);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY).AddCallback(this::OnDamageDealt);
        GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.SLASH_HEAVY).AddCallback(this::OnDamageDealt);

        GameActions.Bottom.ModifyAllInstances(uuid)
        .AddCallback(c -> GameUtilities.SetCardTag(c, HASTE, true));
    }

    protected void OnDamageDealt(AbstractCreature target)
    {
        if (GameUtilities.GetPower(target, VulnerablePower.POWER_ID) != null && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainInspiration(magicNumber);
        }
    }
}