package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class AlinaGray extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(AlinaGray.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public AlinaGray()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Blue(1, 1, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.AddAffinity(Affinity.Blue, secondaryValue);
        GameActions.Bottom.EvokeOrb(1).AddCallback(orbs -> {
            for (AbstractOrb o : orbs) {
                GameActions.Bottom.ChannelOrb(o);
            }
        });

        if (CheckSpecialCondition(true) && info.TryActivateLimited()) {
            for (AbstractPower po : m.powers) {
                for (PowerHelper commonBuffHelper : GameUtilities.GetCommonBuffs()) {
                    if (commonBuffHelper.ID.equals(po.ID)) {
                        int powAmount = Math.min(magicNumber,GameUtilities.GetPowerAmount(m, po.ID));
                        GameActions.Bottom.ReducePower(po, powAmount);
                        GameActions.Bottom.StackPower(TargetHelper.Player(), commonBuffHelper, powAmount);
                    }
                }
            }
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return JUtils.Any(CombatStats.CardsExhaustedThisTurn(), c -> c instanceof AnimatorCard && ((AnimatorCard) c).cooldown != null && ((AnimatorCard) c).cooldown.cardConstructor != null);
    }
}