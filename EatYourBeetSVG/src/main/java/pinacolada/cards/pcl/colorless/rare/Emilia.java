package pinacolada.cards.pcl.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Emilia extends PCLCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(Emilia.class)
            .SetSkill(2, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
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
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.EvokeOrb(player.filledOrbCount(), EvokeOrb.Mode.Sequential).AddFocus(secondaryValue).AddCallback(() -> {
            PCLActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        });
        PCLCombatStats.onStartOfTurnPostDraw.Subscribe((Emilia) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PCLGameEffects.Queue.ShowCardBriefly(this);
        PCLActions.Bottom.GainOrbSlots(PCLGameUtilities.GetUniqueOrbsCount());
        PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}