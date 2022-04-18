package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Enchain extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Enchain.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .ModifyRewards((data, rewards) ->
            {
                final int copies = data.GetTotalCopies(player.masterDeck);
                if (copies > 0 && copies < data.MaxCopies)
                {
                    GR.Common.Dungeon.TryReplaceFirstCardReward(rewards, 0.075f, true, data);
                }
            });

    public Enchain()
    {
        super(DATA);

        Initialize(7, 0, 4);
        SetUpgrade(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DARK);
        GameActions.Bottom.Draw(1);

        if (CheckSpecialCondition(true))
        {
            //GameActions.Bottom.SFX(SFX.POWER_CONSTRICTED, 0.6f, 0.6f);
            GameActions.Bottom.ApplyConstricted(p, m, magicNumber);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return tryUse ? !CombatStats.TryActivateSemiLimited(cardID) : CombatStats.HasActivatedSemiLimited(cardID);
    }
}