package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.ElectrifiedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class KujouSara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KujouSara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public KujouSara()
    {
        super(DATA);

        Initialize(0, 9, 2, 20);
        SetUpgrade(0, 0, 1);
        SetAffinity_Green(1,0,2);
        SetAffinity_Orange(1, 0, 2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        this.AddScaling(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.IncreaseScaling(player.hand, player.hand.size(), Affinity.Green, 1).SetFilter(c -> c.uuid != uuid);

        GameActions.Last.DiscardFromHand(name, player.hand.size() - 1, true)
                .AddCallback(cards -> {
            if (cards.size() > 0) {
                GameActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), cards.size() * magicNumber);
            }
        });

        if (IsStarter() && info.TryActivateLimited()) {
            GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(ElectrifiedPower.POWER_ID, secondaryValue));
        }
    }
}