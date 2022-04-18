package eatyourbeets.cards.unnamed.uncommon;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.unnamed.AmplificationPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LightningStrike extends UnnamedCard
{
    public static final EYBCardData DATA = Register(LightningStrike.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public LightningStrike()
    {
        super(DATA);

        Initialize(16, 0, 30);
        SetUpgrade(6, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.LIGHTNING)
        .AddCallback(c ->
        {
           if (!GameUtilities.IsFatal(c, true)
             && GameUtilities.GetPowerAmount(c, AmplificationPower.POWER_ID) >= magicNumber
             && CombatStats.TryActivateLimited(cardID))
           {
             GameActions.Bottom.ApplyPower(player, c, new StunMonsterPower((AbstractMonster) c, 1));
           }
        });
    }
}