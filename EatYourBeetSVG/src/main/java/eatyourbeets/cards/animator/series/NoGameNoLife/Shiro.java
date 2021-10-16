package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Shiro extends AnimatorCard implements OnOrbApplyFocusSubscriber
{
    public static final EYBCardData DATA = Register(Shiro.class)
            .SetSkill(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();
    public static final int CHARGE_COST = 8;
    private AbstractOrb focusedOrb;

    public Shiro()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetCostUpgrade(-1);

        SetAffinity_Water(2);
        SetAffinity_Light(1);

        SetProtagonist(true);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Scry(magicNumber).AddCallback(
                cards -> {
                    if (cards.size() > 0) {
                        GameActions.Bottom.TriggerOrbPassive(cards.size(), true, false);
                    }

                    if (GameUtilities.SpendSuperchargedCharge(CHARGE_COST)) {
                        focusedOrb = null;
                        for (AbstractOrb orb : player.orbs)
                        {
                            if (GameUtilities.IsValidOrb(orb))
                            {
                                focusedOrb = orb;
                                break;
                            }
                        }
                        if (focusedOrb != null) {
                            CombatStats.onOrbApplyFocus.Subscribe(this);
                            GameActions.Bottom.EvokeOrb(secondaryValue, focusedOrb).AddCallback(() -> {
                                CombatStats.onOrbApplyFocus.Unsubscribe(this);
                            });
                        }

                    }
                }
        );
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        if (orb == focusedOrb) {
            orb.passiveAmount += secondaryValue;
            orb.evokeAmount += secondaryValue;
        }
    }
}