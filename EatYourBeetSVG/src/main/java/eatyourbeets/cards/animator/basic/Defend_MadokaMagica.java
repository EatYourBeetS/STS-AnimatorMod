package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Defend_MadokaMagica extends Defend
{
    public static final String ID = Register(Defend_MadokaMagica.class).ID;

    public Defend_MadokaMagica()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 6);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDrawPile(new Curse_GriefSeed()).SetDestination(CardSelection.Top);
        }
    }
}