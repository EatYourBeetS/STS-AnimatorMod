package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Emilia extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Emilia.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.ReZero);

    public Emilia()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential).AddCallback(() -> {
            GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        });
        CombatStats.onStartOfTurnPostDraw.Subscribe((Emilia) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(this);
        GameActions.Bottom.GainOrbSlots(GameUtilities.GetUniqueOrbsCount());
        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}