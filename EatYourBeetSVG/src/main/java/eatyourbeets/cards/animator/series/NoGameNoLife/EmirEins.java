package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.common.ImpairedPower;
import eatyourbeets.utilities.GameActions;

public class EmirEins extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EmirEins.class)
            .SetSkill(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public EmirEins()
    {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetCostUpgrade(-1);

        SetAffinity_Blue(1);
        SetAffinity_Silver(2);

        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();

        GameActions.Bottom.Add(new ShuffleAction(player.drawPile));
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Add(new ShuffleAction(player.drawPile));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (player.filledOrbCount() > 0) {
            GameActions.Bottom.Reload(name, cards -> {
                for (int i = 0; i < magicNumber; i++) {
                    GameActions.Bottom.InduceOrb(player.orbs.get(0).makeCopy(), true);
                }
            });
        }
        else {
            GameActions.Bottom.ChannelOrb(new Plasma());
        }

        GameActions.Bottom.StackPower(new ImpairedPower(player, secondaryValue));
    }
}