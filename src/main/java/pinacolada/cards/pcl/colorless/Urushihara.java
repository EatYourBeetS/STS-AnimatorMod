package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Urushihara extends PCLCard implements OnStartOfTurnPostDrawSubscriber //TODO
{
    public static final PCLCardData DATA = Register(Urushihara.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Dark, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HatarakuMaouSama);

    private int turns = 0;

    public Urushihara()
    {
        super(DATA);

        Initialize(23, 0, 2, 5);
        SetUpgrade(0, 0, -1, -1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1);

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrb(new Dark());

        Urushihara other = (Urushihara) makeStatEquivalentCopy();
        other.turns = rng.random(magicNumber, secondaryValue);
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (turns > 0)
        {
            turns -= 1;
        }
        else
        {
            applyPowers();

            PCLGameEffects.Queue.ShowCardBriefly(this);

            PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.FIRE);
            PCLGameUtilities.UsePenNib();

            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}