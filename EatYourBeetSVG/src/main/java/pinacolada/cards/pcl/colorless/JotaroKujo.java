package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.JotaroKujo_StarPlatinum;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class JotaroKujo extends PCLCard
{
    public static final PCLCardData DATA = Register(JotaroKujo.class).SetSkill(3, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Jojo)
            .PostInitialize(data -> data.AddPreview(new JotaroKujo_StarPlatinum(), false));

    private int turns;

    public JotaroKujo()
    {
        super(DATA);

        Initialize(0, 16, 0, 0);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Red(1,0,2);
        SetAffinity_Light(1, 0, 1);

        SetSoul(5, 0, JotaroKujo_StarPlatinum::new);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(PCLAffinity.Red, 1);
        this.AddScaling(PCLAffinity.Light, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.FetchFromPile(name, 1, player.drawPile)
        .SetOptions(true, false)
            .SetFilter(c -> c.cost >= 2)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    PCLGameUtilities.Retain(c);
                    PCLCard eCard = PCLJUtils.SafeCast(c, PCLCard.class);
                    if (eCard != null && PCLGameUtilities.GetPCLAffinityLevel(eCard, PCLAffinity.General, true) > 0)
                    {
                        PCLActions.Top.SelectFromHand(name, 1, false)
                                .SetFilter(ca -> eCard != ca)
                                .AddCallback(cards2 ->
                                {
                                    for (AbstractCard c2 : cards2)
                                    {
                                        for (PCLCardAffinity cardAffinity : eCard.affinities.List) {
                                            PCLActions.Top.IncreaseScaling(c, cardAffinity.type, cardAffinity.scaling);
                                        }
                                    }
                                });
                    }
                }
            });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}