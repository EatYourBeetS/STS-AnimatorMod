package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Curse_GriefSeed extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_GriefSeed.class)
            .SetCurse(0, PCLCardTarget.None, false)
            .SetSeries(CardSeries.MadokaMagica);

    public Curse_GriefSeed()
    {
        super(DATA, false);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Dark(1);

        SetPurge(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (block > 0) {
            PCLActions.Bottom.GainBlock(block);
        }
        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetFilter(PCLGameUtilities::HasCooldown)
                .AddCallback(cards -> {
                    for (AbstractCard c : cards) {
                        if (PCLGameUtilities.HasCooldown(c)) {
                            ((PCLCard) c).cooldown.ProgressCooldownAndTrigger(magicNumber, PCLGameUtilities.GetRandomEnemy(true));
                            c.flash();
                        }
                    }
                });
    }
}
