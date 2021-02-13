package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class SakuraKashima extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SakuraKashima.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Miracle(), false);
    }

    public SakuraKashima()
    {
        super(DATA);

        Initialize(0, 0, 10);
        SetUpgrade(0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Rewrite);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new Miracle());
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new EndTurnDeathPower(p));

        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainIntellect(magicNumber);

        GameActions.Last.Add(new EvokeAllOrbsAction());
    }
}