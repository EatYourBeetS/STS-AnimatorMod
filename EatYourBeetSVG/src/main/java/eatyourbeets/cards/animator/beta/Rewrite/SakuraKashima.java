package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.defect.EvokeAllOrbsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import eatyourbeets.cards.animator.beta.EYBMiracle;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class SakuraKashima extends AnimatorCard {
    public static final EYBCardData DATA = Register(SakuraKashima.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new EYBMiracle(), false);
    }

    public SakuraKashima() {
        super(DATA);

        Initialize(0, 0, 10);
        SetUpgrade(0, 0, 0);
        SetExhaust(true);

        SetSpellcaster();
        SetSynergy(Synergies.Rewrite);
    }

    @Override
    protected void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new EYBMiracle());
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