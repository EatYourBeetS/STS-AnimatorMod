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
            .SetMultiformData(2, false)
            .SetSeries(CardSeries.ReZero);

    public Emilia()
    {
        super(DATA);

        Initialize(0, 1, 2, 0);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                Initialize(0, 1, 2, 0);
                SetUpgrade(0, 0, 0, 0);
                SetCostUpgrade(-1);
                this.cardText.OverrideDescription(cardData.Strings.DESCRIPTION, true);
            }
            else {
                Initialize(0, 1, 2, 0);
                SetUpgrade(0, 0, 0, 2);
                SetCostUpgrade(0);
                this.cardText.OverrideDescription(null, true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential).AddFocus(secondaryValue).AddCallback(() -> {
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