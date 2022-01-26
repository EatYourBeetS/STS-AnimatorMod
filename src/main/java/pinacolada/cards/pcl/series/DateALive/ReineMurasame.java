package pinacolada.cards.pcl.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.modifiers.BlockModifiers;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ReineMurasame extends PCLCard
{
    public static final PCLCardData DATA = Register(ReineMurasame.class).SetSkill(-1, CardRarity.UNCOMMON, PCLCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ShidoItsuka(), false));

    public ReineMurasame()
    {
        super(DATA);

        Initialize(0, 1, 3);
        SetUpgrade(0, 1);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1);
        SetAffinity_Silver(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        int stacks = PCLGameUtilities.UseXCostEnergy(this);

        for (int i = 0; i < stacks; i++)
        {
            PCLActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
            .SetUpgrade(upgraded, true)
            .AddCallback(card -> {
                if (card.costForTurn > 0)
                {
                    final String key = cardID + uuid;

                    CostModifiers.For(card).Add(key, -1);

                    if (card.baseBlock >= 0)
                    {
                        BlockModifiers.For(card).Add(key, -magicNumber);
                    }

                    PCLGameUtilities.TriggerWhenPlayed(card, key, (k, c) ->
                        CostModifiers.For(c).Remove(k, false)
                    );
                }
            });
        }

        if (info.IsSynergizing && stacks > 0)
        {
            PCLActions.Bottom.StackPower(new DrawCardNextTurnPower(p, stacks));
        }
    }
}