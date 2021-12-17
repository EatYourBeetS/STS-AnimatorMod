package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;

public class HataNoKokoro extends PCLCard {
    public static final PCLCardData DATA = Register(HataNoKokoro.class)
            .SetSkill(1, CardRarity.RARE)
            .SetSeriesFromClassPackage(true);

    public HataNoKokoro() {
        super(DATA);

        Initialize(0, 2, 7, 2);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 4);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(PCLAffinity.General);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.AddAffinity(PCLAffinity.Blue, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((chosenAffinities) -> {
            for (AffinityChoice choice : chosenAffinities) {
                PCLActions.Bottom.SelectFromHand(name, player.hand.size() - 1, true)
                        .SetOptions(true, true, true)
                        .AddCallback((cards) -> {
                            for (AbstractCard c : cards) {
                                if (c instanceof PCLCard) {
                                    PCLCardAffinities newAffinities = new PCLCardAffinities(null);
                                    newAffinities.Set(choice.Affinity, 2);
                                    newAffinities.SetScaling(choice.Affinity, magicNumber);
                                    ((PCLCard) c).affinities.Initialize(newAffinities);
                                }
                            }
                        });
            }
        });
    }
}

