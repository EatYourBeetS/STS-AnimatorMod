package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class FocusedEffort extends UnnamedCard
{
    public static final EYBCardData DATA = Register(FocusedEffort.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Minion);

    public FocusedEffort()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        final AbstractMonster.Intent targetIntent = m.intent;
        GameActions.Bottom.Callback(m.intent, (intent, __) ->
        {
            for (UnnamedDoll doll : CombatStats.Dolls.GetAll())
            {
                CombatStats.Dolls.ChangeIntent(doll, intent, true);
            }
        });
        GameActions.Bottom.ActivateDoll(999).SetSequential(true);
    }
}