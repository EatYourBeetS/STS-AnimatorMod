package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.unnamed.MarkedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class AttackOrder extends UnnamedCard
{
    public static final EYBCardData DATA = Register(AttackOrder.class)
            .SetAttack(1, CardRarity.COMMON);

    public AttackOrder()
    {
        super(DATA);

        Initialize(5, 0);
        SetUpgrade(3, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY)
        .AddCallback(c ->
        {
            if (!GameUtilities.IsFatal(c, true))
            {
                GameActions.Top.ApplyPower(player, c, new MarkedPower(c)).IgnoreArtifact(true).ShowEffect(false, true);
            }
        });
        GameActions.Bottom.ActivateDoll(1).SetFilter(d -> GameUtilities.IsAttacking(d.intent));
    }
}