package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.JotaroKujo_StarPlatinum;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class JotaroKujo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JotaroKujo.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Jojo)
            .PostInitialize(data -> data.AddPreview(new JotaroKujo_StarPlatinum(), false));

    private int turns;

    public JotaroKujo()
    {
        super(DATA);

        Initialize(0, 17, 0, 0);
        SetUpgrade(0, 2, 0, 0);

        SetAffinity_Red(2,0,2);
        SetAffinity_Light(1, 0, 1);

        SetSoul(7, 0, JotaroKujo_StarPlatinum::new);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Red, 1);
        this.AddScaling(Affinity.Light, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.FetchFromPile(name, 1, player.drawPile)
        .SetOptions(true, false)
            .SetFilter(c -> c.type.equals(CardType.POWER))
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameUtilities.Retain(c);
                    final int powerCost = Math.max(0, c.costForTurn);
                    if (powerCost > 0)
                    {
                        cooldown.ProgressCooldownAndTrigger(powerCost, m);
                        GameActions.Top.SelectFromHand(name, 1, false)
                                .AddCallback(cards2 ->
                                {
                                    for (AbstractCard c2 : cards2)
                                    {
                                        for (int i = 0; i < powerCost; i++) {
                                            GameActions.Top.IncreaseScaling(c, null, 1);
                                        }
                                    }
                                });
                    }
                }
            });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}