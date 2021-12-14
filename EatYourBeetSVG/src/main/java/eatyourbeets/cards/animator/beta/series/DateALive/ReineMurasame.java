package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ReineMurasame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReineMurasame.class).SetSkill(-1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
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
        GameActions.Bottom.GainBlock(block);
        int stacks = GameUtilities.UseXCostEnergy(this);

        for (int i = 0; i < stacks; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
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

                    GameUtilities.TriggerWhenPlayed(card, key, (k, c) ->
                        CostModifiers.For(c).Remove(k, false)
                    );
                }
            });
        }

        if (info.IsSynergizing && stacks > 0)
        {
            GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, stacks));
        }
    }
}