package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Defend_Bleach extends Defend
{
    public static final String ID = Register(Defend_Bleach.class).ID;

    public Defend_Bleach()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (!CombatStats.HasActivatedLimited(cardID))
        {
            GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
                    .ShowEffect(false, false)
                    .SetOptions(false, false, false)
                    .AddCallback(() ->
                    {
                        if (CombatStats.TryActivateLimited(cardID))
                        {
                            for (int i = 0; i < secondaryValue; i++)
                            {
                                GameActions.Bottom.ChannelOrb(new Fire());
                            }
                        }
                    });
        }
    }
}