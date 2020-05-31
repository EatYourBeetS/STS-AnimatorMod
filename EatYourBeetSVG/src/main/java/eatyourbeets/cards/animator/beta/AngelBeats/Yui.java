package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_OnSpecialPlay;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yui extends AnimatorCard_OnSpecialPlay
{
    public static final EYBCardData DATA = AnimatorCard.Register(Yui.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Yui()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.AngelBeats);
        SetExhaust(true);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Motivate(secondaryValue);
    }

    @Override
    public void OnSpecialPlay() {
        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(magicNumber);
        }
    }
}