package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class Yui extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.AngelBeats);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Motivate(secondaryValue);

        if (CombatStats.ControlPile.Contains(this) && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(magicNumber);
        }
    }
}