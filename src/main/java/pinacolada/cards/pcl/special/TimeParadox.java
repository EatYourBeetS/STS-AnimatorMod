package pinacolada.cards.pcl.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.colorless.MakiseKurisu;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class TimeParadox extends PCLCard_Curse implements OnStartOfTurnPostDrawSubscriber
{
    public static final PCLCardData DATA = Register(TimeParadox.class)
            .SetCurse(-1, PCLCardTarget.None, true)
            .SetSeries(MakiseKurisu.DATA.Series);
    protected final ArrayList<AbstractCard> cards = new ArrayList<>();
    protected int turns = 0;

    public TimeParadox()
    {
        super(DATA, true);

        Initialize(0, 0, 1, 5);

        SetAffinity_Silver(1);

        SetAutoplay(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);
        PCLActions.Bottom.PurgeFromPile(name,stacks,player.hand)
                .ShowEffect(true, false)
                .SetOptions(true, true)
                .AddCallback(
                pc -> {
                    if (pc.size() > 0) {
                        TimeParadox other = (TimeParadox) makeStatEquivalentCopy();
                        other.cards.addAll(pc);
                        other.turns = rng.random(magicNumber, secondaryValue);
                        PCLCombatStats.onStartOfTurnPostDraw.Subscribe(other);
                    }
                });
        PCLActions.Bottom.Draw(stacks);
        PCLActions.Bottom.GainEnergy(stacks);
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

            PCLGameEffects.Queue.ShowCardBriefly(this);

            for (AbstractCard c : cards) {
                if (c.cost >= 0) {
                    int newCost = MathUtils.random(0,3);
                    float modifier = 1.5f * (newCost - c.cost);
                    PCLCard eC = PCLJUtils.SafeCast(c, PCLCard.class);
                    if (eC != null) {
                        if (eC.cardData != null && eC.cardData.MaxForms > 1) {
                            eC.SetForm(MathUtils.random(0, eC.cardData.MaxForms - 1), eC.timesUpgraded);
                        }
                        if (eC.baseHitCount > 1) {
                            PCLGameUtilities.ModifyHitCount(eC, Math.max(1,MathUtils.random(-1,1)), false);
                        }
                        if (eC.baseSecondaryValue > 0) {
                            PCLGameUtilities.ModifySecondaryValue(eC, Math.max(1,eC.baseSecondaryValue + MathUtils.round(MathUtils.random(0f, eC.baseSecondaryValue) * modifier)), false);
                        }
                    }
                    PCLGameUtilities.ModifyCostForCombat(c, newCost, false);
                    if (c.baseDamage > 0) {
                        PCLGameUtilities.ModifyDamage(c, Math.max(1,c.baseDamage + MathUtils.round(MathUtils.random(0f, c.baseDamage) * modifier)), false);
                    }
                    if (c.baseBlock > 0) {
                        PCLGameUtilities.ModifyBlock(c, Math.max(1,c.baseBlock + MathUtils.round(MathUtils.random(0f, c.baseBlock) * modifier)), false);
                    }
                    if (c.baseMagicNumber > 0) {
                        PCLGameUtilities.ModifyMagicNumber(c, Math.max(1,c.baseMagicNumber + MathUtils.round(MathUtils.random(0f, c.baseMagicNumber) * modifier)), false);
                    }
                }
                PCLActions.Last.MakeCardInHand(c);
            }

            PCLCombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}