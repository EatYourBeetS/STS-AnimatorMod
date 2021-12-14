package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class HataNoKokoro extends AnimatorCard {
    public static final EYBCardData DATA = Register(HataNoKokoro.class)
            .SetSkill(1, CardRarity.RARE)
            .SetSeriesFromClassPackage(true);

    public HataNoKokoro() {
        super(DATA);

        Initialize(0, 2, 7, 2);
        SetUpgrade(0, 0, 2, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(Affinity.General, 4);

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
        return super.cardPlayable(m) && CheckAffinity(Affinity.General);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.AddAffinity(Affinity.Blue, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback((chosenAffinities) -> {
            for (AffinityChoice choice : chosenAffinities) {
                GameActions.Bottom.SelectFromHand(name, player.hand.size() - 1, true)
                        .SetOptions(true, true, true)
                        .AddCallback((cards) -> {
                            for (AbstractCard c : cards) {
                                if (c instanceof EYBCard) {
                                    EYBCardAffinities newAffinities = new EYBCardAffinities(null);
                                    newAffinities.Set(choice.Affinity, 2);
                                    newAffinities.SetScaling(choice.Affinity, magicNumber);
                                    ((EYBCard) c).affinities.Initialize(newAffinities);
                                }
                            }
                        });
            }
        });
    }
}

